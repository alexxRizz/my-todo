package alexx.rizz.mytodo.feature.todolist

import alexx.rizz.mytodo.feature.*
import alexx.rizz.mytodo.feature.todolist.ui.components.*
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*
import kotlin.time.Duration.Companion.seconds

// private val Log = getLogger<TodoListVM>()

@HiltViewModel
class TodoListVM @Inject constructor(
  private val mTodoRep: ITodoRepository,
) : ViewModeBase() {

  sealed interface UserIntent {
    data class ShowListItems(val listOwnerId: TodoListId) : UserIntent
    data object Back : UserIntent
    data class EditList(val id: TodoListId) : UserIntent
    data class EditItem(val id: TodoItemId) : UserIntent
    data object CancelEditing : UserIntent
    data class ConfirmListEditing(val id: TodoListId, val text: String) : UserIntent
    data class ConfirmItemEditing(val id: TodoItemId, val text: String) : UserIntent
    data class Done(val id: TodoItemId, val isDone: Boolean) : UserIntent
    data class DeleteList(val id: TodoListId) : UserIntent
    data class DeleteItem(val id: TodoItemId) : UserIntent
    data class ReorderLists(val reordered: List<TodoList>) : UserIntent
    data class ReorderItems(val reordered: List<TodoItem>) : UserIntent
  }

  private val mEditDialogState = MutableStateFlow<TodoEditDialogState?>(null)
  private val mListOwnerId = MutableStateFlow<TodoListId>(TodoListId.Unknown)

  val screenState = newScreenState().stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5.seconds),
    initialValue = TodoListScreenState.Loading
  )

  fun onUserIntent(intent: UserIntent) =
    viewModelScope.launch {
      UserIntentHandler().handle(intent)
    }

  private inner class UserIntentHandler {

    suspend fun handle(intent: UserIntent) {
      when (intent) {
        UserIntent.Back -> onBack()
        is UserIntent.ShowListItems -> onShowListItems(intent)
        is UserIntent.EditList -> onEditList(intent)
        is UserIntent.EditItem -> onEditItem(intent)
        is UserIntent.CancelEditing -> onCancelEditing()
        is UserIntent.ConfirmListEditing -> onConfirmListEditing(intent)
        is UserIntent.ConfirmItemEditing -> onConfirmItemEditing(intent)
        is UserIntent.Done -> onDone(intent)
        is UserIntent.DeleteList -> onDeleteList(intent)
        is UserIntent.DeleteItem -> onDeleteItem(intent)
        is UserIntent.ReorderLists -> onReorderLists(intent)
        is UserIntent.ReorderItems -> onReorderItems(intent)
      }
    }

    private fun onBack() {
      mListOwnerId.value = TodoListId.Unknown
    }

    private fun onShowListItems(intent: UserIntent.ShowListItems) {
      mListOwnerId.value = intent.listOwnerId
    }

    private suspend fun onEditList(intent: UserIntent.EditList) {
      if (intent.id == TodoListId.Unknown) {
        showEditDialog(TodoEditDialogState.List(title = "Новый список"))
        return
      }
      val list = mTodoRep.getListById(intent.id)
        ?: return
      showEditDialog(TodoEditDialogState.List(
        id = list.id,
        title = "Редактирование списка",
        text = list.text,
        isDeleteVisible = true,
      ))
    }

    private suspend fun onEditItem(intent: UserIntent.EditItem) {
      if (intent.id == TodoItemId.Unknown) {
        showEditDialog(TodoEditDialogState.Item(title = "Новый пункт"))
        return
      }
      val item = mTodoRep.getItemById(intent.id)
        ?: return
      showEditDialog(TodoEditDialogState.Item(
        id = item.id,
        title = "Редактирование пункта",
        text = item.text,
        isDeleteVisible = true,
      ))
    }

    private fun onCancelEditing() {
      hideEditDialog()
    }

    private fun onConfirmListEditing(intent: UserIntent.ConfirmListEditing) {
      viewModelScope.launch {
        if (intent.id == TodoListId.Unknown)
          mTodoRep.addList(TodoList(intent.text))
        else
          mTodoRep.updateList(intent.id, intent.text)
        hideEditDialog()
      }
    }

    private fun onConfirmItemEditing(intent: UserIntent.ConfirmItemEditing) {
      viewModelScope.launch {
        if (intent.id == TodoItemId.Unknown)
          mTodoRep.addItem(TodoItem(intent.text, isDone = false, listOwnerId = mListOwnerId.value))
        else
          mTodoRep.updateItem(intent.id, intent.text)
        hideEditDialog()
      }
    }

    private suspend fun onDone(intent: UserIntent.Done) {
      mTodoRep.doneItem(intent.id, intent.isDone)
    }

    private suspend fun onDeleteList(intent: UserIntent.DeleteList) {
      mTodoRep.removeList(intent.id)
      hideEditDialog()
    }

    private suspend fun onDeleteItem(intent: UserIntent.DeleteItem) {
      mTodoRep.removeItem(intent.id)
      hideEditDialog()
    }

    private suspend fun onReorderLists(intent: UserIntent.ReorderLists) {
      mTodoRep.reorderLists(intent.reordered)
    }

    private suspend fun onReorderItems(intent: UserIntent.ReorderItems) {
      mTodoRep.reorderItems(intent.reordered)
    }

    private fun showEditDialog(state: TodoEditDialogState) {
      mEditDialogState.value = state
    }

    private fun hideEditDialog() {
      mEditDialogState.value = null
    }
  }

  private fun newScreenState(): Flow<TodoListScreenState> {
    val listsFlow = mTodoRep.observeLists()
    val itemsFlow = mListOwnerId.flatMapLatest { newListOwnerId ->
      if (newListOwnerId == TodoListId.Unknown)
        flowOf(emptyList())
      else
        mTodoRep.observeItems(newListOwnerId)
    }
    return combine(
      listsFlow,
      itemsFlow,
      mEditDialogState,
    ) { lists, items, editDialogState ->
      val listOwnerId = mListOwnerId.value
      val isListsVisible = listOwnerId == TodoListId.Unknown
      val title = if (isListsVisible) "Списки" else lists.first { it.id == listOwnerId }.text
      TodoListScreenState.Success(
        lists = lists,
        items = items,
        title = title,
        isBackVisible = !isListsVisible,
        isListsVisible = isListsVisible,
        editDialog = editDialogState
      )
    }
  }
}
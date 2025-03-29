package alexx.rizz.mytodo.feature.todolist

import alexx.rizz.mytodo.app.utils.*
import alexx.rizz.mytodo.feature.*
import alexx.rizz.mytodo.feature.todolist.ui.*
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*
import kotlin.time.Duration.Companion.seconds

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
      }
    }

    private fun onBack() {
      mListOwnerId.value = TodoListId.Unknown
    }

    private fun onShowListItems(intent: UserIntent.ShowListItems) {
      mListOwnerId.value = intent.listOwnerId
    }

    private fun onEditList(intent: UserIntent.EditList) {
      if (intent.id == TodoListId.Unknown) {
        showEditDialog(TodoEditDialogState.List(title = "Новый список"))
        return
      }
      // TODO
    }

    private suspend fun onEditItem(intent: UserIntent.EditItem) {
      if (intent.id == TodoItemId.Unknown) {
        showEditDialog(TodoEditDialogState.Item(title = "Новый пункт"))
        return
      }
      val todo = mTodoRep.getItemById(intent.id)
        ?: return
      showEditDialog(TodoEditDialogState.Item(
        id = todo.id,
        title = "Редактирование пункта",
        text = todo.text
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
      val items = if (newListOwnerId == TodoListId.Unknown)
        flowOf(emptyList())
      else
        mTodoRep.observeItems(newListOwnerId)
      items.asResult()
    }
    return combine(
      listsFlow,
      itemsFlow,
      mListOwnerId,
      mEditDialogState,
    ) { lists, items, listOwnerId, editDialogState ->
      val listOwnerName =
        if (listOwnerId == TodoListId.Unknown)
          null
        else
          lists.first { it.id == listOwnerId }.text
      when (items) {
        is Result.Loading -> TodoListScreenState.Loading
        is Result.Success -> TodoListScreenState.Success(
          lists = lists,
          items = items.data,
          listOwnerName = listOwnerName,
          editDialog = editDialogState
        )
        is Result.Error -> TODO()
      }
    }
  }

}
package alexx.rizz.mytodo.feature.todolist

import alexx.rizz.mytodo.app.resources.*
import alexx.rizz.mytodo.feature.*
import alexx.rizz.mytodo.feature.keyvalue.*
import alexx.rizz.mytodo.feature.todolist.ui.*
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*
import kotlin.time.Duration.Companion.seconds

// private val Log = getLogger<TodoListVM>()

@HiltViewModel
class TodoListVM @Inject constructor(
  private val mResources: IResources,
  private val mKeyValueService: IKeyValueService,
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
  private var mListOwnerId = TodoListId.Unknown

  val screenState = newScreenState()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), initialValue = TodoListScreenState.Loading)

  fun onUserIntent(intent: UserIntent) =
    viewModelScope.launch {
      UserIntentHandler().handle(intent)
    }

  private fun newScreenState(): Flow<TodoListScreenState> {
    val listsFlow = mTodoRep.observeLists()
    val itemsFlow = mKeyValueService.observeCurrentListOwnerId()
      .flatMapLatest { newListOwnerId ->
        mListOwnerId = newListOwnerId // сохраняем для последующего использования
        if (newListOwnerId == TodoListId.Unknown)
          flowOf(emptyList())
        else
          mTodoRep.observeItems(newListOwnerId)
      }
    return combine(
      listsFlow, itemsFlow, mEditDialogState,
    ) { lists, items, editDialogState ->
      val showLists = mListOwnerId == TodoListId.Unknown
      val title = getScreenTitle(showLists, lists, mListOwnerId)
      if (showLists)
        TodoListScreenState.SuccessLists(lists, title, editDialogState)
      else
        TodoListScreenState.SuccessItems(items, title, editDialogState)
    }
  }

  private fun getScreenTitle(showLists: Boolean, lists: List<TodoList>, listOwnerId: TodoListId): String =
    if (showLists)
      mResources.getString(ResStringId.ListsTitle)
    else
      lists.first { it.id == listOwnerId }.text

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

    private suspend fun onBack() {
      setListOwnerId(TodoListId.Unknown)
    }

    private suspend fun onShowListItems(intent: UserIntent.ShowListItems) {
      setListOwnerId(intent.listOwnerId)
    }

    private suspend fun onEditList(intent: UserIntent.EditList) {
      if (intent.id == TodoListId.Unknown) {
        showEditDialog(TodoEditDialogState.List(title = mResources.getString(ResStringId.EditListDialogTitleNew)))
        return
      }
      val list = mTodoRep.getListById(intent.id)
        ?: return
      showEditDialog(TodoEditDialogState.List(
        id = list.id,
        title = mResources.getString(ResStringId.EditListDialogTitle),
        text = list.text,
        isDeleteVisible = true,
      ))
    }

    private suspend fun onEditItem(intent: UserIntent.EditItem) {
      if (intent.id == TodoItemId.Unknown) {
        showEditDialog(TodoEditDialogState.Item(title = mResources.getString(ResStringId.EditItemDialogTitleNew)))
        return
      }
      val item = mTodoRep.getItemById(intent.id)
        ?: return
      showEditDialog(TodoEditDialogState.Item(
        id = item.id,
        title = mResources.getString(ResStringId.EditItemDialogTitle),
        text = item.text,
        isDeleteVisible = true,
      ))
    }

    private fun onCancelEditing() {
      hideEditDialog()
    }

    private suspend fun onConfirmListEditing(intent: UserIntent.ConfirmListEditing) {
      if (intent.id == TodoListId.Unknown)
        mTodoRep.addList(TodoList(intent.text, doneCount = 0, itemCount = 0))
      else
        mTodoRep.updateList(intent.id, intent.text)
      hideEditDialog()
    }

    private suspend fun onConfirmItemEditing(intent: UserIntent.ConfirmItemEditing) {
      if (intent.id == TodoItemId.Unknown)
        mTodoRep.addItem(TodoItem(intent.text, isDone = false, listOwnerId = mListOwnerId))
      else
        mTodoRep.updateItem(intent.id, intent.text)
      hideEditDialog()
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

    private suspend fun setListOwnerId(id: TodoListId) {
      mKeyValueService.setCurrentListOwnerId(id)
    }
  }
}
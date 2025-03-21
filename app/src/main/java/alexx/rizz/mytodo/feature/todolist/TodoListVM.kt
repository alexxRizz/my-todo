package alexx.rizz.mytodo.feature.todolist

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
    data class EditCategory(val id: Int) : UserIntent
    data class EditTodo(val id: Int) : UserIntent
    data object CancelEditing : UserIntent
    data class ConfirmEditing(val id: Int, val text: String) : UserIntent
    data class Done(val id: Int, val isDone: Boolean) : UserIntent
  }

  private val mEditDialogState = MutableStateFlow<TodoEditDialogState?>(null)

  val screenState = combine(mTodoRep.getAll(), mEditDialogState) { allItems, editDialogState ->
    TodoListScreenState.LoadedSuccessfully(
      items = allItems,
      editDialog = editDialogState
    )
  }.stateIn(
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
        is UserIntent.EditCategory -> onEditCategory(intent)
        is UserIntent.EditTodo -> onEditTodo(intent)
        is UserIntent.CancelEditing -> onCancelEditing()
        is UserIntent.ConfirmEditing -> onConfirmEditing(intent)
        is UserIntent.Done -> onDone(intent)
        is UserIntent.EditTodo -> onEditTodo(intent)
      }
    }

    private fun onEditCategory(intent: UserIntent.EditCategory) {
      if (intent.id == 0) {
        showEditDialog(TodoEditDialogState.Category(title = "Новая категория"))
        return
      }
      // TODO
    }

    private suspend fun onEditTodo(intent: UserIntent.EditTodo) {
      if (intent.id == 0) {
        showEditDialog(TodoEditDialogState.Item(title = "Новое дело"))
        return
      }
      val todo = mTodoRep.getById(intent.id)
        ?: return
      showEditDialog(TodoEditDialogState.Item(
        id = todo.id,
        title = "Редактирование дела",
        text = todo.text
      ))
    }

    private fun onCancelEditing() {
      hideEditDialog()
    }

    private fun onConfirmEditing(intent: UserIntent.ConfirmEditing) {
      viewModelScope.launch {
        if (intent.id == 0)
          mTodoRep.addTodo(TodoItem(intent.text, isDone = false))
        else
          mTodoRep.updateTodo(intent.id, intent.text)
        hideEditDialog()
      }
    }

    private suspend fun onDone(intent: UserIntent.Done) {
      mTodoRep.done(intent.id, intent.isDone)
    }

    private fun showEditDialog(state: TodoEditDialogState) {
      mEditDialogState.value = state
    }

    private fun hideEditDialog() {
      mEditDialogState.value = null
    }
  }
}
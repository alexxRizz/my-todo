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
    data object AddCategory : UserIntent
    data object AddTodo : UserIntent
    data object CancelAdding : UserIntent
    data class ConfirmAdding(val todoText: String) : UserIntent
    data class Done(val todoId: Int, val isDone: Boolean) : UserIntent
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
        UserIntent.AddCategory -> onAddCategory()
        UserIntent.AddTodo -> onAddTodo()
        UserIntent.CancelAdding -> onAddingCancelled()
        is UserIntent.ConfirmAdding -> onAddingConfirmed(intent)
        is UserIntent.Done -> onToggleTodo(intent)
      }
    }

    private fun onAddCategory() {
      showEditDialog(TodoEditDialogState.Category("Категория"))
    }

    private fun onAddTodo() {
      showEditDialog(TodoEditDialogState.Item("Дело"))
    }

    private fun onAddingCancelled() {
      hideEditDialog()
    }

    private fun onAddingConfirmed(intent: UserIntent.ConfirmAdding) {
      hideEditDialog()
      viewModelScope.launch {
        mTodoRep.addTodo(TodoItem(intent.todoText, isDone = false))
      }
    }

    private suspend fun onToggleTodo(intent: UserIntent.Done) {
      mTodoRep.done(intent.todoId, intent.isDone)
    }

    private fun showEditDialog(state: TodoEditDialogState) {
      mEditDialogState.value = state
    }

    private fun hideEditDialog() {
      mEditDialogState.value = null
    }
  }
}
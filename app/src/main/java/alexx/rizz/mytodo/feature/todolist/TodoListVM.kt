package alexx.rizz.mytodo.feature.todolist

import alexx.rizz.mytodo.feature.*
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
  }

  private val mEditDialogState = MutableStateFlow<TodoEditDialogState?>(null)
  private var mAllItems = emptyList<TodoItem>()

  val screenState = combine(mTodoRep.getAll(), mEditDialogState) { allItems, editDialogState ->
    mAllItems = allItems
    TodoListScreenState.LoadedSuccessfully(
      items = allItems,
      editDialog = editDialogState
    )
  }.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5.seconds),
    initialValue = TodoListScreenState.Loading
  )

  private val mTodoList get() = (screenState.value as TodoListScreenState.LoadedSuccessfully).items

  fun onUserIntent(intent: UserIntent) =
    UserIntentHandler().handle(intent)

  private inner class UserIntentHandler {

    fun handle(intent: UserIntent) {
      when (intent) {
        UserIntent.AddCategory -> onAddCategory()
        UserIntent.AddTodo -> onAddTodo()
        UserIntent.CancelAdding -> onAddingCancelled()
        is UserIntent.ConfirmAdding -> onAddingConfirmed(intent)
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

    private fun showEditDialog(state: TodoEditDialogState) {
      mEditDialogState.value = state
    }

    private fun hideEditDialog() {
      mEditDialogState.value = null
    }
  }
}
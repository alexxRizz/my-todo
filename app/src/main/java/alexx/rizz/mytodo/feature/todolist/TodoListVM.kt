package alexx.rizz.mytodo.feature.todolist

import alexx.rizz.mytodo.feature.*
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.flow.*
import javax.inject.*
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class TodoListVM @Inject constructor(
  private val mTodoRep: ITodoRepository,
) : ViewModeBase() {

  val screenState = mTodoRep.getAll()
    .map { TodoListScreenState(items = it, isLoading = false) }
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5.seconds),
      initialValue = TodoListScreenState(isLoading = true)
    )

  fun onAddCategory() {
  }

  fun onAddTodo() {

  }
}
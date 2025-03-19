package alexx.rizz.mytodo.features.todolist

import alexx.rizz.mytodo.features.*
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.flow.*
import javax.inject.*
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class TodoListVM @Inject constructor() : ViewModeBase() {

  private val mScreenState = MutableStateFlow(TodoListScreenState())
  val screenState = mScreenState.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5.seconds),
    initialValue = TodoListScreenState()
  )

  init {
    mScreenState.update { it.copy(items = listOf(
      TodoItem(1, 1, "Todo 1", isDone = false),
      TodoItem(2, 2, "Todo 2", isDone = false),
      TodoItem(3, 3, "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", isDone = true),
    )) }
  }
}
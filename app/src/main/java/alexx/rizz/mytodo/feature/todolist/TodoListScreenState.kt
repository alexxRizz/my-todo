package alexx.rizz.mytodo.feature.todolist

data class TodoListScreenState(
  val items: List<TodoItem> = emptyList(),
  val isLoading: Boolean = false,
)
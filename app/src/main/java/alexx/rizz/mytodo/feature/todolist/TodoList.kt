package alexx.rizz.mytodo.feature.todolist

data class TodoList(
  val text: String,
  val id: TodoListId = TodoListId.Unknown,
)


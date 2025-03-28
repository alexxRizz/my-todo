package alexx.rizz.mytodo.feature.todolist

data class TodoItem(
  val text: String,
  val isDone: Boolean,
  val listOwnerId: TodoListId,
  val id: TodoItemId = TodoItemId.Unknown,
)
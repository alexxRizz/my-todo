package alexx.rizz.mytodo.features.todolist

data class TodoItem(
  val id: Int,
  val orderNumber: Int,
  val text: String,
  val isDone: Boolean
)
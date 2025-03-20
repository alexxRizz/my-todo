package alexx.rizz.mytodo.feature.todolist

data class TodoItem(
  val id: Int,
  val orderNumber: Int,
  val text: String,
  val isDone: Boolean
)
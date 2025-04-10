package alexx.rizz.mytodo.feature.todolist

data class TodoList(
  val text: String,
  val doneCount: Int,
  val itemCount: Int,
  val id: TodoListId = TodoListId.Unknown,
)

fun TodoList.isAllDone(): Boolean =
  doneCount == itemCount && itemCount > 0
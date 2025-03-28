package alexx.rizz.mytodo.feature.todolist

@JvmInline
value class TodoListId(val asInt: Int) {

  companion object {
    val Unknown = TodoListId(0)
  }

  override fun toString(): String = asInt.toString()
}

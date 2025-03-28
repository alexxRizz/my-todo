package alexx.rizz.mytodo.feature.todolist

@JvmInline
value class TodoItemId(val asInt: Int) {

  companion object {
    val Unknown = TodoItemId(0)
  }

  override fun toString(): String = asInt.toString()
}

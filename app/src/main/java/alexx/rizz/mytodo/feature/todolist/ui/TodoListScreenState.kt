package alexx.rizz.mytodo.feature.todolist.ui

import alexx.rizz.mytodo.feature.todolist.*

sealed interface TodoListScreenState {

  object Loading : TodoListScreenState

  data class LoadedSuccessfully(
    val items: List<TodoItem> = emptyList(),
    val editDialog: TodoEditDialogState? = null,
  ) : TodoListScreenState
}

sealed interface TodoEditDialogState {

  val id: Int
  val title: String
  val text: String

  data class Item(
    override val id: Int = 0,
    override val title: String = "",
    override val text: String = "",
  ) : TodoEditDialogState

  data class Category(
    override val id: Int = 0,
    override val title: String = "",
    override val text: String = "",
  ) : TodoEditDialogState
}

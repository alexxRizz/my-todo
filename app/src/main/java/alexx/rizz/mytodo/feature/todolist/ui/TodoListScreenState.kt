package alexx.rizz.mytodo.feature.todolist.ui

import alexx.rizz.mytodo.feature.todolist.*

sealed interface TodoListScreenState {

  data object Loading : TodoListScreenState

  sealed interface Success : TodoListScreenState {
    val title: String
    val editDialog: TodoEditDialogState?
  }

  data class SuccessLists(
    val lists: List<TodoList> = emptyList(),
    override val title: String = "",
    override val editDialog: TodoEditDialogState? = null,
  ) : Success

  data class SuccessItems(
    val items: List<TodoItem> = emptyList(),
    override val title: String = "",
    override val editDialog: TodoEditDialogState? = null,
  ) : Success
}

sealed interface TodoEditDialogState {

  data class Item(
    val id: TodoItemId = TodoItemId.Unknown,
    val title: String = "",
    val text: String = "",
    val isDeleteVisible: Boolean = false,
  ) : TodoEditDialogState

  data class List(
    val id: TodoListId = TodoListId.Unknown,
    val title: String = "",
    val text: String = "",
    val isDeleteVisible: Boolean = false,
  ) : TodoEditDialogState
}

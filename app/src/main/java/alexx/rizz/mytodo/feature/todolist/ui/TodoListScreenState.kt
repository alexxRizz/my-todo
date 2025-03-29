package alexx.rizz.mytodo.feature.todolist.ui

import alexx.rizz.mytodo.feature.todolist.*

sealed interface TodoListScreenState {

  data object Loading : TodoListScreenState

  // TODO: разделить состояние на SuccessLists и SuccessItems ?
  data class Success(
    val lists: List<TodoList> = emptyList(),
    val items: List<TodoItem> = emptyList(),
    val title: String = "",
    val isBackVisible: Boolean = false,
    val isListsVisible: Boolean = false,
    val editDialog: TodoEditDialogState? = null,
  ) : TodoListScreenState
}

sealed interface TodoEditDialogState {

  data class Item(
    val id: TodoItemId = TodoItemId.Unknown,
    val title: String = "",
    val text: String = "",
  ) : TodoEditDialogState

  data class List(
    val id: TodoListId = TodoListId.Unknown,
    val title: String = "",
    val text: String = "",
  ) : TodoEditDialogState
}

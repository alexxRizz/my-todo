package alexx.rizz.mytodo.feature.todolist.ui

import alexx.rizz.mytodo.feature.todolist.*

sealed interface TodoListScreenState {

  object Loading : TodoListScreenState

  data class LoadedSuccessfully(
    val lists: List<TodoList> = emptyList(),
    val items: List<TodoItem> = emptyList(),
    val listOwnerName: String? = null,
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

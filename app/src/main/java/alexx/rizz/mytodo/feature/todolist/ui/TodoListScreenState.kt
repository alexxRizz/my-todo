package alexx.rizz.mytodo.feature.todolist.ui

import alexx.rizz.mytodo.feature.common.*
import alexx.rizz.mytodo.feature.todolist.*

sealed interface TodoListScreenState {

  data object Loading : TodoListScreenState

  data class Success(
    val title: String,
    val listState: SuccessListState
  ) : TodoListScreenState {

    val navButtonType: NavButtonType
      get() = when (listState.content.contentType) {
        ListContentType.Lists -> NavButtonType.Menu
        ListContentType.Items -> NavButtonType.Back
      }
  }

  enum class ListContentType {
    /** Отображаются списки (названия) */
    Lists,
    /** Отображаются содержимое списков (айтемы) */
    Items,
  }

  data class SuccessListState(
    val content: SuccessListContent,
    val editDialog: TodoEditDialogState?
  )

  sealed interface SuccessListContent {
    val contentType: ListContentType
  }

  data class SuccessLists(
    val lists: List<TodoList> = emptyList(),
  ) : SuccessListContent {

    override val contentType = ListContentType.Lists
  }

  data class SuccessItems(
    val items: List<TodoItem> = emptyList(),
  ) : SuccessListContent {

    override val contentType = ListContentType.Items
  }
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

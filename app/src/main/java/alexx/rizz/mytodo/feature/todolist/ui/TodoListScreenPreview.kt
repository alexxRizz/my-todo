package alexx.rizz.mytodo.feature.todolist.ui

import alexx.rizz.mytodo.feature.todolist.*
import alexx.rizz.mytodo.ui.theme.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*

private class TodoListScreenPreviewParameterProvider : PreviewParameterProvider<TodoListScreenState> {
  override val values = sequenceOf(
    TodoListScreenState.Loading,
    TodoListScreenState.SuccessLists(emptyList()),
    TodoListScreenState.SuccessItems(emptyList()),
    TodoListScreenState.SuccessLists(
      lists = List(20) {
        val i = it + 1
        val text = if (i % 3 == 0) "Lorem Ipsum is simply dummy text of the printing and typesetting industry." else "Todo $i"
        TodoList(text, TodoListId(i))
      },
    ),
    TodoListScreenState.SuccessItems(
      items = List(20) {
        val i = it + 1
        val text = if (i % 3 == 0) "Lorem Ipsum is simply dummy text of the printing and typesetting industry." else "Todo $i"
        TodoItem(text, isDone = i % 2 == 0, TodoListId(i), TodoItemId(i))
      }
    )
  )
}

@Composable
@Preview(locale = "ru", showBackground = true, showSystemUi = true)
@Preview(locale = "en", showBackground = true, showSystemUi = true)
private fun TodoListScreenPreview(
  @PreviewParameter(TodoListScreenPreviewParameterProvider::class)
  screenState: TodoListScreenState
) {
  MyTodoTheme {
    TodoListScreenContent(
      Modifier.safeDrawingPadding(),
      screenState,
      onUserIntent = {},
    )
  }
}
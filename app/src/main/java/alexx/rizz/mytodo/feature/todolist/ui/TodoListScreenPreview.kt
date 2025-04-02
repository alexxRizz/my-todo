package alexx.rizz.mytodo.feature.todolist.ui

import alexx.rizz.mytodo.feature.todolist.TodoItem
import alexx.rizz.mytodo.feature.todolist.TodoItemId
import alexx.rizz.mytodo.feature.todolist.TodoListId
import alexx.rizz.mytodo.ui.theme.MyTodoTheme
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

private class TodoListScreenPreviewParameterProvider : PreviewParameterProvider<TodoListScreenState> {
  override val values = sequenceOf(
    TodoListScreenState.Loading,
    TodoListScreenState.Success(emptyList()),
    TodoListScreenState.Success(
      items = List(20) {
        val i = it + 1
        val text = if (i % 3 == 0) "Lorem Ipsum is simply dummy text of the printing and typesetting industry." else "Todo $i"
        TodoItem(text, isDone = i % 2 == 0, TodoListId(i), TodoItemId(i))
      }
    )
  )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun TodoListScreenPreview(
  @PreviewParameter(TodoListScreenPreviewParameterProvider::class) screenState: TodoListScreenState
) {
  MyTodoTheme {
    TodoListScreenContent(
      Modifier.safeDrawingPadding(),
      screenState,
      onUserIntent = {},
    )
  }
}
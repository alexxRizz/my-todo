package alexx.rizz.mytodo.feature.todolist.ui.components

import alexx.rizz.mytodo.R
import alexx.rizz.mytodo.feature.todolist.*
import alexx.rizz.mytodo.feature.todolist.TodoListVM.*
import alexx.rizz.mytodo.feature.todolist.ui.*
import alexx.rizz.mytodo.ui.*
import alexx.rizz.mytodo.ui.theme.*
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*

@Composable
fun TodoListSuccess(
  modifier: Modifier,
  listState: TodoListScreenState.SuccessListState,
  onUserIntent: (UserIntent) -> Unit,
) {
  Column(modifier
    .fillMaxSize()
    .padding(10.dp, 5.dp, 10.dp, 10.dp)
  ) {
    val initialState = if (isPreview) listState.content else null // иначе в Preview будет пусто
    rememberTransition(initialState = initialState, targetState = listState.content).AnimatedContent(
      contentKey = { it?.contentType }, // иначе анимация будет срабатывать при добавлении / удалении айтемов
      transitionSpec = { commonInAndOutTransform() }
    ) { content ->
      when (content) {
        is TodoListScreenState.SuccessLists ->
          TodoListsOrEmpty(Modifier.weight(1f), content.lists, onUserIntent)
        is TodoListScreenState.SuccessItems ->
          TodoItemsOrEmpty(Modifier.weight(1f), content.items, onUserIntent)
        null -> Unit
      }
    }
    val editDialog = listState.editDialog
    if (editDialog != null)
      TodoEditDialog(editDialog, onUserIntent)
  }
}

@Composable
private fun TodoListsOrEmpty(
  modifier: Modifier,
  lists: List<TodoList>,
  onUserIntent: (UserIntent) -> Unit
) {
  if (lists.isEmpty())
    TodoListEmpty(modifier, stringResource(R.string.no_lists_prompt))
  else
    TodoLists(
      modifier,
      lists,
      onClick = { id -> onUserIntent(UserIntent.ShowListItems(id)) },
      onEditClick = { id -> onUserIntent(UserIntent.EditList(id)) },
      onDragStopped = { onUserIntent(it) },
    )
}

@Composable
private fun TodoItemsOrEmpty(
  modifier: Modifier,
  items: List<TodoItem>,
  onUserIntent: (UserIntent) -> Unit
) {
  if (items.isEmpty())
    TodoListEmpty(modifier, stringResource(R.string.no_items_prompt))
  else
    TodoItems(
      modifier,
      items,
      onDoneClick = { id, isDone -> onUserIntent(UserIntent.Done(id, isDone)) },
      onEditClick = { id -> onUserIntent(UserIntent.EditItem(id)) },
      onDragStopped = { onUserIntent(it) },
    )
}

@Composable
private fun TodoListEmpty(modifier: Modifier, title: String) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    Text(
      title,
      textAlign = TextAlign.Center,
      color = MyColors.Tertiary,
      fontSize = 25.sp,
    )
  }
}

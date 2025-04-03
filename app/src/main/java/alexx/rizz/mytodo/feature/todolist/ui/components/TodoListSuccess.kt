package alexx.rizz.mytodo.feature.todolist.ui.components

import alexx.rizz.mytodo.R
import alexx.rizz.mytodo.feature.todolist.TodoListVM.*
import alexx.rizz.mytodo.feature.todolist.ui.*
import alexx.rizz.mytodo.ui.theme.*
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
  screenState: TodoListScreenState.Success,
  onUserIntent: (UserIntent) -> Unit,
) {
  Column(modifier
    .fillMaxSize()
    .padding(10.dp)
  ) {
    when (screenState) {
      is TodoListScreenState.SuccessLists ->
        TodoListsOrEmpty(Modifier.weight(1f), screenState, onUserIntent)
      is TodoListScreenState.SuccessItems ->
        TodoItemsOrEmpty(Modifier.weight(1f), screenState, onUserIntent)
    }
    val editDialog = screenState.editDialog
    if (editDialog != null)
      TodoEditDialog(editDialog, onUserIntent)
  }
}

@Composable
private fun TodoListsOrEmpty(
  modifier: Modifier,
  screenState: TodoListScreenState.SuccessLists,
  onUserIntent: (UserIntent) -> Unit
) {
  if (screenState.lists.isEmpty())
    TodoListEmpty(modifier, stringResource(R.string.no_lists_prompt))
  else
    TodoLists(
      modifier,
      screenState.lists,
      onClick = { id -> onUserIntent(UserIntent.ShowListItems(id)) },
      onEditClick = { id -> onUserIntent(UserIntent.EditList(id)) },
      onDragStopped = { onUserIntent(it) },
    )
}

@Composable
private fun TodoItemsOrEmpty(
  modifier: Modifier,
  screenState: TodoListScreenState.SuccessItems,
  onUserIntent: (UserIntent) -> Unit
) {
  if (screenState.items.isEmpty())
    TodoListEmpty(modifier, stringResource(R.string.no_items_prompt))
  else
    TodoItems(
      modifier,
      screenState.items,
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
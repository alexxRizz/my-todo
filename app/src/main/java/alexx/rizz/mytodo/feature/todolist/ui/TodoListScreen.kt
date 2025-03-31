package alexx.rizz.mytodo.feature.todolist.ui

import alexx.rizz.mytodo.feature.todolist.*
import alexx.rizz.mytodo.feature.todolist.TodoListVM.*
import alexx.rizz.mytodo.ui.*
import alexx.rizz.mytodo.ui.theme.*
import androidx.activity.compose.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.*
import androidx.lifecycle.compose.*

private val RowPadding = PaddingValues(10.dp, 5.dp, 0.dp, 5.dp)

@Composable
fun TodoListScreen(vm: TodoListVM = hiltViewModel()) {
  val screenState by vm.screenState.collectAsStateWithLifecycle()
  val successState = screenState as? TodoListScreenState.Success
  val isBackVisible = successState?.isBackVisible == true
  BackHandler(enabled = isBackVisible) {
    vm.onUserIntent(UserIntent.Back)
  }
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TodoListTopBar(
        isBackVisible = isBackVisible,
        title = successState?.title ?: "",
        onBack = { vm.onUserIntent(UserIntent.Back) }
      )
    },
    floatingActionButton = {
      if (successState != null)
        TodoListFloatingActionButton(successState.isListsVisible, vm::onUserIntent)
    }
  ) { innerPadding ->
    TodoListScreenContent(
      Modifier.padding(innerPadding),
      screenState,
      vm::onUserIntent,
    )
  }
}

@Composable
private fun TodoListScreenContent(
  modifier: Modifier,
  screenState: TodoListScreenState,
  onUserIntent: (UserIntent) -> Unit,
) {
  when (screenState) {
    TodoListScreenState.Loading -> TodoListLoading(modifier)
    is TodoListScreenState.Success -> TodoListLoadedSuccessfully(modifier, screenState, onUserIntent)
  }
}

@Composable
private fun TodoListLoading(modifier: Modifier) {
  Column(
    modifier
      .fillMaxSize()
      .padding(10.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    CircularProgressIndicator(
      modifier = Modifier.width(64.dp),
      color = Color.White,
      trackColor = MyColors.Primary,
    )
  }
}

@Composable
private fun TodoListLoadedSuccessfully(
  modifier: Modifier,
  screenState: TodoListScreenState.Success,
  onUserIntent: (UserIntent) -> Unit,
) {
  Column(modifier
    .fillMaxSize()
    .padding(10.dp)
  ) {
    TodoList(
      Modifier.weight(1f),
      screenState.lists,
      screenState.items,
      screenState.isListsVisible,
      onListClick = { id -> onUserIntent(UserIntent.ShowListItems(id)) },
      onListEditClick = { id -> onUserIntent(UserIntent.EditList(id)) },
      onItemDoneClick = { id, isDone -> onUserIntent(UserIntent.Done(id, isDone)) },
      onItemEditClick = { id -> onUserIntent(UserIntent.EditItem(id)) },
    )
    when (screenState.editDialog) {
      null -> Unit
      is TodoEditDialogState.List ->
        TodoItemEditDialog(
          title = screenState.editDialog.title,
          text = screenState.editDialog.text,
          isDeleteVisible = screenState.editDialog.isDeleteVisible,
          onCancel = { onUserIntent(UserIntent.CancelEditing) },
          onOk = { text -> onUserIntent(UserIntent.ConfirmListEditing(screenState.editDialog.id, text)) },
          onDelete = { onUserIntent(UserIntent.DeleteList(screenState.editDialog.id)) },
        )

      is TodoEditDialogState.Item ->
        TodoItemEditDialog(
          title = screenState.editDialog.title,
          text = screenState.editDialog.text,
          isDeleteVisible = screenState.editDialog.isDeleteVisible,
          onCancel = { onUserIntent(UserIntent.CancelEditing) },
          onOk = { text -> onUserIntent(UserIntent.ConfirmItemEditing(screenState.editDialog.id, text)) },
          onDelete = { onUserIntent(UserIntent.DeleteItem(screenState.editDialog.id)) },
        )
    }
  }
}

@Composable
private fun TodoList(
  modifier: Modifier,
  lists: List<TodoList>,
  items: List<TodoItem>,
  useListsOrItems: Boolean,
  onListClick: (TodoListId) -> Unit,
  onListEditClick: (TodoListId) -> Unit,
  onItemDoneClick: (TodoItemId, Boolean) -> Unit,
  onItemEditClick: (TodoItemId) -> Unit,
) {
  if ((useListsOrItems && lists.isEmpty()) || (!useListsOrItems && items.isEmpty()))
    TodoListEmpty(modifier, useListsOrItems)
  else if (useListsOrItems)
    TodoLists(modifier, lists, onListClick, onListEditClick)
  else
    TodoItems(modifier, items, onItemDoneClick, onItemEditClick)
}

@Composable
private fun TodoListEmpty(modifier: Modifier, useListsOrItems: Boolean) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    Text(
      if (useListsOrItems) "Добавьте список" else "Добавьте пункт",
      textAlign = TextAlign.Center,
      color = MyColors.Tertiary,
      fontSize = 25.sp,
    )
  }
}

@Composable
private fun TodoLists(
  modifier: Modifier,
  lists: List<TodoList>,
  onClick: (TodoListId) -> Unit,
  onEditClick: (TodoListId) -> Unit,
) {
  val todoListState = rememberLazyListState()
  LazyColumn(
    modifier,
    state = todoListState,
    verticalArrangement = Arrangement.spacedBy(7.dp)
  ) {
    items(lists, key = { it.id.asInt }) {
      ListRow(it, onClick = onClick, onEditClick = onEditClick)
    }
  }
}

@Composable
private fun TodoItems(
  modifier: Modifier,
  items: List<TodoItem>,
  onDoneClick: (TodoItemId, Boolean) -> Unit,
  onEditClick: (TodoItemId) -> Unit,
) {
  val todoListState = rememberLazyListState()
  LazyColumn(
    modifier,
    state = todoListState,
    verticalArrangement = Arrangement.spacedBy(7.dp)
  ) {
    items(items, key = { it.id.asInt }) {
      ItemRow(
        it,
        onDoneClick = { isDone -> onDoneClick(it.id, isDone) },
        onEditClick = { onEditClick(it.id) },
      )
    }
  }
}

@Composable
private fun ListRow(
  list: TodoList,
  onClick: (TodoListId) -> Unit,
  onEditClick: (TodoListId) -> Unit,
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(5.dp),
    colors = CardDefaults.cardColors(containerColor = MyColors.UndoneCard),
    onClick = { onClick(list.id) },
  ) {
    Row(
      Modifier.padding(RowPadding),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(list.text, fontSize = 18.sp)
      Spacer(Modifier.weight(1f))
      CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
        IconButton(onClick = { onEditClick(list.id) }) { Icon(Icons.Default.Edit, null) }
      }
    }
  }
}

@Composable
private fun ItemRow(
  item: TodoItem,
  onDoneClick: (Boolean) -> Unit,
  onEditClick: () -> Unit,
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(5.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (item.isDone) MyColors.DoneCard else MyColors.UndoneCard,
    ),
  ) {
    Row(
      Modifier.padding(RowPadding),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 20.dp) {
        Checkbox(
          item.isDone,
          colors = CheckboxDefaults.colors(checkedColor = MyColors.UndoneCard),
          modifier = Modifier
            .scale(1.5f)
            .padding(vertical = 5.dp),
          onCheckedChange = onDoneClick,
        )
      }
      Spacer(Modifier.width(10.dp))
      Text(item.text, fontSize = 18.sp, modifier =
        Modifier
          .conditional(item.isDone, {
            val crossColor = MyColors.UndoneCard
            val strokeWidth = 12f
            drawBehind {
              drawLine(crossColor, Offset(0f, 0f), Offset(size.width, size.height), strokeWidth = strokeWidth)
              drawLine(crossColor, Offset(0f, size.height), Offset(size.width, 0f), strokeWidth = strokeWidth)
            }
          })
      )
      Spacer(Modifier.weight(1f))
      CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
        IconButton(onEditClick) { Icon(Icons.Default.Edit, null) }
      }
    }
  }
}

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
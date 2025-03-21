package alexx.rizz.mytodo.feature.todolist.ui

import alexx.rizz.mytodo.feature.todolist.*
import alexx.rizz.mytodo.feature.todolist.TodoListVM.*
import alexx.rizz.mytodo.ui.*
import alexx.rizz.mytodo.ui.theme.*
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
import androidx.lifecycle.compose.*
import androidx.lifecycle.viewmodel.compose.*

@Composable
fun TodoListScreen(
  modifier: Modifier,
  vm: TodoListVM = viewModel()
) {
  val screenState by vm.screenState.collectAsStateWithLifecycle()
  TodoListScreenContent(
    modifier,
    screenState,
    vm::onUserIntent,
  )
}

@Composable
private fun TodoListScreenContent(
  modifier: Modifier,
  screenState: TodoListScreenState,
  onUserIntent: (UserIntent) -> Unit,
) {
  when (screenState) {
    TodoListScreenState.Loading -> TodoListLoading(modifier)
    is TodoListScreenState.LoadedSuccessfully -> TodoListLoadedSuccessfully(modifier, screenState, onUserIntent)
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
  screenState: TodoListScreenState.LoadedSuccessfully,
  onUserIntent: (UserIntent) -> Unit,
) {
  Column(modifier
    .fillMaxSize()
    .padding(10.dp)
  ) {
    TodoList(
      Modifier.weight(1f),
      screenState.items,
      onItemDoneClick = { todoId, isDone -> onUserIntent(UserIntent.Done(todoId, isDone)) },
      onItemClick = { todoId -> onUserIntent(UserIntent.EditTodo(todoId)) }
    )
    BottomActions(
      onAddCategory = { onUserIntent(UserIntent.EditCategory(id = 0)) },
      onAddTodo = { onUserIntent(UserIntent.EditTodo(id = 0)) },
    )
    if (screenState.editDialog != null)
      TodoItemEditDialog(
        title = screenState.editDialog.title,
        text = screenState.editDialog.text,
        onCancel = { onUserIntent(UserIntent.CancelEditing) },
        onOk = { onUserIntent(UserIntent.ConfirmEditing(screenState.editDialog.id, it)) },
      )
  }
}

@Composable
private fun TodoList(
  modifier: Modifier,
  items: List<TodoItem>,
  onItemDoneClick: (Int, Boolean) -> Unit,
  onItemClick: (Int) -> Unit,
) {
  if (items.isEmpty())
    TodoListEmpty(modifier)
  else
    TodoListWithItems(modifier, items, onItemDoneClick, onItemClick)
}

@Composable
private fun TodoListEmpty(modifier: Modifier) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    Text(
      "Добавьте категорию\nили дело",
      textAlign = TextAlign.Center,
      color = MyColors.Tertiary,
      fontSize = 25.sp,
    )
  }
}

@Composable
private fun TodoListWithItems(
  modifier: Modifier,
  items: List<TodoItem>,
  onItemDoneClick: (Int, Boolean) -> Unit,
  onItemClick: (Int) -> Unit,
) {
  val todoListState = rememberLazyListState()
  LazyColumn(
    modifier,
    state = todoListState,
    verticalArrangement = Arrangement.spacedBy(7.dp)
  ) {
    items(items, key = { it.id }) {
      TodoRow(
        it,
        onDoneClick = { isDone -> onItemDoneClick(it.id, isDone) },
        onClick = { onItemClick(it.id) },
      )
    }
  }
}

@Composable
private fun TodoRow(
  item: TodoItem,
  onDoneClick: (Boolean) -> Unit,
  onClick: () -> Unit,
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(5.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (item.isDone) MyColors.DoneCard else MyColors.UndoneCard,
    ),
    onClick = onClick
  ) {
    Row(
      Modifier
        .padding(10.dp, 5.dp, 7.dp, 5.dp),
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
      // Spacer(Modifier.weight(1f))
      // TodoItemContextMenu()
    }
  }
}

@Composable
private fun BottomActions(
  onAddCategory: () -> Unit,
  onAddTodo: () -> Unit,
) {
  Row(
    Modifier
      .fillMaxWidth()
      .padding(top = 3.dp),
    horizontalArrangement = Arrangement.spacedBy(15.dp),
  ) {
    Button(
      onClick = onAddCategory,
      modifier = Modifier.weight(1f),
      shape = RoundedCornerShape(10.dp),
      contentPadding = ButtonDefaults.ContentPadding.copy(top = 10.dp, bottom = 10.dp),
      colors = ButtonDefaults.buttonColors(containerColor = MyColors.Tertiary),
    ) {
      Icon(Icons.Default.Add, contentDescription = null)
      Spacer(Modifier.width(10.dp))
      Text("Категория", fontSize = 18.sp)
    }
    Button(
      onClick = onAddTodo,
      modifier = Modifier.weight(1f),
      shape = RoundedCornerShape(10.dp),
      contentPadding = ButtonDefaults.ContentPadding.copy(top = 10.dp, bottom = 10.dp),
    ) {
      Icon(Icons.Default.Add, contentDescription = null)
      Spacer(Modifier.width(10.dp))
      Text("Дело", fontSize = 18.sp)
    }
  }
}

private class TodoListScreenPreviewParameterProvider : PreviewParameterProvider<TodoListScreenState> {
  override val values = sequenceOf(
    TodoListScreenState.Loading,
    TodoListScreenState.LoadedSuccessfully(emptyList()),
    TodoListScreenState.LoadedSuccessfully(
      List(20) {
        val i = it + 1
        val text = if (i % 3 == 0) "Lorem Ipsum is simply dummy text of the printing and typesetting industry." else "Todo $i"
        TodoItem(text, isDone = i % 2 == 0, i)
      }
    )
  )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun TodoListScreenPreview(
  @PreviewParameter(TodoListScreenPreviewParameterProvider::class) screenState: TodoListScreenState
) {
  MyToDoTheme {
    TodoListScreenContent(
      Modifier.safeDrawingPadding(),
      screenState,
      onUserIntent = {},
    )
  }
}
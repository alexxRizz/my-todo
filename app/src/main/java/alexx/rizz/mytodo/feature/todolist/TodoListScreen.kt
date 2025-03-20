package alexx.rizz.mytodo.feature.todolist

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
    vm::onAddCategory,
    vm::onAddTodo,
  )
}

@Composable
private fun TodoListScreenContent(
  modifier: Modifier,
  screenState: TodoListScreenState,
  onAddCategory: () -> Unit,
  onAddTodo: () -> Unit,
) {
  if (screenState.isLoading) {
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
    return
  }
  Column(modifier
    .fillMaxSize()
    .padding(10.dp)
  ) {
    TodoList(Modifier.weight(1f), screenState.items)
    BottomActions(onAddCategory, onAddTodo)
  }
}

@Composable
private fun TodoList(
  modifier: Modifier,
  items: List<TodoItem>,
) {
  if (items.isNotEmpty())
    TodoListWithItems(modifier, items)
  else
    TodoListEmpty(modifier)
}

@Composable
private fun TodoListWithItems(modifier: Modifier, items: List<TodoItem>) {
  val todoListState = rememberLazyListState()
  LazyColumn(
    modifier,
    state = todoListState,
    verticalArrangement = Arrangement.spacedBy(7.dp)
  ) {
    items(items, key = { it.id }) {
      TodoRow(it)
    }
  }
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
private fun TodoRow(
  item: TodoItem,
  onDoneClick: (TodoItem) -> Unit = {},
) {
  Card(
    Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(
      containerColor = if (item.isDone) MyColors.DoneCard else MyColors.UndoneCard,
    )
  ) {
    Row(
      Modifier
        .fillMaxSize()
        .padding(3.dp, 10.dp, 10.dp, 10.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Checkbox(
        item.isDone,
        colors = CheckboxDefaults.colors(checkedColor = MyColors.UndoneCard),
        modifier = Modifier.scale(1.5f),
        onCheckedChange = { }
      )
      Spacer(Modifier.width(5.dp))
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
      modifier = Modifier.weight(1f),
      shape = RoundedCornerShape(10.dp),
      contentPadding = ButtonDefaults.ContentPadding.copy(top = 10.dp, bottom = 10.dp),
      colors = ButtonDefaults.buttonColors(containerColor = MyColors.Tertiary),
      onClick = onAddCategory,
    ) {
      Icon(Icons.Default.Add, contentDescription = null)
      Spacer(Modifier.width(10.dp))
      Text("Категория", fontSize = 18.sp)
    }
    Button(
      modifier = Modifier.weight(1f),
      shape = RoundedCornerShape(10.dp),
      contentPadding = ButtonDefaults.ContentPadding.copy(top = 10.dp, bottom = 10.dp),
      onClick = onAddTodo,
    ) {
      Icon(Icons.Default.Add, contentDescription = null)
      Spacer(Modifier.width(10.dp))
      Text("Дело", fontSize = 18.sp)
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TodoListScreenPreview() {
  TodoListScreenPreviewImpl(
    TodoListScreenState(
      List(20) {
        val i = it + 1
        val text = if (i % 3 == 0) "Lorem Ipsum is simply dummy text of the printing and typesetting industry." else "Todo $i"
        TodoItem(i, i, text, isDone = i % 2 == 0)
      }
    ))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EmptyTodoListScreenPreview() {
  TodoListScreenPreviewImpl(TodoListScreenState(emptyList()))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoadingTodoListScreenPreview() {
  TodoListScreenPreviewImpl(TodoListScreenState(emptyList(), isLoading = true))
}

@Composable
private fun TodoListScreenPreviewImpl(screenState: TodoListScreenState) {
  MyToDoTheme {
    TodoListScreenContent(
      Modifier.safeDrawingPadding(),
      screenState,
      {},
      {}
    )
  }
}
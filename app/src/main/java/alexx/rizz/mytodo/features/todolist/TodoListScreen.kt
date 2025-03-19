package alexx.rizz.mytodo.features.todolist

import alexx.rizz.mytodo.ui.theme.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
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
  TodoListScreenContent(modifier, screenState.items)
}

@Composable
fun TodoListScreenContent(modifier: Modifier, items: List<TodoItem>) {
  TodoList(modifier, items)
}

@Composable
fun TodoList(
  modifier: Modifier,
  items: List<TodoItem>,
) {
  LazyColumn(
    modifier.padding(10.dp),
    verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
    items(
      items,
      key = { it.id }
    ) {
      TodoRow(it)
    }
  }
}

@Composable
fun TodoRow(
  item: TodoItem,
  onItemClick: (TodoItem) -> Unit = {},
) {
  Card(
    Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
    )
  ) {
    Row(
      Modifier.fillMaxSize().padding(15.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(item.text)
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TodoListScreenPreview() {
  MyToDoTheme {
    TodoListScreenContent(
      Modifier.safeDrawingPadding(),
      listOf(
        TodoItem(1, 1, "Todo 1", isDone = false),
        TodoItem(2, 2, "Todo 2", isDone = false),
        TodoItem(3, 3, "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", isDone = true),
      )
    )
  }
}
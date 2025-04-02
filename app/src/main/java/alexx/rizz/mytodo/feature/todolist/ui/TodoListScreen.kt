package alexx.rizz.mytodo.feature.todolist.ui

import alexx.rizz.mytodo.feature.todolist.*
import alexx.rizz.mytodo.feature.todolist.TodoListVM.*
import alexx.rizz.mytodo.ui.theme.*
import androidx.activity.compose.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.*
import androidx.lifecycle.compose.*

object TodoListScreenCommon {
  val RowPadding = PaddingValues(10.dp, 5.dp, 0.dp, 5.dp)
}

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
fun TodoListScreenContent(
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
      onListDragStopped = { onUserIntent(it) },
      onItemDoneClick = { id, isDone -> onUserIntent(UserIntent.Done(id, isDone)) },
      onItemEditClick = { id -> onUserIntent(UserIntent.EditItem(id)) },
      onItemDragStopped = { onUserIntent(it) },
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
  onListDragStopped: (UserIntent.ReorderLists) -> Unit,
  onItemDoneClick: (TodoItemId, Boolean) -> Unit,
  onItemEditClick: (TodoItemId) -> Unit,
  onItemDragStopped: (UserIntent.ReorderItems) -> Unit,
) {
  if ((useListsOrItems && lists.isEmpty()) || (!useListsOrItems && items.isEmpty()))
    TodoListEmpty(modifier, useListsOrItems)
  else if (useListsOrItems)
    TodoLists(modifier, lists, onListClick, onListEditClick, onListDragStopped)
  else
    TodoItems(modifier, items, onItemDoneClick, onItemEditClick, onItemDragStopped)
}
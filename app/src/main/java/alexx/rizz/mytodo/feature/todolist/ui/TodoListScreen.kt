package alexx.rizz.mytodo.feature.todolist.ui

import alexx.rizz.mytodo.feature.todolist.*
import alexx.rizz.mytodo.feature.todolist.TodoListVM.*
import alexx.rizz.mytodo.feature.todolist.ui.components.*
import alexx.rizz.mytodo.ui.theme.*
import androidx.activity.compose.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.*
import androidx.lifecycle.compose.*

object TodoListScreenCommon {
  val RowPadding = PaddingValues(10.dp, 5.dp, 0.dp, 5.dp)
  const val CrossfadeBetweenItemsMillis = 300
}

@Composable
fun TodoListScreen(vm: TodoListVM = hiltViewModel()) {
  val screenState by vm.screenState.collectAsStateWithLifecycle()
  val successState = screenState as? TodoListScreenState.Success
  val isBackVisible = successState is TodoListScreenState.SuccessItems
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
        TodoListFloatingActionButton(successState is TodoListScreenState.SuccessLists, vm::onUserIntent)
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
fun TodoListFloatingActionButton(
  isListsShown: Boolean,
  onUserIntent: (UserIntent) -> Unit,
) {
  FloatingActionButton(
    modifier = Modifier.offset(x = (-30).dp),
    containerColor = MyColors.Primary,
    shape = RoundedCornerShape(50.dp),
    onClick = {
      val intent = if (isListsShown)
        UserIntent.EditList(TodoListId.Unknown)
      else
        UserIntent.EditItem(TodoItemId.Unknown)
      onUserIntent(intent)
    }
  ) {
    Icon(Icons.Default.Add, null)
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
    is TodoListScreenState.Success -> TodoListSuccess(modifier, screenState, onUserIntent)
  }
}
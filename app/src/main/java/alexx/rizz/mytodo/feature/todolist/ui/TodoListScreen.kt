package alexx.rizz.mytodo.feature.todolist.ui

import alexx.rizz.mytodo.feature.common.*
import alexx.rizz.mytodo.feature.todolist.*
import alexx.rizz.mytodo.feature.todolist.TodoListVM.*
import alexx.rizz.mytodo.feature.todolist.ui.components.*
import alexx.rizz.mytodo.ui.theme.*
import androidx.activity.compose.*
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

@Composable
fun TodoListScreen(vm: TodoListVM = hiltViewModel(), onMenuClick: () -> Unit = {}) {
  val screenState by vm.screenState.collectAsStateWithLifecycle()
  TodoListScreenContent(screenState, vm::onUserIntent, onMenuClick)
}

@Composable
fun TodoListScreenContent(
  screenState: TodoListScreenState,
  onUserIntent: (UserIntent) -> Unit,
  onMenuClick: () -> Unit,
) {
  val successState = screenState as? TodoListScreenState.Success
  BackHandler(enabled = successState?.navButtonType == NavButtonType.Back) {
    onUserIntent(UserIntent.Back)
  }
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TodoListTopBar(
        icon = successState?.navButtonType?.toTopBarIcon(),
        title = successState?.title,
        onClick = {
          when (successState?.navButtonType) {
            NavButtonType.Menu -> onMenuClick()
            NavButtonType.Back -> onUserIntent(UserIntent.Back)
            null -> Unit
          }
        },
      )
    },
    floatingActionButton = {
      if (successState != null)
        TodoListFloatingActionButton(successState.listState.content.contentType, onUserIntent = onUserIntent)
    }
  ) { innerPadding ->
    val modifier = Modifier.padding(innerPadding)
    when (screenState) {
      TodoListScreenState.Loading -> TodoListLoading(modifier)
      is TodoListScreenState.Success -> TodoListSuccess(modifier, screenState.listState, onUserIntent)
    }
  }
}

@Composable
private fun TodoListFloatingActionButton(
  listContentType: TodoListScreenState.ListContentType,
  onUserIntent: (UserIntent) -> Unit,
) {
  FloatingActionButton(
    modifier = Modifier.offset(x = (-30).dp),
    containerColor = MyColors.Primary,
    shape = RoundedCornerShape(50.dp),
    onClick = {
      val userIntent = when (listContentType) {
        TodoListScreenState.ListContentType.Lists -> UserIntent.EditList(TodoListId.Unknown)
        TodoListScreenState.ListContentType.Items -> UserIntent.EditItem(TodoItemId.Unknown)
      }
      onUserIntent(userIntent)
    }
  ) {
    Icon(Icons.Default.Add, null)
  }
}

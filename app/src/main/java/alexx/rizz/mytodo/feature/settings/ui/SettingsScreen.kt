package alexx.rizz.mytodo.feature.settings.ui

import alexx.rizz.mytodo.R
import alexx.rizz.mytodo.feature.todolist.ui.components.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.*

@Composable
fun SettingsScreen(onMenuClick: () -> Unit = {}) {
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TodoListTopBar(
        isBackVisible = false,
        title = stringResource(R.string.main_menu_item_settings),
        onBackClick = {},
        onMenuClick = onMenuClick
      )
    },
  ) { innerPadding ->
    Box(Modifier
      .fillMaxSize()
      .padding(innerPadding),
      contentAlignment = Alignment.Center
    ) {
      Text("TODO", fontSize = 24.sp)
    }
  }
}

// @Composable
// fun TodoListScreenContent(
//   modifier: Modifier,
//   screenState: TodoListScreenState,
//   onUserIntent: (UserIntent) -> Unit,
// ) {
//   when (screenState) {
//     TodoListScreenState.Loading -> TodoListLoading(modifier)
//     is TodoListScreenState.Success -> TodoListSuccess(modifier, screenState, onUserIntent)
//   }
// }
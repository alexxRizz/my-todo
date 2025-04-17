package alexx.rizz.mytodo.feature.settings.ui

import alexx.rizz.mytodo.R
import alexx.rizz.mytodo.feature.common.*
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
        icon = NavButtonType.Menu.toTopBarIcon(),
        title = stringResource(R.string.main_menu_item_settings),
        onClick = onMenuClick,
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
package alexx.rizz.mytodo.ui.navigation

import alexx.rizz.mytodo.*
import alexx.rizz.mytodo.R
import alexx.rizz.mytodo.ui.theme.*
import androidx.activity.compose.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import kotlinx.coroutines.*

private val Items = listOf(
  TodoDrawerItem(Icons.Default.Home, R.string.main_menu_item_lists, TodoNavDestination.TodoList),
  TodoDrawerItem(Icons.Default.Settings, R.string.main_menu_item_settings, TodoNavDestination.Settings),
)

@OptIn(ExperimentalMaterial3Api::class)
private val DrawerItemRippleConfiguration = RippleConfiguration(color = MyColors.Primary)

@Composable
fun TodoDrawer(
  onItemClick: (TodoNavDestination) -> Unit,
) {
  ModalDrawerSheet(
    modifier = Modifier
      .padding(end = 100.dp)
      .shadow(15.dp, RoundedCornerShape(10.dp)),
    drawerContainerColor = MyColors.Primary,
  ) {
    DrawerHeader()
    HorizontalDivider()
    DrawerItems(onItemClick)
    DrawerAppVersion()
  }
}

@Composable
private fun DrawerHeader() {
  Box(Modifier.fillMaxWidth()) {
    Text("My.todo",
      fontSize = 20.sp,
      fontWeight = FontWeight.Bold,
      color = Color.White,
      modifier = Modifier
        .padding(start = 16.dp, top = 13.dp, end = 16.dp, bottom = 13.dp)
    )
  }
}

@Composable
private fun ColumnScope.DrawerItems(onItemClick: (TodoNavDestination) -> Unit) {
  var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
  Column(modifier =
    Modifier
      .fillMaxWidth()
      .weight(1f)
      .background(Color.White)
      .padding(top = 16.dp)
  ) {
    @OptIn(ExperimentalMaterial3Api::class)
    CompositionLocalProvider(LocalRippleConfiguration provides DrawerItemRippleConfiguration) {
      Items.forEachIndexed { i, item ->
        NavigationDrawerItem(
          icon = { Icon(item.icon, contentDescription = null) },
          label = { Text(stringResource(item.textResId)) },
          selected = i == selectedItemIndex,
          colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MyColors.DoneCard,

            ),
          shape = RectangleShape,
          onClick = {
            onItemClick(item.destination)
            selectedItemIndex = i
          }
        )
      }
    }
  }
}

@Composable
private fun DrawerAppVersion() {
  Text("v${BuildConfig.VERSION_NAME}",
    fontSize = 16.sp,
    color = MyColors.SecondaryCardText,
    textAlign = TextAlign.End,
    modifier = Modifier
      .fillMaxWidth()
      .background(Color.White)
      .padding(end = 10.dp, bottom = 5.dp)
  )
}

@Composable
fun CloseDrawerOnBackPressed(drawerState: DrawerState, coroutineScope: CoroutineScope) {
  BackHandler(enabled = drawerState.isOpen) {
    if (drawerState.isOpen)
      coroutineScope.launchCloseDrawer(drawerState)
  }
}

fun CoroutineScope.launchOpenDrawer(drawerState: DrawerState) {
  this.launch {
    drawerState.open()
  }
}

fun CoroutineScope.launchCloseDrawer(drawerState: DrawerState) {
  this.launch {
    drawerState.close()
  }
}
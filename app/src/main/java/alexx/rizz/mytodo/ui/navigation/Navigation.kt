package alexx.rizz.mytodo.ui.navigation

import alexx.rizz.mytodo.*
import alexx.rizz.mytodo.R
import alexx.rizz.mytodo.app.logging.*
import alexx.rizz.mytodo.feature.settings.ui.*
import alexx.rizz.mytodo.feature.todolist.ui.*
import alexx.rizz.mytodo.ui.theme.*
import android.annotation.*
import androidx.activity.compose.*
import androidx.annotation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import androidx.navigation.compose.*
import kotlinx.coroutines.*

private data class DrawerItem(
  val icon: ImageVector,
  @StringRes val textResId: Int,
  val destination: TodoNavDestination,
)

private val Items = listOf(
  DrawerItem(Icons.Default.Home, R.string.main_menu_item_lists, TodoNavDestination.TodoList),
  DrawerItem(Icons.Default.Settings, R.string.main_menu_item_settings, TodoNavDestination.Settings),
)

private val Log = getLogger("Navigation")

@Composable
fun Navigation(
  navController: NavHostController = rememberNavController(),
  coroutineScope: CoroutineScope = rememberCoroutineScope(),
  drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
  startDestination: TodoNavDestination = TodoNavDestination.TodoList,
) {
  LogNavBackStack(navController)
  ModalNavigationDrawer(
    drawerState = drawerState,
    gesturesEnabled = drawerState.isOpen,
    drawerContent = { TodoModalDrawerSheet(navController, coroutineScope, drawerState) }
  ) {
    TodoNavHost(navController, startDestination, coroutineScope, drawerState)
  }
}

@Composable
private fun TodoModalDrawerSheet(navController: NavHostController, coroutineScope: CoroutineScope, drawerState: DrawerState) {
  ModalDrawerSheet(
    modifier = Modifier.padding(end = 100.dp),
    drawerContainerColor = MyColors.Primary,
  ) {
    TodoDrawerContent(onMenuClick = { navDestination ->
      navController.navigate(navDestination) {
        popUpTo(0) { // очищаем back stack полностью перед переходом на очередную вкладку
          saveState = true
        }
        launchSingleTop = true
        restoreState = true
      }
      coroutineScope.launchCloseDrawer(drawerState)
    })
  }
}

@Composable
fun ColumnScope.TodoDrawerContent(onMenuClick: (TodoNavDestination) -> Unit) {
  Box(Modifier.fillMaxWidth()) {
    Text("My.todo",
      fontSize = 20.sp,
      fontWeight = FontWeight.Bold,
      color = Color.White,
      modifier = Modifier
        .padding(16.dp)
    )
  }
  HorizontalDivider()
  var selectedItemIndex by remember { mutableIntStateOf(0) }
  Column(modifier =
    Modifier
      .fillMaxWidth()
      .weight(1f)
      .background(Color.White) // TODO: ripple-эффект не работает, т.к. цвет фона drawer'a - MyColors.Primary
      .padding(top = 16.dp)
  ) {
    Items.forEachIndexed { i, item ->
      NavigationDrawerItem(
        icon = { Icon(item.icon, contentDescription = null) },
        label = { Text(stringResource(item.textResId)) },
        selected = i == selectedItemIndex,
        colors = NavigationDrawerItemDefaults.colors(selectedContainerColor = MyColors.DoneCard),
        shape = RectangleShape,
        onClick = {
          onMenuClick(item.destination)
          selectedItemIndex = i
        }
      )
    }
  }
  Text("v${BuildConfig.VERSION_NAME}",
    fontSize = 16.sp,
    color = Color.Black,
    textAlign = TextAlign.End,
    modifier = Modifier
      .fillMaxWidth()
      .background(Color.White)
      .padding(end = 10.dp, bottom = 5.dp)
  )
}

@Composable
private fun TodoNavHost(navController: NavHostController, startDestination: TodoNavDestination, coroutineScope: CoroutineScope, drawerState: DrawerState) {
  NavHost(navController, startDestination = startDestination) {
    composable<TodoNavDestination.TodoList> {
      CloseDrawerOnBackPressed(drawerState, coroutineScope)
      TodoListScreen(onMenuClick = { coroutineScope.launchOpenDrawer(drawerState) })
    }
    composable<TodoNavDestination.Settings> {
      CloseDrawerOnBackPressed(drawerState, coroutineScope)
      SettingsScreen(onMenuClick = { coroutineScope.launchOpenDrawer(drawerState) })
    }
  }
}

@Composable
private fun CloseDrawerOnBackPressed(drawerState: DrawerState, coroutineScope: CoroutineScope) {
  BackHandler(enabled = drawerState.isOpen) {
    if (drawerState.isOpen)
      coroutineScope.launchCloseDrawer(drawerState)
  }
}

@Composable @SuppressLint("RestrictedApi")
private fun LogNavBackStack(navController: NavHostController) {
  LaunchedEffect(navController.currentBackStack) {
    navController.currentBackStack
      .collect { entries ->
        val route = entries.mapNotNull { it.destination.route?.substringAfterLast('.') }.joinToString(" -> ")
        Log.d("NavBackStack: $route")
      }
  }
}

private fun CoroutineScope.launchOpenDrawer(drawerState: DrawerState) {
  this.launch {
    drawerState.open()
  }
}

private fun CoroutineScope.launchCloseDrawer(drawerState: DrawerState) {
  this.launch {
    drawerState.close()
  }
}
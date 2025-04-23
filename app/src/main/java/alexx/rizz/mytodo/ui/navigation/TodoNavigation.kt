package alexx.rizz.mytodo.ui.navigation

import alexx.rizz.mytodo.app.logging.*
import alexx.rizz.mytodo.feature.settings.ui.*
import alexx.rizz.mytodo.feature.todolist.ui.*
import android.annotation.*
import androidx.compose.animation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.*
import androidx.navigation.compose.*
import kotlinx.coroutines.*

private val Log = getLogger("Navigation")

@Composable
fun TodoNavigation(
  navController: NavHostController = rememberNavController(),
  coroutineScope: CoroutineScope = rememberCoroutineScope(),
  drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
  startDestination: TodoNavDestination = TodoNavDestination.TodoList,
) {
  ModalNavigationDrawer(
    drawerState = drawerState,
    gesturesEnabled = drawerState.isOpen,
    drawerContent = {
      TodoDrawer(onItemClick = { navDestination ->
        navigate(navController, navDestination, drawerState, coroutineScope)
      })
    }
  ) {
    TodoNavHost(navController, startDestination, drawerState, coroutineScope)
  }
  LogNavBackStack(navController)
}

private fun navigate(
  navController: NavHostController,
  navDestination: TodoNavDestination,
  drawerState: DrawerState,
  coroutineScope: CoroutineScope,
) {
  navController.navigate(navDestination) {
    popUpTo(0) { // очищаем back stack полностью перед переходом на очередную вкладку
      saveState = true
    }
    launchSingleTop = true
    restoreState = true
  }
  coroutineScope.launchCloseDrawer(drawerState)
}


@Composable
private fun TodoNavHost(navController: NavHostController, startDestination: TodoNavDestination, drawerState: DrawerState, coroutineScope: CoroutineScope) {
  val onMenuClick = { coroutineScope.launchOpenDrawer(drawerState) }
  NavHost(
    navController,
    startDestination = startDestination,
    enterTransition = { EnterTransition.None },
    exitTransition = { ExitTransition.None },
  ) {
    composable<TodoNavDestination.TodoList> {
      CloseDrawerOnBackPressed(drawerState, coroutineScope)
      TodoListScreen(onMenuClick = onMenuClick)
    }
    composable<TodoNavDestination.Settings> {
      CloseDrawerOnBackPressed(drawerState, coroutineScope)
      SettingsScreen(onMenuClick = onMenuClick)
    }
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
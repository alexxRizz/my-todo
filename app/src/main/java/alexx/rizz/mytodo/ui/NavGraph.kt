package alexx.rizz.mytodo.ui

import alexx.rizz.mytodo.feature.todolist.ui.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.*
import androidx.navigation.compose.*
import kotlinx.coroutines.*

@Composable
fun NavGraph(
  navController: NavHostController = rememberNavController(),
  coroutineScope: CoroutineScope = rememberCoroutineScope(),
  drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
  startDestination: Any = NavDestinations.TodoList,
) {
  NavHost(navController, startDestination = startDestination) {
    composable<NavDestinations.TodoList> {
      TodoListScreen()
    }
  }
}
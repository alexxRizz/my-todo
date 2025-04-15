package alexx.rizz.mytodo.ui.navigation

import androidx.annotation.*
import androidx.compose.ui.graphics.vector.*

data class TodoDrawerItem(
  val icon: ImageVector,
  @StringRes val textResId: Int,
  val destination: TodoNavDestination,
)

package alexx.rizz.mytodo.ui.navigation

import kotlinx.serialization.*

sealed interface TodoNavDestination {

  @Serializable
  object TodoList : TodoNavDestination

  @Serializable
  object Settings : TodoNavDestination
}

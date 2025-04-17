package alexx.rizz.mytodo.feature.common

import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.*

enum class NavButtonType {
  Menu,
  Back
}

fun NavButtonType.toTopBarIcon(): ImageVector =
  when (this) {
    NavButtonType.Menu -> Icons.Default.Menu
    NavButtonType.Back -> Icons.AutoMirrored.Filled.ArrowBack
  }
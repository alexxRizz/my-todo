package alexx.rizz.mytodo.feature.todolist.ui.components

import alexx.rizz.mytodo.feature.todolist.ui.TodoListScreenCommon.CrossfadeBetweenItemsMillis
import alexx.rizz.mytodo.ui.theme.*
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListTopBar(
  isBackVisible: Boolean,
  title: String,
  onBack: () -> Unit,
) {
  TopAppBar(
    modifier = Modifier
      .heightIn(max = 65.dp)
      .shadow(3.dp)
      .padding(bottom = 3.dp),
    navigationIcon = {
      IconButton(onBack) {
        Crossfade(targetState = isBackVisible, animationSpec = tween(CrossfadeBetweenItemsMillis)) { isBackVisible ->
          val icon = if (isBackVisible) Icons.AutoMirrored.Filled.ArrowBack else Icons.Default.Menu
          Icon(icon, null)
        }
      }
    },
    title = {
      Crossfade(targetState = title, animationSpec = tween(CrossfadeBetweenItemsMillis)) { title ->
        Text(title)
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MyColors.Primary,
      titleContentColor = Color.White,
      navigationIconContentColor = Color.White,
    ),
  )
}
package alexx.rizz.mytodo.feature.todolist.ui.components

import alexx.rizz.mytodo.feature.todolist.ui.commonInAndOutTransform
import alexx.rizz.mytodo.ui.theme.*
import androidx.compose.animation.*
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
  onBackClick: () -> Unit,
  onMenuClick: () -> Unit,
) {
  TopAppBar(
    modifier = Modifier
      .heightIn(max = 70.dp)
      .shadow(3.dp)
      .padding(bottom = 3.dp),
    navigationIcon = {
      AnimatedContent(
        targetState = isBackVisible,
        transitionSpec = { commonInAndOutTransform() }
      ) { isBackVisible ->
        IconButton(onClick = { if (isBackVisible) onBackClick() else onMenuClick() }) {
          val icon = if (isBackVisible) Icons.AutoMirrored.Filled.ArrowBack else Icons.Default.Menu
          Icon(icon, null)
        }
      }
    },
    title = {
      AnimatedContent(
        targetState = title,
        transitionSpec = { commonInAndOutTransform() },
      ) { title ->
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
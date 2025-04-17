package alexx.rizz.mytodo.feature.todolist.ui.components

import alexx.rizz.mytodo.feature.todolist.ui.*
import alexx.rizz.mytodo.ui.theme.*
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.*
import androidx.compose.ui.unit.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListTopBar(
  icon: ImageVector?,
  title: String?,
  onClick: () -> Unit,
) {
  TopAppBar(
    modifier = Modifier
      .heightIn(max = 70.dp)
      .shadow(3.dp)
      .padding(bottom = 3.dp),
    navigationIcon = {
      AnimatedContent(
        targetState = icon,
        transitionSpec = { commonInAndOutTransform() }
      ) { icon ->
        if (icon != null)
          IconButton(onClick = onClick) { Icon(icon, null) }
      }
    },
    title = {
      AnimatedContent(
        targetState = title,
        transitionSpec = { commonInAndOutTransform() },
      ) { title ->
        if (title != null)
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
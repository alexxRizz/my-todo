package alexx.rizz.mytodo.feature.todolist.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*

object TodoListScreenCommon {
  val RowPadding = PaddingValues(10.dp, 5.dp, 0.dp, 5.dp)
}

@Composable
fun getItemTextColor(isDone: Boolean): Color =
  if (isDone) LocalContentColor.current.copy(alpha = 0.7f) else LocalContentColor.current

fun Color.getItemButtonIconColor(isDone: Boolean): Color =
  this.copy(alpha = if (isDone) 0.35f else 1f)
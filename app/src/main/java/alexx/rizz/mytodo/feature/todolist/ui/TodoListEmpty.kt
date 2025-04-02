package alexx.rizz.mytodo.feature.todolist.ui

import alexx.rizz.mytodo.ui.theme.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*

@Composable
fun TodoListEmpty(modifier: Modifier, useListsOrItems: Boolean) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    Text(
      if (useListsOrItems) "Добавьте список" else "Добавьте пункт",
      textAlign = TextAlign.Center,
      color = MyColors.Tertiary,
      fontSize = 25.sp,
    )
  }
}
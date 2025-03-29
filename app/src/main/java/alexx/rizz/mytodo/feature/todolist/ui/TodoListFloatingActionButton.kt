package alexx.rizz.mytodo.feature.todolist.ui

import alexx.rizz.mytodo.feature.todolist.*
import alexx.rizz.mytodo.feature.todolist.TodoListVM.*
import alexx.rizz.mytodo.ui.theme.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*

@Composable
fun TodoListFloatingActionButton(
  isListsVisible: Boolean,
  onUserIntent: (UserIntent) -> Unit,
) {
  FloatingActionButton(
    modifier = Modifier.offset(y = (-10).dp),
    containerColor = MyColors.Primary,
    onClick = {
      val intent = if (isListsVisible)
        UserIntent.EditList(id = TodoListId.Unknown)
      else
        UserIntent.EditItem(id = TodoItemId.Unknown)
      onUserIntent(intent)
    }
  ) {
    Icon(Icons.Filled.Add, null)
  }
}
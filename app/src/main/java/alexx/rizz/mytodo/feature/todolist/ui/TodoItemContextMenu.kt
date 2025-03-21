package alexx.rizz.mytodo.feature.todolist.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*

@Composable
fun TodoItemContextMenu() {
  var expanded by remember { mutableStateOf(false) }
  Box(
    modifier = Modifier
      //.padding(16.dp)
  ) {
    IconButton(onClick = { expanded = !expanded }) {
      Icon(Icons.Default.MoreVert, contentDescription = null)
    }
    DropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false }
    ) {
      DropdownMenuItem(
        text = { Text("Option 1") },
        onClick = { /* Do something... */ }
      )
      DropdownMenuItem(
        text = { Text("Option 2") },
        onClick = { /* Do something... */ }
      )
    }
  }
}

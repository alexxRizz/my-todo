package alexx.rizz.mytodo.feature.todolist.ui.components

import alexx.rizz.mytodo.ui.theme.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
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
      if (isBackVisible)
        IconButton(onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
    },
    title = { Text(title) },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MyColors.Primary,
      titleContentColor = Color.White,
      navigationIconContentColor = Color.White,
    ),
  )
}
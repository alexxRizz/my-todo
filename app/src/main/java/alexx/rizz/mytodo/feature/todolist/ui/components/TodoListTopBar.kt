package alexx.rizz.mytodo.feature.todolist.ui.components

import alexx.rizz.mytodo.ui.theme.MyColors
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListTopBar(
  isBackVisible: Boolean,
  title: String,
  onBack: () -> Unit,
) {
  TopAppBar(
    modifier = Modifier.heightIn(max = 65.dp),
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
package alexx.rizz.mytodo.ui

import alexx.rizz.mytodo.feature.todolist.ui.*
import alexx.rizz.mytodo.ui.theme.*
import android.os.*
import androidx.activity.*
import androidx.activity.compose.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.*
import dagger.hilt.android.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyToDoTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          TodoListScreen(
            modifier = Modifier.padding(innerPadding)
          )
        }
      }
    }
  }
}
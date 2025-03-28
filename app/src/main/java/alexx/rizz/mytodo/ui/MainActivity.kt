package alexx.rizz.mytodo.ui

import alexx.rizz.mytodo.ui.theme.*
import android.os.*
import androidx.activity.*
import androidx.activity.compose.*
import dagger.hilt.android.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyTodoTheme {
        NavGraph()
      }
    }
  }
}
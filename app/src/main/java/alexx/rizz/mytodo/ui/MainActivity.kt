package alexx.rizz.mytodo.ui

import alexx.rizz.mytodo.ui.theme.*
import android.os.*
import androidx.activity.*
import androidx.activity.compose.*
import androidx.core.view.*
import dagger.hilt.android.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setLightTextColorAtSystemStatusBar()
    setContent {
      MyTodoTheme {
        NavGraph()
      }
    }
  }

  private fun setLightTextColorAtSystemStatusBar() {
    val window = this.window
    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false
  }
}
package alexx.rizz.mytodo.ui

import alexx.rizz.mytodo.R
import alexx.rizz.mytodo.ui.navigation.*
import alexx.rizz.mytodo.ui.theme.*
import android.os.*
import androidx.activity.*
import androidx.activity.compose.*
import androidx.compose.material3.*
import androidx.core.view.*
import dagger.hilt.android.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    /** Заменяем тему сплеш-скрина [R.style.AppTheme_Launcher] на тему приложения,
     * иначе сплеш будет "вылезать" в самые неподходящие моменты, например, при закрытии drawer'a */
    setTheme(R.style.AppTheme)
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setLightTextColorAtSystemStatusBar()
    setContent {
      AppTheme {
        Surface {
          TodoNavigation()
        }
      }
    }
  }

  private fun setLightTextColorAtSystemStatusBar() {
    val window = this.window
    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false
  }
}
package alexx.rizz.mytodo.ui.theme

import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*

private val DarkColorScheme = darkColorScheme()

private val LightColorScheme = lightColorScheme(
  primary = Primary,
  secondary = Secondary,
  tertiary = Tertiary,
)

@Composable
fun MyToDoTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
    typography = Typography,
    content = content
  )
}
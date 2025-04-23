package alexx.rizz.mytodo.feature.settings.ui

import alexx.rizz.mytodo.ui.theme.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.*

@Composable
@Preview(locale = "ru", showBackground = true, showSystemUi = true)
@Preview(locale = "en", showBackground = true, showSystemUi = true)
private fun SettingsScreenPreview() {
  AppTheme {
    SettingsScreen(onMenuClick = {})
  }
}
package alexx.rizz.mytodo.ui.theme

import androidx.compose.ui.graphics.*

object MyColors {
  val Primary = Color(0xFF03A9F4)
  val Secondary = Color(0xFF0288D1)
  val Tertiary = Color(0xFF607D8B)
  val Background = Color(0xFFFAFAFA)

  val DoneCard = Primary.copy(alpha = 0.15f)
  val UndoneCard = Primary.copy(alpha = 0.33f)
  val SecondaryCardText = Color(0xFF506D7B)
}

package alexx.rizz.mytodo.feature.todolist.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*

fun <S> AnimatedContentTransitionScope<S>.commonInAndOutTransform(): ContentTransform {
  val duration = tween<Float>(600)
  val enter = fadeIn(duration) + scaleIn(duration)
  val exit = fadeOut(duration) + scaleOut(duration)
  return enter togetherWith exit using SizeTransform(clip = false)
}

/** Позволяет создать [Transition] с [initialState] != [targetState].
 * Таким образом анимация начинается сразу после показа экрана. */
@Composable
fun <T> rememberTransition(initialState: T, targetState: T, label: String? = null): Transition<T> {
  var currentState = remember { MutableTransitionState<T>(initialState) }
  currentState.targetState = targetState
  return rememberTransition(currentState, label = label)
}
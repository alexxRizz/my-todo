package alexx.rizz.mytodo.testscommon

import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.rules.*
import org.junit.runner.*

class MainDispatcherRule(
  private val mDispatcher: CoroutineDispatcher,
) : TestWatcher() {

  override fun starting(description: Description) =
    Dispatchers.setMain(mDispatcher)

  override fun finished(description: Description) =
    Dispatchers.resetMain()
}
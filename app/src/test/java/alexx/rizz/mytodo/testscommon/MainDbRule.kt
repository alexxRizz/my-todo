package alexx.rizz.mytodo.testscommon

import alexx.rizz.mytodo.db.*
import androidx.test.platform.app.*
import kotlinx.coroutines.*
import org.junit.rules.*
import org.junit.runner.*

class MainDbRule(
  private val mDispatcher: CoroutineDispatcher? = null,
) : TestWatcher() {

  lateinit var dbProvider: TestMainDbProvider; private set
  val db get() = dbProvider.db

  override fun starting(description: Description) {
    val db = DbBuilder.buildMain(
      InstrumentationRegistry.getInstrumentation().targetContext,
      inMemory = true,
      dispatcher = mDispatcher,
      allowMainThreadQueries = true,
    )
    dbProvider = TestMainDbProvider(db)
  }

  override fun finished(description: Description) =
    dbProvider.db.close()
}
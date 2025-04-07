package alexx.rizz.mytodo.testscommon.logging

import alexx.rizz.mytodo.app.logging.*
import io.kotest.matchers.*

@Suppress("unused")
class TestLoggerFactory(private val mLogImpl: ITestLoggerImpl) : ILoggerFactoryImpl {

  @PublishedApi
  internal val mLogs = mutableMapOf<String, TestLogger>()

  override fun create(className: String): ILogger {
    val log = mLogs[className]
    if (log != null)
      return log
    val newLog = TestLogger(className, mLogImpl)
    mLogs[className] = newLog
    return newLog
  }

  fun clear() {
    mLogs.forEach {
      it.value.clear()
    }
  }

  fun get(className: String) =
    mLogs.getValue(className)

  fun shouldBeNoErrors(andNoWarnings: Boolean = true) =
    mLogs.forEach {
      it.value.shouldBeNoErrors(andNoWarnings)
    }

  inline fun <reified T> shouldBeNoLogOfType() =
    mLogs.contains(T::class.java.simpleName) shouldBe false
}
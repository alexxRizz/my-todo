package alexx.rizz.mytodo.testscommon.logging

import alexx.rizz.mytodo.app.utils.*
import alexx.rizz.mytodo.testscommon.logging.TestLogger.*

class ConsoleTestLoggerImpl : ITestLoggerImpl {

  override fun d(shortClassName: String, msg: String) = println(shortClassName, Level.D, msg)
  override fun i(shortClassName: String, msg: String) = println(shortClassName, Level.I, msg)
  override fun w(shortClassName: String, msg: String) = println(shortClassName, Level.W, msg)
  override fun e(shortClassName: String, t: Throwable?, msg: String) = println(shortClassName, Level.E, msg, t)

  private fun println(shortClassName: String, level: Level, msg: String, t: Throwable? = null) {
    val printMsg = "${Dates.now().toString("yyyy.MM.dd HH:mm:ss.SSS")} [$shortClassName] $level $msg"
    when (level) {
      Level.D -> println(printMsg)
      Level.I -> println(printMsg)
      Level.W -> println(printMsg)
      Level.E -> println(printMsg +
        if (t != null)
          "\n" + t.stackTraceToString() else
          "")
    }
  }
}
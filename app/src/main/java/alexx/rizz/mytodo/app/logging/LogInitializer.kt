package alexx.rizz.mytodo.app.logging

import java.io.*

object LogInitializer {

  fun init(logsDir: File) {
    logsDir.mkdirs()
    System.setProperty("LOG_DIR", logsDir.path) // Так задается переменная в logback.xml
    LoggerFactory.setImpl(Slf4jLoggerFactory())
    setThreadDefaultUncaughtExceptionHandler()
  }

  private fun setThreadDefaultUncaughtExceptionHandler() {
    val oldHandler = Thread.getDefaultUncaughtExceptionHandler()
    Thread.setDefaultUncaughtExceptionHandler { thread: Thread, t: Throwable ->
      getLogger<LogInitializer>().e(t, "Uncaught exception!")
      oldHandler?.uncaughtException(thread, t)
    }
  }
}
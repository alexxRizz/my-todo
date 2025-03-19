package alexx.rizz.mytodo.app.logging

class Slf4jLogger(loggerName: String) : ILogger {

  private val mLogger = org.slf4j.LoggerFactory.getLogger(loggerName)

  override fun d(msg: String) =
    mLogger.debug(msg)

  override fun i(msg: String) =
    mLogger.info(msg)

  override fun w(msg: String) =
    mLogger.warn(msg)

  override fun e(t: Throwable?, msg: String) =
    mLogger.error(msg, t)
}
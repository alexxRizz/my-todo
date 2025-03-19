package alexx.rizz.mytodo.app.logging

interface ILogger {
  fun d(msg: String)
  fun i(msg: String)
  fun w(msg: String)
  fun e(t: Throwable?, msg: String)
}

package alexx.rizz.mytodo.testscommon.logging

interface ITestLoggerImpl {

  fun d(shortClassName: String, msg: String)
  fun i(shortClassName: String, msg: String)
  fun w(shortClassName: String, msg: String)
  fun e(shortClassName: String, t: Throwable?, msg: String)
}
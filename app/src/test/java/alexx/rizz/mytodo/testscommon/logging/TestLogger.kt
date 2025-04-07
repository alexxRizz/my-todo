package alexx.rizz.mytodo.testscommon.logging

import alexx.rizz.mytodo.app.logging.*
import io.kotest.assertions.*
import io.kotest.matchers.*
import io.kotest.matchers.string.*
import io.kotest.matchers.throwable.*
import io.kotest.matchers.types.*

class TestLogger(
  className: String,
  private val mLogImpl: ITestLoggerImpl
) : ILogger {

  enum class Level { D, I, W, E }

  data class Line(val level: Level, val message: String, val exception: Throwable?) {

    fun shouldBeError(ex: Exception?, logMsg: String) {
      shouldBe(Level.E, logMsg)
      if (ex != null) {
        exception should beOfType(ex::class)
        if (ex.message != null)
          exception!! shouldHaveMessage ex.message!!
        else
          exception!!.message shouldBe null
      }
    }

    fun shouldBeWarn(msg: String) {
      shouldBe(Level.W, msg)
    }

    fun shouldBeInfo(msg: String) {
      shouldBe(Level.I, msg)
    }

    fun shouldBeInfoStartWith(msg: String) {
      shouldStartWith(Level.I, msg)
    }

    fun shouldBeDebug(msg: String) {
      shouldBe(Level.D, msg)
    }

    fun shouldBeDebugStartWith(msg: String) {
      shouldStartWith(Level.D, msg)
    }

    private fun shouldBe(lvl: Level, msg: String) {
      message shouldBe msg
      level shouldBe lvl
    }

    private fun shouldStartWith(lvl: Level, msg: String) {
      message shouldStartWith msg
      level shouldBe lvl
    }
  }

  private val mShortClassName = className.split(".").last()

  private val mLines = mutableListOf<Line>()

  override fun d(msg: String) =
    print(Level.D, msg)

  override fun i(msg: String) =
    print(Level.I, msg)

  override fun w(msg: String) =
    print(Level.W, msg)

  override fun e(t: Throwable?, msg: String) =
    print(Level.E, msg, t)

  private fun print(level: Level, msg: String, ex: Throwable? = null) {
    mLines.add(Line(level, msg, ex))
    when (level) {
      Level.D -> mLogImpl.d(mShortClassName, msg)
      Level.I -> mLogImpl.i(mShortClassName, msg)
      Level.W -> mLogImpl.w(mShortClassName, msg)
      Level.E -> mLogImpl.e(mShortClassName, ex, msg)
    }
  }

  val last get() = mLines.last()
  val beforeLast get() = offset(1)
  val first get() = mLines.first()
  val second get() = mLines[1]
  @Suppress("MemberVisibilityCanBePrivate") val count get() = mLines.size
  @Suppress("MemberVisibilityCanBePrivate") fun offset(i: Int) = mLines[mLines.lastIndex - i]
  fun get(i: Int) = mLines[i]

  fun clear() =
    mLines.clear()

  fun shouldBeNoErrors(andNoWarnings: Boolean = true) {
    mLines.forEachIndexed { i, line ->
      var description = "$i: [${line.level}]"
      if (line.message.isNotEmpty())
        description += " ${line.message}"
      if (line.exception != null)
        description += "\n" + line.exception.stackTraceToString()
      withClue(description) {
        line.level shouldNotBe Level.E
        if (andNoWarnings)
          line.level shouldNotBe Level.W
      }
    }
  }

  fun shouldHaveCount(expectedCount: Int): TestLogger {
    count shouldBe expectedCount
    return this
  }
}
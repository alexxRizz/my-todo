package alexx.rizz.mytodo.testscommon

import alexx.rizz.automockker.*
import alexx.rizz.mytodo.app.logging.*
import alexx.rizz.mytodo.app.utils.*
import alexx.rizz.mytodo.testscommon.logging.*
import org.junit.*
import org.junit.jupiter.api.*

abstract class TestsBase : INeedMockks {

  protected companion object {
    protected val TestLoggerFactory = TestLoggerFactory(ConsoleTestLoggerImpl())

    inline fun <reified T> getTestLogger() =
      getTestLogger(T::class.java.name)

    fun getTestLogger(name: String) =
      TestLoggerFactory.get(name)
  }

  override lateinit var mocks: AutoMockker

  protected val mLogging get() = TestLoggerFactory

  protected val mCoroutines get() = mocks.mock<ICoroutines>()

  @Before
  @BeforeEach
  fun setUpTestsBase() {
    LoggerFactory.setImpl(TestLoggerFactory)
    mocks = AutoMockker()
  }

  @After
  @AfterEach
  fun tearDownTestsBase() {
    TestLoggerFactory.clear()
    Coroutines.resetImpl()
  }
}
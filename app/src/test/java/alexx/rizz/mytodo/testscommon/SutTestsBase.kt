package alexx.rizz.mytodo.testscommon

import alexx.rizz.automockker.*
import org.junit.Before
import org.junit.jupiter.api.*
import kotlin.reflect.*

abstract class SutTestsBase<TSut : Any> : TestsBase(), INeedMockksWithSut<TSut> {

  override lateinit var sut: TSut

  protected val mLog get() = getTestLogger(sut.javaClass.name)

  @Before
  @BeforeEach
  fun setUpSutTestsBase() {
    beforeSutCreation()
    val newSut = newSut()
    this.sut =
      if (newSut != null) {
        newSut
      } else {
        @Suppress("UNCHECKED_CAST")
        val sutClass = SutReflectionExtensions.getSutClass(this) as KClass<TSut>
        mocks.sut(sutClass)
      }
  }

  protected open fun beforeSutCreation() = Unit
}

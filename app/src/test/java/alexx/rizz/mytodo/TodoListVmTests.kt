package alexx.rizz.mytodo

import alexx.rizz.mytodo.app.resources.*
import alexx.rizz.mytodo.feature.keyvalue.*
import alexx.rizz.mytodo.feature.todolist.*
import alexx.rizz.mytodo.feature.todolist.ui.*
import alexx.rizz.mytodo.testscommon.*
import io.kotest.matchers.*
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.*
import org.robolectric.*

private const val ListsTitle = "Списки"

@RunWith(RobolectricTestRunner::class)
class TodoListVmTests : SutTestsBase<TodoListVM>() {

  private val mTestDispatcher: TestDispatcher = UnconfinedTestDispatcher()
  private val mTestScope = TestScope(mTestDispatcher)

  @get:Rule val dispatcherRule = MainDispatcherRule(mTestDispatcher)
  @get:Rule val mainDbRule = MainDbRule(mTestDispatcher)

  private val mResources get() = mocks.mock<IResources>()
  // private val mKeyValueService get() = mocks.mock<IKeyValueService>()
  private val mTodoRep get() = mocks.mock<ITodoRepository>()

  private lateinit var mScreenStates: MutableList<TodoListScreenState>

  override fun beforeSutCreation() {
    mocks.use<IKeyValueService>(KeyValueService(KeyValueRepository(mainDbRule.dbProvider)))
    mocks.use<ITodoRepository>(TodoRepository(mainDbRule.dbProvider))
  }

  @Before
  fun setUp() = runTest {
    every { mResources.getString(ResStringId.ListsTitle) } returns ListsTitle
    mScreenStates = mutableListOf()
    backgroundScope.launch {
      sut.screenState.collect {
        println("collect: $it")
        mScreenStates.add(it)
      }
    }
  }

  @Test
  fun should_be_Loading_in_initial_state() = runTest {
    mScreenStates.first() shouldBe TodoListScreenState.Loading
  }

  @Test
  fun should_be_empty_SuccessLists_if_no_lists_in_db() = runTest {
    sut.screenState.value shouldBe TodoListScreenState.SuccessLists(title = ListsTitle)
  }

  @Test
  fun should_be_SuccessLists_with_lists_from_db() = runTest {
    mTodoRep.addList(TodoList("list1"))
    mTodoRep.addList(TodoList("list2"))

    sut.screenState.value shouldBe TodoListScreenState.SuccessLists(listOf(
      TodoList("list1", TodoListId(1)),
      TodoList("list2", TodoListId(2)),
    ), ListsTitle)
  }

  @Test
  fun should_be_empty_SuccessItems_if_no_items_in_db() = runTest {
    mTodoRep.addList(TodoList("list1"))
    sut.onUserIntent(TodoListVM.UserIntent.ShowListItems(TodoListId(1)))

    sut.screenState.value shouldBe TodoListScreenState.SuccessItems(listOf(), "list1")
  }

  @Test
  fun should_be_SuccessItems_with_items_from_db() = runTest {
    mTodoRep.addList(TodoList("list1"))
    mTodoRep.addItem(TodoItem("item1", false, TodoListId(1)))
    mTodoRep.addItem(TodoItem("item2", false, TodoListId(1)))
    sut.onUserIntent(TodoListVM.UserIntent.ShowListItems(TodoListId(1)))

    sut.screenState.value shouldBe TodoListScreenState.SuccessItems(listOf(
      TodoItem("item1", false, TodoListId(1), TodoItemId(1)),
      TodoItem("item2", false, TodoListId(1), TodoItemId(2)),
    ), "list1")
  }

  private fun runTest(testBody: suspend TestScope.() -> Unit) =
    runTest(mTestDispatcher) {
      mTestScope.testBody()
    }
}
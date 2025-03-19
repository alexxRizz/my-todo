package alexx.rizz.mytodo.app.utils

import kotlinx.coroutines.*
import alexx.rizz.mytodo.app.utils.ICoroutines.*
import kotlin.coroutines.*
import kotlin.time.*

interface ICoroutines {

  interface IDispatchers {
    val io: CoroutineDispatcher
    fun ioSingle(): CoroutineDispatcher
    val main: CoroutineDispatcher
  }

  val dispatchers: IDispatchers

  suspend fun yield()
  suspend fun delay(duration: Duration)

  fun scope(context: CoroutineContext): CoroutineScope
  fun mainScope(): CoroutineScope
  fun supervisorJob(): CompletableJob
  fun <T> completableDeferred(): CompletableDeferred<T>
}

object Coroutines : ICoroutines {

  private var mImpl: ICoroutines = CoroutinesImpl()

  fun setImpl(impl: ICoroutines) {
    mImpl = impl
  }

  fun resetImpl() {
    mImpl = CoroutinesImpl()
  }

  override val dispatchers get() = mImpl.dispatchers
  override suspend fun yield() = mImpl.yield()
  override suspend fun delay(duration: Duration) = mImpl.delay(duration)
  override fun scope(context: CoroutineContext) = mImpl.scope(context)

  override fun mainScope() = mImpl.mainScope()
  override fun supervisorJob() = mImpl.supervisorJob()
  override fun <T> completableDeferred() = mImpl.completableDeferred<T>()

  private class CoroutinesImpl : ICoroutines {

    private class DispatchersImpl : IDispatchers {
      override val io: CoroutineDispatcher
        get() = Dispatchers.IO

      override fun ioSingle(): CoroutineDispatcher =
        Dispatchers.ioSingle()

      override val main: CoroutineDispatcher
        get() = Dispatchers.Main
    }

    override val dispatchers: IDispatchers = DispatchersImpl()

    override suspend fun yield() =
      kotlinx.coroutines.yield()

    override suspend fun delay(duration: Duration) =
      kotlinx.coroutines.delay(duration)

    override fun scope(context: CoroutineContext) = CoroutineScope(context)

    override fun mainScope() = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun supervisorJob() = SupervisorJob()

    override fun <T> completableDeferred(): CompletableDeferred<T> =
      CompletableDeferred()
  }
}

fun Throwable.rethrowIfCancellation() {
  if (this is CancellationException)
    throw this
}

fun Dispatchers.ioSingle() = IO.limitedParallelism(1)

@Suppress("SuspendFunctionOnCoroutineScope")
/** Ожидание завершения всех дочерних задач */
suspend fun CoroutineScope.joinAllChildren() {
  coroutineContext[Job]?.children?.forEach { it.join() }
}

fun CoroutineScope.cancelAllChildren() {
  coroutineContext[Job]?.children?.forEach { it.cancel() }
}

suspend fun Job.cancelAndJoinChildren() {
  children.forEach { it.cancel() }
  children.forEach { it.join() }
}

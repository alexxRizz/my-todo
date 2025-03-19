package alexx.rizz.mytodo.app.logging

interface ILoggerFactoryImpl {
  fun create(className: String): ILogger
}

object LoggerFactory {
  private lateinit var mImpl: ILoggerFactoryImpl

  fun setImpl(impl: ILoggerFactoryImpl) {
    mImpl = impl
  }

  fun <T> create(type: Class<T>) =
    create(type.name)

  fun create(className: String): ILogger =
    mImpl.create(className)
}

inline fun <reified T> getLogger(): ILogger = LoggerFactory.create(T::class.java)

fun getLogger(klass: Class<*>): ILogger = LoggerFactory.create(klass)
fun getLogger(name: String): ILogger = LoggerFactory.create(name)
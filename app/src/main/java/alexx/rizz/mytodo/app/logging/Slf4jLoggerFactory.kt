package alexx.rizz.mytodo.app.logging

class Slf4jLoggerFactory : ILoggerFactoryImpl {

  override fun create(className: String) =
    Slf4jLogger(className)
}
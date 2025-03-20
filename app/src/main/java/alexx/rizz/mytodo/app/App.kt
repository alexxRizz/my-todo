package alexx.rizz.mytodo.app

import alexx.rizz.mytodo.app.logging.*
import alexx.rizz.mytodo.db.*
import android.app.*
import dagger.hilt.android.*
import javax.inject.*

@HiltAndroidApp
class App : Application() {

  @Inject lateinit var mAppContext: IAppContext
  @Inject lateinit var mainDbInitializer: MainDbInitializer

  override fun onCreate() {
    super.onCreate()
    initLog()
    initDb()
  }

  private fun initLog() {
    LogInitializer.init(mAppContext.logsDir)
    getLogger(javaClass.name).i("*** APP STARTED: versionName=${mAppContext.appVersion} versionCode=${mAppContext.appVersionCode}")
  }

  private fun initDb() {
    mainDbInitializer.init()
  }
}
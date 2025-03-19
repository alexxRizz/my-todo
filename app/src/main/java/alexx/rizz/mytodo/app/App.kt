package alexx.rizz.mytodo.app

import alexx.rizz.mytodo.app.logging.*
import android.app.*
import dagger.hilt.android.*
import javax.inject.*

@HiltAndroidApp
class App : Application() {

  @Inject lateinit var mAppContext: IAppContext

  override fun onCreate() {
    super.onCreate()
    initLog()

  }

  private fun initLog() {
    LogInitializer.init(mAppContext.logsDir)
    getLogger(javaClass.name).i("*** APP STARTED: versionName=${mAppContext.appVersion} versionCode=${mAppContext.appVersionCode}")
  }
}
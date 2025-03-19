package alexx.rizz.mytodo.app

import alexx.rizz.mytodo.BuildConfig
import alexx.rizz.mytodo.app.utils.*
import android.content.*
import android.os.*
import dagger.hilt.android.qualifiers.*
import java.io.*
import javax.inject.*

interface IAppContext {

  /** Версия приложения в виде строки */
  val appVersion: String

  /** Версия приложения в виде числа */
  val appVersionCode: Int

  /** Папка внешнего хранилища (/sdcard) */
  val externalStorageDir: File

  /** Папка приложения на внешнем хранилище (/sdcard/Android/${app.package}) */
  val externalFilesDir: File

  /** Папка с файлами приложения (/data/data/${app.package}/files) */
  val filesDir: File

  /** Папка с логами (/data/data/${app.package}/files/logs). */
  val logsDir: File
}

@Singleton
class AppContext @Inject constructor(
  @ApplicationContext private val mContext: Context,
) : IAppContext {

  override val appVersion: String
    get() = BuildConfig.VERSION_NAME

  override val appVersionCode: Int
    get() = BuildConfig.VERSION_CODE

  override val externalStorageDir: File
    get() = Environment.getExternalStorageDirectory() ?: throw IllegalStateException("Unable to get path to external storage dir")

  override val externalFilesDir: File
    get() = mContext.getExternalFilesDir(null) ?: throw IllegalStateException("Unable to get path to external files dir")

  override val filesDir: File
    get() = mContext.filesDir ?: throw IllegalStateException("Unable to get path to files dir")

  override val logsDir: File
    get() = externalFilesDir.file("logs")
}
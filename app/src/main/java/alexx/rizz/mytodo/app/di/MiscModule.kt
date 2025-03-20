package alexx.rizz.mytodo.app.di

import alexx.rizz.mytodo.app.*
import alexx.rizz.mytodo.db.*
import dagger.*
import dagger.hilt.*
import dagger.hilt.components.*

@Module @InstallIn(SingletonComponent::class) @Suppress("unused")
interface MiscModule {

  @Binds
  fun appContext(value: AppContext): IAppContext

  @Binds
  fun mainDbProvider(value: MainDbInitializer): IMainDbProvider
}


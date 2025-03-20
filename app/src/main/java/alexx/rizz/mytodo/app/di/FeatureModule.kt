package alexx.rizz.mytodo.app.di

import alexx.rizz.mytodo.feature.todolist.*
import dagger.*
import dagger.hilt.*
import dagger.hilt.components.*

@Module @InstallIn(SingletonComponent::class) @Suppress("unused")
interface FeatureModule {

  @Binds
  fun todoRepository(value: TodoRepository): ITodoRepository
}


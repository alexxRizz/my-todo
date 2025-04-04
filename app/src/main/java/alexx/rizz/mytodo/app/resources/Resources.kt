package alexx.rizz.mytodo.app.resources

import alexx.rizz.mytodo.*
import android.content.*
import dagger.hilt.android.qualifiers.*
import javax.inject.*

interface IResources {
  fun getString(resId: ResStringId): String
}

@Singleton
class Resources @Inject constructor(
  @ApplicationContext private val mContext: Context,
) : IResources {

  override fun getString(resId: ResStringId): String =
    mContext.getString(getAndroidResId(resId))
}

private fun getAndroidResId(resId: ResStringId): Int =
  when (resId) {
    ResStringId.ListsTitle -> R.string.lists_title
    ResStringId.EditListDialogTitle -> R.string.edit_list_dialog_title
    ResStringId.EditListDialogTitleNew -> R.string.edit_list_dialog_title_new
    ResStringId.EditItemDialogTitle -> R.string.edit_item_dialog_title
    ResStringId.EditItemDialogTitleNew -> R.string.edit_item_dialog_title_new
  }
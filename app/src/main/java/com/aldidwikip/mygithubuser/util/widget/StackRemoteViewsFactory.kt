package com.aldidwikip.mygithubuser.util.widget

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.aldidwikip.mygithubuser.R
import com.aldidwikip.mygithubuser.data.model.UserProperty
import com.aldidwikip.mygithubuser.helper.mapCursorToList
import com.aldidwikip.mygithubuser.util.provider.FavoriteProvider.Companion.CONTENT_URI
import com.bumptech.glide.Glide

class StackRemoteViewsFactory(private val mContext: Context)
    : RemoteViewsService.RemoteViewsFactory {
    private var userFav = mutableListOf<UserProperty>()

    override fun onCreate() {}

    override fun onDestroy() {}

    override fun getCount(): Int = userFav.size

    override fun onDataSetChanged() {
        val identifyToken = Binder.clearCallingIdentity()

        userFav.clear()
        val cursor = mContext.contentResolver.query(CONTENT_URI, null, null, null, null)
        userFav = mapCursorToList(cursor)
        Log.i(TAG, "onDataSetChanged: data updated")

        Binder.restoreCallingIdentity(identifyToken)
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)

        try {
            val bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load(userFav[position].avatar)
                    .submit()
                    .get()

            rv.setImageViewBitmap(R.id.imageView, bitmap)
            rv.setTextViewText(R.id.tv_username_widget, userFav[position].username)
            val extras = bundleOf(
                    ImageBannerWidget.EXTRA_ITEM to userFav[position].username
            )

            val fillInIntent = Intent().putExtras(extras)
            rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        } catch (e: IndexOutOfBoundsException) {
            Log.e(TAG, "getViewAt: ${e.message}")
        }

        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    companion object {
        private const val TAG = "StackRemoteViewsFactory"
    }
}
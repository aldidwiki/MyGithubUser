package com.aldidwikip.mygithubuser.util.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.aldidwikip.mygithubuser.data.local.LocalService
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent

class FavoriteProvider : ContentProvider() {
    companion object {
        private const val USER = 1
        private const val SCHEME = "content"
        private const val AUTHORITY = "com.aldidwikip.mygithubuser"
        private const val TABLE_NAME = "favorite_table"

        val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
    }

    private val sUriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, TABLE_NAME, USER)
    }

    @InstallIn(ApplicationComponent::class)
    @EntryPoint
    interface LocalServiceContentProviderEntryPoint {
        fun localService(): LocalService
    }

    private fun getLocalService(appContext: Context): LocalService {
        val hiltEntryPoint = EntryPointAccessors
                .fromApplication(appContext, LocalServiceContentProviderEntryPoint::class.java)
        return hiltEntryPoint.localService()
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
            uri: Uri, projection: Array<String>?, selection: String?,
            selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val appContext = context?.applicationContext ?: throw IllegalStateException()
        val localService: LocalService = getLocalService(appContext)

        val cursor = if (sUriMatcher.match(uri) == USER) {
            localService.provideFavorite()
        } else {
            null
        }
        cursor?.setNotificationUri(appContext.contentResolver, uri)

        return cursor
    }

    override fun getType(uri: Uri): String? {
        throw UnsupportedOperationException("Only reading operations are allowed")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (USER) {
            sUriMatcher.match(uri) -> 1
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
            uri: Uri, values: ContentValues?, selection: String?,
            selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Only reading operations are allowed")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (USER) {
            sUriMatcher.match(uri) -> 1
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }
}
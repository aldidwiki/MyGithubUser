package com.aldidwiki.consumerapp.data.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.aldidwikip.mygithubuser"
    const val SCHEME = "content"

    class UserColumns : BaseColumns {
        companion object {
            private const val TABLE_NAME = "favorite_table"
            const val USERNAME_FAV = "username_fav"
            const val IS_FAVORITE = "isFavorite"
            const val AVATAR = "avatar_fav"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
        }
    }
}
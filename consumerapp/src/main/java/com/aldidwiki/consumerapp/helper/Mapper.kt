package com.aldidwiki.consumerapp.helper

import android.database.Cursor
import com.aldidwiki.consumerapp.data.db.DatabaseContract.UserColumns.Companion.AVATAR
import com.aldidwiki.consumerapp.data.db.DatabaseContract.UserColumns.Companion.IS_FAVORITE
import com.aldidwiki.consumerapp.data.db.DatabaseContract.UserColumns.Companion.USERNAME_FAV
import com.aldidwiki.consumerapp.data.entity.UserProperty

fun mapCursorToList(cursor: Cursor?): List<UserProperty> {
    val favList = ArrayList<UserProperty>()
    cursor?.apply {
        while (moveToNext()) {
            val usernameFav = getString(getColumnIndexOrThrow(USERNAME_FAV))
            val isFavorite = getInt(getColumnIndexOrThrow(IS_FAVORITE)) > 0
            val avatar = getString(getColumnIndexOrThrow(AVATAR))
            favList.add(UserProperty(usernameFav, avatar, isFavorite))
        }
    }

    return favList
}
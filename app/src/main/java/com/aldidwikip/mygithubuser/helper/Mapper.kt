package com.aldidwikip.mygithubuser.helper

import android.database.Cursor
import com.aldidwikip.mygithubuser.data.model.UserProperty

fun mapCursorToList(cursor: Cursor?): MutableList<UserProperty> {
    val favList = mutableListOf<UserProperty>()
    cursor?.apply {
        while (moveToNext()) {
            val usernameFav = getString(getColumnIndexOrThrow("username_fav"))
            val isFavorite = getInt(getColumnIndexOrThrow("isFavorite")) > 0
            val avatar = getString(getColumnIndexOrThrow("avatar_fav"))
            favList.add(UserProperty(usernameFav, avatar, isFavorite))
        }
    }

    return favList
}
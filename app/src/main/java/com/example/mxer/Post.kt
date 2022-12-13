package com.example.mxer

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser


@ParseClassName("Post")
class Post : ParseObject() {
    fun getId(): String?{
        return getString(KEY_ID)
    }
    fun getAuthor(): ParseUser? {
        return getParseUser(KEY_AUTHOR)
    }
    fun setAuthor(author: ParseUser) {
        put(KEY_AUTHOR, author)
    }
    fun getImage(): ParseFile? {
        return getParseFile(KEY_IMAGE)
    }
    fun setImage(image: ParseFile) {
        put(KEY_IMAGE, image)
    }
    fun getDesc(): String? {
        return getString(KEY_DESC)
    }
    fun setDesc(desc: String) {
        put(KEY_DESC, desc)
    }
    fun getProfane(): Double {
        return getDouble(KEY_PROFANE)
    }
    fun setProfane(profane: Double) {
        put(KEY_PROFANE, profane)
    }
    fun getComm(): ParseObject? {
        return getParseObject(KEY_COMM)
    }
    fun setComm(obj: ParseObject) {
        put(KEY_COMM, obj)
    }

    companion object {
        const val KEY_ID = "objectId"
        const val KEY_AUTHOR = "author"
        const val KEY_IMAGE = "image"
        const val KEY_DESC = "description"
        const val KEY_PROFANE = "profane"
        const val KEY_COMM = "community"
    }
}
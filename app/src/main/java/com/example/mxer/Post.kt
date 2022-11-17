package com.example.mxer

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser


@ParseClassName("Post")
class Post : ParseObject() {
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


    companion object {
        const val KEY_AUTHOR = "author"
        const val KEY_IMAGE = "image"
        const val KEY_DESC = "description"
        const val KEY_COMM = "community"

    }
}
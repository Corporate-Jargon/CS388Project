package com.example.mxer

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Comment")
class Comment: ParseObject() {
    fun getAuthor(): ParseUser? {
        return getParseUser(KEY_AUTHOR)
    }
    fun setAuthor(author: ParseUser){
        put(KEY_AUTHOR, author)
    }
    fun getPost(): ParseObject? {
        return getParseObject(KEY_REPLY)
    }
    fun setPost(post: ParseObject){
        put(KEY_REPLY, post)
    }
    fun getDescription(): String? {
        return getString(KEY_DESCRIPTION)
    }
    fun setDescription(desc: String) {
        put(KEY_DESCRIPTION, desc)
    }
    fun getProfane(): Double? {
        return getDouble(KEY_PROFANE)
    }
    fun setProfane(profane: Double) {
        put(KEY_PROFANE, profane)
    }


    companion object {
        const val KEY_AUTHOR = "author"
        const val KEY_REPLY = "reply_to"
        const val KEY_DESCRIPTION = "description"
        const val KEY_PROFANE = "profane"
    }
}
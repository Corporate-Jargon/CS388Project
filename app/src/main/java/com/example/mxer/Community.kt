package com.example.mxer

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Community")
class Community: ParseObject() {
    fun getId(): String? {
        return getString(KEY_ID)
    }
    fun getName(): String? {
        return getString(KEY_NAME)
    }
    fun setName(name: String) {
        put(KEY_NAME, name)
    }
    fun getIcon(): ParseFile? {
        return getParseFile(KEY_ICON)
    }
    fun setIcon(icon: ParseFile) {
        put(KEY_ICON, icon)
    }
    fun getOwner(): ParseUser? {
        return getParseUser(KEY_OWNER)
    }
    fun setOwner(owner: ParseUser) {
        put(KEY_OWNER, owner)
    }
    fun getDesc(): String? {
        return getString(KEY_DESC)
    }
    fun setDesc(desc: String) {
        put(KEY_NAME, desc)
    }
    fun getMxe1(): ParseObject? {
        return getParseObject(KEY_EVENT1)
    }
    fun setMxe1(id: ParseObject) {
        put(KEY_EVENT1, id)
    }
    fun getMxe2(): ParseObject? {
        return getParseObject(KEY_EVENT2)
    }
    fun setMxe2(id: ParseObject) {
        put(KEY_EVENT2, id)
    }
    //TODO Delete function below once feed is fleshed out
    fun setId(id: String) {
        put(KEY_ID, id)
    }
    companion object {
        const val KEY_ID = "objectId"
        const val KEY_NAME = "name"
        const val KEY_ICON = "icon"
        const val KEY_DESC = "description"
        const val KEY_EVENT1 = "event_community1"
        const val KEY_EVENT2 = "event_community2"
        const val KEY_OWNER = "owner"
    }
}
package com.example.mxer

import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("Community")
class Community: ParseObject() {

    companion object {
        const val KEY_NAME = "name"
        const val KEY_ICON = "icon"
        const val KEY_DESC = "description"
        const val KEY_EVENT1 = "event_community1"
        const val KEY_EVENT2 = "event_community2"
    }
}
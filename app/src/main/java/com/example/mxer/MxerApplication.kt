package com.example.mxer

import android.app.Application
import com.parse.Parse
import com.parse.ParseObject

class MxerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ParseObject.registerSubclass(Post::class.java)
        ParseObject.registerSubclass(Community::class.java)
        ParseObject.registerSubclass(Comment::class.java)
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.parse_app_id))
                .clientKey(getString(R.string.parse_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        )
    }
}
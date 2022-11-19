package com.example.mxer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.parse.ParseUser

open class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Check if logged in
        // If true, go to main activity
        if (ParseUser.getCurrentUser() != null) {
            goToMainActivity()
        }

        findViewById<Button>(R.id.login_button).setOnClickListener {
            val username = findViewById<EditText>(R.id.et_username).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            loginUser(username, password)
        }
        findViewById<TextView>(R.id.login_link).setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(username: String, password: String) {
        ParseUser.logInInBackground(
            username, password, ({ user, e ->
                if (user != null) {
                    Log.i(TAG, "Successfully mixed in!")
                    goToMainActivity()
                } else {
                    e.printStackTrace()
                    Toast.makeText(this, "Error mixing in", Toast.LENGTH_SHORT).show()
                }
            })
        )
    }

    fun goToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        // End app after using back button by closing this activity
        finish()
    }

    companion object {
        const val TAG = "LoginActivity"
    }
}
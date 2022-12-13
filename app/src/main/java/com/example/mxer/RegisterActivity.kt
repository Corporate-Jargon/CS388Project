package com.example.mxer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.parse.ParseUser

class RegisterActivity : LoginActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Check if logged in
        // If true, go to main activity
        if (ParseUser.getCurrentUser() != null) {
            goToMainActivity()
        }

        findViewById<Button>(R.id.register_button).setOnClickListener {
            val username = findViewById<EditText>(R.id.et_username).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            signUpUser(username, password)
        }
        findViewById<TextView>(R.id.login_link).setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signUpUser(username: String, password: String) {
        // Create the ParseUser
        val user = ParseUser()
        // Set fields for the user to be created
        user.username = username
        user.setPassword(password)
        user.signUpInBackground { e ->
            if (e == null) {
                goToComposeActivity()
                Toast.makeText(this, "Mixed up!", Toast.LENGTH_SHORT).show()
            } else {
                e.printStackTrace()
                Toast.makeText(this, "Error mixing up", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun goToComposeActivity() {
        val intent = Intent(this@RegisterActivity, ComposeActivity::class.java)
        startActivity(intent)
        // End app after using back button by closing this activity
        finish()
    }
    companion object {
        const val TAG = "RegisterActivity"
    }
}
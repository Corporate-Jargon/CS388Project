package com.example.mxer

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.google.android.material.switchmaterial.SwitchMaterial
import com.parse.ParseUser
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class SettingsActivity : AppCompatActivity() {
    private var listOfNums = mutableListOf("true","true","true","true")
    enum class Setting(val pos: Int) {
        PUSH(0),
        FILTER(1),
        ACCESSIBILITY(2)
    }
    private lateinit var pushSwitch: SwitchMaterial
    private lateinit var filterSwitch: SwitchMaterial
    private lateinit var accessibleSwitch: SwitchMaterial
    private lateinit var saveBtn: Button
    private lateinit var logoutBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        loadItems()

        pushSwitch = findViewById(R.id.sw_notification)
        filterSwitch = findViewById(R.id.sw_filter)
        accessibleSwitch = findViewById(R.id.sw_accessibility)
        saveBtn = findViewById(R.id.btn_save)
        logoutBtn = findViewById(R.id.btn_logout)

        // Load all the toggles to the appropriate values
        pushSwitch.isChecked = listOfNums[Setting.PUSH.pos].toBoolean()
        filterSwitch.isChecked = listOfNums[Setting.FILTER.pos].toBoolean()
        accessibleSwitch.isChecked = listOfNums[Setting.ACCESSIBILITY.pos].toBoolean()

        pushSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            listOfNums[Setting.PUSH.pos] = isChecked.toString()
            // Make push notifications also isChecked
        }
        filterSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                showDialog()
            }
            if (!isChecked) listOfNums[Setting.FILTER.pos] = isChecked.toString()
            // Make profanity filter also isChecked
            // It would have a threshold of 75%
        }
        accessibleSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            listOfNums[Setting.ACCESSIBILITY.pos] = isChecked.toString()
            // Make accessibility also isChecked
        }
        saveBtn.setOnClickListener {
            saveItems()
        }
        logoutBtn.setOnClickListener {
            logoutUser()
        }
    }
    private fun getDataFile(): File {
        // Every line is going to represent a specific task in our list of tasks
        val username = ParseUser.getCurrentUser().username
        return File(filesDir, "settings${username}.txt")
    }
    // Load the items by reading every line in the data file
    private fun loadItems() {
        try {
            listOfNums = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // Save items by writing them into our data file
    private fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfNums)
            Toast.makeText(this, "Saved settings!", Toast.LENGTH_SHORT).show()
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_back, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_back){
            val intent = Intent(this@SettingsActivity, MainActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
    private fun onDialogPositiveClick() {
        listOfNums[Setting.FILTER.pos] = "true"
        filterSwitch.isChecked = listOfNums[Setting.FILTER.pos].toBoolean()

    }
    private fun onDialogPositiveClick1() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.confirmation))
            .setPositiveButton(getString(R.string.ok)) { _,_ -> onDialogPositiveClick2() }
            .setNegativeButton(getString(R.string.cancel)) { _,_ -> onDialogNegativeClick() }
            .show()

    }

    private fun onDialogPositiveClick2() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.confirmation))
            .setPositiveButton(getString(R.string.ok)) { _,_ -> onDialogPositiveClick3() }
            .setNegativeButton(getString(R.string.cancel)) { _,_ -> onDialogNegativeClick() }
            .show()

    }

    private fun onDialogPositiveClick3() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.confirmation))
            .setPositiveButton(getString(R.string.ok)) { _,_ -> onDialogPositiveClick4() }
            .setNegativeButton(getString(R.string.cancel)) { _,_ -> onDialogNegativeClick() }
            .show()

    }

    private fun onDialogPositiveClick4() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.confirmation))
            .setPositiveButton(getString(R.string.yeah)) { _,_ -> onDialogPositiveClick() }
            .setNegativeButton(getString(R.string.maybe)) { _,_ -> onDialogNegativeClick() }
            .show()

    }

    private fun onDialogNegativeClick() {
        listOfNums[Setting.FILTER.pos] = "false"
        filterSwitch.isChecked = listOfNums[Setting.FILTER.pos].toBoolean()
    }
    private fun showDialog(){
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.confirmation))
            .setPositiveButton(getString(R.string.ok)) { _,_ -> onDialogPositiveClick1() }
            .setNegativeButton(getString(R.string.cancel)) { _,_ -> onDialogNegativeClick() }
            .show()
    }
    private fun logoutUser() {
        ParseUser.logOut()
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
        val currentUser = ParseUser.getCurrentUser() // this will now be null
        goToLoginActivity()
    }
    private fun goToLoginActivity() {
        val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
        startActivity(intent)
        // End app after using back button by closing this activity
        finish()
    }
}
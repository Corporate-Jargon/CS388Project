package com.example.mxer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.switchmaterial.SwitchMaterial
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class SettingsActivity : AppCompatActivity() {
    var listOfNums = mutableListOf<String>("true","true","true","true")
    enum class Setting(val pos: Int) {
        PUSH(0),
        FILTER(1)
    }
    lateinit var pushSwitch: SwitchMaterial
    lateinit var filterSwitch: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        loadItems()

        pushSwitch = findViewById(R.id.sw_notification)
        filterSwitch = findViewById(R.id.sw_filter)
        if (listOfNums[Setting.PUSH.pos].isEmpty()) {
            listOfNums[Setting.PUSH.pos] = "true"
        }
        if (listOfNums[Setting.FILTER.pos].isEmpty()) {
            listOfNums[Setting.FILTER.pos] = "true"
        }

        pushSwitch.isChecked = listOfNums[Setting.PUSH.pos].toBoolean()
        filterSwitch.isChecked = listOfNums[Setting.FILTER.pos].toBoolean()

        pushSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            listOfNums[Setting.PUSH.pos] = isChecked.toString()
            // Make push notifications also isChecked
            saveItems()
        }
        filterSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            listOfNums[Setting.FILTER.pos] = isChecked.toString()
            // Make profanity filter also isChecked
            // It would have a threshold of 75%
            saveItems()
        }
    }
    fun getDataFile(): File {
        // Every line is going to represent a specific task in our list of tasks
        return File(filesDir, "settings.txt")
    }
    // Load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfNums = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // Save items by writing them into our data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfNums)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}
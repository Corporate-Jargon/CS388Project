package com.example.mxer

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class SettingsActivity : AppCompatActivity() {
    lateinit var pushSwitch: Switch
    lateinit var filterSwitch: Switch
    var listOfNums = mutableListOf<String>("1.0","1.0","1.0","1.0")
    enum class Setting(val pos: Int) {
        PUSH(0),
        FILTER(1)
    }
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        loadItems()

        pushSwitch = findViewById(R.id.sw_notification)
        filterSwitch = findViewById(R.id.sw_filter)
        pushSwitch.isChecked = listOfNums[Setting.PUSH.pos].toBoolean()
        pushSwitch.isChecked = listOfNums[Setting.FILTER.pos].toBoolean()

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
        var file = File(filesDir, "settings.txt")
        if (!file.exists()) {
            saveItems()
        }
        return file
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

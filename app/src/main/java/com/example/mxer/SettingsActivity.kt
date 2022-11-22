package com.example.mxer

import PurchaseConfirmationDialogFragment
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.mxer.fragments.NoticeDialogFragment
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.NonCancellable.start
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class SettingsActivity : AppCompatActivity(), NoticeDialogFragment.NoticeDialogListener {
    var listOfNums = mutableListOf<String>("true","true","true","true")
    enum class Setting(val pos: Int) {
        PUSH(0),
        FILTER(1),
        ACCESSIBILITY(2)
    }
    lateinit var pushSwitch: SwitchMaterial
    lateinit var filterSwitch: SwitchMaterial
    lateinit var accessibleSwitch: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        loadItems()


        pushSwitch = findViewById(R.id.sw_notification)
        filterSwitch = findViewById(R.id.sw_filter)
        accessibleSwitch = findViewById(R.id.sw_accessibility)

        // Load all the toggles to the appropriate values
        pushSwitch.isChecked = listOfNums[Setting.PUSH.pos].toBoolean()
        filterSwitch.isChecked = listOfNums[Setting.FILTER.pos].toBoolean()
        accessibleSwitch.isChecked = listOfNums[Setting.ACCESSIBILITY.pos].toBoolean()

        pushSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            listOfNums[Setting.PUSH.pos] = isChecked.toString()
            // Make push notifications also isChecked
            saveItems()
        }
        filterSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
//            listOfNums[Setting.FILTER.pos] = isChecked.toString()
            if (isChecked) {
                showDialog()
            }
            // Make profanity filter also isChecked
            // It would have a threshold of 75%
            saveItems()
        }
        accessibleSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            listOfNums[Setting.ACCESSIBILITY.pos] = isChecked.toString()
            // Make accessibility also isChecked
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
    fun showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        val dialog = NoticeDialogFragment()
        dialog.show(supportFragmentManager, "NoticeDialogFragment")
    }
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        listOfNums[Setting.FILTER.pos] = "true"
        filterSwitch.isChecked = listOfNums[Setting.FILTER.pos].toBoolean()

    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        listOfNums[Setting.FILTER.pos] = "false"
        filterSwitch.isChecked = listOfNums[Setting.FILTER.pos].toBoolean()
    }
    fun showDialog(){
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.confirmation))
            .setPositiveButton(getString(R.string.ok)) { _,_ -> onDialogPositiveClick(DialogFragment()) }
            .setNegativeButton(getString(R.string.cancel)) { _,_ -> onDialogNegativeClick(DialogFragment()) }
            .show()
    }
}
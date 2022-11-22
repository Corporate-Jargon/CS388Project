package com.example.mxer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mxer.R
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class SettingsFragment : Fragment() {
    lateinit var pushSwitch: Switch
    lateinit var filterSwitch: Switch

    var listOfNums = mutableListOf<String>("1.0","1.0","1.0","1.0")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        saveItems()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_browse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadItems()

        pushSwitch = view.findViewById(R.id.sw_notification)
        filterSwitch = view.findViewById(R.id.sw_filter)


    }
    fun getDataFile(): File {
        // Every line is going to represent a specific task in our list of tasks
        return File(context?.filesDir, "settings.txt")
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
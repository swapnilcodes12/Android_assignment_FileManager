package com.swapnil.filemanager_new

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

class MainActivity : AppCompatActivity() {

    private val REQUEST_PERMISSION_CODE = 1
    private lateinit var currentDirectory: File
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)

        if (checkPermission()) {
            loadExternalStorage(Environment.getExternalStorageDirectory())
        } else {
            requestPermission()
        }
    }

    override fun onBackPressed() {
        if(currentDirectory.parentFile != null){
            loadExternalStorage(currentDirectory.parentFile)
        }else{
            super.onBackPressed()
        }
    }

    private fun checkPermission(): Boolean {
        val permission =
            Manifest.permission.WRITE_EXTERNAL_STORAGE

        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadExternalStorage(Environment.getExternalStorageDirectory())
            }
        }
    }

    private fun loadExternalStorage(directory: File) {
        currentDirectory = directory

        val items = ArrayList<String>()
        val files = directory.listFiles()

        if (files != null) {

            val sortedFiles = files.sortedWith(compareBy({ it.isDirectory }, { it.name }))

            for (file in sortedFiles) {
                items.add(file.name)
            }
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        listView.adapter = adapter

        listView.setOnItemClickListener(AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedFile = File(directory, items[position])

            if (selectedFile.isDirectory) {

                loadExternalStorage(selectedFile)
            } else {
                openFile(selectedFile)
            }
        })
    }


    private fun openFile(file: File) {
        val uri = Uri.fromFile(file)

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "*/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }
}

package com.swapnil.filemanager_new

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.io.File

class FileListAdapter(
    context: Context,
    private val resource: Int,
    private val objects: List<File>
) : ArrayAdapter<File>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
        val item = objects[position]

        val itemNameTextView: TextView = itemView.findViewById(R.id.textViewItemName)
        itemNameTextView.text = item.name

        return itemView
    }
}

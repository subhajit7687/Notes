package com.subhajit0061.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.subhajit0061.notes.databinding.ActivityHomeBinding
import com.subhajit0061.notes.functions.DBHelper
import com.subhajit0061.notes.functions.DBHelper.Companion.COLUMN_BODY
import com.subhajit0061.notes.functions.DBHelper.Companion.COLUMN_DATE
import com.subhajit0061.notes.functions.DBHelper.Companion.COLUMN_ID
import com.subhajit0061.notes.functions.DBHelper.Companion.COLUMN_TITLE
import com.subhajit0061.notes.functions.DBHelper.Companion.TABLE_NAME
import com.subhajit0061.notes.functions.MyAdapter
import com.subhajit0061.notes.functions.NotesDetails
import com.subhajit0061.notes.functions.OnItemClick

class Home : AppCompatActivity(), OnItemClick {

    //Declaring properties
    companion object {
        val list = ArrayList<NotesDetails>()
        var listUpdateType = ""
    }

    private lateinit var binding: ActivityHomeBinding
    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: MyAdapter
    private var listIsEmpty = true
    private var updatePosition = 0

    //On create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initializing
        dbHelper = DBHelper(this)
        adapter = MyAdapter(this, this)

        readAndDisplayNotes()

        //Preparing recycler view
        binding.myRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.myRecyclerView.adapter = adapter

        //action when add button clicked
        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, Add::class.java)
            startActivity(intent)
        }
    }

    //function for reading and showing local data after opening the app
    private fun readAndDisplayNotes() {
        val db = dbHelper.readableDatabase
        val cursor =
            db.rawQuery("SELECT $COLUMN_ID, $COLUMN_TITLE, $COLUMN_DATE FROM $TABLE_NAME", null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val title = cursor.getString(1)
            val date = cursor.getString(2)

            list.add(NotesDetails(id, title, date))
        }
        cursor.close()
        db.close()
        if (list.size == 0)
            binding.txtNoNotes.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        if (listIsEmpty && list.size > 0) {
            listIsEmpty = false
            binding.txtNoNotes.visibility = View.GONE
        }
        if (listUpdateType == "new")
            adapter.notifyItemInserted(list.size - 1)
        else if (listUpdateType == "update")
            adapter.notifyItemChanged(updatePosition)
    }

    //Action when clicked on list items
    override fun onItemClick(position: Int) {
        updatePosition = position
        val db = dbHelper.readableDatabase
        val id = list[position].id
        val title = list[position].tile

        val cursor = db.rawQuery(
            "SELECT $COLUMN_BODY FROM $TABLE_NAME WHERE $COLUMN_ID = ?",
            arrayOf(id.toString())
        )
        cursor.moveToFirst()
        val body = cursor.getString(0)

        cursor.close()
        db.close()

        val intent = Intent(this, Add::class.java)
        intent.putExtra("id", id)
        intent.putExtra("title", title)
        intent.putExtra("body", body)
        intent.putExtra("position", position)

        startActivity(intent)
    }

    //Action when clicked on list delete icon
    override fun onDeleteClick(position: Int) {
        val db = dbHelper.writableDatabase
        val id = list[position].id

        val res = db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        if (res != -1) {
            list.removeAt(position)
            adapter.notifyItemRemoved(position)
            if (list.size == 0) {
                listIsEmpty = true
                binding.txtNoNotes.visibility = View.VISIBLE
            }
        }
    }
}
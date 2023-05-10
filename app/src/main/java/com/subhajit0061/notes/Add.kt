package com.subhajit0061.notes

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.subhajit0061.notes.databinding.ActivityAddBinding
import com.subhajit0061.notes.functions.DBHelper
import com.subhajit0061.notes.functions.DBHelper.Companion.COLUMN_BODY
import com.subhajit0061.notes.functions.DBHelper.Companion.COLUMN_DATE
import com.subhajit0061.notes.functions.DBHelper.Companion.COLUMN_ID
import com.subhajit0061.notes.functions.DBHelper.Companion.COLUMN_TITLE
import com.subhajit0061.notes.functions.DBHelper.Companion.TABLE_NAME
import com.subhajit0061.notes.functions.NotesDetails
import java.text.SimpleDateFormat
import java.util.Date

class Add : AppCompatActivity() {

    //Declaring properties
    private lateinit var binding: ActivityAddBinding
    private var saveType = "new"
    private lateinit var dbHelper: DBHelper
    private var saveStatus = 0
    private var id = 0
    private var position = 0
    private var oldBody: String? = ""
    private var oldTitle: String? = ""

    //On create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = DBHelper(this)

        //Setting custom toolbar as actionbar
        setSupportActionBar(binding.addPageMyToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Checking the status of action through bundle
        val bundle = intent.extras
        if (bundle != null) {
            saveType = "update"

            id = bundle.getInt("id")
            position = bundle.getInt("position")
            oldBody = bundle.getString("body")
            oldTitle = bundle.getString("title")
            val noteDate = bundle.getString("date")

            binding.txtDate.text = noteDate
            binding.edtTitle.setText(oldTitle)
            binding.edtBody.setText(oldBody)
        } else {
            //Setting date and time to text view
            val formatter = SimpleDateFormat.getDateTimeInstance()
            val date = formatter.format(Date())
            binding.txtDate.text = date
        }

        //Action on back pressed
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (saveStatus == 0) {
                    val body = binding.edtBody.text.toString()
                    if (body.isBlank())
                        Home.listUpdateType = ""
                    else {
                        if (saveType == "new")
                            insertData()
                        else {
                            val title = binding.edtTitle.text.toString().trim()
                            if (body != oldBody || oldTitle != title)
                                updateData()
                            else
                                Home.listUpdateType = ""
                        }
                    }
                }
                finish()
            }
        })
    }

    //Linking menu bar with project
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Assigning click events on menu items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressedDispatcher.onBackPressed()
            else -> {
                if (saveType == "new")
                    insertData()
                else
                    updateData()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //Updating data in database and list
    private fun updateData() {
        val db = dbHelper.writableDatabase

        val title = binding.edtTitle.text.toString().trim()
        val body = binding.edtBody.text.toString()

        val formatter = SimpleDateFormat.getDateTimeInstance()
        val date = formatter.format(Date())

        if (body.isBlank())
            Toast.makeText(this, "Body cannot be empty", Toast.LENGTH_SHORT).show()
        else {
            val values = ContentValues().apply {
                put(COLUMN_TITLE, title)
                put(COLUMN_BODY, body)
                put(COLUMN_DATE, date)
            }

            val res = db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
            if (res == -1)
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
            else {
                Home.list[position].date = date
                Home.list[position].tile = title

                Home.listUpdateType = "update"
                saveStatus = 1
                Toast.makeText(this, "Updated successfully!", Toast.LENGTH_SHORT).show()
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    //Inserting data to database and list
    private fun insertData() {
        val db = dbHelper.writableDatabase
        val title = binding.edtTitle.text.toString().trim()
        val body = binding.edtBody.text.toString()
        val date = binding.txtDate.text.toString()

        if (body.isBlank())
            Toast.makeText(this, "Body cannot be empty", Toast.LENGTH_SHORT).show()
        else {
            val values = ContentValues().apply {
                put(COLUMN_DATE, date)
                put(COLUMN_TITLE, title)
                put(COLUMN_BODY, body)
            }
            val res = db.insert(TABLE_NAME, null, values)
            if (res == -1L)
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
            else {
                val cursor = db.rawQuery(
                    "SELECT $COLUMN_ID FROM $TABLE_NAME WHERE $COLUMN_DATE = ? AND $COLUMN_TITLE = ? AND $COLUMN_BODY = ?",
                    arrayOf(date, title, body)
                )
                cursor.moveToLast()
                val id = cursor.getInt(0)
                cursor.close()
                db.close()

                Home.list.add(NotesDetails(id, title, date))

                saveStatus = 1
                Home.listUpdateType = "new"
                Toast.makeText(this, "Added successfully!", Toast.LENGTH_SHORT).show()
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}
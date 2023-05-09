package com.subhajit0061.notes.functions

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {
    companion object {
        const val DATABASE_NAME = "notes.db"
        const val VERSION = 1
        const val TABLE_NAME = "notes"
        const val COLUMN_ID = "_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DATE = "date"
        const val COLUMN_BODY = "body"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TITLE TEXT, $COLUMN_DATE TEXT, $COLUMN_BODY TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val upgradeTable = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(upgradeTable)
        onCreate(db)
    }
}
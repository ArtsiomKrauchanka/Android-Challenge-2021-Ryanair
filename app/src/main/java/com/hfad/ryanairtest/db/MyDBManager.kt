package com.hfad.ryanairtest.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.hfad.ryanairtest.Circle
import com.hfad.ryanairtest.Line
import com.hfad.ryanairtest.Rectangle

class MyDBManager(context: Context) {
    val myDBHelper = MyDBHelper(context)
    var db: SQLiteDatabase? = null

    fun openDB() {
        db = myDBHelper.writableDatabase
    }

    fun closeDB() {
        myDBHelper.close()
    }

    fun insertToDB(number: Int, shape: String, data: String) {
        val values = ContentValues().apply {
            put(MyDBClass.COLUMN_NAME_NUMBER, number)
            put(MyDBClass.COLUMN_NAME_SHAPE, shape)
            put(MyDBClass.COLUMN_NAME_DATA, data)
        }
        db?.insert(MyDBClass.TABLE_NAME, null, values)
    }

    fun readDBCircles(): MutableList<Circle> {
        val dataList = mutableListOf<Circle>()
        val selection = "${MyDBClass.COLUMN_NAME_SHAPE} = ?"
        val selectionArgs = arrayOf("C")
        val cursor = db?.query(
            MyDBClass.TABLE_NAME,
            null,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null
        )

        with(cursor) {
            while (this?.moveToNext()!!) {
                val id =
                    cursor?.getInt(cursor.getColumnIndexOrThrow(MyDBClass.COLUMN_NAME_NUMBER)) ?: 0
                val shape =
                    cursor?.getString(cursor.getColumnIndexOrThrow(MyDBClass.COLUMN_NAME_SHAPE))
                        ?: ""
                val data =
                    cursor?.getString(cursor.getColumnIndexOrThrow(MyDBClass.COLUMN_NAME_DATA))
                        ?: ""
                dataList.add(Circle(id, shape, data))
            }
        }



        cursor?.close()
        return dataList
    }

    fun readDBRectangle(): MutableList<Rectangle> {
        val dataList = mutableListOf<Rectangle>()
        val selection = "${MyDBClass.COLUMN_NAME_SHAPE} = ?"
        val selectionArgs = arrayOf("R")
        val cursor = db?.query(
            MyDBClass.TABLE_NAME,
            null,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null
        )

        with(cursor) {
            while (this?.moveToNext()!!) {
                val id =
                    cursor?.getInt(cursor.getColumnIndexOrThrow(MyDBClass.COLUMN_NAME_NUMBER)) ?: 0
                val shape =
                    cursor?.getString(cursor.getColumnIndexOrThrow(MyDBClass.COLUMN_NAME_SHAPE))
                        ?: ""
                val data =
                    cursor?.getString(cursor.getColumnIndexOrThrow(MyDBClass.COLUMN_NAME_DATA))
                        ?: ""
                dataList.add(Rectangle(id, shape, data))
            }
        }


        cursor?.close()
        return dataList
    }

    fun readDBLines(): MutableList<Line> {
        val dataList = mutableListOf<Line>()
        val selection = "${MyDBClass.COLUMN_NAME_SHAPE} = ?"
        val selectionArgs = arrayOf("L")
        val cursor = db?.query(
            MyDBClass.TABLE_NAME,
            null,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null
        )

        with(cursor) {
            while (cursor?.moveToNext()!!) {
                val id =
                    cursor.getInt(cursor.getColumnIndexOrThrow(MyDBClass.COLUMN_NAME_NUMBER)) ?: 0
                val shape =
                    cursor.getString(cursor.getColumnIndexOrThrow(MyDBClass.COLUMN_NAME_SHAPE))
                        ?: ""
                val data =
                    cursor.getString(cursor.getColumnIndexOrThrow(MyDBClass.COLUMN_NAME_DATA)) ?: ""
                dataList.add(Line(id, shape, data))
            }
        }

        cursor?.close()
        return dataList
    }

    fun isEmptyDB(): Boolean {
        val cursor = db?.query(
            MyDBClass.TABLE_NAME,
            null,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null
        )

        with(cursor) {
            while (this?.moveToNext()!!) {
                cursor?.close()
                return false
            }
        }

        cursor?.close()
        return true
    }
}
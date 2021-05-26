package com.hfad.ryanairtest.db

import android.provider.BaseColumns

object MyDBClass {
    const val TABLE_NAME = "geometry"
    const val COLUMN_NAME_NUMBER = "number"
    const val COLUMN_NAME_SHAPE = "shape"
    const val COLUMN_NAME_DATA = "data"

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "AndroidChallengeDB"

    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY, $COLUMN_NAME_NUMBER INTEGER, $COLUMN_NAME_SHAPE TEXT, $COLUMN_NAME_DATA TEXT)"

    const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}
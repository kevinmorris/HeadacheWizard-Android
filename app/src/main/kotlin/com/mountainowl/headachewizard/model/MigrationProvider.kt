package com.mountainowl.headachewizard.model

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri


class MigrationProvider : ContentProvider() {

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    private lateinit var databaseHelper : DatabaseHelper

    init {
        uriMatcher.addURI("com.mountainowl.headachewizard.model.MigrationProvider.free", "headacheEntries", 1)
        uriMatcher.addURI("com.mountainowl.headachewizard.model.MigrationProvider.free", "factors", 2)
        uriMatcher.addURI("com.mountainowl.headachewizard.model.MigrationProvider.free", "factorEntries", 3)
    }

    override fun onCreate(): Boolean {
        databaseHelper = DatabaseHelper(context)
        return true
    }

    override fun query(
            uri: Uri?,
            projection: Array<out String>?,
            selection: String?,
            selectionArgs: Array<out String>?,
            sortOrder: String?): Cursor {

        when(uriMatcher.match(uri)) {
            1 -> return databaseHelper.headacheCursor()
            2 -> return databaseHelper.factorCursor()
            3 -> return databaseHelper.factorEntryCursor()
            else -> throw IllegalArgumentException()
        }
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        throw UnsupportedOperationException("not implemented")
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getType(uri: Uri?): String {
        throw UnsupportedOperationException("not implemented")
    }

    companion object {
        val HEADACHE_CONTENT_URI = Uri.parse("content://com.mountainowl.headachewizard.model.MigrationProvider.free/headacheEntries")
        val FACTOR_CONTENT_URI = Uri.parse("content://com.mountainowl.headachewizard.model.MigrationProvider.free/factors")
        val FACTOR_ENTRIES_CONTENT_URI = Uri.parse("content://com.mountainowl.headachewizard.model.MigrationProvider.free/factorEntries")
    }
}
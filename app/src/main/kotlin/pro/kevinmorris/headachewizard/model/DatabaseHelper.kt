package pro.kevinmorris.headachewizard.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.joda.time.DateTimeZone
import org.joda.time.Days
import org.joda.time.LocalDate
import java.util.*

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DatabaseHelper.DATABASE_FILE_NAME, null, DatabaseHelper.DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_HEADACHE_ENTRIES_TABLE_SQL)
        db.execSQL(CREATE_FACTORS_TABLE_SQL)
        db.execSQL(CREATE_FACTOR_ENTRIES_TABLE)
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.execSQL(ENABLE_FOREIGN_KEYS)
    }

    fun resetDatabase() {
        writableDatabase.delete(FACTOR_ENTRIES_TABLE, null, null)
        writableDatabase.delete(FACTORS_TABLE, null, null)
        writableDatabase.delete(HEADACHE_ENTRIES_TABLE, null, null)
    }

    val headacheEntries: SortedMap<LocalDate, Double>
        get() {
            val c = headacheCursor()
            val data = TreeMap<LocalDate, Double>()
            while (c.moveToNext()) {
                data.put(LocalDate(0, DateTimeZone.UTC).plusDays(c.getInt(1)), c.getDouble(2))
            }
            c.close()

            return data
        }

    fun headacheCursor(): Cursor {
        val c = readableDatabase.query(HEADACHE_ENTRIES_TABLE,
                null,
                null,
                null,
                null,
                null,
                null)

        return c
    }

    fun insertOrUpdateHeadacheEntry(date: LocalDate, value: Double?): Long {

        val dayCount = Days.daysBetween(LocalDate(0, DateTimeZone.UTC), date).days

        val c = readableDatabase.query(HEADACHE_ENTRIES_TABLE,
                arrayOf("id"),
                HEADACHE_ENTRIES_DATE_COLUMN + " = ?",
                arrayOf<String>(dayCount.toString()),
                null,
                null,
                null,
                "1")

        val id: Long
        val cv = ContentValues()

        if (c.moveToNext()) { //update
            id = c.getLong(0)
            cv.put(HEADACHE_ENTRIES_VALUE_COLUMN, value)
            writableDatabase.update(HEADACHE_ENTRIES_TABLE, cv, HEADACHE_ENTRIES_DATE_COLUMN + " = ?", arrayOf<String>(dayCount.toString()))
        } else { //insert
            cv.put(HEADACHE_ENTRIES_DATE_COLUMN, dayCount)
            cv.put(HEADACHE_ENTRIES_VALUE_COLUMN, value)
            id = writableDatabase.insert(HEADACHE_ENTRIES_TABLE, null, cv)
        }

        c.close()

        return id
    }


    val emptyFactors: List<Factor>
        get() {
            val factorC = factorCursor()

            val factors = HashMap<Long, Factor>()

            while (factorC.moveToNext()) {
                val f = Factor(factorC.getLong(0), factorC.getString(1))
                factors.put(factorC.getLong(0), f)
            }
            factorC.close()

            return ArrayList(factors.values)
        }

    fun factorCursor(): Cursor {
        val factorC = readableDatabase.query(FACTORS_TABLE, null, null, null, null, null, "name")
        return factorC
    }

    fun factorEntryCursor(): Cursor {
        val entriesC = readableDatabase.query(FACTOR_ENTRIES_TABLE, null, null, null, null, null, null)
        return entriesC
    }

    fun getFactorsUsingHeadache(headache: Headache): List<Factor> {

        val entriesC = factorEntryCursor()

        val emptyFactors = emptyFactors
        val factors = HashMap<Long, Factor>()

        for (f in emptyFactors) {
            factors.put(f.id, f)
        }

        while (entriesC.moveToNext()) {
            val factorId = entriesC.getLong(1)
            val dayCount = entriesC.getInt(2)
            val value = entriesC.getDouble(3)

            factors[factorId]?.also { f ->
                f.set(LocalDate(0, DateTimeZone.UTC).plusDays(dayCount), value, headache)
            }
        }

        val factorList = ArrayList(factors.values).sortedBy { it.name }
        entriesC.close()

        return factorList
    }

    fun getFactorUsingHeadache(factorId: Long?, headache: Headache): Factor? {

        val c = readableDatabase.query(FACTORS_TABLE,
                null,
                "id = ?",
                arrayOf<String>(factorId!!.toString()),
                null,
                null,
                null,
                "1")

        if (c.moveToNext()) {
            val f = Factor(c.getLong(0), c.getString(1))

            val entriesC = readableDatabase.query(FACTOR_ENTRIES_TABLE,
                    null,
                    FACTOR_ENTRIES_FACTOR_ID_COLUMN + " = ?",
                    arrayOf<String>(factorId.toString()),
                    null,
                    null,
                    null)

            while (entriesC.moveToNext()) {
                val dayCount = entriesC.getInt(2)
                val value = entriesC.getDouble(3)
                f.set(LocalDate(0, DateTimeZone.UTC).plusDays(dayCount), value, headache)
            }

            return f
        } else {
            return null
        }
    }

    fun insertFactor(name: String): Long {

        val c = readableDatabase.query(FACTORS_TABLE,
                arrayOf("id"),
                FACTORS_NAME_COLUMN + " = ? ",
                arrayOf(name),
                null,
                null,
                null,
                "1")

        val id: Long
        if (c.moveToNext()) {
            id = c.getLong(0)
        } else {
            val cv = ContentValues()
            cv.put(FACTORS_NAME_COLUMN, name)

            id = writableDatabase.insert(FACTORS_TABLE, null, cv)
        }

        return id
    }

    fun insertFactor(id: Long, name: String): Long {
        val cv = ContentValues()
        cv.put(FACTORS_ID_COLUMN, id)
        cv.put(FACTORS_NAME_COLUMN, name)

        val newId = writableDatabase.insert(FACTORS_TABLE, null, cv)
        return newId
    }


    fun factorExists(name: String): Boolean {

        val c = readableDatabase.query(FACTORS_TABLE,
                arrayOf("id"),
                FACTORS_NAME_COLUMN + " = ? ",
                arrayOf(name),
                null,
                null,
                null,
                "1")

        return c.moveToNext()
    }


    fun insertOrUpdateFactorEntry(factorId: Long?, date: LocalDate, value: Double?): Long {

        val dayCount = Days.daysBetween(LocalDate(0, DateTimeZone.UTC), date).days

        val c = readableDatabase.query(FACTOR_ENTRIES_TABLE,
                arrayOf("id"),
                "$FACTOR_ENTRIES_FACTOR_ID_COLUMN = ? AND $FACTOR_ENTRIES_DATE_COLUMN = ?",
                arrayOf(factorId!!.toString(), dayCount.toString()),
                null,
                null,
                null,
                "1")

        val id: Long
        val cv = ContentValues()
        if (c.moveToNext()) { //update
            cv.put(FACTOR_ENTRIES_VALUE_COLUMN, value)
            id = writableDatabase.update(FACTOR_ENTRIES_TABLE,
                    cv,
                    "$FACTOR_ENTRIES_FACTOR_ID_COLUMN = ? AND $FACTOR_ENTRIES_DATE_COLUMN = ?",
                    arrayOf(factorId.toString(), dayCount.toString())).toLong()
        } else { //insert
            cv.put(FACTOR_ENTRIES_FACTOR_ID_COLUMN, factorId)
            cv.put(FACTOR_ENTRIES_DATE_COLUMN, dayCount)
            cv.put(FACTOR_ENTRIES_VALUE_COLUMN, value)

            id = writableDatabase.insert(FACTOR_ENTRIES_TABLE, null, cv)
        }

        c.close()

        return id
    }


    fun insertFactorEntry(id: Long, factorId: Long, date: LocalDate, value: Double): Long {

        val dayCount = Days.daysBetween(LocalDate(0, DateTimeZone.UTC), date).days
        val cv = ContentValues()

        cv.put(FACTOR_ENTRIES_ID_COLUMN, id)
        cv.put(FACTOR_ENTRIES_FACTOR_ID_COLUMN, factorId)
        cv.put(FACTOR_ENTRIES_DATE_COLUMN, dayCount)
        cv.put(FACTOR_ENTRIES_VALUE_COLUMN, value)

        val newId = writableDatabase.insert(FACTOR_ENTRIES_TABLE, null, cv)
        return newId
    }


    fun deleteFactorByName(name: String): Long? {

        val c = readableDatabase.query(FACTORS_TABLE,
                arrayOf("id"),
                FACTORS_NAME_COLUMN + " = ? ",
                arrayOf(name),
                null,
                null,
                null,
                "1")

        if (c.moveToNext()) {

            val id = c.getLong(0)
            val rowsDeleted = writableDatabase.delete(FACTORS_TABLE,
                    FACTORS_NAME_COLUMN + " = ? ",
                    arrayOf(name))

            if (rowsDeleted == 1) {
                return id
            } else {
                throw IllegalStateException("Number of rows not equal to 1 for factor name: " + name)
            }
        } else {
            return null
        }
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Not Implemented
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        super.onDowngrade(db, oldVersion, newVersion)
    }

    companion object {

        val DATABASE_FILE_NAME = "headache_wizard.sqlite"
        val DATABASE_VERSION = 1

        private val HEADACHE_ENTRIES_TABLE = "HeadacheEntries"
        private val HEADACHE_ENTRIES_ID_COLUMN = "id"
        private val HEADACHE_ENTRIES_DATE_COLUMN = "date"
        private val HEADACHE_ENTRIES_VALUE_COLUMN = "value"

        private val FACTORS_TABLE = "Factors"
        private val FACTORS_ID_COLUMN = "id"
        private val FACTORS_NAME_COLUMN = "name"

        private val FACTOR_ENTRIES_TABLE = "FactorEntries"
        private val FACTOR_ENTRIES_ID_COLUMN = "id"
        private val FACTOR_ENTRIES_FACTOR_ID_COLUMN = "factor_id"
        private val FACTOR_ENTRIES_DATE_COLUMN = "date"
        private val FACTOR_ENTRIES_VALUE_COLUMN = "value"

        private val CREATE_HEADACHE_ENTRIES_TABLE_SQL =
                "create table " + HEADACHE_ENTRIES_TABLE + " (" +
                        "  " + HEADACHE_ENTRIES_ID_COLUMN + " integer primary key autoincrement," +
                        "  " + HEADACHE_ENTRIES_DATE_COLUMN + " integer not null," +
                        "  " + HEADACHE_ENTRIES_VALUE_COLUMN + " float not null" +
                        ");"

        private val CREATE_FACTORS_TABLE_SQL =
                "create table " + FACTORS_TABLE + " (" +
                        "  " + FACTORS_ID_COLUMN + " integer primary key autoincrement," +
                        "  " + FACTORS_NAME_COLUMN + " varchar(64) not null" +
                        ");"

        private val CREATE_FACTOR_ENTRIES_TABLE =
                "create table " + FACTOR_ENTRIES_TABLE + " (" +
                        "  " + FACTOR_ENTRIES_ID_COLUMN + " integer primary key autoincrement," +
                        "  " + FACTOR_ENTRIES_FACTOR_ID_COLUMN + " integer not null," +
                        "  " + FACTOR_ENTRIES_DATE_COLUMN + " integer not null," +
                        "  " + FACTOR_ENTRIES_VALUE_COLUMN + " float not null," +
                        "  foreign key(" + FACTOR_ENTRIES_FACTOR_ID_COLUMN + ") references " + FACTORS_TABLE + " on delete cascade" +
                        ");"

        private val ENABLE_FOREIGN_KEYS = "PRAGMA foreign_keys = ON;"
    }
}

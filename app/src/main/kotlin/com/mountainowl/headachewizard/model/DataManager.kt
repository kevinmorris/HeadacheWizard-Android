package com.mountainowl.headachewizard.model

import android.content.Context
import com.google.common.collect.Iterators
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate

/**
 * Thin layer of abstraction between front end and database helper.
 * This singleton object gives the app the opportunity to repackage data
 * going in and out of the database into a more elegant form for the app
 * as well as provide a caching layer if needs be.

 * @author kevinmorris
 */
class DataManager private constructor(context: Context) {
    private val db: DatabaseHelper

    var headache: Headache
        private set
    private val factors: MutableList<Factor>

    init {
        db = DatabaseHelper(context)

        headache = Headache(db.headacheEntries)
        factors = db.getFactorsUsingHeadache(headache).toMutableList()
    }

    fun migrate() {

        val contentResolver = context.contentResolver

        val headacheCursor = contentResolver.query(MigrationProvider.HEADACHE_CONTENT_URI, null,  null, null, null)
        while(headacheCursor.moveToNext()) {
            val date = LocalDate(0, DateTimeZone.UTC).plusDays(headacheCursor.getInt(1))
            val value = headacheCursor.getDouble(2)
            db.insertOrUpdateHeadacheEntry(date, value)
        }
        headacheCursor.close()

        val factorCursor = contentResolver.query(MigrationProvider.FACTOR_CONTENT_URI, null,  null, null, null)
        while(factorCursor.moveToNext()) {
            val id = factorCursor.getLong(0)
            val name = factorCursor.getString(1)
            db.insertFactor(id, name)
        }
        factorCursor.close()

        val factorEntriesCursor = contentResolver.query(MigrationProvider.FACTOR_ENTRIES_CONTENT_URI, null,  null, null, null)
        while(factorEntriesCursor.moveToNext()) {
            val id = factorEntriesCursor.getLong(0)
            val factorId = factorEntriesCursor.getLong(1)
            val date = LocalDate(0, DateTimeZone.UTC).plusDays(factorEntriesCursor.getInt(2))
            val value = factorEntriesCursor.getDouble(3)

            db.insertFactorEntry(id, factorId, date, value)
        }
        factorEntriesCursor.close()

        headache = Headache(db.headacheEntries)
        factors.addAll(db.getFactorsUsingHeadache(headache))
    }

    fun insertOrUpdateHeadacheEntry(date: LocalDate, value: Double?): Long {
        val id = db.insertOrUpdateHeadacheEntry(date, value)
        headache = Headache(db.headacheEntries)
        return id
    }

    fun factorExists(name: String): Boolean {
        return db.factorExists(name)
    }

    /**
     * Functional style method for adding a new factor that returns all the current
     * factors in the system
     * @param name the name of the factor
     * *
     * @return list of all factors
     */
    fun addFactor(name: String): List<Factor> {
        val factorId = db.insertFactor(name)
        val factor = db.getFactorUsingHeadache(factorId, headache)

        if(factor != null) {
            this.factors.add(factor)
        }

        return this.factors
    }

    /**
     * Functional style method for deleting a factor that returns all the current
     * factors in the system
     * @param name the name of the factor
     * *
     * @return list of all factors
     */
    fun deleteFactor(name: String): List<Factor> {

        val factorId = db.deleteFactorByName(name)!!
        val factorToDelete = Iterators.find(this.factors.iterator()) { factor -> factor!!.id == factorId }

        this.factors.remove(factorToDelete)

        return this.factors
    }

    fun insertOrUpdateFactorEntry(factorId: Long?, date: LocalDate, value: Double?): Long {
        return db.insertOrUpdateFactorEntry(factorId, date, value)
    }

    fun getFactors(): List<Factor> {
        return factors
    }

    companion object {

        lateinit var context: Context

        val instance: DataManager by lazy {

            val manager = DataManager(context)
            manager
        }
    }
}

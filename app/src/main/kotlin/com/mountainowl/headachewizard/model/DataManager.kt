package com.mountainowl.headachewizard.model

import android.content.Context
import com.google.common.base.Predicate
import com.google.common.collect.Iterators
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

    var headache: Headache? = null
        private set
    private val factors: MutableList<Factor>

    init {
        db = DatabaseHelper(context)

        headache = Headache(db.headacheEntries)
        factors = db.getFactorsUsingHeadache(headache!!)
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
        val factor = db.getFactorUsingHeadache(factorId, headache!!)
        this.factors.add(factor)

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

        @Volatile private var instance: DataManager? = null

        fun getInstance(context: Context): DataManager {

            if (instance == null) {
                instance = DataManager(context)
                if (!instance!!.factorExists("Test Factor A")) {
                    instance!!.addFactor("Test Factor A")
                }

                if (!instance!!.factorExists("Test Factor B")) {
                    instance!!.addFactor("Test Factor B")
                }
            }

            return instance
        }
    }
}

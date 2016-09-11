package com.mountainowl.headachewizard.model

import com.mountainowl.headachewizard.CorrelationCoefficientTransformEngine
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FactorTest {

    lateinit var factor : Factor
    lateinit var headache : Headache

    @Before
    fun setup() {
        factor = Factor(0, "Test Factor")
        headache = Headache()
    }

    @Test
    fun testEmpty() {
        factor.evaluateCorrelationParameters(headache)

        assertEquals(0.0, factor.r, 0.0001)
    }

    @Test
    fun testEmptyFactor() {

        headache.setDate(LocalDate(0, DateTimeZone.UTC), 1.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(1), -1.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(2), -1.0)

        factor.evaluateCorrelationParameters(headache)

        assertEquals(0.0, factor.r, 0.0001)
    }

    @Test
    fun testEmptyHeadache() {
        factor.setDate(LocalDate(0, DateTimeZone.UTC), -1.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(1), 1.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(2), 1.0)

        factor.evaluateCorrelationParameters(headache)

        assertEquals(0.0, factor.r, 0.0001)
    }

    @Test
    fun testNoHeadacheVariance() {

        headache.setDate(LocalDate(0, DateTimeZone.UTC), 1.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(1), 1.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(2), 1.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(3), 1.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(4), 1.0)

        factor.setDate(LocalDate(0, DateTimeZone.UTC), 1.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(1), -1.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(2), 0.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(3), 1.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(4), 0.0)

        factor.evaluateCorrelationParameters(headache)

        assertEquals(0.2, factor.r, 0.0001)
    }

    @Test
    fun testNoFactorVariance() {

        headache.setDate(LocalDate(0, DateTimeZone.UTC), 1.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(1), -1.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(2), -1.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(3), -1.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(4), 0.0)

        factor.setDate(LocalDate(0, DateTimeZone.UTC), 1.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(1), 1.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(2), 1.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(3), 1.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(4), 1.0)

        factor.evaluateCorrelationParameters(headache)

        assertEquals(-0.4, factor.r, 0.0001)
    }

    @Test
    fun testPostiveCorrelation() {

        headache.setDate(LocalDate(0, DateTimeZone.UTC), 0.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(1), 1.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(2), 1.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(3), 0.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(4), -1.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(5), -1.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(6), -1.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(7), -1.0)

        factor.setDate(LocalDate(0, DateTimeZone.UTC), -1.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(1), 1.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(2), 1.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(3), -1.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(4), 1.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(5), 1.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(6), -1.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(7), 0.0)

        factor.evaluateCorrelationParameters(headache)

        assertEquals(CorrelationCoefficientTransformEngine.transform(0.2032789), factor.r, 0.0001)
    }

    @Test
    fun testNegativeCorrelation() {

        headache.setDate(LocalDate(0, DateTimeZone.UTC), 0.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(1), 1.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(2), 1.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(3), 0.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(4), -1.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(5), -1.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(6), -1.0)
        headache.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(7), -1.0)

        factor.setDate(LocalDate(0, DateTimeZone.UTC), 0.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(1), -1.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(2), -1.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(3), -1.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(4), 1.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(5), -1.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(6), -1.0)
        factor.setDate(LocalDate(0, DateTimeZone.UTC).plusDays(7), 1.0)

        factor.evaluateCorrelationParameters(headache)

        assertEquals(CorrelationCoefficientTransformEngine.transform(-0.4837794), factor.r, 0.0001)
    }
}
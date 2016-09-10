package com.mountainowl.headachewizard.model

import android.os.Parcel
import android.os.Parcelable
import org.joda.time.DateTimeZone
import org.joda.time.Days
import org.joda.time.LocalDate

import java.util.*

class Headache @JvmOverloads constructor(val data: SortedMap<LocalDate, Double> = TreeMap<LocalDate, Double>()) : Parcelable {

    fun getDate(date: LocalDate): Double? {
        val datum = data[date]
        return datum
    }

    fun allDates(): Set<LocalDate> = data.keys

    fun setDate(date: LocalDate, hValue: Double?) {
        this.data.put(date, hValue)
    }

    override fun describeContents(): Int {
        return Parcelable.CONTENTS_FILE_DESCRIPTOR
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {

        dest.writeInt(data.size)
        for (date in data.keys) {
            dest.writeInt(Days.daysBetween(LocalDate(0, DateTimeZone.UTC), date).days)
            dest.writeDouble(data[date] ?: 0.0)
        }
    }

    companion object {

        val CREATOR: Parcelable.Creator<Headache> = object : Parcelable.Creator<Headache> {

            //Potentially use http://stackoverflow.com/questions/8254654/how-write-java-util-map-into-parcel-in-a-smart-way
            override fun createFromParcel(parcel: Parcel): Headache {

                val data = TreeMap<LocalDate, Double>()

                parcel.setDataPosition(0)

                val size = parcel.readInt()
                for (i in 0..size - 1) {
                    val key = LocalDate(0, DateTimeZone.UTC).plusDays(parcel.readInt())
                    val value = parcel.readDouble()
                    data.put(key, value)
                }

                val headache = Headache(data)
                return headache
            }

            override fun newArray(size: Int): Array<Headache> {
                return Array(size, { Headache() })
            }
        }
    }
}

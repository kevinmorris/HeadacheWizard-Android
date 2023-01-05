package pro.kevinmorris.headachewizard.util

import android.os.AsyncTask
import pro.kevinmorris.headachewizard.model.DataManager
import pro.kevinmorris.headachewizard.model.MigrationProvider
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate


class MigrationAsyncTask(val callback : IMigrationListener) : AsyncTask<Void, Int, Void>() {

    override fun doInBackground(vararg params: Void?): Void? {
        publishProgress(0)

        val contentResolver = DataManager.context.contentResolver

        val headacheCursor = contentResolver.query(MigrationProvider.HEADACHE_CONTENT_FREE_URI, null,  null, null, null)
        if(headacheCursor != null) {

            DataManager.instance.resetDatabase()

            publishProgress(20)

            while (headacheCursor.moveToNext()) {
                val date = LocalDate(0, DateTimeZone.UTC).plusDays(headacheCursor.getInt(1))
                val value = headacheCursor.getDouble(2)
                DataManager.instance.insertOrUpdateHeadacheEntry(date, value)
            }
            headacheCursor.close()

            publishProgress(40)

            contentResolver.query(MigrationProvider.FACTOR_CONTENT_FREE_URI, null, null, null, null)?.also { factorCursor ->
                while (factorCursor.moveToNext()) {
                    val id = factorCursor.getLong(0)
                    val name = factorCursor.getString(1)
                    DataManager.instance.insertFactor(id, name)
                }
                factorCursor.close()
            }

            publishProgress(60)

            contentResolver.query(MigrationProvider.FACTOR_ENTRIES_CONTENT_FREE_URI, null, null, null, null)?.also { factorEntriesCursor ->
                while (factorEntriesCursor.moveToNext()) {
                    val id = factorEntriesCursor.getLong(0)
                    val factorId = factorEntriesCursor.getLong(1)
                    val date =
                        LocalDate(0, DateTimeZone.UTC).plusDays(factorEntriesCursor.getInt(2))
                    val value = factorEntriesCursor.getDouble(3)

                    DataManager.instance.insertFactorEntry(id, factorId, date, value)
                }
                factorEntriesCursor.close()
            }

            publishProgress(80)

            DataManager.instance.finalizeMigration()

            publishProgress(100)
        }

        return null
    }

    override fun onProgressUpdate(vararg values: Int?) {
        callback.processMigrationProgress(values.first() ?: 0)
    }

    override fun onPostExecute(result: Void?) {
        callback.migrationComplete()
    }

    interface IMigrationListener {
        fun processMigrationProgress(progress : Int)
        fun migrationComplete()
    }

}
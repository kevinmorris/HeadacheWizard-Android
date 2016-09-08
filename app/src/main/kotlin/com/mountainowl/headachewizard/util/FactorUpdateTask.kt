package com.mountainowl.headachewizard.util

import android.os.AsyncTask
import com.mountainowl.headachewizard.model.Factor
import com.mountainowl.headachewizard.model.Headache
import com.mountainowl.headachewizard.model.IFactorUpdateComplete

class FactorUpdateTask(val callback: IFactorUpdateComplete) : AsyncTask<Pair<Headache, Factor>, Void, Factor>() {

    override fun doInBackground(vararg params: Pair<Headache, Factor>): Factor {
        val (h, f) = params[0]

        f.evaluateCorrelationParameters(h)
        return f
    }

    override fun onPostExecute(result: Factor) {
        callback.factorUpdateComplete(result)
    }
}
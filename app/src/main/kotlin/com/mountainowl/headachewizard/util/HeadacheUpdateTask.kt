package com.mountainowl.headachewizard.util

import android.os.AsyncTask
import com.mountainowl.headachewizard.model.Factor
import com.mountainowl.headachewizard.model.Headache
import com.mountainowl.headachewizard.model.IHeadacheUpdateComplete

class HeadacheUpdateTask(val callback: IHeadacheUpdateComplete) : AsyncTask<Pair<Headache, List<Factor>>, Void, Headache>() {
    override fun doInBackground(vararg params: Pair<Headache, List<Factor>>): Headache {

        val (headache, factors) = params[0]
        for (factor in factors) {
            factor.evaluateCorrelationParameters(headache)
        }

        return headache
    }

    override fun onPostExecute(result: Headache) {
        callback.headacheUpdateComplete(result)
    }
}
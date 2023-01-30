package pro.kevinmorris.headachewizard.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.joda.time.LocalDate
import pro.kevinmorris.headachewizard.model.DataManager
import pro.kevinmorris.headachewizard.model.Factor
import pro.kevinmorris.headachewizard.util.ThreewaySwitchChanged


class EditDailyEntryViewModel(val date: LocalDate) : ViewModel() {

    private val factors = DataManager.instance.getFactors()
    private val _state: MutableStateFlow<State> = MutableStateFlow(
        State.RefreshAll(factors,
            DataManager.instance.headache[date]?.toInt() ?: 0),
    )
    val state: StateFlow<State> = _state

    fun factorAction(f: Factor): ThreewaySwitchChanged {
        return { v ->
            f.setDate(date, v.toDouble(), DataManager.instance.headache)
            DataManager.instance.insertOrUpdateFactorEntry(f.id, date, v.toDouble())
        }
    }

    fun setHeadache(value: Int) {
        DataManager.instance.insertOrUpdateHeadacheEntry(date, value.toDouble())
        val headache = DataManager.instance.headache
        for (factor in factors) { factor.evaluateCorrelationParameters(headache) }
    }

    sealed class State {
        data class RefreshAll(val factors: List<Factor>, val headacheValue : Int) : State()
    }
}
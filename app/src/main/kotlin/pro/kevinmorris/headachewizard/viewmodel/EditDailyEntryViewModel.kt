package pro.kevinmorris.headachewizard.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.joda.time.LocalDate
import pro.kevinmorris.headachewizard.model.DataManager
import pro.kevinmorris.headachewizard.model.Factor
import pro.kevinmorris.headachewizard.util.ThreewaySwitchChanged


class EditDailyEntryViewModel(val date : LocalDate) : ViewModel() {

    private val _factors = MutableStateFlow(DataManager.instance.getFactors())
    val factors : StateFlow<List<Factor>> = _factors


    fun factorAction(f : Factor) : ThreewaySwitchChanged {
        return { v ->
            f.setDate(date, v.toDouble())
            DataManager.instance.insertOrUpdateFactorEntry(f.id, date, v.toDouble())
        }
    }

}
package pro.kevinmorris.headachewizard.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import org.joda.time.LocalDate
import pro.kevinmorris.headachewizard.model.DataManager
import pro.kevinmorris.headachewizard.model.Factor
import pro.kevinmorris.headachewizard.util.ThreewaySwitchChanged


class EditDailyEntryViewModel(date : LocalDate) : ViewModel() {

    private val _factors = MutableStateFlow(DataManager.instance.getFactors())
    val factors : StateFlow<List<Factor>> = _factors

    val factorUpdatedActions : Flow<List<ThreewaySwitchChanged>> = factors.map { fs ->
        fs.map { f ->
            { v : Int ->
                f.setDate(date, v.toDouble())
                DataManager.instance.insertOrUpdateFactorEntry(f.id, date, v.toDouble())
            }
        }
    }

}
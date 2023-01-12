package pro.kevinmorris.headachewizard.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import pro.kevinmorris.headachewizard.model.Factor
import pro.kevinmorris.headachewizard.ui.components.FactorSwitchPanel
import pro.kevinmorris.headachewizard.viewmodel.EditDailyEntryViewModel

class EditDailyEntryFragment() : Fragment() {

    val viewModel : EditDailyEntryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.factors.collect { render(it) }
            }
        }

    }

    fun render(factors : List<Factor>) {
        //TODO add to adapter
        factors.forEach { factor ->
            val factorPanel = FactorSwitchPanel(requireContext()).apply {
                threewaySwitchChanged = viewModel.factorAction(factor)
            }
        }
    }
}
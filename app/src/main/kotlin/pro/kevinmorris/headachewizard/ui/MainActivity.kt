package pro.kevinmorris.headachewizard.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import kotlinx.coroutines.launch
import org.joda.time.LocalDate
import pro.kevinmorris.headachewizard.NavigationDirections
import pro.kevinmorris.headachewizard.R
import pro.kevinmorris.headachewizard.databinding.ActivityMainBinding
import pro.kevinmorris.headachewizard.viewmodel.MainViewModel
import pro.kevinmorris.headachewizard.viewmodel.NavigationState

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewModel = viewModel

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                handleNavigation()
            }
        }
    }

    private suspend fun handleNavigation() {
        viewModel.navState.collect {
            findNavController(R.id.fragment_container).navigate(when (it) {
                is NavigationState.Calendar -> calendarAction(it.date)
                is NavigationState.EditDay -> editDayAction(it.date)
                is NavigationState.EditFactors -> editFactorsAction()
            })
        }
    }

    private fun calendarAction(date: LocalDate): NavDirections {
        return NavigationDirections.actionGlobalCalendarFragment(date)
    }

    private fun editDayAction(date: LocalDate): NavDirections {
        return NavigationDirections.actionGlobalEditDailyEntryFragment(date)
    }

    private fun editFactorsAction(): NavDirections {
        return NavigationDirections.actionGlobalEditFactorsFragment()
    }
}

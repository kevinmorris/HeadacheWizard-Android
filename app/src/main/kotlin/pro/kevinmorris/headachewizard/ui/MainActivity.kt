package pro.kevinmorris.headachewizard.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import kotlinx.coroutines.launch
import org.joda.time.LocalDate
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
            when (it) {
                is NavigationState.Calendar -> showCalendar()
                is NavigationState.EditDay -> showEditDay(it.date)
                is NavigationState.EditFactors -> showEditFactors()
            }
        }
    }

    fun showCalendar() {
        Log.i("XXXXX128", "XXXXX128")

    }

    private fun showEditDay(date: LocalDate) {
        val action = CalendarFragmentDirections.actionCalendarFragmentToEditDailyEntryFragment(date)
        findNavController(R.id.fragment_container).navigate(action)
    }

    fun showEditFactors() {
        Log.i("XXXXX256", "XXXXX256")
    }
}

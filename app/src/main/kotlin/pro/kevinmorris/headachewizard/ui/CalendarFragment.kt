package pro.kevinmorris.headachewizard.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import org.joda.time.LocalDate
import pro.kevinmorris.headachewizard.R
import pro.kevinmorris.headachewizard.databinding.FragmentCalendarBinding
import pro.kevinmorris.headachewizard.viewmodel.CalendarViewModel

class CalendarFragment : Fragment() {

    private val args: CalendarFragmentArgs by navArgs()
    private val viewModel: CalendarViewModel by viewModels {
        CalendarViewModelFactory(args.date)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentCalendarBinding>(
            inflater,
            R.layout.fragment_calendar,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    @Suppress("UNCHECKED_CAST")
    class CalendarViewModelFactory(val date : LocalDate) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CalendarViewModel(date) as T
        }
    }
}

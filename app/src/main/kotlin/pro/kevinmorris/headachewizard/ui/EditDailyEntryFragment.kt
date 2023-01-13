package pro.kevinmorris.headachewizard.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.LocalDate
import pro.kevinmorris.headachewizard.R
import pro.kevinmorris.headachewizard.databinding.FragmentEditDailyEntryBinding
import pro.kevinmorris.headachewizard.model.Factor
import pro.kevinmorris.headachewizard.ui.components.CorrelationView
import pro.kevinmorris.headachewizard.ui.components.FactorSwitchPanel
import pro.kevinmorris.headachewizard.viewmodel.EditDailyEntryViewModel

class EditDailyEntryFragment() : Fragment() {

    val viewModel: EditDailyEntryViewModel by viewModels {
        EditDailyEntryViewModelFactory(date)
    }

    val date : LocalDate = DateTime().toLocalDate() //TODO temp
    private lateinit var factorView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { render(it) }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = DataBindingUtil.inflate<FragmentEditDailyEntryBinding>(
            inflater,
            R.layout.fragment_edit_daily_entry,
            container,
            false
        )

        binding.viewModel = viewModel
        factorView = binding.factorView
        factorView.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun render(state: EditDailyEntryViewModel.State) {
        when(state) {
            is EditDailyEntryViewModel.State.RefreshFactors -> {
                factorView.adapter = FactorAdapter(state.factors)
            }
            is EditDailyEntryViewModel.State.HeadacheUpdated -> {
                factorView.adapter?.notifyDataSetChanged()
            }
            is EditDailyEntryViewModel.State.FactorUpdated -> {
                (factorView.adapter as? FactorAdapter)?.notifyItemChanged(state.factor)
            }
        }
    }

    inner class FactorAdapter(private val dataSet: List<Factor>) :
        RecyclerView.Adapter<FactorAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            val switchPanel: FactorSwitchPanel
            val correlationView: CorrelationView

            init {
                switchPanel = view.findViewById(R.id.factor_switch_panel)
                correlationView = view.findViewById(R.id.correlation_view)
            }
        }

        fun notifyItemChanged(factor : Factor) {
            val position = dataSet.indexOf(factor)
            notifyItemChanged(position)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_edit_daily_entry, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val factor = dataSet[position]
            holder.correlationView.value = factor.r
            holder.switchPanel.binding = Pair(
                factor.getDate(date)?.toInt() ?: 0,
                viewModel.factorAction(factor)
            )

            holder.switchPanel.switchLabel.text = factor.name
        }

        override fun getItemCount(): Int = dataSet.size
    }

    @Suppress("UNCHECKED_CAST")
    class EditDailyEntryViewModelFactory(val date : LocalDate) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditDailyEntryViewModel(date) as T
        }
    }
}


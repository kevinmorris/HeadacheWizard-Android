package pro.kevinmorris.headachewizard.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import pro.kevinmorris.headachewizard.viewmodel.MainViewModel
import pro.kevinmorris.headachewizard.viewmodel.NavigationState

class MainActivity : AppCompatActivity() {

    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                handleNavigation()
            }
        }
    }

    private suspend fun handleNavigation() {
        viewModel.navState.collect {
            when(it) {
                is NavigationState.Calendar -> TODO()
                is NavigationState.EditDay -> TODO()
                NavigationState.EditFactors -> TODO()
            }
        }
    }
}

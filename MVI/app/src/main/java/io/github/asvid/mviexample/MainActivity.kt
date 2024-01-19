package io.github.asvid.mviexample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.github.asvid.mviexample.ui.theme.mviExampleTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel = ViewModel()

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.container.sideEffect.collect {
                        handleSideEffect(it)
                    }
                }
            }
        }

        setContent {
            mviExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val state by viewModel.container.state.collectAsState()
                    render(innerPadding, state) { event -> viewModel.handleEvent(event) }
                }
            }
        }
    }

    @Composable
    private fun render(
        innerPadding: PaddingValues,
        state: State,
        handleEvent: (event: Event) -> Unit // no need to pass VM, or proxy its methods - nice
    ) {
        View(
            modifier = Modifier.padding(innerPadding),
            state = state,
            handleEvent = handleEvent // passing single method instead of separate ones for each interaction - nice
        )
    }

    private fun handleSideEffect(sideEffect: SideEffect) {
        when (sideEffect) {
            is SideEffect.ShowError -> Toast.makeText(
                this@MainActivity,
                "ERROR: ${sideEffect.text}",
                Toast.LENGTH_SHORT
            ).show()

            is SideEffect.ShowToast -> Toast.makeText(
                this@MainActivity,
                sideEffect.text,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
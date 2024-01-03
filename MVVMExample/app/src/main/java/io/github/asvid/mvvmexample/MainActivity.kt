package io.github.asvid.mvvmexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.github.asvid.mvvmexample.ui.theme.MVVMExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel = ViewModel()

        setContent {
            MVVMExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val state by viewModel.state.collectAsState()
                    View(
                        modifier = Modifier.padding(innerPadding),
                        state = state,
                        inputChanged = { viewModel.inputChanged(it) },
                        addButtonClicked = { viewModel.addButtonClicked() },
                        removeItem = { viewModel.removeItem(it) }
                    )
                }
            }
        }
    }
}
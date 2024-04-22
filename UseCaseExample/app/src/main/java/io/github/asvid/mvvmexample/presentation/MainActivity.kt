package io.github.asvid.mvvmexample.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.github.asvid.mvvmexample.data.repositories.InMemoryItemsRepository
import io.github.asvid.mvvmexample.data.repositories.RemoteItemsRepository
import io.github.asvid.mvvmexample.domain.items.usecases.AddItemUseCase
import io.github.asvid.mvvmexample.domain.items.usecases.AddItemUseCaseImpl
import io.github.asvid.mvvmexample.domain.items.usecases.ReadItemsUseCase
import io.github.asvid.mvvmexample.domain.items.usecases.ReadItemsUseCaseImpl
import io.github.asvid.mvvmexample.domain.items.usecases.RemoveItemUseCase
import io.github.asvid.mvvmexample.domain.items.usecases.RemoveItemUseCaseImpl
import io.github.asvid.mvvmexample.domain.items.usecases.ValidateInputUseCase
import io.github.asvid.mvvmexample.domain.items.usecases.ValidateInputUseCaseImpl
import io.github.asvid.mvvmexample.presentation.theme.MVVMExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Use the 'by viewModels()' Kotlin property delegate
        // from the activity-ktx artifact

        val viewModel: ViewModel by viewModels()

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
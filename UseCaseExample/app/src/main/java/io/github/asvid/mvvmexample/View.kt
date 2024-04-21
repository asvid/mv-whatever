package io.github.asvid.mvvmexample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.asvid.mvvmexample.ui.theme.MVVMExampleTheme

@Composable
fun View(
    modifier: Modifier = Modifier,
    state: State,
    inputChanged: (String) -> Unit,
    addButtonClicked: () -> Unit,
    removeItem: (Int) -> Unit,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            // this could be calculated here
            // since there is no logic around this SUM, its just UI
            text = "The sum is: ${state.items.sum()}",
            style = MaterialTheme.typography.headlineLarge
        )

        TextField(
            value = state.currentInput.orEmpty(),
            onValueChange = {
                inputChanged(it)
            },
            isError = state.inputError?.isNotEmpty() == true,
            label = { Text("Type just numbers 1-100") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        if (state.inputError?.isNotEmpty() == true) {
            Text(
                text = state.inputError.toString(),
                color = Color.Red,
                modifier = Modifier
                    .padding(top = 4.dp)
            )
        }

        Button(
            onClick = {
                addButtonClicked()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("+ add")
        }

        if (state.isLoading) {
            CircularProgressIndicator()
        }

        LazyColumn {
            items(state.items) { item ->
                ListItem(item) { removeItem(item) }
            }
        }
    }
}

@Composable
fun ListItem(
    item: Int,
    onRemoveClicked: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = item.toString(),
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        Button(onClick = { onRemoveClicked(item) }) {
            Text(text = "X")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun View_Preview() {
    MVVMExampleTheme {
        View(state = State(
            items = listOf(1, 2, 3),
            inputError = null,
            currentInput = null
        ),
            inputChanged = {},
            addButtonClicked = {},
            removeItem = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun View_Loading_Preview() {
    MVVMExampleTheme {
        View(state = State(
            items = listOf(1, 2, 3),
            inputError = null,
            currentInput = null,
            isLoading = true
        ),
            inputChanged = {},
            addButtonClicked = {},
            removeItem = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun View_Error_Preview() {
    MVVMExampleTheme {
        View(state = State(
            items = listOf(1, 2, 3),
            inputError = "error",
            currentInput = null
        ),
            inputChanged = {},
            addButtonClicked = {},
            removeItem = {})
    }
}
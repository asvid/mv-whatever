package io.github.asvid.mvcexample

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var textView: TextView
    lateinit var button: Button
    lateinit var input: EditText
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: Adapter
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        textView = findViewById(R.id.sumTextView)
        button = findViewById(R.id.button)
        input = findViewById(R.id.editTextText)
        progressBar = findViewById(R.id.progressBar)

        setupPresenter()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = Adapter(emptyList()) { item ->
            removeItem(item)
        }
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            Model.itemsFlow.collect { items ->
                displayItems(items)
            }
        }
    }

    private fun setupPresenter() {
        button.setOnClickListener {
            lifecycleScope.launch {
                Model.validateInput(input.text.toString())
                    .onSuccess { validatedItem ->
                        displayLoading()
                        Model.addItem(validatedItem)
                            .onFailure { error ->
                                hideLoading()
                                displayErrorMessage(error)
                            }.onSuccess {
                                hideLoading()
                            }
                    }
                    .onFailure { error ->
                        displayErrorMessage(error)
                    }
            }
        }
        input.addTextChangedListener {
            inputChanged(input.text.toString())
        }
    }

    private fun displayErrorMessage(exception: Throwable) {
        displayError(exception.message.orEmpty())
    }

    private fun inputChanged(newInput: String) {
        Model.validateInput(newInput)
            .onFailure { error ->
                displayErrorMessage(error)
            }.onSuccess {
                hideError()
            }
    }

    private fun removeItem(item: Int) {
        displayLoading()
        lifecycleScope.launch {
            Model.removeItem(item)
                .onFailure { error ->
                    hideLoading()
                    displayErrorMessage(error)
                }
                .onSuccess {
                    hideLoading()
                }
        }
    }

    private fun displayError(errorMessage: String) {
        runOnUiThread {
            input.error = errorMessage
        }
    }

    private fun hideError() {
        runOnUiThread {
            input.error = null
        }
    }

    fun displayItems(items: List<Int>) {
        runOnUiThread {
            adapter.updateData(items)
        }
    }

    private fun displayLoading() {
        runOnUiThread {
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        runOnUiThread {
            progressBar.visibility = View.GONE
        }
    }
}

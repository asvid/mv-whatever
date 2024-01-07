package io.github.asvid.mvpexample

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), Contract.ConcreteView {

    private val presenter: Contract.ConcretePresenter = MainViewPresenter()

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

    private fun setupPresenter() {
        presenter.attachView(this)
        button.setOnClickListener {
            presenter.addButtonClicked()
        }
        input.addTextChangedListener {
            presenter.inputChanged(input.text.toString())
        }
    }

    private fun setupRecyclerView() {
        adapter = Adapter(emptyList()) { item ->
            presenter.removeItem(item)
        }
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    // CONTRACT methods
    override fun displayError(errorMessage: String) {
        runOnUiThread {
            input.error = errorMessage
        }
    }

    override fun hideError() {
        runOnUiThread {
            input.error = null
        }
    }

    override fun displayItems(items: List<Int>) {
        runOnUiThread {
            adapter.updateData(items)
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    override fun displayLoading() {
        runOnUiThread {
            progressBar.visibility = View.VISIBLE
        }
    }

    override fun hideLoading() {
        runOnUiThread {
            progressBar.visibility = View.GONE
        }
    }
}
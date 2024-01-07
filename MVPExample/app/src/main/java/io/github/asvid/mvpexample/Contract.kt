package io.github.asvid.mvpexample

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

interface Presenter<VIEW : View> {
    fun attachView(view: VIEW)
    fun detachView()
}

interface View

abstract class BasePresenter<VIEW : View> : Presenter<VIEW>, CoroutineScope {
    internal lateinit var view: VIEW

    private var job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    override fun attachView(view: VIEW) {
        this.view = view
        onViewAttached()
    }

    override fun detachView() {
        job.cancel()
        onViewDetached()
    }

    abstract fun onViewAttached()
    abstract fun onViewDetached()
}

interface Contract {
    interface ConcreteView : View {
        fun displayError(errorMessage: String)
        fun hideError()
        fun displayItems(items: List<Int>)
        fun displayLoading()
        fun hideLoading()
    }

    interface ConcretePresenter : Presenter<ConcreteView> {
        fun addButtonClicked()
        fun inputChanged(newInput: String)
        fun removeItem(item: Int)
    }
}
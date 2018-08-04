package com.codephrase.android.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import com.codephrase.android.BR
import com.codephrase.android.R
import com.codephrase.android.error.NotImplementedError
import com.codephrase.android.error.NotSupportedError
import com.codephrase.android.viewmodel.ViewModel
import kotlin.reflect.KClass

abstract class FrameActivity : AppCompatActivity() {
    protected open val toolbarEnabled: Boolean
        get() = false

    protected open val upButtonEnabled: Boolean
        get() = false

    protected open val layoutId: Int
        get() {
            if (toolbarEnabled) {
                if (headerLayoutId > 0)
                    return R.layout.activity_frame_collapsing
                else
                    return R.layout.activity_frame_toolbar
            } else {
                return R.layout.activity_frame_default
            }
        }

    protected open val toolbarId: Int
        get() = R.id.toolbar

    protected open val headerPlaceholderId: Int
        get() = R.id.header_layout

    protected open val headerLayoutId: Int
        get() = 0

    protected open val contentPlaceholderId: Int
        get() = R.id.content_layout

    protected open val contentLayoutId: Int
        get() = 0

    protected open val menuId: Int
        get() = 0

    protected open val swipeRefreshLayoutId: Int
        get() = 0

    protected open val viewModelType: KClass<out ViewModel>
        get() = throw NotImplementedError()

    protected lateinit var viewModel: ViewModel

    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    init {
        initialize()
    }

    open fun initialize() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(viewModelType.java)

        super.onCreate(savedInstanceState)

        val root = findViewById<ViewGroup>(android.R.id.content)

        val view = initializeView(layoutInflater, root, savedInstanceState)
        if (view != null)
            setContentView(view)

        onViewInitialized(savedInstanceState)
    }

    protected open fun initializeView(layoutInflater: LayoutInflater, root: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (layoutId > 0) {
            val view = layoutInflater.inflate(layoutId, root, false)
            if (view != null) {
                if (headerPlaceholderId > 0) {
                    val headerPlaceholder = view.findViewById<ViewGroup>(headerPlaceholderId)
                    if (headerPlaceholder != null) {
                        val headerView = initializeHeaderView(layoutInflater, headerPlaceholder, savedInstanceState)
                        if (headerView != null)
                            headerPlaceholder.addView(headerView)
                    }
                }

                if (contentPlaceholderId > 0) {
                    val contentPlaceholder = view.findViewById<ViewGroup>(contentPlaceholderId)
                    if (contentPlaceholder != null) {
                        val contentView = initializeContentView(layoutInflater, contentPlaceholder, savedInstanceState)
                        if (contentView != null) {
                            val binding: ViewDataBinding? = DataBindingUtil.bind(contentView);
                            binding?.let {
                                it.setLifecycleOwner(this)
                                it.setVariable(BR.viewModel, viewModel);
                                it.executePendingBindings()
                            }

                            contentPlaceholder.addView(contentView)
                        }
                    }
                }
            }

            return view
        }

        return null
    }

    protected open fun initializeHeaderView(layoutInflater: LayoutInflater, headerPlaceholder: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (headerLayoutId > 0)
            return layoutInflater.inflate(headerLayoutId, headerPlaceholder, false)
        else
            return null
    }

    protected open fun initializeContentView(layoutInflater: LayoutInflater, contentPlaceholder: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (contentLayoutId > 0) {
            val contentView = layoutInflater.inflate(contentLayoutId, contentPlaceholder, false)
            if (contentView != null) {
                if (swipeRefreshLayoutId > 0) {
                    swipeRefreshLayout = contentView.findViewById(swipeRefreshLayoutId)
                    swipeRefreshLayout?.let {
                        it.setOnRefreshListener {
                            if (canLoadData()) {
                                loadData()
                            } else {
                                it.isRefreshing = false
                            }
                        }
                    }
                }
            }

            return contentView
        }

        return null
    }

    protected open fun onViewInitialized(savedInstanceState: Bundle?) {
        if (toolbarEnabled) {
            if (toolbarId > 0) {
                val toolbar = findViewById<Toolbar>(toolbarId)
                if (toolbar != null) {
                    setSupportActionBar(toolbar)
                    updateToolbarState(upButtonEnabled)
                }
            }
        }

        invalidateToolbarTitle()

        viewModel.title.observe(this, Observer {
            invalidateToolbarTitle()
        })

        viewModel.dataLoading.observe(this, Observer {
            if (it != true) {
                swipeRefreshLayout?.isRefreshing = false
            }
        })
    }

    override fun onResume() {
        super.onResume()

        if (viewModel.dataLoaded.value != true) {
            if (canLoadData())
                loadData()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (toolbarEnabled) {
            if (menuId > 0) {
                menuInflater.inflate(menuId, menu)
                return true
            }
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (it.itemId == android.R.id.home) {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    protected fun <T : Activity> navigate(type: KClass<T>) {
        val intent = Intent(this, type.java)



        startActivity(intent)
    }

    internal fun updateToolbarState(upButtonEnabled: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(upButtonEnabled)
    }

    internal fun invalidateToolbarTitle() {
        var title = viewModel.title.value

        if (title == null) {
            try {
                val activityInfo = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)
                title = activityInfo.loadLabel(packageManager).toString()
            } catch (e: Exception) {

            }
        }

        supportActionBar?.title = title
    }

    private fun canLoadData(): Boolean {
        return viewModel.dataLoading.value != true
    }

    private fun loadData() {
        viewModel.loadData()
    }

    final override fun setTitle(title: CharSequence?) {
        throw NotSupportedError()
    }

    final override fun setTitle(titleId: Int) {
        throw NotSupportedError()
    }
}
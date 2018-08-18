package com.codephrase.android.activity

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
import com.codephrase.android.constant.NavigationConstants
import com.codephrase.android.error.NotImplementedError
import com.codephrase.android.error.NotSupportedError
import com.codephrase.android.fragment.FrameFragment
import com.codephrase.android.helper.JsonHelper
import com.codephrase.android.helper.ObjectHelper
import com.codephrase.android.viewmodel.ViewModel
import com.codephrase.android.viewstate.FrameActivityState
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

    protected open val headerContainerId: Int
        get() = R.id.header_layout

    protected open val headerLayoutId: Int
        get() = 0

    protected open val contentContainerId: Int
        get() = R.id.content_layout

    protected open val contentLayoutId: Int
        get() = 0

    protected open val contentFragmentType: KClass<out FrameFragment>?
        get() = null

    protected open val menuId: Int
        get() = 0

    protected open val swipeRefreshLayoutId: Int
        get() = 0

    protected open val viewModelType: KClass<out ViewModel>
        get() = throw NotImplementedError()

    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    protected lateinit var viewState: FrameActivityState
    protected lateinit var viewModel: ViewModel

    private var internalBackStackModification: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewState = savedInstanceState?.getParcelable("view-state") ?: onCreateViewState()
        viewModel = ViewModelProviders.of(this).get(viewModelType.java)

        var sender: KClass<*> = this::class
        var data: Any? = null

        intent.extras?.let {
            if (it.containsKey(NavigationConstants.SENDER))
                sender = (it.getSerializable(NavigationConstants.SENDER) as Class<*>).kotlin

            if (it.containsKey(NavigationConstants.DATA_TYPE) && it.containsKey(NavigationConstants.DATA_OBJECT)) {
                val type = (it.getSerializable(NavigationConstants.DATA_TYPE) as Class<*>).kotlin
                val str = it.getString(NavigationConstants.DATA_OBJECT)

                if (!str.isNullOrEmpty())
                    data = JsonHelper.deserialize(str, type)
            }
        }

        onNavigated(sender, data)

        val container = findViewById<ViewGroup>(android.R.id.content)

        val view = initializeView(layoutInflater, container, savedInstanceState)
        if (view != null)
            setContentView(view)

        onViewInitialized(savedInstanceState)
    }

    protected open fun initializeView(layoutInflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (layoutId > 0) {
            val view = layoutInflater.inflate(layoutId, container, false)
            if (view != null) {
                if (headerContainerId > 0) {
                    val headerContainer = view.findViewById<ViewGroup>(headerContainerId)
                    if (headerContainer != null) {
                        val headerView = initializeHeaderView(layoutInflater, headerContainer, savedInstanceState)
                        if (headerView != null)
                            headerContainer.addView(headerView)
                    }
                }

                if (contentFragmentType == null) {
                    if (contentContainerId > 0) {
                        val contentContainer = view.findViewById<ViewGroup>(contentContainerId)
                        if (contentContainer != null) {
                            val contentView = initializeContentView(layoutInflater, contentContainer, savedInstanceState)
                            contentView?.let {
                                val binding: ViewDataBinding? = DataBindingUtil.bind(it)
                                binding?.let {
                                    it.setLifecycleOwner(this)
                                    it.setVariable(BR.viewModel, viewModel)
                                    it.executePendingBindings()
                                }

                                contentContainer.addView(contentView)
                            }
                        }
                    }
                }
            }

            return view
        }

        return null
    }

    protected open fun initializeHeaderView(layoutInflater: LayoutInflater, headerContainer: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (headerLayoutId > 0)
            return layoutInflater.inflate(headerLayoutId, headerContainer, false)
        else
            return null
    }

    protected open fun initializeContentView(layoutInflater: LayoutInflater, contentContainer: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (contentLayoutId > 0) {
            val contentView = layoutInflater.inflate(contentLayoutId, contentContainer, false)
            if (contentView != null) {
                if (swipeRefreshLayoutId > 0) {
                    swipeRefreshLayout = contentView.findViewById(swipeRefreshLayoutId)
                    swipeRefreshLayout?.let {
                        it.setOnRefreshListener {
                            viewModel.loadData()
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

        if (savedInstanceState == null) {
            contentFragmentType?.let {
                navigateFragment(it, false)
            }
        }

        viewModel.title.observe(this, Observer {
            invalidateToolbarTitle()
        })

        viewModel.dataLoading.observe(this, Observer {
            swipeRefreshLayout?.isRefreshing = (it == true)
        })
    }

    override fun onResume() {
        super.onResume()

        if (viewModel.dataLoaded.value != true)
            viewModel.loadData()
    }

    protected open fun onCreateViewState(): FrameActivityState {
        return FrameActivityState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable("view-state", viewState)
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

    protected open fun onNavigated(sender: KClass<*>, data: Any?) {
        viewModel.onNavigatedInternal(data)
    }

    fun navigate(type: KClass<out FrameActivity>) {
        navigate(type, null)
    }

    fun navigate(type: KClass<out FrameActivity>, data: Any?) {
        val intent = Intent(this, type.java)
        intent.putExtra(NavigationConstants.SENDER, javaClass);

        data?.let {
            intent.putExtra(NavigationConstants.DATA_TYPE, it.javaClass)
            intent.putExtra(NavigationConstants.DATA_OBJECT, JsonHelper.serialize(it))
        }

        startActivity(intent)
    }

    fun navigateFragment(type: KClass<out FrameFragment>) {
        navigateFragment(type, true)
    }

    fun navigateFragment(type: KClass<out FrameFragment>, data: Any?) {
        navigateFragment(type, data, true)
    }

    fun navigateFragment(type: KClass<out FrameFragment>, addToBackStack: Boolean) {
        navigateFragment(type, null, addToBackStack)
    }

    fun navigateFragment(type: KClass<out FrameFragment>, data: Any?, addToBackStack: Boolean) {
        navigateFragment(contentContainerId, type, data, addToBackStack)
    }

    private fun navigateFragment(contentContainerId: Int, type: KClass<out FrameFragment>, data: Any?, addToBackStack: Boolean) {
        if (contentContainerId > 0) {
            val fragment = ObjectHelper.create(type)
            fragment?.let {
                internalBackStackModification = true

                val arguments = Bundle()
                arguments.putSerializable(NavigationConstants.SENDER, javaClass);

                data?.let {
                    arguments.putSerializable(NavigationConstants.DATA_TYPE, it.javaClass)
                    arguments.putString(NavigationConstants.DATA_OBJECT, JsonHelper.serialize(it))
                }

                it.arguments = arguments

                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(contentContainerId, it)

                if (addToBackStack) {
                    fragmentTransaction.addToBackStack(type.qualifiedName)

                    val currentFragmentType = viewState.currentFragmentType
                    if (currentFragmentType != null)
                        viewState.navigationStack.push(currentFragmentType)
                }

                viewState.currentFragmentType = type

                fragmentTransaction.commit()
            }
        }
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

    final override fun setTitle(title: CharSequence?) {
        throw NotSupportedError()
    }

    final override fun setTitle(titleId: Int) {
        throw NotSupportedError()
    }
}
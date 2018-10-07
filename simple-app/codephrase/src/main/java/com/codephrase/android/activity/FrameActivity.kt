package com.codephrase.android.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codephrase.android.BR
import com.codephrase.android.R
import com.codephrase.android.common.navigation.NavigationUri
import com.codephrase.android.constant.NavigationConstants
import com.codephrase.android.error.NotImplementedError
import com.codephrase.android.error.NotSupportedError
import com.codephrase.android.fragment.FrameFragment
import com.codephrase.android.helper.JsonHelper
import com.codephrase.android.helper.NavigationHelper
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
        get() = throw NotImplementedError("viewModelType")

    protected var toolbar: Toolbar? = null
    protected var swipeRefreshLayout: SwipeRefreshLayout? = null

    protected lateinit var viewState: FrameActivityState
    protected lateinit var viewModel: ViewModel

    internal var internalBackStackModification: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewState = savedInstanceState?.getParcelable("view-state") ?: onCreateViewState()
        viewModel = ViewModelProviders.of(this).get(viewModelType.java)

        var sender: KClass<*> = this::class
        var data: Any? = null

        intent.data?.let {
            data = NavigationUri(it)
        } ?: run {
            intent.extras?.let {
                if (it.containsKey(NavigationConstants.SENDER))
                    sender = (it.getSerializable(NavigationConstants.SENDER) as Class<*>).kotlin

                if (it.containsKey(NavigationConstants.DATA_TYPE) && it.containsKey(NavigationConstants.DATA_OBJECT)) {
                    val type = (it.getSerializable(NavigationConstants.DATA_TYPE) as Class<*>).kotlin
                    val str = it.getString(NavigationConstants.DATA_OBJECT)

                    str?.let {
                        data = JsonHelper.deserialize(it, type)
                    }
                }
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
                toolbar = findViewById(toolbarId)
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

    private val backStackChangedListener = FragmentManager.OnBackStackChangedListener {
        onBackStackChanged()
    }

    override fun onResume() {
        super.onResume()

        supportFragmentManager.addOnBackStackChangedListener(backStackChangedListener)

        if (viewModel.dataLoaded.value != true)
            viewModel.loadData()
    }

    override fun onPause() {
        supportFragmentManager.removeOnBackStackChangedListener(backStackChangedListener)

        super.onPause()
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

    protected open fun onBackStackChanged() {
        if (!internalBackStackModification) {
            val navigationStack = viewState.navigationStack

            if (navigationStack.size > 0)
                navigationStack.pop()
        } else {
            internalBackStackModification = false
        }
    }

    fun navigate(type: KClass<out FrameActivity>) {
        navigate(type, null)
    }

    fun navigate(type: KClass<out FrameActivity>, data: Any?) {
        NavigationHelper.navigateToActivity(this, type, data)
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

    fun navigateUri(uri: String) {
        NavigationHelper.navigateToUri(this, uri)
    }

    internal fun clearNavigationStack() {
        val navigationStack = viewState.navigationStack
        val count = navigationStack.size

        for (i in 0 until count) {
            internalBackStackModification = true

            supportFragmentManager.popBackStackImmediate()
        }

        navigationStack.clear()
    }

    internal open fun updateToolbarState(upButtonEnabled: Boolean) {
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
        throw NotSupportedError("setTitle")
    }

    final override fun setTitle(titleId: Int) {
        throw NotSupportedError("setTitle")
    }
}
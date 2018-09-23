package com.codephrase.android.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import com.codephrase.android.BR
import com.codephrase.android.R
import com.codephrase.android.activity.FrameActivity
import com.codephrase.android.constant.NavigationConstants
import com.codephrase.android.error.NotImplementedError
import com.codephrase.android.helper.JsonHelper
import com.codephrase.android.viewmodel.ViewModel
import com.codephrase.android.viewstate.FrameFragmentState
import kotlin.reflect.KClass

abstract class FrameFragment : Fragment() {
    protected open val toolbarEnabled: Boolean
        get() = false

    protected open val upButtonEnabled: Boolean
        get() = false

    protected open val layoutId: Int
        get() {
            if (toolbarEnabled) {
                if (headerLayoutId > 0)
                    return R.layout.fragment_frame_collapsing
                else
                    return R.layout.fragment_frame_toolbar
            } else {
                return R.layout.fragment_frame_default
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

    protected open val menuId: Int
        get() = 0

    protected open val swipeRefreshLayoutId: Int
        get() = 0

    protected open val viewModelType: KClass<out ViewModel>
        get() = throw NotImplementedError()

    val supportActionBar: ActionBar?
        get() {
            val activity = activity as AppCompatActivity?
            return activity?.supportActionBar
        }

    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    protected lateinit var viewState: FrameFragmentState
    protected lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewState = savedInstanceState?.getParcelable("view-state") ?: onCreateViewState()
        viewModel = ViewModelProviders.of(this).get(viewModelType.java)

        var sender: KClass<*> = this::class
        var data: Any? = null

        arguments?.let { arguments ->
            if (arguments.containsKey(NavigationConstants.SENDER))
                sender = (arguments.getSerializable(NavigationConstants.SENDER) as Class<*>).kotlin

            if (arguments.containsKey(NavigationConstants.DATA_TYPE) && arguments.containsKey(NavigationConstants.DATA_OBJECT)) {
                val type = (arguments.getSerializable(NavigationConstants.DATA_TYPE) as Class<*>).kotlin
                val str = arguments.getString(NavigationConstants.DATA_OBJECT)

                if (!str.isNullOrEmpty())
                    data = JsonHelper.deserialize(str, type)
            }
        }

        onNavigated(sender, data)

        if (toolbarEnabled)
            setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = initializeView(layoutInflater, container, savedInstanceState)
        if (view != null)
            return view

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                    val activity = activity as FrameActivity?
                    activity?.let {
                        it.setSupportActionBar(toolbar)
                        it.updateToolbarState(upButtonEnabled)
                    }
                }
            }
        }

        invalidateToolbarTitle()

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

    protected open fun onCreateViewState(): FrameFragmentState {
        return FrameFragmentState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable("view-state", viewState)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (toolbarEnabled) {
            if (menuId > 0)
                inflater?.inflate(menuId, menu)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    protected open fun onNavigated(sender: KClass<*>, data: Any?) {
        viewModel.onNavigatedInternal(data)
    }

    fun navigate(type: KClass<out FrameActivity>) {
        val activity = activity as FrameActivity?
        activity?.navigate(type)
    }

    fun navigateFragment(type: KClass<out FrameFragment>) {
        val activity = activity as FrameActivity?
        activity?.navigateFragment(type)
    }

    fun navigateFragment(type: KClass<out FrameFragment>, addToBackStack: Boolean) {
        val activity = activity as FrameActivity?
        activity?.navigateFragment(type, addToBackStack)
    }

    fun navigateUri(uri: String) {
        val activity = activity as FrameActivity?
        activity?.navigateUri(uri)
    }

    fun <T : View> findViewById(@IdRes id: Int): T? {
        return view?.findViewById(id)
    }

    internal fun invalidateToolbarTitle() {
        var title = viewModel.title.value

        if (title != null) {
            supportActionBar?.title = title
        } else {
            val activity = activity as FrameActivity?
            activity?.invalidateToolbarTitle()
        }
    }
}
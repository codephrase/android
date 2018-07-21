package com.codephrase.android.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.codephrase.android.R
import com.codephrase.android.exception.NotSupportedException
import kotlin.reflect.KClass

abstract class FrameActivity : AppCompatActivity() {

    open val toolbarEnabled: Boolean
        get() = false

    open val upButtonEnabled: Boolean
        get() = false

    open val layoutId: Int
        get() {
            if (toolbarEnabled)
                return R.layout.activity_frame_toolbar
            else
                return R.layout.activity_frame_default
        }

    open val contentPlaceholderId: Int
        get() = 0

    open val contentLayoutId: Int
        get() = 0

    open val toolbarId: Int
        get() = R.id.toolbar

    open val menuId : Int
        get() = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = findViewById<ViewGroup>(android.R.id.content)

        val view = initializeView(layoutInflater, root, savedInstanceState);
        if (view != null)
            setContentView(view)

        onViewInitialized(savedInstanceState)
    }

    open fun initializeView(layoutInflater: LayoutInflater, root: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (layoutId > 0) {
            val view = layoutInflater.inflate(layoutId, root, false);
            if (view != null) {
                if (contentPlaceholderId > 0) {
                    val contentPlaceholder = view.findViewById<ViewGroup>(contentPlaceholderId);
                    if (contentPlaceholder != null) {
                        val contentView = initializeContentView(layoutInflater, contentPlaceholder, savedInstanceState)
                        if (contentView != null) {


                            contentPlaceholder.addView(contentView)
                        }
                    }
                }
            }

            return view
        }

        return null
    }

    open fun initializeContentView(layoutInflater: LayoutInflater, contentPlaceholder: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (contentLayoutId > 0) {
            val contentView = layoutInflater.inflate(layoutId, contentPlaceholder, false);



            return contentView
        }

        return null
    }

    open fun onViewInitialized(savedInstanceState: Bundle?) {
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

    fun <T : Activity> navigate(type : KClass<T>) {
        val intent = Intent(this, type.java)



        startActivity(intent)
    }

    internal fun updateToolbarState(upButtonEnabled: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(upButtonEnabled)
    }

    internal fun invalidateToolbarTitle() {
        var title: String? = null

        try {
            val activityInfo = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)
            title = activityInfo.loadLabel(packageManager).toString()
        } catch (e: Exception) {

        }

        if (title != null)
            supportActionBar?.setTitle(title)
    }

    final override fun setTitle(title: CharSequence?) {
        throw NotSupportedException()
    }

    final override fun setTitle(titleId: Int) {
        throw NotSupportedException()
    }
}
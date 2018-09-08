package com.codephrase.android.activity

import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.codephrase.android.R
import com.codephrase.android.fragment.FrameFragment
import com.codephrase.android.helper.ObjectHelper
import com.codephrase.android.viewstate.DrawerActivityState
import kotlin.reflect.KClass

abstract class DrawerActivity : FrameActivity() {
    final override val upButtonEnabled: Boolean
        get() = true

    override val layoutId: Int
        get() {
            if (toolbarEnabled) {
                if (headerLayoutId > 0)
                    return R.layout.activity_drawer_collapsing
                else
                    return R.layout.activity_drawer_toolbar
            } else {
                return R.layout.activity_drawer_default
            }
        }

    final override val contentLayoutId: Int
        get() = super.contentLayoutId

    final override val contentFragmentType: KClass<out FrameFragment>?
        get() = super.contentFragmentType

    protected open val drawerId: Int
        get() = R.id.drawer

    protected open val navigationViewId: Int
        get() = R.id.navigation_view

    protected open val navigationHeaderLayoutId: Int
        get() = 0

    protected open val navigationMenuId: Int
        get() = 0

    protected open val homeNavigationId: Int
        get() = 0

    protected var drawer: DrawerLayout? = null
    protected var drawerToggle: ActionBarDrawerToggle? = null
    protected var navigationView: NavigationView? = null
    protected var navigationHeader: View? = null
    protected var navigationMenu: Menu? = null

    override fun onViewInitialized(savedInstanceState: Bundle?) {
        super.onViewInitialized(savedInstanceState)

        if (drawerId > 0) {
            drawer = findViewById(drawerId)
            drawer?.let { drawer ->
                drawerToggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
                drawerToggle?.let { drawerToggle ->
                    drawer.addDrawerListener(drawerToggle)
                    drawerToggle.syncState()
                }
            }
        }

        if (navigationViewId > 0) {
            navigationView = findViewById(navigationViewId)
            navigationView?.let { navigationView ->
                navigationView.setNavigationItemSelectedListener { item ->
                    onNavigationItemSelected(item)
                    drawer?.closeDrawer(GravityCompat.START)
                    true
                }

                if (navigationHeaderLayoutId > 0)
                    navigationHeader = navigationView.inflateHeaderView(navigationHeaderLayoutId)

                if (navigationMenuId > 0) {
                    navigationView.inflateMenu(navigationMenuId)
                    navigationMenu = navigationView.menu
                    navigationMenu?.let { navigationMenu ->
                        if (savedInstanceState == null) {
                            val navigationItemCount = navigationMenu.size()
                            if (navigationItemCount > 0) {
                                var selectedNavigationItem: MenuItem? = null

                                if (homeNavigationId > 0) {
                                    for (i in 0 until navigationItemCount) {
                                        val navigationItem = navigationMenu.getItem(i)
                                        if (navigationItem.itemId == homeNavigationId) {
                                            selectedNavigationItem = navigationItem
                                            break
                                        }
                                    }
                                }

                                if (selectedNavigationItem == null)
                                    selectedNavigationItem = navigationMenu.getItem(0)

                                selectedNavigationItem?.let { selectedNavigationItem ->
                                    val homeNavigationId = selectedNavigationItem.itemId

                                    val drawerViewState = viewState as DrawerActivityState
                                    drawerViewState.homeNavigationId = homeNavigationId

                                    if (!selectedNavigationItem.isChecked && selectedNavigationItem.isCheckable)
                                        navigationView.setCheckedItem(homeNavigationId)

                                    onNavigationItemSelected(selectedNavigationItem)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected open fun onNavigationItemSelected(item: MenuItem) {
        if (item.isCheckable) {
            val navigationId = item.itemId
            if (navigationId > 0) {
                val fragmentType = resolveFragmentType(navigationId)
                if (fragmentType != null) {
                    val drawerViewState = viewState as DrawerActivityState
                    val selectedNavigationId = drawerViewState.selectedNavigationId

                    if (selectedNavigationId == 0) {
                        drawerViewState.selectedNavigationId = navigationId

                        navigateDrawer(fragmentType, false)
                    } else if (selectedNavigationId != navigationId) {
                        clearNavigationStack()

                        val homeNavigationId = drawerViewState.homeNavigationId

                        if (selectedNavigationId == homeNavigationId) {
                            drawerViewState.selectedNavigationId = navigationId

                            navigateDrawer(fragmentType)
                        } else {
                            internalBackStackModification = true

                            supportFragmentManager.popBackStackImmediate()

                            drawerViewState.selectedNavigationId = navigationId

                            if (navigationId != homeNavigationId)
                                navigateDrawer(fragmentType)
                            else
                                navigateDrawer(fragmentType, false)
                        }
                    }
                }
            }
        }
    }

    protected open fun resolveFragmentType(menuId: Int): KClass<out FrameFragment>? {
        return null
    }

    override fun onCreateViewState(): DrawerActivityState {
        return DrawerActivityState()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        drawerToggle?.let { drawerToggle ->
            if (drawerToggle.onOptionsItemSelected(item))
                return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        drawer?.let { drawer ->
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
                return
            }
        }

        super.onBackPressed()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        drawerToggle?.onConfigurationChanged(newConfig)
    }

    override fun onBackStackChanged() {
        if (!internalBackStackModification) {
            val drawerViewState = viewState as DrawerActivityState
            val navigationStack = drawerViewState.navigationStack

            if (navigationStack.size > 0) {
                navigationStack.pop()
            } else {
                val selectedNavigationId = drawerViewState.homeNavigationId

                drawerViewState.selectedNavigationId = selectedNavigationId

                navigationView?.setCheckedItem(selectedNavigationId)
            }
        } else {
            internalBackStackModification = false
        }
    }

    fun navigateDrawer(type: KClass<out FrameFragment>) {
        navigateDrawer(contentContainerId, type, true)
    }

    fun navigateDrawer(type: KClass<out FrameFragment>, addToBackStack: Boolean) {
        navigateDrawer(contentContainerId, type, addToBackStack)
    }

    private fun navigateDrawer(contentContainerId: Int, type: KClass<out FrameFragment>, addToBackStack: Boolean) {
        if (contentContainerId > 0) {
            val fragment = ObjectHelper.create(type)
            fragment?.let { fragment ->
                internalBackStackModification = true

                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(contentContainerId, fragment)

                if (addToBackStack)
                    fragmentTransaction.addToBackStack(type.qualifiedName)

                fragmentTransaction.commit()
            }
        }
    }

    override fun updateToolbarState(upButtonEnabled: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawerToggle?.let { drawerToggle ->
            drawerToggle.isDrawerIndicatorEnabled = !upButtonEnabled
            drawerToggle.syncState()
        }
    }
}
package com.example.daoan.simplemvvm.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.daoan.simplemvvm.R
import com.example.daoan.simplemvvm.ui.statistic.StatisticFragment
import com.example.daoan.simplemvvm.ui.tasklist.TaskListFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var statisticFragment: StatisticFragment
    private lateinit var taskListFragment: TaskListFragment
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.main_content)
        appBarConfiguration = AppBarConfiguration(navController.graph, draw_layout)

        // Set up ActionBar
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        // Set up navigation menu
        nav_view.setupWithNavController(navController)
        setUpDrawerItemSelectedListener()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setUpDrawerItemSelectedListener() {
        statisticFragment = StatisticFragment.newInstance()
        taskListFragment = TaskListFragment.newInstance()
        main_content.childFragmentManager.beginTransaction()
            .replace(R.id.main_content, taskListFragment)
            .add(R.id.main_content, statisticFragment)
            .hide(statisticFragment)
            .commit()

        nav_view.setNavigationItemSelectedListener { navItem ->
            var fragmentInstance: Fragment? = null
            when (navItem.itemId) {
                R.id.drawer_task_list_item -> fragmentInstance = taskListFragment
                R.id.drawer_statistic_item -> fragmentInstance = statisticFragment
            }
            fragmentInstance?.let {
                main_content.childFragmentManager.beginTransaction()
                    .hide(taskListFragment)
                    .hide(statisticFragment)
                    .show(fragmentInstance)
                    .commit()
            }
            draw_layout.closeDrawers()
            false
        }
    }

    override fun onBackPressed() {
        if (draw_layout.isDrawerOpen(GravityCompat.START)) {
            draw_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}

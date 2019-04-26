package com.example.daoan.simplemvvm.base

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.daoan.simplemvvm.R
import com.example.daoan.simplemvvm.ui.statistic.StatisticFragment
import com.example.daoan.simplemvvm.ui.tasklist.TaskListFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var statisticFragment: StatisticFragment
    private lateinit var taskListFragment: TaskListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(
            this, draw_layout, toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        draw_layout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        setUpContent()
        setUpDrawerItemSelectedListener()
    }

    private fun setUpContent() {
        statisticFragment = StatisticFragment.newInstance()
        taskListFragment =  TaskListFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(R.id.mainContent, taskListFragment)
            .add(R.id.mainContent, statisticFragment)
            .hide(statisticFragment)
            .commit()
    }

    private fun setUpDrawerItemSelectedListener() {
        nav_view.setNavigationItemSelectedListener { navItem ->
            var fragmentInstance: Fragment? = null
            when (navItem.itemId) {
                R.id.drawer_task_list_item -> fragmentInstance = taskListFragment
                R.id.drawer_statistic_item -> fragmentInstance = statisticFragment
            }
            fragmentInstance?.let {
                supportFragmentManager.beginTransaction()
                    .hide(taskListFragment)
                    .hide(statisticFragment)
                    .show(fragmentInstance)
                    .commit()
            }
            draw_layout.closeDrawers()
            false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.home -> {
                draw_layout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        if (draw_layout.isDrawerOpen(GravityCompat.START)) {
            draw_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}

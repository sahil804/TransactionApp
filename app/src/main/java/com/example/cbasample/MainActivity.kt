package com.example.cbasample

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavGraph
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DaggerAppCompatActivity() {
    lateinit var graph: NavGraph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.transactionListFragment
        ).build()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        toolbar
            .setupWithNavController(navHostFragment.navController, appBarConfiguration)
        setSupportActionBar(toolbar)
        supportActionBar?.setIcon(R.drawable.action_bar_image)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun initNavigation() : NavHostFragment {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        graph = inflater.inflate(R.navigation.nav_graph)
        graph.startDestination = R.id.transactionListFragment
        navHostFragment.navController.graph = graph
        return navHostFragment
    }
}
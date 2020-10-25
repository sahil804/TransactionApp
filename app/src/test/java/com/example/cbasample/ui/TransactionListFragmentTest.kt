package com.example.cbasample.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.cbasample.MainActivity
import com.example.cbasample.MySampleRunner
import com.example.cbasample.R
import com.example.cbasample.data.network.Resource
import com.nhaarman.mockitokotlin2.mock
import junit.framework.TestCase.assertFalse
import kotlinx.android.synthetic.main.account_details_fragment.*
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric

@RunWith(MySampleRunner::class)
class TransactionListFragmentTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()
    lateinit var mainActivity: MainActivity

    lateinit var transactionListFragment: TransactionListFragment

    @Before
    fun setup() {
        initMainActivity()
        transactionListFragment = TransactionListFragment()
        startFragmentInMainContentFrame(transactionListFragment)
    }

    @Test
    fun testFailedNetworkCondition() {
        (transactionListFragment.transactionListViewModel.transactions).value =
            Resource.Failed("Something went wrong")
        assertTrue(transactionListFragment.errorView.isVisible)
        assertFalse(transactionListFragment.rvTransactions.isVisible)
        assertFalse(transactionListFragment.progressBar.isVisible)
    }

    @Test
    fun testFailedLoadingScreen() {
        (transactionListFragment.transactionListViewModel.transactions).value =
            Resource.Loading()
        assertFalse(transactionListFragment.errorView.isVisible)
        assertFalse(transactionListFragment.rvTransactions.isVisible)
        assertTrue(transactionListFragment.progressBar.isVisible)
    }

    @Test
    fun testLoadedScreen() {
        (transactionListFragment.transactionListViewModel.transactions).value =
            Resource.Loaded(mock())
        assertFalse(transactionListFragment.errorView.isVisible)
        assertTrue(transactionListFragment.rvTransactions.isVisible)
        assertFalse(transactionListFragment.progressBar.isVisible)
    }

    private fun initMainActivity() {
        mainActivity = Robolectric.buildActivity(MainActivity::class.java).create().resume().get()
        mainActivity.initNavigation()
    }

    private fun startFragmentInMainContentFrame(fragment: Fragment) {
        val navHostFragment = mainActivity.supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        navHostFragment.childFragmentManager.beginTransaction()
            .replace(R.id.navHostFragment, fragment)
            .setPrimaryNavigationFragment(fragment)
            .commitNow()
    }
}
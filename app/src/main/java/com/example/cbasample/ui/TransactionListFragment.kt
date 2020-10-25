package com.example.cbasample.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.cbasample.R
import com.example.cbasample.data.model.Atm
import com.example.cbasample.data.network.Resource
import com.example.cbasample.util.setVisibility
import com.example.samplemapdemo.di.DaggerViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.account_details_fragment.*
import javax.inject.Inject

class TransactionListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory
    lateinit var transactionListViewModel: TransactionListViewModel
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.account_details_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionListViewModel = ViewModelProvider(this, viewModelFactory).get(TransactionListViewModel::class.java)
        transactionListViewModel.getTransactions()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        initObservers()
        retryButton.setOnClickListener {
            transactionListViewModel.getTransactions()
        }
    }

    private fun setupRecyclerView() {
        transactionAdapter = TransactionAdapter(mutableListOf(), ::onLocationSelected)
        rvTransactions.apply {
            setHasFixedSize(true)
            adapter = transactionAdapter
        }
    }

    private fun onLocationSelected(atm: Atm?) {
        atm?.let {
            findNavController().navigate(TransactionListFragmentDirections.actionAccountDetailsFragmentToFindUsFragment(it))
        }
    }

    private fun initObservers() {
        transactionListViewModel.transactions.observe(viewLifecycleOwner, Observer { resourceState ->
            when (resourceState) {
                is Resource.Loading -> {
                    setViewVisibility(pbVisibility = true)
                }
                is Resource.Failed -> {
                    setViewVisibility(errorVisibilty = true)
                    errorHeadingHeading.text = resourceState.exception
                }
                is Resource.Loaded -> {
                    setViewVisibility(contentVisibilty = true)
                    Log.d("Sahil", "*** $resourceState ->." + resourceState.data)
                    transactionListViewModel.computeTransactionsDetails(resourceState.data)
                }
            }
        })

        transactionListViewModel.accountDetails.observe(viewLifecycleOwner, Observer {
            Log.d("Sahil", "second observer $it")
            transactionAdapter.notifyDataSetChanged(it)
        })
    }

    private fun setViewVisibility(
        pbVisibility: Boolean = false, errorVisibilty: Boolean = false,
        contentVisibilty: Boolean = false
    ) {
        progressBar.setVisibility(pbVisibility)
        errorView.setVisibility(errorVisibilty)
        rvTransactions.setVisibility(contentVisibilty)
    }
}
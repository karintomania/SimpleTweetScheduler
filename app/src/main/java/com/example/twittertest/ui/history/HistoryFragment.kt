package com.example.twittertest.ui.history

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.twittertest.R
import com.example.twittertest.database.AppDatabase
import com.example.twittertest.databinding.FragmentHistoryBinding
import com.example.twittertest.ui.draft.DraftViewModelFactory

class HistoryFragment : Fragment() {

    companion object {
        fun newInstance() = HistoryFragment()
    }

    private lateinit var viewModel: HistoryViewModel
    private lateinit var binding: FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_history, container, false)

        val application = requireNotNull(this.activity).application
        val datasource = AppDatabase.getInstance(application).tweetScheduleDao
        val viewModelFactory = HistoryViewModelFactory(datasource, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(HistoryViewModel::class.java)

        val adapter = HistoryAdapter()
        binding.listHistory.adapter = adapter
        viewModel.histories.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })

        binding.historyViewModel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
package com.example.twittertest.ui.schedule

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.twittertest.R
import com.example.twittertest.database.AppDatabase
import com.example.twittertest.databinding.FragmentScheduleBinding

class ScheduleFragment : Fragment() {

    companion object {
        fun newInstance() = ScheduleFragment()
    }

    private lateinit var viewModel: ScheduleViewModel
    private lateinit var binding: FragmentScheduleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_schedule, container, false)

        val application = requireNotNull(this.activity).application
        val datasource = AppDatabase.getInstance(application).tweetScheduleDao
        val viewModelFactory = ScheduleViewModelFactory(datasource, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(ScheduleViewModel::class.java)

        val adapter = ScheduleAdapter(
                DeleteScheduleListener {
                    Toast.makeText(application, "Delete ${it}", Toast.LENGTH_LONG).show()
                    viewModel.onScheduleDeleteClicked(it)
                },
                EditScheduleListener {
                    Toast.makeText(application, "Edit ${it}", Toast.LENGTH_LONG).show()
                    val action = ScheduleFragmentDirections.actionNavigationScheduleToNavigationEdit(it)
                    requireView().findNavController().navigate(action)

                }
            )
        binding.listSchedule.adapter = adapter
        viewModel.scheduledTweets.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })

        binding.scheduleViewModel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
package com.example.twittertest.ui.draft

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
import com.example.twittertest.databinding.FragmentDraftBinding

class DraftFragment : Fragment() {

    companion object {
        fun newInstance() = DraftFragment()
    }

    private lateinit var viewModel: DraftViewModel
    private lateinit var binding: FragmentDraftBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_draft, container, false)
        val application = requireNotNull(this.activity).application
        val datasource = AppDatabase.getInstance(application).tweetScheduleDao
        val viewModelFactory = DraftViewModelFactory(datasource, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(DraftViewModel::class.java)


        val adapter = DraftAdapter(
            DeleteDraftListener {
                Toast.makeText(application, "delete ${it}", Toast.LENGTH_LONG).show()
                viewModel.onDraftDeleteClicked(it)
            }
            ,
            EditDraftListener {
                Toast.makeText(application, "Edit ${it}", Toast.LENGTH_LONG).show()
                val action = DraftFragmentDirections.actionNavigationDraftToNavigationEdit(it)
                requireView().findNavController().navigate(action)
        })

        binding.listDraft.adapter = adapter
        viewModel.drafts.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })


        binding.draftViewModel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DraftViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
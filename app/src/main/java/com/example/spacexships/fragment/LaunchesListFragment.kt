package com.example.spacexships.fragment

import android.app.ActionBar
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spacexships.adapter.LaunchesListAdapter
import com.example.spacexships.data.LaunchData
import com.example.spacexships.databinding.FragmentLaunchesListBinding
import com.example.spacexships.viewmodel.LaunchesViewModel


class LaunchesListFragment : Fragment() {
    private val viewModel: LaunchesViewModel by viewModels()
    private val args: LaunchesListFragmentArgs by navArgs()
    private lateinit var launchesListAdapter: LaunchesListAdapter
    private lateinit var launchID: Array<String>
    private lateinit var fragmentLaunchesListBinding: FragmentLaunchesListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentLaunchesListBinding = FragmentLaunchesListBinding.inflate(layoutInflater)
        return fragmentLaunchesListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchesListAdapter = LaunchesListAdapter()
        fragmentLaunchesListBinding.recyclerView.adapter = launchesListAdapter
        fragmentLaunchesListBinding.recyclerView.layoutManager =
            LinearLayoutManager(activity?.applicationContext)
        fragmentLaunchesListBinding.simpleProgressBar
        viewModel.progressBar = fragmentLaunchesListBinding.simpleProgressBar

        launchID = args.launchesID

        viewModel.launchesData.observe(viewLifecycleOwner, {
            Log.d("TEST", "OBSERVER")
            launchesListAdapter.initialLaunchesList = it
            launchesListAdapter.submitList(it)
            fragmentLaunchesListBinding.simpleProgressBar.visibility = View.INVISIBLE
            //fragmentLaunchesListBinding.recyclerView.height = RecyclerView.LayoutParams.WRAP_CONTENT TODO: change recyclerViews height to wrap_content
            fragmentLaunchesListBinding.searchView.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    launchesListAdapter.getFilter().filter(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    launchesListAdapter.getFilter().filter(newText)
                    return true
                }

            })
        })
        viewModel.getLaunchesData(launchID)
    }
}

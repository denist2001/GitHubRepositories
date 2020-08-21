package com.codechallenge.githubrepositories.ui.details

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.codechallenge.githubrepositories.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.details_fragment.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.details_fragment), LifecycleOwner {

    @Inject
    lateinit var detailsAdapter: DetailsAdapter

    private val viewModel by viewModels<DetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userName = requireArguments().getString("ownerName")
        val repositoryName = requireArguments().getString("repositoryName")
        val repositoryId = requireArguments().getInt("id")
        val watchersCount = requireArguments().getInt("watchersCount")
        repositoryName?.let { repository_name_tv.text = it }
        watchers_count.text = watchersCount.toString()
        with(watchers_rv) {
            adapter = detailsAdapter
            layoutManager = LinearLayoutManager(context)
        }
        if (userName.isNullOrEmpty() || repositoryName.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Watchers can not be requested", Toast.LENGTH_LONG)
                .show()
            return
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.requestWatchers(userName, repositoryName, watchersCount).collectLatest {
                detailsAdapter.submitData(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            detailsAdapter.loadStateFlow.collectLatest { loadState ->
                progressBar.visibility = when (loadState.refresh) {
                    is LoadState.Loading -> View.VISIBLE
                    is LoadState.NotLoading -> View.INVISIBLE
                    is LoadState.Error -> View.INVISIBLE
                }
            }
        }
    }
}
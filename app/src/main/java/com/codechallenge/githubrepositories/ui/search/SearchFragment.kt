package com.codechallenge.githubrepositories.ui.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codechallenge.githubrepositories.R
import com.codechallenge.githubrepositories.ui.login.SharedPreferencesProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.search_fragment), LifecycleOwner {

    @Inject
    lateinit var prefsProvider: SharedPreferencesProvider

    @Inject
    lateinit var searchAdapter: SearchAdapter
    private lateinit var userName: String
    private val viewModel by viewModels<SearchViewModel>()
    private var lastTime = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        userName = requireArguments().getString("user_name", "")
        setupSearchField()
        if (savedInstanceState != null) {
            val query = viewModel.lastQuery
            startObserving(query)
        }
    }

    private fun setupRecyclerView() {
        searchAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        with(repos_rv) {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context)
        }
        searchAdapter.setClickCollector { direction ->
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTime >= 1000) {
                lastTime = currentTime
                findNavController().navigate(direction)
            }
        }
    }

    private fun startObserving(query: String) {
        val authKey = prefsProvider.getToken(userName)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.foundedRepositories(authKey, query).collectLatest {
                searchAdapter.submitData(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            searchAdapter.loadStateFlow.collectLatest { loadState ->
                progressBar.visibility = when (loadState.refresh) {
                    is LoadState.Loading -> View.VISIBLE
                    is LoadState.NotLoading -> View.INVISIBLE
                    is LoadState.Error -> View.INVISIBLE
                }
            }
        }
    }

    private fun setupSearchField() {
        search_sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    startObserving(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // It's bad idea to send requests on each symbol
                return true
            }
        })
    }
}
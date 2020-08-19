package com.codechallenge.githubrepositories.ui.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.codechallenge.githubrepositories.R
import com.codechallenge.githubrepositories.ui.login.LoginViewModel
import com.codechallenge.githubrepositories.ui.login.SharedPreferencesProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.search_fragment), LifecycleOwner {

    @Inject
    lateinit var prefsProvider: SharedPreferencesProvider
    private val viewModel by viewModels<LoginViewModel>()


}
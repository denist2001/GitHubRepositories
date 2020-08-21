package com.codechallenge.githubrepositories.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.codechallenge.githubrepositories.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.login_fragment.*
import java.util.*
import javax.inject.Inject

/**
 * here used decision from https://github.com/adriantache/GitHubExplorer
 */
@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.login_fragment), LifecycleOwner {

    @Inject
    lateinit var prefsProvider: SharedPreferencesProvider
    private val viewModel by viewModels<LoginViewModel>()
    private var twoFactorToken: String? = null
    private var userName: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUserName()
        setupPassword()
        setupLoginButton()
        startEventsObserving()
    }

    private fun startEventsObserving() {
        viewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                is LoginViewModelState.UserAuthorised -> {
                    isInProgress(false)
                    userAuthorized(
                        state.userName,
                        state.authHeader
                    )
                }
                is LoginViewModelState.AuthorizationError -> {
                    isInProgress(false)
                    Toast.makeText(
                        requireContext(),
                        state.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    private fun userAuthorized(userName: String, authHeader: String) {
        prefsProvider.addUser(userName, authHeader)
        goOnSearchFragment(userName)
    }

    private fun setupUserName() {
        username_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable) {
                //do nothing
            }

            override fun beforeTextChanged(charSequence: CharSequence, p1: Int, p2: Int, p3: Int) {
                //do nothing
            }

            override fun onTextChanged(charSequence: CharSequence, p1: Int, p2: Int, p3: Int) {
                //check whether we have a 2FA token saved for this user and retrieve it
                val user: String = charSequence.toString().toLowerCase(Locale.getDefault())
                if (prefsProvider.containsUser(user)) {
                    twoFactorToken = prefsProvider.getToken(user)
                    if (!twoFactorToken.isNullOrEmpty()) {
                        isLocked(false)
                        login_bn.isEnabled = true
                    }
                } else {
                    isLocked(true)
                    twoFactorToken = null
                }
            }

        })
    }

    private fun setupPassword() {
        password_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                //do nothing
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                //do nothing
            }

            override fun afterTextChanged(p0: Editable) {
                login_bn.isEnabled = true
            }
        })
    }

    private fun setupLoginButton() {
        login_bn.setOnClickListener { view ->
            run {
                val user = username_et.text.toString().toLowerCase(Locale.getDefault())
                val password = password_et.text.toString()
                val token = twoFA_et.text.toString()
                if (twoFactorToken.isNullOrEmpty()) {
                    isInProgress(true)
                    logIn(user, password, null)
                    return@run
                }
                goOnSearchFragment(user)
            }
        }
    }

    private fun goOnSearchFragment(userName: String?) {
        userName?.let {
            val action = LoginFragmentDirections.actionLoginFragmentToSearchFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun logIn(user: String, password: String, token: String?) {
        if (user.isEmpty()) {
            Toast.makeText(requireContext(), "User doesn't exist", Toast.LENGTH_SHORT).show()
            return
        }
        userName = user
        isInProgress(true)
        viewModel.onAction(LoginViewModelAction.AuthorisationRequest(user, password, token))
    }

    private fun isLocked(isLocked: Boolean) {
        val resource = when (isLocked) {
            true -> R.drawable.ic_baseline_lock_24
            false -> R.drawable.ic_baseline_lock_open_24
        }
        checkmark.setImageResource(resource)
    }

    private fun isInProgress(inProgress: Boolean) {
        val visibility = when (inProgress) {
            true -> View.VISIBLE
            false -> View.INVISIBLE
        }
        progressBar.visibility = visibility
    }

}
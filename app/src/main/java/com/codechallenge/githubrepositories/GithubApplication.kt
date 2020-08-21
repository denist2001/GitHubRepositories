package com.codechallenge.githubrepositories

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Provides
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GithubApplication : Application()
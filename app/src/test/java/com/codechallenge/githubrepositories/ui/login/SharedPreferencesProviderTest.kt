package com.codechallenge.githubrepositories.ui.login

import android.content.Context
import android.content.SharedPreferences
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runner.manipulation.Ordering
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SharedPreferencesProviderTest {

    @Mock
    private lateinit var context: Context
    @Mock
    private lateinit var sharedPreferences: SharedPreferences
    @Mock
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor

    private lateinit var subject: SharedPreferencesProvider

    @Before
    fun setUp() {
        subject= SharedPreferencesProvider(context, sharedPreferences)
    }

    @Test
    fun `check if returns false if user doesn't exist`() {
        assertFalse(subject.containsUser("user"))
    }

    @Test
    fun `check if returns true if user exist`() {
        `when`(sharedPreferences.contains("user")).thenReturn(true)
        assertTrue(subject.containsUser("user"))
    }

    @Test
    fun `check if will save empty value for user`() {
        `when`(sharedPreferences.edit()).thenReturn(sharedPreferencesEditor)
        subject.addUser("user", "")
        verify(sharedPreferences, times(1)).edit()
        verify(sharedPreferencesEditor, times(1)).putString("user", "")
    }

    @Test
    fun `check if user exist should replace value`() {
        `when`(sharedPreferences.contains("user")).thenReturn(true)
        `when`(sharedPreferences.edit()).thenReturn(sharedPreferencesEditor)
        subject.addUser("user", "")
        verify(sharedPreferences, times(2)).edit()
        verify(sharedPreferencesEditor, times(1)).remove("user")
        verify(sharedPreferencesEditor, times(1)).putString("user", "")
    }
}
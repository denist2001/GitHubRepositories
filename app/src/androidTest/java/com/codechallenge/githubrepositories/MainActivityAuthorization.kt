package com.codechallenge.githubrepositories

import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import com.codechallenge.githubrepositories.di.NetworkModule
import com.codechallenge.githubrepositories.utils.getStringFrom
import com.codechallenge.githubrepositories.utils.waitUntilView
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@UninstallModules(NetworkModule::class)
@HiltAndroidTest
class MainActivityAuthorization {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    private lateinit var mockServer: MockWebServer

    @Before
    fun setUp() {
        hiltRule.inject()
        mockServer = MockWebServer()
        mockServer.start(8080)
        mockServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(getStringFrom("authorization_response.json"))
            }
        }
        activityTestRule.launchActivity(null)
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).setOrientationNatural()
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).unfreezeRotation()
    }

    @Test
    fun chechIfLoginFragmentShownWithAllComponents() {
        waitUntilView(R.id.main, 3, ViewMatchers.isDisplayed())
        waitUntilView(R.id.username_et, 3, ViewMatchers.isDisplayed())
        waitUntilView(R.id.password_et, 3, ViewMatchers.isDisplayed())
        waitUntilView(R.id.login_bn, 3, ViewMatchers.isDisplayed())
        waitUntilView(R.id.checkmark, 3, ViewMatchers.isDisplayed())
    }

    @Test
    fun checkIfAfterStartOneRequestGoestoServer() {
        waitUntilView(R.id.main, 3, ViewMatchers.isDisplayed())
        assertEquals(1, mockServer.requestCount)
        val requestBody = mockServer.takeRequest()
        assertTrue(requestBody.path!!.contains("authorization"))
    }


}
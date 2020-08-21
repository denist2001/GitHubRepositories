package com.codechallenge.githubrepositories.util

import com.codechallenge.githubrepositories.CoroutinesTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.timeout
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.lang.Thread.sleep

@RunWith(MockitoJUnitRunner::class)
class MapUtilsKtTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var trigger: Trigger

    @InternalCoroutinesApi
    //@Test need to be investigated
    fun `throttleFirst should pass only first event in flow`() {
        val sourse = listOf<Int>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val flow = callbackFlow {
            for (index in sourse) {
                offer(index)
                sleep(100)
            }
        }
        flow.throttleFirst(1000).onEach {
            trigger.triggerEvent()
        }.launchIn(MainScope())
        coroutinesTestRule.runBlockingTest {
            cancel()
            verify(trigger, timeout(1000)).triggerEvent()
        }
    }

}

class Trigger {
    fun triggerEvent() {
    }
}
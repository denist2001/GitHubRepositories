package com.codechallenge.githubrepositories.util

import com.codechallenge.githubrepositories.data.model.search.Item
import com.codechallenge.githubrepositories.data.model.search.Owner
import com.codechallenge.githubrepositories.data.model.search.SearchResponse
import com.codechallenge.githubrepositories.data.model.watchers.WatchersResponse
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ResponsesMapperTest {

    private val subject = ResponsesMapper()

    @Mock
    private lateinit var searchResponse: SearchResponse
    @Mock
    private lateinit var watchersResponse: WatchersResponse
    @Mock
    private lateinit var item: Item
    @Mock
    private lateinit var owner: Owner

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `transform search response should create object if items doesn't exist`() {
        `when`(searchResponse.items).thenReturn(emptyList())
        val transformedValue = subject.transform(searchResponse)
        assertNotNull(transformedValue)
        assertTrue(transformedValue.isEmpty())
    }

    @Test
    fun `transform search response should create object if needed fields exist`() {
        `when`(owner.avatar_url).thenReturn("avatar_url")
        `when`(owner.login).thenReturn("login")
        `when`(item.id).thenReturn(777)
        `when`(item.owner).thenReturn(owner)
        `when`(item.name).thenReturn("name")
        `when`(item.description).thenReturn("description")
        `when`(item.forks_count).thenReturn(666)
        `when`(item.watchers_count).thenReturn(555)
        `when`(searchResponse.items).thenReturn(listOf(item))
        val transformedValue = subject.transform(searchResponse)
        assertNotNull(transformedValue)
        assertEquals(1, transformedValue.size)
        assertEquals(777, transformedValue[0].id)
        assertEquals("avatar_url", transformedValue[0].avatarImage)
        assertEquals("name", transformedValue[0].repositoryName)
        assertEquals("description", transformedValue[0].description)
        assertEquals(666, transformedValue[0].numberOfForks)
        assertEquals("login", transformedValue[0].ownerName)
        assertEquals(555, transformedValue[0].watchersCount)
    }

    @Test
    fun `transform watchers response should create object if items doesn't exist`() {
        val transformedValue = subject.transform(emptyList())
        assertNotNull(transformedValue)
        assertTrue(transformedValue.isEmpty())
    }

    @Test
    fun `transform watchers response should create object if needed fields exist`() {
        `when`(watchersResponse.login).thenReturn("login")
        val transformedValue = subject.transform(listOf(watchersResponse))
        assertNotNull(transformedValue)
        assertEquals("login", transformedValue[0])
    }
}
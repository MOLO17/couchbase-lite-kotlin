/*
 * Copyright (c) 2020 MOLO17
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.molo17.couchbase.lite

import com.couchbase.lite.ListenerToken
import com.couchbase.lite.Query
import com.couchbase.lite.QueryChange
import com.couchbase.lite.QueryChangeListener
import com.couchbase.lite.ResultSet
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Created by Damiano Giusti on 19/03/2020.
 */
class QueryExtensionsKtTest {

    @Test
    fun `when query succeeds then result are emitted by the flow`() = runBlocking {
        val expectedResultSet = mock<ResultSet>()
        val queryUnderTest = TestQuery(addChangeListenerCalled = { listener ->
            val queryChange = QueryChange(resultSet = expectedResultSet)
            listener.changed(queryChange)
            object : ListenerToken {}
        })

        assertEquals(expectedResultSet, queryUnderTest.asFlow().first())
    }

    @Test
    fun `when query fails then the flow fails`() = runBlocking {
        val queryUnderTest = TestQuery(addChangeListenerCalled = { listener ->
            val queryChange = QueryChange(error = TestException())
            listener.changed(queryChange)
            object : ListenerToken {}
        })

        queryUnderTest
            .asFlow()
            .catch { error -> assertTrue(error is TestException) }
            .collect()
    }

    @Test
    fun `when the flow is cancelled then the query is stopped`() = runBlocking {
        val listenerToken = mock<ListenerToken>()
        val expectedResultSet = mock<ResultSet>()
        val queryUnderTest = TestQuery(addChangeListenerCalled = { listener ->
            val queryChange = QueryChange(resultSet = expectedResultSet)
            listener.changed(queryChange)
            listenerToken
        })

        // Apply the `take(1)` operator for disposing the Flow after the first emission.
        queryUnderTest.asFlow().take(1).collect()

        verify(queryUnderTest).removeChangeListener(listenerToken)
    }
}

private class TestException : Exception()

private fun TestQuery(
    addChangeListenerCalled: (QueryChangeListener) -> ListenerToken
): Query = mock {
    on { addChangeListener(any()) } doAnswer { invocation ->
        val listener = invocation.arguments.single() as QueryChangeListener
        addChangeListenerCalled(listener)
    }
}

private fun QueryChange(
    resultSet: ResultSet = mock(),
    error: Throwable? = null
): QueryChange = mock {
    on { this.results } doReturn resultSet
    on { this.error } doReturn error
}
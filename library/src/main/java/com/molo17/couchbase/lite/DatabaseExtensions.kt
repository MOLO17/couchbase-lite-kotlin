/*
 * Copyright (c) 2024 MOLO17
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

import com.couchbase.lite.Collection
import com.couchbase.lite.CollectionChange
import com.couchbase.lite.Database
import com.couchbase.lite.DatabaseChange
import com.couchbase.lite.DocumentChange
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Returns a [Flow] for receiving the changes that occur in the default collection of the database.
 * The flow is nullable if the default collection doesn't exist
 *
 * @see Collection.addChangeListener
 */
fun Database.defaultCollectionChangesFlow(): Flow<CollectionChange>? = defaultCollection?.changesFlow()

fun Collection.changesFlow(): Flow<CollectionChange> = callbackFlow {
    val token = addChangeListener { change -> trySendBlocking(change) }
    awaitClose { token.remove() }
}

/**
 * Returns a [Flow] for receiving the changes that occur to the specified document of the default collection.
 * The flow is nullable if the default collection doesn't exist
 *
 * @see Collection.addDocumentChangeListener
 */
fun Database.defaultCollectionDocumentChangesFlow(documentId: String): Flow<DocumentChange>? =
    defaultCollection?.documentChangesFlow(documentId)

fun Collection.documentChangesFlow(documentId: String): Flow<DocumentChange> = callbackFlow {
    val token = addDocumentChangeListener(documentId) { change -> trySendBlocking(change) }
    awaitClose { token.remove() }
}

/**
 * Kotlin counterpart of [Database.inBatch] for adding the Database as receiver
 * for the specified operations block.
 *
 * Original doc:
 *
 * Runs a group of database operations in a batch. Use this when performing bulk write operations
 * like multiple inserts/updates; it saves the overhead of multiple database commits, greatly
 * improving performance.
 *
 * @see Database.inBatch
 */
inline fun <T : Exception>Database.doInBatch(crossinline block: Database.() -> Unit) = inBatch<T> { block() }

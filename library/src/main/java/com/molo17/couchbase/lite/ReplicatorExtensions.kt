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

import com.couchbase.lite.DocumentReplication
import com.couchbase.lite.Replicator
import com.couchbase.lite.ReplicatorChange
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Returns a [Flow] for observing the changes in the replication status and progress.
 *
 * @see Replicator.addChangeListener
 */
fun Replicator.changesFlow(): Flow<ReplicatorChange> = callbackFlow {
    val token = addChangeListener { change -> trySendBlocking(change) }
    awaitClose { token.remove() }
}

/**
 * Returns a [Flow] for receiving the replication status of the specified document.
 *
 * @see Replicator.addDocumentReplicationListener
 */
fun Replicator.documentReplicationFlow(): Flow<DocumentReplication> = callbackFlow {
    val token = addDocumentReplicationListener { replication -> trySendBlocking(replication) }
    awaitClose { token.remove() }
}
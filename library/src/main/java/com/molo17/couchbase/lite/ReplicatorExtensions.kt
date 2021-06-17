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

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.couchbase.lite.DocumentReplication
import com.couchbase.lite.Replicator
import com.couchbase.lite.ReplicatorChange
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Returns a [Flow] for observing the changes in the replication status and progress.
 *
 * @see Replicator.addChangeListener
 */
fun Replicator.changesFlow(): Flow<ReplicatorChange> = callbackFlow {
    val token = addChangeListener { change -> sendBlocking(change) }
    awaitClose { removeChangeListener(token) }
}

/**
 * Returns a [Flow] for receiving the replication status of the specified document.
 *
 * @see Replicator.addDocumentReplicationListener
 */
fun Replicator.documentReplicationFlow(): Flow<DocumentReplication> = callbackFlow {
    val token = addDocumentReplicationListener { replication -> sendBlocking(replication) }
    awaitClose { removeChangeListener(token) }
}

/**
 * Binds the [Replicator] instance to the given [Lifecycle].
 *
 * The replicator will be automatically started on the ON_RESUME event,
 * and stopped on the ON_PAUSE event.
 *
 * @see Lifecycle
 * @see Lifecycle.Event.ON_RESUME
 * @see Lifecycle.Event.ON_PAUSE
 */
fun Replicator.bindToLifecycle(lifecycle: Lifecycle) {
    lifecycle.addObserver(ReplicatorLifecycleObserver(this))
}

/**
 * Provides a binding between the Android lifecycle and the Replicator lifecycle.
 *
 * The replicator will be automatically started on the ON_RESUME event,
 * and stopped on the ON_PAUSE event.
 */
internal class ReplicatorLifecycleObserver(
    private val replicator: Replicator
) : LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> replicator.start()
            Lifecycle.Event.ON_PAUSE -> replicator.stop()
            Lifecycle.Event.ON_DESTROY -> source.lifecycle.removeObserver(this)
            else -> { } // ignored.
        }
    }
}
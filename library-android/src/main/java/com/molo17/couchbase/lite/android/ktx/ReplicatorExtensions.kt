package com.molo17.couchbase.lite.android.ktx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.couchbase.lite.Replicator

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
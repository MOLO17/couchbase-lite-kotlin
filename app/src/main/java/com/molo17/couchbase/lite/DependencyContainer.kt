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

import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.couchbase.lite.BasicAuthenticator
import com.couchbase.lite.Database
import com.couchbase.lite.DatabaseConfiguration
import com.couchbase.lite.Replicator
import com.couchbase.lite.ReplicatorConfiguration
import com.couchbase.lite.URLEndpoint
import com.molo17.couchbase.lite.android.ktx.bindToLifecycle
import com.molo17.couchbase.lite.data.CouchbaseHotelRepository
import com.molo17.couchbase.lite.domain.HotelsRepository
import java.net.URI

/**
 * Created by Damiano Giusti on 26/03/2020.
 */
object DependencyContainer : ViewModelProvider.Factory {

    fun init() {
        replicator.bindToLifecycle(ProcessLifecycleOwner.get().lifecycle)
    }

    operator fun invoke(): () -> ViewModelProvider.Factory = { this }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            MainViewModel::class.java -> MainViewModel(hotelsRepository)
            else -> error("${modelClass.simpleName} not handled in DependencyContainer")
        } as T
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private dependencies
    ///////////////////////////////////////////////////////////////////////////

    private val hotelsRepository: HotelsRepository by lazy {
        CouchbaseHotelRepository(databaseProvider = { database })
    }

    private val database by lazy {
        Database("database.db", DatabaseConfiguration())
    }

    private val replicator by lazy {
        val url = URLEndpoint(URI.create(BuildConfig.REPLICATOR_URL))
        val config = ReplicatorConfiguration(database, url)
            .setAuthenticator(
                BasicAuthenticator(BuildConfig.REPLICATOR_USER, BuildConfig.REPLICATOR_PWD.toCharArray())
            )
        Replicator(config)
    }
}
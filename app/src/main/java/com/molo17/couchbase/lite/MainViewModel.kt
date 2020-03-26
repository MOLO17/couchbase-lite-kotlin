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

import androidx.lifecycle.LiveData
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.couchbase.lite.Database
import com.couchbase.lite.DatabaseConfiguration
import com.couchbase.lite.Replicator
import com.couchbase.lite.ReplicatorConfiguration
import com.couchbase.lite.URLEndpoint
import kotlinx.coroutines.Dispatchers
import java.net.URI

/**
 * Created by Damiano Giusti on 19/03/2020.
 */
class MainViewModel : ViewModel() {

    private val database by lazy(LazyThreadSafetyMode.NONE) {
        Database("database.db", DatabaseConfiguration()).also { database ->
            val url = URLEndpoint(URI.create(BuildConfig.REPLICATOR_URL))
            val config = ReplicatorConfiguration(database, url)
            Replicator(config).bindToLifecycle(ProcessLifecycleOwner.get().lifecycle)
        }
    }

    private val usersLiveData by lazy(LazyThreadSafetyMode.NONE) {
        select(User.KEY_NAME, User.KEY_SURNAME, User.KEY_AGE)
            .from(database)
            .where { User.KEY_TYPE equalTo User.TYPE }
            .orderBy { User.KEY_NAME.ascending() }
            .asObjectsFlow(::User)
            .asLiveData(Dispatchers.IO)
    }

    fun users(): LiveData<List<User>> = usersLiveData

}

class User(map: Map<String, Any?>) {
    val name: String by map
    val surname: String by map
    val age: Int by map

    companion object {
        const val KEY_NAME = "name"
        const val KEY_SURNAME = "surname"
        const val KEY_AGE = "surname"
        const val KEY_TYPE = "type"
        const val TYPE = "user"
    }
}

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

package com.molo17.couchbase.lite.android.ktx

import com.couchbase.lite.*
import com.molo17.couchbase.lite.mapToObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach

/**
 * Returns a [Flow] that emits the Query [ResultSet] every time the underlying
 * data set changes.
 *
 * If the query fails, the [Flow] throws an error.
 */
fun Query.asKtxFlow(): Flow<ResultSet> =
    queryChangeFlow()
        .onEach { it.error?.let { error -> throw error } }
        .mapNotNull { it.results }

/**
 * Returns a [Flow] that maps the Query [ResultSet] to instances of a class
 * that can be created using the given [factory] lambda.
 *
 * Example of usage:
 *
 * ```
 * class User(map: Map<String, Any?>) {
 *   val name: String by map
 *   val surname: String by map
 *   val age: Int by map
 * }
 *
 * val users: Flow<List<User>> = query.asKtxObjectsFlow(::User)
 * ```
 *
 * Using Kotlin Map delegation for creating such instances is a great shorthand.
 *
 * @param factory the lambda used for creating object instances.
 */
fun <T : Any> Query.asKtxObjectsFlow(
    factory: (Map<String, Any?>) -> T?
): Flow<List<T>> = queryChangeFlow().mapToObjects(factory)
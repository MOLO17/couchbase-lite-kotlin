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

package com.molo17.couchbase.lite.data

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

abstract class MapBuilder<T>(private val factory: (Map<String, Any?>) -> T) {

    fun asMap(map: Map<String, Any?>): ReadOnlyProperty<Any?, T> {
        return object : ReadOnlyProperty<Any?, T> {
            private var instance: T? = null
            override fun getValue(thisRef: Any?, property: KProperty<*>): T =
                instance ?: factory(map[property.name] as Map<String, Any?>).also { instance = it }
        }
    }

    fun asList(map: Map<String, Any?>): ReadOnlyProperty<Any?, List<T>> {
        return object : ReadOnlyProperty<Any?, List<T>> {
            private var instance: List<T>? = null
            override fun getValue(thisRef: Any?, property: KProperty<*>): List<T> =
                instance ?: (map[property.name] as List<Map<String, Any?>>).map(factory).also { instance = it }
        }
    }
}
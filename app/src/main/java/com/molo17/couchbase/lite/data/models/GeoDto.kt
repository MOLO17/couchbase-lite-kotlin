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

package com.molo17.couchbase.lite.data.models

import com.molo17.couchbase.lite.domain.LatLong
import com.molo17.couchbase.lite.data.MapBuilder

class GeoDto(map: Map<String, Any?>) : LatLong {
    override val lat: Double by map
    override val long: Double get() = lon
    val lon: Double by map

    companion object : MapBuilder<GeoDto>(::GeoDto)
}
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

package com.molo17.couchbase.lite.models

class HotelDto(map: Map<String, Any?>) {
    val id: Long by map
    val name: String by map
    val description: String by map
    val phone: String by map
    val address: String? by map
    val city: String? by map
    val country: String? by map
    val geo: GeoDto by GeoDto.asMap(map)
    val reviews: List<ReviewsDto> by ReviewsDto.asList(map)

    companion object {
        const val KEY_NAME = "name"
        const val KEY_TYPE = "type"
        const val TYPE = "hotel"
    }
}
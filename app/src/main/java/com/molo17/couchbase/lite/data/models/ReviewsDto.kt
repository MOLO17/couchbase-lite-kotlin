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

import com.molo17.couchbase.lite.data.MapBuilder

/**
 * Created by Damiano Giusti on 26/03/2020.
 */
class ReviewsDto(map: Map<String, Any?>) {

    val author: String by map
    val content: String by map
    val date: String by map
    val ratings: RatingDto by RatingDto.asMap(map)

    companion object: MapBuilder<ReviewsDto>(::ReviewsDto)

    class RatingDto(map: Map<String, Any?>) {
        val Overall: Int by map
        val Cleanliness: Int by map
        val Location: Int by map
        val Service: Int by map
        val Value: Int by map

        companion object: MapBuilder<RatingDto>(
            ReviewsDto::RatingDto
        )
    }
}
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

import com.molo17.couchbase.lite.BuildConfig
import com.molo17.couchbase.lite.data.models.HotelDto
import com.molo17.couchbase.lite.domain.Hotel

/**
 * Created by Damiano Giusti on 26/03/2020.
 */

fun hotelMapper(): (Map<String, Any?>) -> Hotel = { map ->
    hotelDtoToHotel(HotelDto(map))
}

fun hotelDtoToHotel(dto: HotelDto): Hotel {
    val address = buildString {
        with(dto) {
            address?.let { append(it).append(", ") }
            city?.let { append(it).append(", ") }
            country?.let { append(it) }
        }
    }
    return Hotel(
        identifier = dto.id.toString(),
        name = dto.name,
        description = dto.description,
        address = address,
        location = dto.geo,
        imageUrl = dto.geo.run {
            MAP_URL.format("$lat,$long", lat, long, BuildConfig.MAPS_API_KEY)
        },
        rating = if (dto.reviews.isEmpty()) null
        else dto.reviews.map { it.ratings.Overall }.average()
    )
}

private const val MAP_URL =
    "https://maps.googleapis.com/maps/api/staticmap" +
        "?center=%1\$s" +
        "&zoom=18" +
        "&size=300x300" +
        "&maptype=roadmap" +
        "&markers=color:red|%2\$f,%3\$f" +
        "&key=%4\$s"
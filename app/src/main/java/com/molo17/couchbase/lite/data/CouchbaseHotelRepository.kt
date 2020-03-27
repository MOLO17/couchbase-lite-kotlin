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

import com.couchbase.lite.Database
import com.molo17.couchbase.lite.domain.HotelsRepository
import com.molo17.couchbase.lite.all
import com.molo17.couchbase.lite.asFlow
import com.molo17.couchbase.lite.data.models.HotelDto
import com.molo17.couchbase.lite.from
import com.molo17.couchbase.lite.domain.Hotel
import com.molo17.couchbase.lite.orderBy
import com.molo17.couchbase.lite.select
import com.molo17.couchbase.lite.toObjects
import com.molo17.couchbase.lite.where
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map

/**
 * Created by Damiano Giusti on 26/03/2020.
 */
class CouchbaseHotelRepository(
    private val databaseProvider: () -> Database
) : HotelsRepository {

    override fun getHotels(): Flow<List<Hotel>> {
        return select(all())
            .from(databaseProvider())
            .where { HotelDto.KEY_TYPE equalTo HotelDto.TYPE }
            .orderBy { HotelDto.KEY_NAME.ascending() }
            .asFlow()
            .debounce(timeoutMillis = 500) // Debounce results in case of first sync.
            .map { resultSet -> resultSet.toObjects(hotelMapper()) }
    }
}
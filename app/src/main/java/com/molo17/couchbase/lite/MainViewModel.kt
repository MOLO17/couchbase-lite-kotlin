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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.couchbase.lite.Database
import com.molo17.couchbase.lite.models.Hotel
import com.molo17.couchbase.lite.models.HotelDto
import com.molo17.couchbase.lite.models.hotelMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

/**
 * Created by Damiano Giusti on 19/03/2020.
 */
class MainViewModel(private val database: Database) : ViewModel() {

    private val hotelsLiveData by lazy(LazyThreadSafetyMode.NONE) {
        select(all())
            .from(database)
            .where { HotelDto.KEY_TYPE equalTo HotelDto.TYPE }
            .orderBy { HotelDto.KEY_NAME.ascending() }
            .asFlow()
            .debounce(500) // Debounce results in case of first sync.
            .map { resultSet -> resultSet.toObjects(hotelMapper()) }
            .filter { it.isNotEmpty() }
            .asLiveData(Dispatchers.IO)
    }

    fun users(): LiveData<List<Hotel>> = hotelsLiveData
}

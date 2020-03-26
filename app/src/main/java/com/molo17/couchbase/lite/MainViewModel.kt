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
import com.molo17.couchbase.lite.domain.Hotel
import com.molo17.couchbase.lite.domain.HotelsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter

/**
 * Created by Damiano Giusti on 19/03/2020.
 */
class MainViewModel(private val hotelsRepository: HotelsRepository) : ViewModel() {

    private val hotelsLiveData by lazy(LazyThreadSafetyMode.NONE) {
        hotelsRepository
            .getHotels()
            .filter { it.isNotEmpty() }
            .asLiveData(Dispatchers.IO)
    }

    fun hotels(): LiveData<List<Hotel>> = hotelsLiveData
}

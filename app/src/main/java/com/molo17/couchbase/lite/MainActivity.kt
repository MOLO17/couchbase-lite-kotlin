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

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.molo17.couchbase.lite.databinding.ActivityMainBinding
import com.molo17.couchbase.lite.domain.Hotel

/**
 * Created by Damiano Giusti on 19/03/2020.
 */
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>(factoryProducer = DependencyContainer())
    private lateinit var views: ActivityMainBinding

    private val adapter by lazy { HotelsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = ActivityMainBinding.inflate(layoutInflater)
        setContentView(views.root)

        views.hotelsRecyclerView.layoutManager = LinearLayoutManager(this)
        views.hotelsRecyclerView.adapter = adapter

        viewModel.hotels().observe(this, Observer(this::onHotels))
    }

    private fun onHotels(hotels: List<Hotel>) {
        views.hotelsPlaceholderTextView.isVisible = false
        adapter.submitList(hotels)
    }

}


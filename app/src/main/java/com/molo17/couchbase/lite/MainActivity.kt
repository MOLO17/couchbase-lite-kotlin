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
import androidx.lifecycle.Observer
import com.molo17.couchbase.lite.databinding.ActivityMainBinding

/**
 * Created by Damiano Giusti on 19/03/2020.
 */
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var views: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = ActivityMainBinding.inflate(layoutInflater)
        setContentView(views.root)

        viewModel.users().observe(this, Observer(this::onUsers))
    }

    private fun onUsers(users: List<User>) {
        val displayableUsers = users.joinToString(
            separator = "\n\n",
            transform = this::userToString
        )

        views.usersTextView.text = displayableUsers
    }

    private fun userToString(user: User) = buildString {
        append("Name: ")
        append(user.name)
        appendln()
        append("Surname: ")
        append(user.surname)
        appendln()
        append("Age: ")
        append(user.age)
    }
}


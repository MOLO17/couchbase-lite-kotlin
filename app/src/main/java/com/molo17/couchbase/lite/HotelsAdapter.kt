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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.molo17.couchbase.lite.databinding.LayoutHotelCellBinding
import com.molo17.couchbase.lite.domain.Hotel

/**
 * Created by Damiano Giusti on 26/03/2020.
 */
class HotelsAdapter: ListAdapter<Hotel, HotelViewHolder>(HotelDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_hotel_cell, parent, false)
            .let(::HotelViewHolder)

    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {
        val hotel = getItem(position) ?: return
        holder.bind(hotel)
    }
}

class HotelViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val views = LayoutHotelCellBinding.bind(itemView)

    fun bind(hotel: Hotel) {
        views.hotelNameTextView.text = hotel.name
        views.hotelRatingTextView.text = hotel.rating?.let { rating ->
            "${itemView.context.getString(R.string.hotel_ratings)} ${String.format("%.0f / 5", rating)}"
        } ?: "N/A"
        views.hotelAddressTextView.text = hotel.address
        views.hotelDescriptionTextView.text = hotel.description
        Glide.with(itemView).load(hotel.imageUrl).into(views.hotelImageView)
    }
}

object HotelDiffCallback: DiffUtil.ItemCallback<Hotel>() {
    override fun areItemsTheSame(oldItem: Hotel, newItem: Hotel): Boolean {
        return oldItem.identifier == newItem.identifier
    }

    override fun areContentsTheSame(oldItem: Hotel, newItem: Hotel): Boolean {
        return oldItem == newItem
    }
}
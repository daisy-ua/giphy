package com.daisy.giphy.ui.utils.adapter

import androidx.recyclerview.widget.DiffUtil
import com.daisy.data.network.models.GIFObjectDto

internal object Comparator : DiffUtil.ItemCallback<GIFObjectDto>() {
    override fun areItemsTheSame(oldItem: GIFObjectDto, newItem: GIFObjectDto): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: GIFObjectDto, newItem: GIFObjectDto): Boolean =
        oldItem == newItem
}
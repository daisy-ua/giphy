package com.daisy.giphy.ui.utils

import androidx.recyclerview.widget.DiffUtil
import com.daisy.domain.models.GIFObject

internal object Comparator : DiffUtil.ItemCallback<GIFObject>() {
    override fun areItemsTheSame(oldItem: GIFObject, newItem: GIFObject): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: GIFObject, newItem: GIFObject): Boolean =
        oldItem == newItem
}
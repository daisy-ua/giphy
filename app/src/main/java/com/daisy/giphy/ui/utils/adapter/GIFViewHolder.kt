package com.daisy.giphy.ui.utils.adapter

import androidx.core.view.isVisible
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.daisy.domain.models.GIFObject
import com.daisy.giphy.databinding.ContainerRvGifBinding
import com.daisy.giphy.ui.utils.ImageManager

class GIFViewHolder(
    private val binding: ContainerRvGifBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private var itemId: String? = null

    fun bind(gif: GIFObject, isSelected: Boolean = false) {
        itemId = gif.id

        with(binding) {
            gifView.contentDescription = gif.title
            ImageManager.getImage(gifView, gif)
            selection.isVisible = isSelected
        }
    }


    fun getItemDetails() = object : ItemDetailsLookup.ItemDetails<String>() {

        override fun getPosition() = bindingAdapterPosition

        override fun getSelectionKey() = itemId
    }
}
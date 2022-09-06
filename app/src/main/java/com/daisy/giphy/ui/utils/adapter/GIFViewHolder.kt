package com.daisy.giphy.ui.utils.adapter

import androidx.recyclerview.widget.RecyclerView
import com.daisy.domain.models.GIFObject
import com.daisy.giphy.databinding.ContainerRvGifBinding
import com.daisy.giphy.ui.utils.ImageManager

class GIFViewHolder(
    private val binding: ContainerRvGifBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(gif: GIFObject) = with(binding) {
        gifView.contentDescription = gif.title
        ImageManager.getImage(gifView, gif.images.fixedHeightImage.url)
    }
}
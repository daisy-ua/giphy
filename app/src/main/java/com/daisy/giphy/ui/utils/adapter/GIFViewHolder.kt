package com.daisy.giphy.ui.utils.adapter

import androidx.recyclerview.widget.RecyclerView
import com.daisy.data.network.models.GIFObjectDto
import com.daisy.giphy.databinding.ContainerRvGifBinding
import com.daisy.giphy.ui.utils.ImageManager

class GIFViewHolder(
    private val binding: ContainerRvGifBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(gif: GIFObjectDto) = with(binding) {
        ImageManager.getImage(gifView, gif.images.fixedHeight.url)
    }
}
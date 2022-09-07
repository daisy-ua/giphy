package com.daisy.giphy.ui.utils.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.daisy.domain.models.GIFObject
import com.daisy.giphy.databinding.ContainerRvGifBinding
import com.daisy.giphy.ui.utils.ImageManager
import kotlinx.coroutines.Job

class GIFViewHolder(
    private val binding: ContainerRvGifBinding,
    private val cacheImageCallback: (Context, GifDrawable, String) -> Job,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(gif: GIFObject) = with(binding) {
        gifView.contentDescription = gif.title
        ImageManager.getImage(gifView, gif, cacheImageCallback)
    }
}
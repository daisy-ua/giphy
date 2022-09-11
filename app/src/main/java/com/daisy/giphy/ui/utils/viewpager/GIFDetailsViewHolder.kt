package com.daisy.giphy.ui.utils.viewpager

import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import com.daisy.domain.models.GIFObject
import com.daisy.giphy.databinding.ContainerGifDetailsBinding
import com.daisy.giphy.ui.utils.ImageManager
import kotlin.math.roundToInt


class GIFDetailsViewHolder(
    private val binding: ContainerGifDetailsBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(gif: GIFObject) {
        with(binding) {
            originalGifView.contentDescription = gif.title

            originalGifView.controller =
                ImageManager.getImage(originalGifView, gif.originalUrl, gif.fixedStill)
        }
    }
}

internal val Float.toPx get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

internal val Float.toDp get() = (this / Resources.getSystem().displayMetrics.density).roundToInt()
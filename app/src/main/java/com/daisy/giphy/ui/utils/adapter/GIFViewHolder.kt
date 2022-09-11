package com.daisy.giphy.ui.utils.adapter

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.daisy.domain.models.GIFObject
import com.daisy.giphy.databinding.ContainerRvGifBinding
import com.daisy.giphy.ui.utils.ImageManager
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.request.ImageRequest

class GIFViewHolder(
    private val binding: ContainerRvGifBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private var itemId: String? = null

    fun bind(gif: GIFObject, isSelected: Boolean = false) {
        itemId = gif.id

        with(binding) {
            val params: ViewGroup.LayoutParams = gifView.layoutParams as ViewGroup.LayoutParams
            params.width = getSize(isSelected)
            params.height = getSize(isSelected)
            gifView.layoutParams = params

            gifView.contentDescription = gif.title

            gifView.controller = ImageManager.getImage(gifView, gif.fixedHeightUrl, gif.fixedStill)

            selectionCheck.isVisible = isSelected
            selectionBackground.isVisible = isSelected
        }
    }

    private fun getSize(isSelected: Boolean) = if (isSelected) SELECTED_SIZE else ORIGINAL_SIZE

    fun getItemDetails() = object : ItemDetailsLookup.ItemDetails<String>() {

        override fun getPosition() = bindingAdapterPosition

        override fun getSelectionKey() = itemId
    }

    companion object {
        const val SELECTED_SIZE = 400

        const val ORIGINAL_SIZE = 500
    }
}
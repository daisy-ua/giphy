package com.daisy.giphy.ui.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.daisy.domain.models.GIFObject
import com.daisy.giphy.databinding.ContainerRvGifBinding
import javax.inject.Inject

class GIFPagingAdapter @Inject constructor(
) : PagingDataAdapter<GIFObject, RecyclerView.ViewHolder>(Comparator) {
    var tracker: SelectionTracker<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GIFViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ContainerRvGifBinding.inflate(layoutInflater, parent, false)
        return GIFViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { gif ->
            if (holder is GIFViewHolder) {
                tracker?.let {
                    holder.bind(gif, it.isSelected(gif.id))
                }
            }
        }
    }
}

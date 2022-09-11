package com.daisy.giphy.ui.utils.viewpager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.daisy.domain.models.GIFObject
import com.daisy.giphy.databinding.ContainerGifDetailsBinding
import com.daisy.giphy.ui.utils.Comparator
import javax.inject.Inject

class ViewPagerAdapter @Inject constructor(
) : PagingDataAdapter<GIFObject, RecyclerView.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GIFDetailsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ContainerGifDetailsBinding.inflate(layoutInflater, parent, false)
        return GIFDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { gif ->
            if (holder is GIFDetailsViewHolder) {
                holder.bind(gif)
            }
        }
    }
}
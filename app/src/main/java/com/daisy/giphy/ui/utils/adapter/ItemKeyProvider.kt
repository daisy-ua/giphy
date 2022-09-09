package com.daisy.giphy.ui.utils.adapter

import androidx.recyclerview.selection.ItemKeyProvider

class ItemsKeyProvider(private val adapter: GIFPagingAdapter) :
    ItemKeyProvider<String>(SCOPE_CACHED) {

    override fun getKey(position: Int): String? =
        adapter.snapshot()[position]?.id

    override fun getPosition(key: String): Int =
        adapter.snapshot().indexOfFirst { it?.id == key }
}
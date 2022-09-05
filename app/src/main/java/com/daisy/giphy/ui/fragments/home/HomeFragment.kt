package com.daisy.giphy.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.daisy.data.network.models.GIFObjectDto
import com.daisy.giphy.databinding.FragmentHomeBinding
import com.daisy.giphy.ui.utils.adapter.GIFPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.bindRecyclerView(
            adapter = GIFPagingAdapter()
        )
        return binding.root
    }

    private fun FragmentHomeBinding.bindRecyclerView(
        adapter: GIFPagingAdapter,
    ) {
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pagingDataFlow.collectLatest(adapter::submitData)
        }
    }
}
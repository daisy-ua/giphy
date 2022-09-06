package com.daisy.giphy.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.daisy.giphy.databinding.FragmentHomeBinding
import com.daisy.giphy.ui.utils.adapter.GIFPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
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
            gifAdapter = GIFPagingAdapter()
        )
        return binding.root
    }

    private fun FragmentHomeBinding.bindRecyclerView(
        gifAdapter: GIFPagingAdapter,
    ) {
        recyclerView.apply {
            adapter = gifAdapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, PORTRAIT_COLUMNS)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pagingDataFlow.collectLatest(gifAdapter::submitData)
        }
    }

    private companion object {
        const val PORTRAIT_COLUMNS = 2
    }
}

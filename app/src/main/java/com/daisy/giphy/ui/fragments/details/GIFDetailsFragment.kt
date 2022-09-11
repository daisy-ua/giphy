package com.daisy.giphy.ui.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.daisy.giphy.databinding.FragmentGifDetailsBinding
import com.daisy.giphy.ui.fragments.home.HomeViewModel
import com.daisy.giphy.ui.utils.viewpager.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class GIFDetailsFragment : Fragment() {
    private val args: GIFDetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentGifDetailsBinding
    private val viewModel: HomeViewModel by viewModels({ requireActivity() })

    private lateinit var adapter: ViewPagerAdapter
    private var shouldScroll = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentGifDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(isInitialPageLoaded, shouldScroll)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        savedInstanceState?.let { state ->
            shouldScroll = state.getBoolean(isInitialPageLoaded)
        }
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        adapter = ViewPagerAdapter()

        binding.viewPager.adapter = adapter

        binding.viewPager.viewTreeObserver.addOnGlobalLayoutListener {
            if (adapter.snapshot().size > 0 && !shouldScroll) {
                shouldScroll = true
                binding.viewPager.setCurrentItem(args.position, false)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.pagingDataFlow.collectLatest(adapter::submitData)
        }
    }

    companion object {
        const val isInitialPageLoaded = "isInitialPageLoaded"
    }
}
package com.daisy.giphy.ui.fragments.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.appcompat.view.ActionMode
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daisy.domain.models.GIFObject
import com.daisy.giphy.R
import com.daisy.giphy.databinding.FragmentHomeBinding
import com.daisy.giphy.ui.activity.MainActivity
import com.daisy.giphy.ui.utils.adapter.GIFPagingAdapter
import com.daisy.giphy.ui.utils.adapter.ItemsDetailsLookup
import com.daisy.giphy.ui.utils.adapter.ItemsKeyProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var gifAdapter: GIFPagingAdapter
    private lateinit var tracker: SelectionTracker<String>
    private var actionMode: ActionMode? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        gifAdapter = GIFPagingAdapter()

        binding.bindState(
            uiState = viewModel.state,
            pagingData = viewModel.pagingDataFlow,
            uiActions = viewModel.accept
        )
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        tracker.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        tracker.onRestoreInstanceState(savedInstanceState)
        if (tracker.hasSelection()) {
            actionMode = (activity as MainActivity).startSupportActionMode(actionModeCallback)
            actionMode?.title = "${tracker.selection.size()} ${getString(R.string.action_selected)}"
        }

        super.onViewStateRestored(savedInstanceState)
    }

    private fun FragmentHomeBinding.bindState(
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<GIFObject>>,
        uiActions: (UiAction) -> Unit,
    ) {
        bindSearchView(
            uiState = uiState,
            onQueryChanged = uiActions
        )

        bindRecyclerView(
            uiState = uiState,
            pagingData = pagingData,
            onScrollChanged = uiActions
        )

        bindSelection()
    }

    private fun FragmentHomeBinding.bindSearchView(
        uiState: StateFlow<UiState>,
        onQueryChanged: (UiAction) -> Unit,
    ) {
        searchBar.setQuery(uiState.value.query, false)

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { query ->
                    onQueryChanged(UiAction.Search(query = query))
                }
                return false
            }
        })
    }

    private fun FragmentHomeBinding.bindRecyclerView(
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<GIFObject>>,
        onScrollChanged: (UiAction) -> Unit,
    ) {
        val orientation = requireActivity().resources.configuration.orientation

        val columnSize =
            if (orientation == Configuration.ORIENTATION_PORTRAIT) PORTRAIT_COLUMNS else LANDSCAPE_COLUMN

        recyclerView.apply {
            adapter = gifAdapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, columnSize)
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) onScrollChanged(UiAction.Scroll(currentQuery = uiState.value.query))
            }
        })

        val notLoading = gifAdapter.loadStateFlow
            .map { it.source.refresh is LoadState.NotLoading }

        val hasNotScrolledForCurrentSearch = uiState
            .map { it.hasNotScrolledForCurrentSearch }
            .distinctUntilChanged()

        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        )
            .distinctUntilChanged()

        viewLifecycleOwner.lifecycleScope.launch {
            pagingData
                .combine(viewModel.modificationEvents) { pagingData, modification ->
                    modification.fold(pagingData) { acc, event ->
                        viewModel.applyEvents(acc, event)
                    }
                }
                .distinctUntilChanged()
                .collectLatest(gifAdapter::submitData)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            shouldScrollToTop.collect { shouldScroll ->
                if (shouldScroll) recyclerView.scrollToPosition(0)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            gifAdapter.loadStateFlow.collectLatest { loadState ->
                notFoundMsg.isVisible =
                    (loadState.refresh is LoadState.NotLoading
                            && (gifAdapter.itemCount) < 1)

                recyclerView.isVisible =
                    loadState.source.refresh is LoadState.NotLoading
            }
        }
    }

    private fun FragmentHomeBinding.bindSelection() {
        tracker = SelectionTracker.Builder(
            TRACKER_SELECTION_ID,
            recyclerView,
            ItemsKeyProvider(gifAdapter),
            ItemsDetailsLookup(recyclerView),
            StorageStrategy.createStringStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        gifAdapter.tracker = tracker

        tracker.addObserver(
            object : SelectionTracker.SelectionObserver<String>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    if (actionMode == null) {
                        val currentActivity = activity as MainActivity
                        actionMode = currentActivity.startSupportActionMode(actionModeCallback)
                    }

                    val selectedSize = tracker.selection.size()
                    if (selectedSize > 0) {
                        actionMode?.title = "$selectedSize ${getString(R.string.action_selected)}"
                    } else {
                        actionMode?.finish()
                    }
                }
            }
        )
    }

    private val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(
            mode: ActionMode?,
            menu: Menu?,
        ): Boolean {
            mode?.menuInflater?.inflate(R.menu.menu, menu)
            return true
        }

        override fun onPrepareActionMode(
            mode: ActionMode?,
            menu: Menu?,
        ) = true

        override fun onActionItemClicked(
            mode: ActionMode?,
            item: MenuItem?,
        ): Boolean {
            return when (item?.itemId) {
                R.id.action_delete -> {

                    val gifsSelected = gifAdapter.snapshot().filter { gif ->
                        gif != null && tracker.selection.contains(gif.id)
                    }.map { gif ->
                        gif!!.id
                    }

                    viewModel.emitEvent(BaseEvents.Remove(gifsSelected))

                    actionMode?.finish()
                    true
                }
                else -> {
                    false
                }
            }
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            tracker.clearSelection()
            actionMode = null
        }
    }


    private companion object {
        const val PORTRAIT_COLUMNS = 2

        const val LANDSCAPE_COLUMN = 4

        const val TRACKER_SELECTION_ID = "selectionItem"
    }
}

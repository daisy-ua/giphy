package com.daisy.giphy.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.daisy.domain.models.GIFObject
import com.daisy.domain.usecases.ExcludeGIFsFromResponse
import com.daisy.domain.usecases.FetchSearchResultGIFs
import com.daisy.domain.usecases.FetchTrendingGIFs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchTrendingGIFs: FetchTrendingGIFs,
    private val fetchSearchResultGIFs: FetchSearchResultGIFs,
    private val excludeGIFsFromResponse: ExcludeGIFsFromResponse,
) : ViewModel() {
    private val _modificationEvents = MutableStateFlow<List<BaseEvents>>(emptyList())
    val modificationEvents = _modificationEvents.asStateFlow()

    fun emitEvent(events: BaseEvents) {
        _modificationEvents.value += events
    }

    val state: StateFlow<UiState>

    var pagingDataFlow: Flow<PagingData<GIFObject>>

    val accept: (UiAction) -> Unit

    private var shouldCacheImage = true

    init {
        val actionStateFlow = MutableSharedFlow<UiAction>()

        val searches = actionStateFlow
            .filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(UiAction.Search(query = DEFAULT_QUERY)) }

        val queriesScrolled = actionStateFlow
            .filterIsInstance<UiAction.Scroll>()
            .distinctUntilChanged()
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
            .onStart { emit(UiAction.Scroll(currentQuery = DEFAULT_QUERY)) }

        pagingDataFlow = searches
            .flatMapLatest {
                if (it.query.isNotEmpty()) {
                    shouldCacheImage = false
                    fetchSearchResultGIFs(it.query)
                } else {
                    shouldCacheImage = true
                    fetchTrendingGIFs()
                }
            }
            .cachedIn(viewModelScope)

        state = combine(
            searches,
            queriesScrolled,
            ::Pair
        ).map { (search, scroll) ->
            UiState(
                query = search.query,
                lastQueryScrolled = scroll.currentQuery,
                hasNotScrolledForCurrentSearch = search.query != scroll.currentQuery,
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = UiState()
            )

        accept = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }
    }

    private suspend fun fetchTrendingGIFs() =
        fetchTrendingGIFs.invoke()

    private suspend fun fetchSearchResultGIFs(query: String) =
        fetchSearchResultGIFs.invoke(query)

    private fun excludeGIFsFromResponse(ids: List<String>) =
        viewModelScope.launch {
            excludeGIFsFromResponse.invoke(ids)
        }

    fun applyEvents(
        paging: PagingData<GIFObject>,
        event: BaseEvents,
    ): PagingData<GIFObject> {
        return when (event) {
            is BaseEvents.Remove -> {
                paging
                    .filter { gif ->
                        !event.ids.contains(gif.id)
                    }.also {
                        excludeGIFsFromResponse(event.ids)
                    }
            }
        }
    }
}

data class UiState(
    val query: String = DEFAULT_QUERY,
    val lastQueryScrolled: String = DEFAULT_QUERY,
    val hasNotScrolledForCurrentSearch: Boolean = false,
)

sealed class UiAction {
    data class Search(val query: String) : UiAction()

    data class Scroll(val currentQuery: String) : UiAction()
}

sealed class BaseEvents {
    data class Remove(val ids: List<String>) : BaseEvents()
}

const val DEFAULT_QUERY = ""
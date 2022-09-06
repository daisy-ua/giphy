package com.daisy.giphy.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.daisy.domain.models.GIFObject
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
) : ViewModel() {
    val state: StateFlow<UiState>

    var pagingDataFlow: Flow<PagingData<GIFObject>>

    val accept: (UiAction) -> Unit

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
                if (it.query.isNotEmpty()) fetchSearchResultGIFs(it.query)
                else fetchTrendingGIFs()
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

const val DEFAULT_QUERY = ""
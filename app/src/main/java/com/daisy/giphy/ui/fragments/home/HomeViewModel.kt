package com.daisy.giphy.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.daisy.domain.models.GIFObject
import com.daisy.domain.usecases.FetchTrendingGIFs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchTrendingGIFs: FetchTrendingGIFs,
) : ViewModel() {

    var pagingDataFlow = MutableStateFlow<PagingData<GIFObject>>(PagingData.empty())

    init {
        viewModelScope.launch {
            fetchTrendingGIFs().collectLatest { response ->
                pagingDataFlow.value = response
            }
        }
    }

    private suspend fun fetchTrendingGIFs() = fetchTrendingGIFs.invoke().cachedIn(viewModelScope)
}
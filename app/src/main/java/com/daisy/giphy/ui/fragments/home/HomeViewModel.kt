package com.daisy.giphy.ui.fragments.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.daisy.data.network.models.GIFObjectDto
import com.daisy.data.repository.repository.GIFRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: GIFRepository,
) : ViewModel() {

    var pagingDataFlow = MutableStateFlow<PagingData<GIFObjectDto>>(PagingData.empty())

    init {
        viewModelScope.launch {
            fetchTrendingGIFs().collectLatest { response ->
                pagingDataFlow.value = response
            }
        }
    }

    fun fetch() = viewModelScope.launch {
//        repository.test().apply {
//            this?.data?.get(0)?.let { Log.d("daisy-ua", it?.toString()) }
//        }

        repository.fetchTrendingGIFs().collectLatest {
            Log.d("daisy-ua", it.toString())
        }
    }

    suspend fun fetchTrendingGIFs() = repository.fetchTrendingGIFs().cachedIn(viewModelScope)
}
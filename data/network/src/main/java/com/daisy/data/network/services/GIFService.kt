package com.daisy.data.network.services

import com.daisy.data.network.models.GIFObjectListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GIFService {

    @GET("trending")
    suspend fun getTrendingGIFs(
        @Query("offset") offset: Int,
    ): GIFObjectListDto
}
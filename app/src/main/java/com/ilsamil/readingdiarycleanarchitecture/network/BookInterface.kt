package com.ilsamil.readingdiarycleanarchitecture.network

import com.ilsamil.readingdiarycleanarchitecture.BuildConfig
import com.ilsamil.readingdiarycleanarchitecture.data.remote.model.SearchBookDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface BookInterface {
    @GET("/v3/search/book?target=title")
    @Headers("Authorization:${BuildConfig.kakaoAuthorization}")
    suspend fun getBookInfo(
        @Query("query") query : String,
        @Query("sort") sort : String,
        @Query("size") size : Int,
        @Query("target") target : String
    ) : Response<SearchBookDto>
}
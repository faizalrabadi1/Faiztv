package com.example.faiztv.data.api

import com.example.faiztv.data.model.Category
import com.example.faiztv.data.model.ContentItem
import retrofit2.http.GET

interface ApiService {
    @GET("api/public/channels")
    suspend fun getChannels(): List<ContentItem>

    @GET("api/public/movies")
    suspend fun getMovies(): List<ContentItem>

    @GET("api/public/series")
    suspend fun getSeries(): List<ContentItem>

    @GET("api/public/featured")
    suspend fun getFeatured(): List<ContentItem>

    @GET("api/public/categories")
    suspend fun getCategories(): List<Category>
}

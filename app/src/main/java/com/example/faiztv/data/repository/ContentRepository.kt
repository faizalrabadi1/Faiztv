package com.example.faiztv.data.repository

import com.example.faiztv.data.api.RetrofitClient
import com.example.faiztv.data.model.Category
import com.example.faiztv.data.model.ContentItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContentRepository {
    private val api = RetrofitClient.apiService

    suspend fun getChannels(): Result<List<ContentItem>> = makeSafeRequest {
        api.getChannels()
    }

    suspend fun getMovies(): Result<List<ContentItem>> = makeSafeRequest {
        api.getMovies()
    }

    suspend fun getSeries(): Result<List<ContentItem>> = makeSafeRequest {
        api.getSeries()
    }

    suspend fun getFeatured(): Result<List<ContentItem>> = makeSafeRequest {
        api.getFeatured()
    }

    suspend fun getCategories(): Result<List<Category>> = makeSafeRequest {
        api.getCategories()
    }

    private suspend fun <T> makeSafeRequest(
        request: suspend () -> T
    ): Result<T> = withContext(Dispatchers.IO) {
        try {
            Result.success(request())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

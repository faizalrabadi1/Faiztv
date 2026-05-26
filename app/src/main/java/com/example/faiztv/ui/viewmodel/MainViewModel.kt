package com.example.faiztv.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faiztv.data.model.Category
import com.example.faiztv.data.model.ContentItem
import com.example.faiztv.data.repository.ContentRepository
import com.example.faiztv.utils.M3uParser
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class MainViewModel : ViewModel() {
    private val repository = ContentRepository()

    private val _featuredState = MutableStateFlow<UiState<List<ContentItem>>>(UiState.Loading)
    val featuredState: StateFlow<UiState<List<ContentItem>>> = _featuredState.asStateFlow()

    private val _channelsState = MutableStateFlow<UiState<List<ContentItem>>>(UiState.Loading)
    val channelsState: StateFlow<UiState<List<ContentItem>>> = _channelsState.asStateFlow()

    private val _moviesState = MutableStateFlow<UiState<List<ContentItem>>>(UiState.Loading)
    val moviesState: StateFlow<UiState<List<ContentItem>>> = _moviesState.asStateFlow()

    private val _seriesState = MutableStateFlow<UiState<List<ContentItem>>>(UiState.Loading)
    val seriesState: StateFlow<UiState<List<ContentItem>>> = _seriesState.asStateFlow()

    private val m3uSources = listOf(
        "https://iptv-org.github.io/iptv/languages/ara.m3u",
        "https://iptv-org.github.io/iptv/languages/eng.m3u",
        "https://iptv-org.github.io/iptv/categories/news.m3u",
        "https://iptv-org.github.io/iptv/categories/sports.m3u",
        "https://iptv-org.github.io/iptv/categories/movies.m3u",
        "https://iptv-org.github.io/iptv/categories/kids.m3u",
        "https://raw.githubusercontent.com/Free-TV/IPTV/master/playlist.m3u8"
    )

    init {
        loadAllData()
    }

    fun loadAllData() {
        fetchFeatured()
        fetchChannels()
        fetchMovies()
        fetchSeries()
    }

    private fun fetchFeatured() = viewModelScope.launch {
        _featuredState.value = UiState.Loading
        repository.getFeatured().fold(
            onSuccess = { _featuredState.value = UiState.Success(it) },
            onFailure = { _featuredState.value = UiState.Error(it.message ?: "Unknown Error") }
        )
    }

    private fun fetchChannels() = viewModelScope.launch {
        _channelsState.value = UiState.Loading
        
        val apiChannelsDeferred = async { repository.getChannels().getOrNull() ?: emptyList() }
        val m3uDeferred = m3uSources.map { url ->
            async { M3uParser.parseFromUrl(url) }
        }
        
        val apiChannels = apiChannelsDeferred.await()
        val m3uChannels = m3uDeferred.awaitAll().flatten()
        
        // Remove duplicates and combine safely
        val combined = apiChannels + m3uChannels.distinctBy { it.streamUrl }.take(1500)
        
        if (combined.isNotEmpty()) {
            _channelsState.value = UiState.Success(combined)
        } else {
            _channelsState.value = UiState.Error("Failed to load live channels")
        }
    }

    private fun fetchMovies() = viewModelScope.launch {
        _moviesState.value = UiState.Loading
        repository.getMovies().fold(
            onSuccess = { _moviesState.value = UiState.Success(it) },
            onFailure = { _moviesState.value = UiState.Error(it.message ?: "Unknown Error") }
        )
    }

    private fun fetchSeries() = viewModelScope.launch {
        _seriesState.value = UiState.Loading
        repository.getSeries().fold(
            onSuccess = { _seriesState.value = UiState.Success(it) },
            onFailure = { _seriesState.value = UiState.Error(it.message ?: "Unknown Error") }
        )
    }
}

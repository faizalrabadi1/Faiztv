package com.example.faiztv.data.model

import com.google.gson.annotations.SerializedName

data class ContentItem(
    val id: Int,
    val title: String,
    val description: String? = null,
    @SerializedName("image_url") val imageUrl: String? = null,
    @SerializedName("stream_url") val streamUrl: String? = null,
    val type: String? = null,
    val rating: String? = null,
    val duration: String? = null,
    val year: String? = null
)

data class Category(
    val id: Int,
    val name: String,
    val icon: String? = null
)

data class ApiResponse<T>(
    val data: T
)

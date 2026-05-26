package com.example.navigation

import kotlinx.serialization.Serializable

@Serializable
object SplashRoute

@Serializable
object HomeRoute

@Serializable
object LiveTvRoute

@Serializable
object MoviesRoute

@Serializable
object SeriesRoute

@Serializable
data class PlayerRoute(val videoUrl: String, val title: String)

@Serializable
data class DetailsRoute(val id: Int, val title: String, val type: String) // type: Movie or Series


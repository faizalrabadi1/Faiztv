package com.example.faiztv.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.faiztv.ui.screens.MainScreen
import com.example.faiztv.ui.screens.PlayerScreen
import kotlinx.serialization.Serializable
import androidx.navigation.toRoute

@Serializable
object HomeRoute

@Serializable
data class PlayerRoute(val videoUrl: String)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = HomeRoute) {
        composable<HomeRoute> {
            MainScreen(
                onNavigateToPlayer = { url ->
                    navController.navigate(PlayerRoute(url))
                }
            )
        }
        
        composable<PlayerRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<PlayerRoute>()
            PlayerScreen(
                videoUrl = android.net.Uri.decode(route.videoUrl),
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

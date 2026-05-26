package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.ui.screens.splash.SplashScreen
import com.example.ui.screens.home.MainScreen
import com.example.ui.screens.player.PlayerScreen
import com.example.ui.screens.details.DetailsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = SplashRoute) {
        composable<SplashRoute> {
            SplashScreen(onSplashFinished = {
                navController.navigate(HomeRoute) {
                    popUpTo(SplashRoute) { inclusive = true }
                }
            })
        }
        
        composable<HomeRoute> {
            MainScreen(
                onNavigateToDetails = { id, title, type ->
                    navController.navigate(DetailsRoute(id, title, type))
                },
                onNavigateToPlayer = { url, title ->
                    navController.navigate(PlayerRoute(url, title))
                }
            )
        }
        
        composable<DetailsRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<DetailsRoute>()
            DetailsScreen(
                id = route.id,
                title = route.title,
                type = route.type,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlayer = { url, title ->
                    navController.navigate(PlayerRoute(url, title))
                },
                onNavigateToDetails = { newId, newTitle, newType ->
                    navController.navigate(DetailsRoute(newId, newTitle, newType))
                }
            )
        }
        
        composable<PlayerRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<PlayerRoute>()
            PlayerScreen(
                videoUrl = route.videoUrl,
                title = route.title,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

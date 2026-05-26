package com.example.ui.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryRed

enum class AppTab(val label: String, val icon: ImageVector) {
    HOME("الرئيسية", Icons.Default.Home),
    LIVE("مباشر", Icons.Default.LiveTv),
    MOVIES("أفلام", Icons.Default.Movie),
    SERIES("مسلسلات", Icons.Default.Tv)
}

@Composable
fun MainScreen(
    onNavigateToDetails: (Int, String, String) -> Unit,
    onNavigateToPlayer: (String, String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    var selectedTab by remember { mutableStateOf(AppTab.HOME) }

    Scaffold(
        bottomBar = {
            if (!isLandscape) {
                NavigationBar(
                    containerColor = Color.Black.copy(alpha = 0.8f),
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    AppTab.values().forEach { tab ->
                        NavigationBarItem(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                selectedTextColor = PrimaryRed,
                                indicatorColor = PrimaryRed,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLandscape) {
                NavigationRail(
                    containerColor = Color.Black.copy(alpha = 0.8f),
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ) {
                    AppTab.values().forEach { tab ->
                        NavigationRailItem(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                selectedTextColor = PrimaryRed,
                                indicatorColor = PrimaryRed,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                when (selectedTab) {
                    AppTab.HOME -> HomeContent(onNavigateToDetails, onNavigateToPlayer)
                    AppTab.LIVE -> LiveTvContent(onNavigateToPlayer)
                    AppTab.MOVIES -> MoviesContent(onNavigateToDetails)
                    AppTab.SERIES -> SeriesContent(onNavigateToDetails)
                }
            }
        }
    }
}

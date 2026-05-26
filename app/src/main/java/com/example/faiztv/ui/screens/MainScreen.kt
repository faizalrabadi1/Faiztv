package com.example.faiztv.ui.screens

import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.faiztv.data.model.ContentItem
import com.example.faiztv.ui.viewmodel.MainViewModel
import com.example.faiztv.ui.viewmodel.UiState

enum class AppTab(val title: String, val icon: ImageVector) {
    HOME("الرئيسية", Icons.Default.Home),
    LIVE("البث المباشر", Icons.Default.LiveTv),
    MOVIES("أفلام", Icons.Default.Movie),
    SERIES("مسلسلات", Icons.Default.Tv)
}

val DarkBg = Color(0xFF050505)
val BrandRed = Color(0xFFE50914)
val BrandGold = Color(0xFFD4AF37)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToPlayer: (String) -> Unit
) {
    val viewModel: MainViewModel = viewModel()
    var selectedTab by remember { mutableStateOf(AppTab.HOME) }

    Scaffold(
        containerColor = DarkBg,
        bottomBar = {
            NavigationBar(
                containerColor = Color.Black.copy(alpha = 0.8f),
                contentColor = Color.White
            ) {
                AppTab.values().forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = { Icon(tab.icon, contentDescription = tab.title) },
                        label = { Text(tab.title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BrandGold,
                            selectedTextColor = BrandGold,
                            indicatorColor = Color.Transparent,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (selectedTab) {
                AppTab.HOME -> HomeContent(viewModel, onNavigateToPlayer)
                AppTab.LIVE -> LiveTvContent(viewModel, onNavigateToPlayer)
                AppTab.MOVIES -> MoviesContent(viewModel, onNavigateToPlayer)
                AppTab.SERIES -> SeriesContent(viewModel, onNavigateToPlayer)
            }
        }
    }
}

@Composable
fun HomeContent(viewModel: MainViewModel, onNavigateToPlayer: (String) -> Unit) {
    val featuredState = viewModel.featuredState.collectAsState().value
    val moviesState = viewModel.moviesState.collectAsState().value
    val seriesState = viewModel.seriesState.collectAsState().value
    
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            FeaturedBanner(featuredState, onNavigateToPlayer)
        }
        item {
            ContentRow("أحدث الأفلام", moviesState, onNavigateToPlayer, isSquare = false)
        }
        item {
            ContentRow("المسلسلات المميزة", seriesState, onNavigateToPlayer, isSquare = false)
        }
        item {
             Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun LiveTvContent(viewModel: MainViewModel, onNavigateToPlayer: (String) -> Unit) {
    val channelsState = viewModel.channelsState.collectAsState().value
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            HeaderTitle("قنوات البث المباشر")
        }
        item {
            ContentRowGridStyle(channelsState, onNavigateToPlayer)
        }
    }
}

@Composable
fun MoviesContent(viewModel: MainViewModel, onNavigateToPlayer: (String) -> Unit) {
    val moviesState = viewModel.moviesState.collectAsState().value
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            HeaderTitle("جميع الأفلام")
        }
        item {
            ContentRowGridStyle(moviesState, onNavigateToPlayer)
        }
    }
}

@Composable
fun SeriesContent(viewModel: MainViewModel, onNavigateToPlayer: (String) -> Unit) {
    val seriesState = viewModel.seriesState.collectAsState().value
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            HeaderTitle("جميع المسلسلات")
        }
        item {
            ContentRowGridStyle(seriesState, onNavigateToPlayer)
        }
    }
}

@Composable
fun HeaderTitle(title: String) {
    Text(
        text = title,
        color = BrandGold,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 16.dp)
    )
}

@Composable
fun FeaturedBanner(state: UiState<List<ContentItem>>, onNavigateToPlayer: (String) -> Unit) {
    when (state) {
        is UiState.Loading -> {
             Box(modifier = Modifier.fillMaxWidth().height(400.dp), contentAlignment = Alignment.Center) {
                 CircularProgressIndicator(color = BrandRed)
             }
        }
        is UiState.Error -> {
             // Fallback
             Box(modifier = Modifier.fillMaxWidth().height(400.dp).background(Color.DarkGray))
        }
        is UiState.Success -> {
            val item = state.data.firstOrNull()
            if (item != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp)
                ) {
                    AsyncImage(
                        model = item.imageUrl ?: "https://images.unsplash.com/photo-1616530940355-351fabd9524b",
                        contentDescription = item.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, DarkBg),
                                    startY = 100f
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = item.title,
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { item.streamUrl?.let { onNavigateToPlayer(Uri.encode(it)) } },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandRed),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("تشغيل الآن", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContentRow(title: String, state: UiState<List<ContentItem>>, onNavigateToPlayer: (String) -> Unit, isSquare: Boolean) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        when (state) {
            is UiState.Loading -> CircularProgressIndicator(color = BrandRed, modifier = Modifier.padding(16.dp))
            is UiState.Error -> {}
            is UiState.Success -> {
                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
                    items(state.data) { item ->
                        TvFocusableCard(item = item, isSquare = isSquare) {
                            item.streamUrl?.let { onNavigateToPlayer(Uri.encode(it)) }
                        }
                    }
                }
            }
        }
    }
}

// Re-using LazyRow for a grid-like flow for simplicity
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ContentRowGridStyle(state: UiState<List<ContentItem>>, onNavigateToPlayer: (String) -> Unit) {
    when (state) {
        is UiState.Loading -> CircularProgressIndicator(color = BrandRed, modifier = Modifier.padding(16.dp))
        is UiState.Error -> {}
        is UiState.Success -> {
            FlowRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.data.forEach { item ->
                    TvFocusableCard(item = item, isSquare = true) {
                        item.streamUrl?.let { onNavigateToPlayer(Uri.encode(it)) }
                    }
                }
            }
        }
    }
}

@Composable
fun TvFocusableCard(item: ContentItem, isSquare: Boolean, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.05f else 1f,
        animationSpec = tween(durationMillis = 200), label = "scale"
    )

    Card(
        modifier = Modifier
            .width(if (isSquare) 120.dp else 110.dp)
            .aspectRatio(if (isSquare) 1f else 2f/3f)
            .padding(4.dp)
            .scale(scale)
            .clip(RoundedCornerShape(8.dp))
            .clickable(interactionSource = interactionSource, indication = androidx.compose.foundation.LocalIndication.current, onClick = onClick)
            .focusable(interactionSource = interactionSource),
        shape = RoundedCornerShape(8.dp),
        border = if (isFocused) BorderStroke(2.dp, BrandGold) else BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111111))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = item.imageUrl ?: "https://images.unsplash.com/photo-1594909122845-11baa439b7bf",
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Title overlay for channels if square
            if (isSquare) {
                 Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha=0.8f)), startY=50f)))
                 Text(
                     text = item.title,
                     color = Color.White,
                     fontSize = 10.sp,
                     maxLines = 2,
                     modifier = Modifier.align(Alignment.BottomCenter).padding(4.dp)
                 )
            }
        }
    }
}

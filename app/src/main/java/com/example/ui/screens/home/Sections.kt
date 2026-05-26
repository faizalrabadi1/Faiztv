package com.example.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.data.MockData
import com.example.data.ContentItem
import com.example.data.Channel
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryRed
import com.example.ui.theme.Gold

@Composable
fun HomeContent(
    onNavigateToDetails: (Int, String, String) -> Unit,
    onNavigateToPlayer: (String, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            HeroBanner(
                item = MockData.movies.first(),
                onPlayClick = { onNavigateToPlayer(it.videoUrl, it.title) },
                onInfoClick = { onNavigateToDetails(it.id, it.title, it.type) }
            )
        }
        item {
            ContentSectionRow(
                title = "أحدث الأفلام",
                items = MockData.movies,
                onItemClick = { onNavigateToDetails(it.id, it.title, it.type) }
            )
        }
        item {
            LiveChannelsRow(
                title = "قنوات مباشرة",
                channels = MockData.liveChannels,
                onItemClick = { onNavigateToPlayer(it.streamUrl, it.name) }
            )
        }
        item {
            ContentSectionRow(
                title = "أفضل المسلسلات",
                items = MockData.series,
                onItemClick = { onNavigateToDetails(it.id, it.title, it.type) }
            )
        }
    }
}

@Composable
fun LiveTvContent(onNavigateToPlayer: (String, String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "البث المباشر",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        
        val categories = MockData.liveCategories
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            categories.forEach { cat ->
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(DarkSurface)
                        .clickable { /* Filter logic */ }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(text = cat, color = Color.White)
                }
            }
        }
        
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(MockData.liveChannels) { channel ->
                ChannelCard(channel = channel, onClick = {
                    onNavigateToPlayer(channel.streamUrl, channel.name)
                })
            }
        }
    }
}

@Composable
fun MoviesContent(onNavigateToDetails: (Int, String, String) -> Unit) {
    ContentGridScreen("الأفلام", MockData.movies, onNavigateToDetails)
}

@Composable
fun SeriesContent(onNavigateToDetails: (Int, String, String) -> Unit) {
    ContentGridScreen("المسلسلات", MockData.series, onNavigateToDetails)
}

@Composable
fun ContentGridScreen(
    title: String,
    items: List<ContentItem>,
    onNavigateToDetails: (Int, String, String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 100.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items) { item ->
                ContentCard(item = item, onClick = {
                    onNavigateToDetails(item.id, item.title, item.type)
                })
            }
        }
    }
}

@Composable
fun HeroBanner(
    item: ContentItem,
    onPlayClick: (ContentItem) -> Unit,
    onInfoClick: (ContentItem) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .padding(16.dp)
            .clip(RoundedCornerShape(32.dp))
    ) {
        AsyncImage(
            model = item.imageUrl,
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
                        colors = listOf(Color.Transparent, Color(0xFF050505))
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
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(
                    onClick = { onPlayClick(item) },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("تشغيل")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { onInfoClick(item) },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("التفاصيل")
                }
            }
        }
    }
}

@Composable
fun ContentSectionRow(
    title: String,
    items: List<ContentItem>,
    onItemClick: (ContentItem) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
            items(items) { item ->
                ContentCard(item = item, onClick = { onItemClick(item) })
            }
        }
    }
}

@Composable
fun LiveChannelsRow(
    title: String,
    channels: List<Channel>,
    onItemClick: (Channel) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
            items(channels) { channel ->
                ChannelCard(channel = channel, onClick = { onItemClick(channel) }, isRow = true)
            }
        }
    }
}

@Composable
fun ContentCard(item: ContentItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .aspectRatio(2f / 3f)
            .padding(4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ChannelCard(channel: Channel, onClick: () -> Unit, isRow: Boolean = false) {
    Card(
        modifier = Modifier
            .width(if (isRow) 120.dp else 100.dp)
            .aspectRatio(1f)
            .padding(4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(DarkSurface)
                .padding(8.dp)
        ) {
            AsyncImage(
                model = channel.logoUrl,
                contentDescription = channel.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = channel.name,
                color = Color.White,
                fontSize = 12.sp,
                maxLines = 1
            )
        }
    }
}

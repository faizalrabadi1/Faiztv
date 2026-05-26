package com.example.faiztv.utils

import com.example.faiztv.data.model.ContentItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedReader
import java.io.InputStreamReader

object M3uParser {
    suspend fun parseFromUrl(url: String): List<ContentItem> = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val items = mutableListOf<ContentItem>()

        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val inputStream = response.body?.byteStream()
                val reader = BufferedReader(InputStreamReader(inputStream))

                var line: String?
                var currentTitle = ""
                var currentLogo = ""
                var currentGroup = ""
                var idCounter = 10000 // Offset for M3U items

                while (reader.readLine().also { line = it } != null) {
                    val currentLine = line?.trim() ?: continue

                    if (currentLine.startsWith("#EXTINF:")) {
                        // Extract tvg-logo
                        val logoRegex = Regex("""tvg-logo="([^"]+)"""")
                        val logoMatch = logoRegex.find(currentLine)
                        currentLogo = logoMatch?.groupValues?.get(1) ?: ""

                        // Extract group-title
                        val groupRegex = Regex("""group-title="([^"]+)"""")
                        val groupMatch = groupRegex.find(currentLine)
                        currentGroup = groupMatch?.groupValues?.get(1) ?: "Uncategorized"

                        // Extract title
                        val titleSplit = currentLine.split(",")
                        if (titleSplit.size > 1) {
                            currentTitle = titleSplit[1].trim()
                        }
                    } else if (currentLine.isNotEmpty() && !currentLine.startsWith("#")) {
                        // It's a stream URL
                        items.add(
                            ContentItem(
                                id = idCounter++,
                                title = currentTitle.ifEmpty { "Unknown Channel" },
                                imageUrl = currentLogo,
                                streamUrl = currentLine,
                                type = "Live TV",
                                description = currentGroup
                            )
                        )
                        currentTitle = ""
                        currentLogo = ""
                        currentGroup = ""
                    }
                }
                reader.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext items
    }
}

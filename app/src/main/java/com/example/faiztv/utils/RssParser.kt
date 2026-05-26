package com.example.faiztv.utils

import android.util.Xml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import java.net.HttpURLConnection
import java.net.URL

data class RssItem(
    val title: String,
    val link: String,
    val description: String,
    val imageUrl: String?
)

object RssParser {
    suspend fun parseFeed(urlString: String): List<RssItem> = withContext(Dispatchers.IO) {
        val items = mutableListOf<RssItem>()
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 15000
            connection.readTimeout = 15000

            val inputStream = connection.inputStream
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)

            var eventType = parser.eventType
            var currentTitle = ""
            var currentLink = ""
            var currentDescription = ""
            var currentImageUrl: String? = null
            var insideItem = false

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        val name = parser.name
                        if (name.equals("item", true) || name.equals("entry", true)) {
                            insideItem = true
                            currentTitle = ""
                            currentLink = ""
                            currentDescription = ""
                            currentImageUrl = null
                        } else if (insideItem) {
                            try {
                                when (name.lowercase()) {
                                    "title" -> currentTitle = parser.nextText()
                                    "link" -> {
                                        val href = parser.getAttributeValue(null, "href")
                                        if (href != null) {
                                            currentLink = href
                                        } else {
                                            currentLink = parser.nextText()
                                        }
                                    }
                                    "description", "summary", "content" -> {
                                        val text = parser.nextText()
                                        if (currentDescription.isEmpty()) currentDescription = text
                                    }
                                    "enclosure" -> {
                                        val type = parser.getAttributeValue(null, "type")
                                        if (type?.startsWith("image") == true) {
                                            currentImageUrl = parser.getAttributeValue(null, "url")
                                        }
                                    }
                                    "media:content", "media:thumbnail" -> {
                                        val urlAttr = parser.getAttributeValue(null, "url")
                                        if (urlAttr != null && (currentImageUrl == null || parser.getAttributeValue(null, "type")?.startsWith("image") == true)) {
                                            currentImageUrl = urlAttr
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                // Ignore text parsing exceptions for complex tags
                            }
                        }
                    }

                    XmlPullParser.END_TAG -> {
                        val name = parser.name
                        if ((name.equals("item", true) || name.equals("entry", true)) && insideItem) {
                            items.add(RssItem(currentTitle, currentLink, currentDescription, currentImageUrl))
                            insideItem = false
                        }
                    }
                }
                eventType = parser.next()
            }
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        items
    }
}

package com.example.data

data class Channel(
    val id: Int,
    val name: String,
    val logoUrl: String,
    val category: String,
    val streamUrl: String
)

data class ContentItem(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val type: String, // Movie, Series
    val description: String = "This is a premium streaming content.",
    val rating: String = "8.5",
    val duration: String = "120 min",
    val videoUrl: String = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"
)

object MockData {
    val liveCategories = listOf(
        "مباشر",
        "قنوات عربية",
        "قنوات أجنبية",
        "رياضة",
        "أطفال",
        "أخبار",
        "باقات متنوعة"
    )

    val liveChannels = listOf(
        Channel(1, "beIN Sports 1", "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/BeIN_Sports.svg/1200px-BeIN_Sports.svg.png", "رياضة", "http://sample.vodobox.com/planete_interdite/planete_interdite_alternate.m3u8"),
        Channel(2, "MBC 1", "https://upload.wikimedia.org/wikipedia/en/thumb/e/ef/MBC_1_Logo.svg/1200px-MBC_1_Logo.svg.png", "قنوات عربية", "http://sample.vodobox.com/planete_interdite/planete_interdite_alternate.m3u8"),
        Channel(3, "Al Jazeera", "https://upload.wikimedia.org/wikipedia/en/thumb/e/ee/Al_Jazeera_logo.svg/1200px-Al_Jazeera_logo.svg.png", "أخبار", "http://sample.vodobox.com/planete_interdite/planete_interdite_alternate.m3u8"),
        Channel(4, "Spacetoon", "https://upload.wikimedia.org/wikipedia/ar/0/00/Spacetoon_Logo.png", "أطفال", "http://sample.vodobox.com/planete_interdite/planete_interdite_alternate.m3u8"),
        Channel(5, "National Geo", "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e0/National_Geographic_Logo.svg/2048px-National_Geographic_Logo.svg.png", "باقات متنوعة", "http://sample.vodobox.com/planete_interdite/planete_interdite_alternate.m3u8"),
        Channel(6, "Netflix TV", "https://upload.wikimedia.org/wikipedia/commons/0/08/Netflix_2015_logo.svg", "قنوات أجنبية", "http://sample.vodobox.com/planete_interdite/planete_interdite_alternate.m3u8")
    )

    val movies = listOf(
        ContentItem(100, "Inception", "https://image.tmdb.org/t/p/w500/9gk7adHYeDvHkYSBAtbEJIEDNse.jpg", "Movie"),
        ContentItem(101, "Interstellar", "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg", "Movie"),
        ContentItem(102, "The Dark Knight", "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg", "Movie"),
        ContentItem(103, "Avatar", "https://image.tmdb.org/t/p/w500/jRXYjXNq0Cs2TcJjLkki24MLp7u.jpg", "Movie"),
        ContentItem(104, "Avengers", "https://image.tmdb.org/t/p/w500/RYMX2wcKCBAr24UyPD7xqlLSyN.jpg", "Movie"),
        ContentItem(105, "Joker", "https://image.tmdb.org/t/p/w500/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg", "Movie")
    )

    val series = listOf(
        ContentItem(200, "Breaking Bad", "https://image.tmdb.org/t/p/w500/ggFHVNu6YYI5L9pCfOacjizRGt.jpg", "Series"),
        ContentItem(201, "Game of Thrones", "https://image.tmdb.org/t/p/w500/u3bZgnGQ9T01sWNhyveQz0wH0Hl.jpg", "Series"),
        ContentItem(202, "Stranger Things", "https://image.tmdb.org/t/p/w500/49WJfeN0moxb9IPfGn8mGNyOMkO.jpg", "Series"),
        ContentItem(203, "The Boys", "https://image.tmdb.org/t/p/w500/dzOxNbbA12NXYFiAwdG21uU1fE3.jpg", "Series"),
        ContentItem(204, "Dark", "https://image.tmdb.org/t/p/w500/1Xm0W6LddJbXlU4q05zK8V9g2Wp.jpg", "Series")
    )
}

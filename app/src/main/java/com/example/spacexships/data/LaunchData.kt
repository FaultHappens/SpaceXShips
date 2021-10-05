package com.example.spacexships.data

data
class LaunchData(
    val date_local: String?,
    val details: String?,
    val id: String,
    val links: Links,
    val name: String,
)

data class Links(
    val article: String,
    val flickr: Flickr,
    val patch: Patch,
    val presskit: String,
    val reddit: Reddit,
    val webcast: String,
    val wikipedia: String,
    val youtube_id: String
)

data class Flickr(
    val original: List<Any>,
    val small: List<Any>
)

data class Patch(
    val large: String,
    val small: String
)

data class Reddit(
    val campaign: Any,
    val launch: Any,
    val media: Any,
    val recovery: Any
)
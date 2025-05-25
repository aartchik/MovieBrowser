package com.example.moviebrowser.model

import com.google.gson.annotations.SerializedName

data class MovieSearchResponse(
    @SerializedName("Search") val results: List<MovieDto>?,
    @SerializedName("totalResults") val total: String?,
    @SerializedName("Response") val response: String,
    @SerializedName("Error") val error: String?
)

data class MovieDto(
    @SerializedName("Title") val Title: String,
    @SerializedName("Year")  val Year: String,
    @SerializedName("imdbID") val imdbID: String,
    @SerializedName("Poster") val Poster: String
) {
    fun toModel() = MovieModel(
        title = Title,
        year = Year,
        imdbID = imdbID,
        posterUrl = Poster
    )
}
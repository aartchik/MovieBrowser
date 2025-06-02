package com.example.moviebrowser.model

import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(
    @SerializedName("Title")        val title: String,
    @SerializedName("Year")         val year: String,
    @SerializedName("Rated")        val rated: String?,
    @SerializedName("Released")     val released: String?,
    @SerializedName("Runtime")      val runtime: String?,
    @SerializedName("Genre")        val genre: String?,
    @SerializedName("Director")     val director: String?,
    @SerializedName("Writer")       val writer: String?,
    @SerializedName("Actors")       val actors: String?,
    @SerializedName("Plot")         val plot: String?,
    @SerializedName("Language")     val language: String?,
    @SerializedName("Country")      val country: String?,
    @SerializedName("Awards")       val awards: String?,
    @SerializedName("Poster")       val posterUrl: String?,
    @SerializedName("imdbRating")   val imdbRating: String?,
    @SerializedName("imdbVotes")    val imdbVotes: String?,
    @SerializedName("imdbID")       val imdbID: String,
    @SerializedName("Type")         val type: String?,
    @SerializedName("DVD")          val dvd: String?,
    @SerializedName("BoxOffice")    val boxOffice: String?,
    @SerializedName("Production")   val production: String?,
    @SerializedName("Website")      val website: String?,
    @SerializedName("Response")     val response: String,
    @SerializedName("Error")        val error: String?
) {
    fun toModel(): MovieDetailModel {
        return MovieDetailModel(
            title      = title,
            year       = year,
            rated      = rated,
            released   = released,
            runtime    = runtime,
            genre      = genre,
            director   = director,
            writer     = writer,
            actors     = actors,
            plot       = plot,
            language   = language,
            country    = country,
            awards     = awards,
            posterUrl  = posterUrl.orEmpty(),
            imdbRating = imdbRating,
            imdbVotes  = imdbVotes,
            imdbID     = imdbID,
            type       = type,
            boxOffice  = boxOffice
        )
    }
}

data class MovieDetailModel(
    val title: String,
    val year: String,
    val rated: String?,
    val released: String?,
    val runtime: String?,
    val genre: String?,
    val director: String?,
    val writer: String?,
    val actors: String?,
    val plot: String?,
    val language: String?,
    val country: String?,
    val awards: String?,
    val posterUrl: String,
    val imdbRating: String?,
    val imdbVotes: String?,
    val imdbID: String,
    val type: String?,
    val boxOffice: String?
)

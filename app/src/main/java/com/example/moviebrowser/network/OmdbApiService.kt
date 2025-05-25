package com.example.moviebrowser.network

import com.example.moviebrowser.model.MovieSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApiService {
    @GET("/")
    suspend fun searchMovies(
        @Query("s") query: String,
        @Query("apikey") apiKey: String = "188daf61"
    ): MovieSearchResponse

    companion object {
        const val BASE_URL = "https://www.omdbapi.com/"
    }
}
package com.example.moviebrowser.repository

import com.example.moviebrowser.model.MovieModel
import com.example.moviebrowser.network.OmdbApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieRepository {
    private val api: OmdbApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(OmdbApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(OmdbApiService::class.java)
    }

    suspend fun search(query: String): List<MovieModel> {
        val resp = api.searchMovies(query)
        if (resp.response == "True" && resp.results != null) {
            return resp.results.map { it.toModel() }
        } else {
            throw Exception(resp.error ?: "Unknown error")
        }
    }
}
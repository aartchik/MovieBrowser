package com.example.moviebrowser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebrowser.model.MovieDetailModel
import com.example.moviebrowser.model.MovieModel
import com.example.moviebrowser.repository.FavoritesRepository
import com.example.moviebrowser.repository.MovieRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val movies: List<MovieModel> = emptyList(),
    val allMovies: List<MovieModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val favorites: Set<String> = emptySet(),
    val selectedMovieDetail: MovieDetailModel? = null,
    val isDetailLoading: Boolean = false,
    val detailError: String? = null
)

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val movieRepo = MovieRepository()
    private val favRepo   = FavoritesRepository(application)

    private val _ui = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _ui

    private val movieMap: MutableMap<String, MovieModel> = mutableMapOf()

    init {
        viewModelScope.launch {
            favRepo.favoritesFlow.collect { favs ->
                _ui.update { it.copy(favorites = favs) }

                favs.forEach { id ->
                    if (!movieMap.containsKey(id)) {
                        try {
                            val detail: MovieDetailModel = movieRepo.getDetails(id)
                            val model = MovieModel(
                                title     = detail.title,
                                year      = detail.year,
                                imdbID    = detail.imdbID,
                                posterUrl = detail.posterUrl
                            )
                            movieMap[id] = model
                        } catch (e: Exception) {
                        }
                    }
                }

                _ui.update { state ->
                    state.copy(allMovies = movieMap.values.toList())
                }
            }
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            _ui.update { it.copy(isLoading = true, error = null) }
            try {
                val list = movieRepo.search(query)
                list.forEach { movie ->
                    movieMap[movie.imdbID] = movie
                }
                val all = movieMap.values.toList()

                _ui.update { state ->
                    state.copy(
                        movies = list,
                        allMovies = all,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _ui.update { state ->
                    state.copy(error = e.message, isLoading = false)
                }
            }
        }
    }

    fun toggleFavorite(movie: MovieModel) {
        viewModelScope.launch {
            if (_ui.value.favorites.contains(movie.imdbID)) {
                favRepo.removeFavorite(movie.imdbID)
            } else {
                favRepo.addFavorite(movie.imdbID)
            }
        }
    }

    fun loadMovieDetails(imdbId: String) {
        viewModelScope.launch {
            _ui.update { it.copy(isDetailLoading = true, detailError = null) }
            try {
                val detail = movieRepo.getDetails(imdbId)
                _ui.update { it.copy(
                    selectedMovieDetail = detail,
                    isDetailLoading     = false
                ) }
            } catch (e: Exception) {
                _ui.update { it.copy(detailError = e.message, isDetailLoading = false) }
            }
        }
    }

    fun clearSelectedDetail() {
        _ui.update { it.copy(selectedMovieDetail = null, detailError = null) }
    }
}

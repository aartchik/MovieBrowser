package com.example.moviebrowser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebrowser.model.MovieModel
import com.example.moviebrowser.repository.FavoritesRepository
import com.example.moviebrowser.repository.MovieRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class UiState(
    val movies: List<MovieModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val favorites: Set<String> = emptySet()
)

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val movieRepo = MovieRepository()
    private val favRepo   = FavoritesRepository(application)

    private val _ui = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _ui

    init {
        // Подписываемся на изменения избранного
        viewModelScope.launch {
            favRepo.favoritesFlow.collect { favs ->
                _ui.update { it.copy(favorites = favs) }
            }
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            _ui.update { it.copy(isLoading = true, error = null) }
            try {
                val list = movieRepo.search(query)
                _ui.update { it.copy(movies = list, isLoading = false) }
            } catch (e: Exception) {
                _ui.update { it.copy(error = e.message, isLoading = false) }
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
}

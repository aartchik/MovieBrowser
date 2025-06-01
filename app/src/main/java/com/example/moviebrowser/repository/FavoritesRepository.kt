package com.example.moviebrowser.repository

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavoritesRepository(context: Context) {

    private val prefs = context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)

    private val FAVORITES_KEY = "favorites"

    private val _favoritesFlow = MutableStateFlow(loadFavoritesFromPrefs())
    val favoritesFlow: StateFlow<Set<String>> = _favoritesFlow

    private fun loadFavoritesFromPrefs(): Set<String> {
        return prefs.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
    }

    suspend fun addFavorite(id: String) {
        val currentSet = loadFavoritesFromPrefs().toMutableSet()
        currentSet.add(id)
        prefs.edit {
            putStringSet(FAVORITES_KEY, currentSet)
        }
        _favoritesFlow.emit(currentSet)
    }

    suspend fun removeFavorite(id: String) {
        val currentSet = loadFavoritesFromPrefs().toMutableSet()
        currentSet.remove(id)
        prefs.edit {
            putStringSet(FAVORITES_KEY, currentSet)
        }
        _favoritesFlow.emit(currentSet)
    }
}

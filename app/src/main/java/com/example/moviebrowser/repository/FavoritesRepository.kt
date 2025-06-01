package com.example.moviebrowser.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(
    name = "favorites_prefs"
)

class FavoritesRepository(private val ctx: Context) {

    companion object {
        private val FAVORITES_KEY = preferencesKey<Set<String>>("favorites")

    }

    val favoritesFlow: Flow<Set<String>> =
        ctx.dataStore.data.map { prefs ->

            prefs[FAVORITES_KEY] ?: emptySet()
        }

    suspend fun addFavorite(id: String) {
        ctx.dataStore.edit { prefs ->
            val oldSet = prefs[FAVORITES_KEY] ?: emptySet()
            prefs[FAVORITES_KEY] = oldSet + id
        }
    }

    suspend fun removeFavorite(id: String) {
        ctx.dataStore.edit { prefs ->
            val oldSet = prefs[FAVORITES_KEY] ?: emptySet()
            prefs[FAVORITES_KEY] = oldSet - id
        }
    }
}

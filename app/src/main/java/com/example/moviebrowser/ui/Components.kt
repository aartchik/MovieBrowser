package com.example.moviebrowser.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.moviebrowser.model.MovieModel
import com.example.moviebrowser.viewmodel.MovieViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

object Components {

    @Composable
    fun MovieApp(vm: MovieViewModel) {
        val state by vm.uiState.collectAsState()
        var showFav by remember { mutableStateOf(false) }

        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Вкладки
            TabRow(selectedTabIndex = if (showFav) 1 else 0) {
                Tab(selected = !showFav, onClick = { showFav = false }) {
                    Text("Search")
                }
                Tab(selected = showFav, onClick = { showFav = true }) {
                    Text("Favorites")
                }
            }

            Spacer(Modifier.height(8.dp))

            if (!showFav) {
                SearchSection(vm)
            }

            Spacer(Modifier.height(8.dp))

            // Список: либо поиск, либо избранное
            val listToShow = if (showFav) {
                state.movies.filter { it.imdbID in state.favorites }
            } else {
                state.movies
            }

            when {
                state.isLoading && !showFav -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                showFav && listToShow.isEmpty() -> {
                    Text("No favorites yet.", color = Color.Gray)
                }
                state.error != null -> {
                    Text("Error: ${state.error}", color = Color.Red)
                }
                else -> {
                    LazyColumn {
                        items(listToShow) { movie ->
                            MovieItem(
                                movie = movie,
                                isFav = movie.imdbID in state.favorites,
                                onClick = { /* можно открыть детали */ },
                                onFavClick = { vm.toggleFavorite(movie) }
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SearchSection(vm: MovieViewModel) {
        var query by remember { mutableStateOf("") }
        Row {
            TextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search movies") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = { vm.searchMovies(query) },
                enabled = query.isNotBlank()
            ) {
                Text("Search")
            }
        }
    }

    @Composable
    fun MovieItem(
        movie: MovieModel,
        isFav: Boolean,
        onClick: () -> Unit,
        onFavClick: () -> Unit
    ) {
        Card(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { onClick() },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = movie.posterUrl,
                    contentDescription = movie.title,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text(movie.title, style = MaterialTheme.typography.titleMedium)
                    Text(movie.year, style = MaterialTheme.typography.bodySmall)
                }
                IconButton(onClick = onFavClick) {
                    if (isFav) {
                        Icon(Icons.Filled.Star, contentDescription = "Unfavorite", tint = Color.Yellow)
                    } else {
                        Icon(Icons.Outlined.Star, contentDescription = "Favorite")
                    }
                }
            }
        }
    }
}

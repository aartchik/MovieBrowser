package com.example.moviebrowser.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.moviebrowser.model.MovieModel
import com.example.moviebrowser.model.MovieDetailModel
import com.example.moviebrowser.viewmodel.MovieViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

object Components {

    @Composable
    fun MovieApp(vm: MovieViewModel) {
        val state by vm.uiState.collectAsState()

        var showFav by remember { mutableStateOf(false) }

        Box(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Вкладки «Search» / «Favorites»
                TabRow(selectedTabIndex = if (showFav) 1 else 0) {
                    Tab(selected = !showFav, onClick = { showFav = false }) {
                        Text("Search")
                    }
                    Tab(selected = showFav, onClick = { showFav = true }) {
                        Text("Favorites")
                    }
                }

                Spacer(Modifier.height(8.dp))

                // «Search» или пустой блок, если showFav = true
                if (!showFav) {
                    SearchSection(vm)
                    Spacer(Modifier.height(8.dp))
                }

                // Список (Search vs Favorites)
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
                                    movie    = movie,
                                    isFav    = movie.imdbID in state.favorites,
                                    onClick  = { vm.loadMovieDetails(movie.imdbID) },
                                    onFavClick = { vm.toggleFavorite(movie) }
                                )
                            }
                        }
                    }
                }
            }

            if (state.selectedMovieDetail != null || state.isDetailLoading || state.detailError != null) {
                MovieDetailDialog(
                    detail       = state.selectedMovieDetail,
                    isLoading    = state.isDetailLoading,
                    errorMessage = state.detailError,
                    onDismiss    = { vm.clearSelectedDetail() }
                )
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
                onClick = { vm.searchMovies(query.trim()) },
                enabled = query.trim().isNotBlank()
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


    @Composable
    private fun MovieDetailDialog(
        detail: MovieDetailModel?,
        isLoading: Boolean,
        errorMessage: String?,
        onDismiss: () -> Unit
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0x80000000))
                .clickable { onDismiss() }
        ) {
            if (isLoading) {
                // Центрируем индикатор загрузки
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else if (errorMessage != null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Card(
                        Modifier
                            .fillMaxWidth(0.8f)
                            .wrapContentHeight(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            Modifier
                                .padding(16.dp)
                                .clickable(enabled = false) { /* блокируем клики внутри */ },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Error:", fontWeight = FontWeight.Bold, color = Color.Red)
                            Spacer(Modifier.height(8.dp))
                            Text(errorMessage)
                            Spacer(Modifier.height(16.dp))
                            Button(onClick = onDismiss) {
                                Text("OK")
                            }
                        }
                    }
                }
            } else if (detail != null) {
                Card(
                    Modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight()
                        .align(Alignment.Center)
                        .clickable(enabled = false) { },
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = detail.title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = onDismiss) {
                                Icon(Icons.Filled.Close, contentDescription = "Close")
                            }
                        }
                        Spacer(Modifier.height(8.dp))

                        AsyncImage(
                            model = detail.posterUrl,
                            contentDescription = detail.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Year: ${detail.year} • Rating: ${detail.imdbRating ?: "N/A"} • ${detail.runtime ?: ""}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(8.dp))

                        detail.genre?.let {
                            Text("Genre: $it", style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.height(4.dp))
                        }

                        detail.plot?.let {
                            Text("Plot:", fontWeight = FontWeight.SemiBold)
                            Text(it, style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(8.dp))
                        }

                        detail.director?.let {
                            Text("Director: $it", style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(4.dp))
                        }
                        detail.writer?.let {
                            Text("Writer: $it", style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(4.dp))
                        }
                        detail.actors?.let {
                            Text("Actors: $it", style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(8.dp))
                        }

                        detail.country?.let {
                            Text("Country: $it", style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(4.dp))
                        }
                        detail.awards?.let {
                            Text("Awards: $it", style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(4.dp))
                        }
                        detail.boxOffice?.let {
                            Text("BoxOffice: $it", style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}

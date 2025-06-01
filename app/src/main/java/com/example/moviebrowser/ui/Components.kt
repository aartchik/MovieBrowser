package com.example.moviebrowser.ui

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.moviebrowser.model.MovieDetailModel
import com.example.moviebrowser.model.MovieModel
import com.example.moviebrowser.viewmodel.MovieViewModel

object Components {

    @Composable
    fun MovieApp(vm: MovieViewModel) {
        val state by vm.uiState.collectAsState()
        var showFav by remember { mutableStateOf(false) }

        Box(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                // 1) Вкладки Search / Favorites
                TabRow(selectedTabIndex = if (showFav) 1 else 0) {
                    Tab(selected = !showFav, onClick = { showFav = false }) {
                        Text("Search")
                    }
                    Tab(selected = showFav, onClick = { showFav = true }) {
                        Text("Favorites")
                    }
                }

                Spacer(Modifier.height(8.dp))

                // 2) Поле поиска (если мы не во вкладке Favorites)
                if (!showFav) {
                    SearchSection(vm)
                    Spacer(Modifier.height(8.dp))
                }

                // 3) Определяем, что показывать в списке:
                //    – если Favorites, то из state.allMovies, иначе — из state.movies
                val listToShow: List<MovieModel> = if (showFav) {
                    state.allMovies.filter { it.imdbID in state.favorites }
                } else {
                    state.movies
                }

                // 4) Логика отображения: загрузка / пусто / ошибка / сам список
                when {
                    state.isLoading && !showFav -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    showFav && listToShow.isEmpty() -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No favorites yet.", color = Color.Gray)
                        }
                    }
                    state.error != null -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Error: ${state.error}", color = MaterialTheme.colorScheme.error)
                        }
                    }
                    else -> {
                        LazyColumn {
                            items(listToShow) { movie ->
                                MovieItem(
                                    movie      = movie,
                                    isFav      = movie.imdbID in state.favorites,
                                    onClick    = { vm.loadMovieDetails(movie.imdbID) },
                                    onFavClick = { vm.toggleFavorite(movie) }
                                )
                            }
                        }
                    }
                }
            }

            // 5) Диалог деталей (как раньше) – плавно показываем через AnimatedVisibility
            AnimatedVisibility(
                visible = (state.selectedMovieDetail != null
                        || state.isDetailLoading
                        || state.detailError != null),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
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
        val context = LocalContext.current
        var query by remember { mutableStateOf("") }

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Search movies") },
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "Search Icon")
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            trailingIcon = {
                Button(
                    onClick = { vm.searchMovies(query.trim()) },
                    enabled = query.trim().isNotEmpty(),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text("Go")
                }
            }
        )
    }

    @Composable
    fun MovieItem(
        movie: MovieModel,
        isFav: Boolean,
        onClick: () -> Unit,
        onFavClick: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(MaterialTheme.shapes.medium)
                .clickable { onClick() },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = movie.posterUrl,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(MaterialTheme.shapes.small)
                )

                Spacer(Modifier.width(12.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = movie.year,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onFavClick) {
                    if (isFav) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = "Unfavorite",
                            tint = Color(0xFFFFD700)
                        )
                    } else {
                        Icon(
                            Icons.Outlined.Star,
                            contentDescription = "Favorite",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
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
        val context = LocalContext.current

        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0x80000000))
                .clickable { onDismiss() }
        ) {
            if (isLoading) {
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
                                .clickable(enabled = false) { },
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
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = MaterialTheme.shapes.large
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
                                modifier = Modifier.weight(1f),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            IconButton(onClick = {
                                val imdbUrl = "https://www.imdb.com/title/${detail.imdbID}"
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "${detail.title} (${detail.year})\nIMDb: $imdbUrl"
                                    )
                                }
                                context.startActivity(
                                    Intent.createChooser(
                                        shareIntent,
                                        "Share movie via"
                                    )
                                )
                            }) {
                                Icon(
                                    Icons.Filled.Share,
                                    contentDescription = "Share",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            IconButton(onClick = onDismiss) {
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = "Close",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        AsyncImage(
                            model = detail.posterUrl,
                            contentDescription = detail.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(MaterialTheme.shapes.medium)
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Year: ${detail.year} • Rating: ${detail.imdbRating
                                ?: "N/A"} • ${detail.runtime ?: ""}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(8.dp))

                        detail.genre?.let {
                            Text(
                                "Genre: $it",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(4.dp))
                        }

                        detail.plot?.let {
                            Text(
                                "Plot:",
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                it,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(8.dp))
                        }

                        detail.director?.let {
                            Text(
                                "Director: $it",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(4.dp))
                        }
                        detail.writer?.let {
                            Text(
                                "Writer: $it",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(4.dp))
                        }
                        detail.actors?.let {
                            Text(
                                "Actors: $it",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(8.dp))
                        }

                        detail.country?.let {
                            Text(
                                "Country: $it",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(4.dp))
                        }
                        detail.awards?.let {
                            Text(
                                "Awards: $it",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(4.dp))
                        }
                        detail.boxOffice?.let {
                            Text(
                                "BoxOffice: $it",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}

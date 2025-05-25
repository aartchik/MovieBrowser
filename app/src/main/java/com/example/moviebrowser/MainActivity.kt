package com.example.moviebrowser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviebrowser.ui.theme.MovieBrowserTheme
import com.example.moviebrowser.viewmodel.MovieViewModel
import com.example.moviebrowser.ui.Components.MovieApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieBrowserTheme {
                Surface {
                    val vm: MovieViewModel = viewModel()
                    MovieApp(vm)
                }
            }
        }
    }
}
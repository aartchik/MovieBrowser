package com.example.moviebrowser.model

import org.junit.Assert.assertEquals
import org.junit.Test


class MovieDtoTest {

    @Test
    fun `toModel should map all fields correctly`() {
        val dto = MovieDto(
            Title  = "Some Movie Title",
            Year   = "2020",
            imdbID = "tt1234567",
            Poster = "https://poster.url/some.jpg"
        )

        val model: MovieModel = dto.toModel()

        assertEquals("Some Movie Title", model.title)
        assertEquals("2020", model.year)
        assertEquals("tt1234567", model.imdbID)
        assertEquals("https://poster.url/some.jpg", model.posterUrl)
    }
}

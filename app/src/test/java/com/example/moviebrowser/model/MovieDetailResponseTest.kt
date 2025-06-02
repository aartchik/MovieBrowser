package com.example.moviebrowser.model

import org.junit.Assert.assertEquals
import org.junit.Test


class MovieDetailResponseTest {

    @Test
    fun `toModel should map all fields correctly`() {
        val dto = MovieDetailResponse(
            title      = "The Example Movie",
            year       = "2021",
            rated      = "PG-13",
            released   = "15 Jul 2021",
            runtime    = "120 min",
            genre      = "Drama, Action",
            director   = "Jane Doe",
            writer     = "John Smith",
            actors     = "Actor A, Actor B",
            plot       = "This is a test plot for Example Movie.",
            language   = "English",
            country    = "USA",
            awards     = "1 win & 2 nominations",
            posterUrl  = "https://example.com/poster.jpg",
            imdbRating = "7.5",
            imdbVotes  = "10,000",
            imdbID     = "tt1234567",
            type       = "movie",
            dvd        = "N/A",
            boxOffice  = "$100,000",
            production = "Example Prod",
            website    = "https://examplemovie.com",
            response   = "True",
            error      = null
        )

        val model: MovieDetailModel = dto.toModel()

        assertEquals("The Example Movie", model.title)
        assertEquals("2021", model.year)
        assertEquals("PG-13", model.rated)
        assertEquals("15 Jul 2021", model.released)
        assertEquals("120 min", model.runtime)
        assertEquals("Drama, Action", model.genre)
        assertEquals("Jane Doe", model.director)
        assertEquals("John Smith", model.writer)
        assertEquals("Actor A, Actor B", model.actors)
        assertEquals("This is a test plot for Example Movie.", model.plot)
        assertEquals("English", model.language)
        assertEquals("USA", model.country)
        assertEquals("1 win & 2 nominations", model.awards)
        assertEquals("https://example.com/poster.jpg", model.posterUrl)
        assertEquals("7.5", model.imdbRating)
        assertEquals("10,000", model.imdbVotes)
        assertEquals("tt1234567", model.imdbID)
        assertEquals("movie", model.type)
        assertEquals("$100,000", model.boxOffice)
    }
}

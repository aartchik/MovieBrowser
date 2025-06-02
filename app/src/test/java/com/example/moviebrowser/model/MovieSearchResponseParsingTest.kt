package com.example.moviebrowser.model

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test


class MovieSearchResponseParsingTest {

    private val sampleJson = """
        {
          "Search": [
            {
              "Title": "Titanic",
              "Year": "1997",
              "imdbID": "tt0120338",
              "Type": "movie",
              "Poster": "https://m.media-amazon.com/images/..."
            },
            {
              "Title": "Titanic II",
              "Year": "2010",
              "imdbID": "tt1640571",
              "Type": "movie",
              "Poster": "https://m.media-amazon.com/images/..."
            }
          ],
          "totalResults": "175",
          "Response": "True"
        }
    """.trimIndent()

    @Test
    fun `parse MovieSearchResponse from JSON`() {
        val gson = Gson()

        val response: MovieSearchResponse = gson.fromJson(sampleJson, MovieSearchResponse::class.java)

        assertEquals("True", response.response)
        assertNull(response.error)
        assertNotNull(response.results)
        assertEquals(2, response.results?.size)

        val first = response.results!![0]
        assertEquals("Titanic", first.Title)
        assertEquals("1997", first.Year)
        assertEquals("tt0120338", first.imdbID)
        assertEquals("https://m.media-amazon.com/images/...", first.Poster)

        val model: MovieModel = first.toModel()
        assertEquals("Titanic", model.title)
        assertEquals("1997", model.year)
        assertEquals("tt0120338", model.imdbID)
        assertEquals("https://m.media-amazon.com/images/...", model.posterUrl)
    }
}

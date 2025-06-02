package com.example.moviebrowser.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.robolectric.RobolectricTestRunner
import org.junit.runner.RunWith


@RunWith(RobolectricTestRunner::class)
class FavoritesRepositoryTest {

    private lateinit var context: Context
    private lateinit var favRepo: FavoritesRepository

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        favRepo = FavoritesRepository(context)


        val prefs = context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
        prefs.edit().clear().commit()
    }

    @Test
    fun `favoritesFlow initially emits empty set`() = runBlocking {
        val initialSet = favRepo.favoritesFlow.first()
        assertTrue(initialSet.isEmpty())
    }

    @Test
    fun `addFavorite should update SharedPreferences and flow`() = runBlocking {
        favRepo.addFavorite("tt0120338")

        val setAfterAdd = favRepo.favoritesFlow.first()
        assertTrue(setAfterAdd.contains("tt0120338"))
        assertEquals(1, setAfterAdd.size)

        favRepo.addFavorite("tt1375666")

        val setAfterTwoAdds = favRepo.favoritesFlow.first()
        assertTrue(setAfterTwoAdds.containsAll(listOf("tt0120338", "tt1375666")))
        assertEquals(2, setAfterTwoAdds.size)
    }

    @Test
    fun `removeFavorite should update SharedPreferences and flow`() = runBlocking {
        favRepo.addFavorite("tt0120338")
        favRepo.addFavorite("tt1375666")

        var currentSet = favRepo.favoritesFlow.first()
        assertEquals(2, currentSet.size)

        favRepo.removeFavorite("tt0120338")

        currentSet = favRepo.favoritesFlow.first()
        assertFalse(currentSet.contains("tt0120338"))
        assertTrue(currentSet.contains("tt1375666"))
        assertEquals(1, currentSet.size)

        favRepo.removeFavorite("tt1375666")
        currentSet = favRepo.favoritesFlow.first()
        assertTrue(currentSet.isEmpty())
    }
}

package com.example.cineaste

import com.example.cineaste.Data.Movie
import com.example.cineaste.Data.getFavoriteMovies
import junit.framework.TestCase.assertEquals
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasProperty
import org.hamcrest.Matchers.not
import org.junit.Test

class UnitTests {
    @Test
    fun testGetFavoriteMovies(){
        val movies = getFavoriteMovies()
        assertEquals(movies.size, 6)
        assertThat(
            movies,
            hasItem<Movie>(
                hasProperty(
                    "title",
                    `is`("Pulp Fiction")
                )
            )
        )
        assertThat(
            movies,
            not(
                hasItem<Movie>(
                    hasProperty(
                        "title",
                        `is`("Black Widow")
                    )
                )
            )
        )
    }
}
package com.example.cineaste

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyLeftOf
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyRightOf
import androidx.test.espresso.assertion.PositionAssertions.isLeftAlignedWith
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cineaste.Data.getFavoriteMovies
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IntentInstrumentedTest {

    private fun withImage(@DrawableRes id: Int) = object : TypeSafeMatcher<View>(){
        override fun describeTo(description: Description) {
            description.appendText("Drawable does not contain image with id: $id")
        }

        override fun matchesSafely(item: View): Boolean {
            val context:Context = item.context
            val bitmap: Bitmap? = context.getDrawable(id)?.toBitmap()
            return item is ImageView && item.drawable.toBitmap().sameAs(bitmap)
        }

    }

    @Test
    fun testDetailActivityInstantiation(){
        val pokreniDetalje = Intent(ApplicationProvider.getApplicationContext(),MovieDetailActivity::class.java)
        pokreniDetalje.putExtra("movie_title","Pulp Fiction")
        launchActivity<MovieDetailActivity>(pokreniDetalje)
        onView(withId(R.id.movie_title)).check(matches(withText("Pulp Fiction")))
        onView(withId(R.id.movie_genre)).check(matches(withText("crime")))
        onView(withId(R.id.movie_overview)).check(
            matches(
                withSubstring(
                    "pair of diner bandits"
                )
            )
        )
        onView(withId(R.id.movie_poster)).check(matches(withImage(R.drawable.crime)))

    }

    @Test
    fun testLinksIntent(){
        Intents.init()
        val pokreniDetalje = Intent(ApplicationProvider.getApplicationContext(),MovieDetailActivity::class.java)
        pokreniDetalje.putExtra("movie_title","Pulp Fiction")
        launchActivity<MovieDetailActivity>(pokreniDetalje)
        onView(withId(R.id.movie_website)).perform(click())
        Intents.intended(hasAction(Intent.ACTION_VIEW))
        Intents.release()
    }

    //ZSR
    @Test
    fun testLayoutDetailsActivity(){
        val pokreniDetalje = Intent(ApplicationProvider.getApplicationContext(),MovieDetailActivity::class.java)
        pokreniDetalje.putExtra("movie_title","Pulp Fiction")
        launchActivity<MovieDetailActivity>(pokreniDetalje)
        onView(withId(R.id.movie_poster)).check(isCompletelyLeftOf(withId(R.id.movie_title)))
        onView(withId(R.id.movie_release_date)).check(isCompletelyBelow(withId(R.id.movie_title)))
        onView(withId(R.id.movie_release_date)).check(
            isCompletelyRightOf(
                withId(
                    R.id.movie_poster
                )
            )
        )
        onView(withId(R.id.movie_genre)).check(isCompletelyBelow(withId(R.id.movie_release_date)))
        onView(withId(R.id.movie_genre)).check(isLeftAlignedWith(withId(R.id.movie_release_date)))
        onView(withId(R.id.movie_website)).check(isCompletelyBelow(withId(R.id.movie_poster)))
        onView(withId(R.id.movie_overview)).check(isCompletelyBelow(withId(R.id.movie_website))).check(
            isLeftAlignedWith(withId(R.id.movie_website))
        )
    }

    @Test
    fun testWebSearchAction(){
        Intents.init()
        val pokreniDetalje = Intent(ApplicationProvider.getApplicationContext(),MovieDetailActivity::class.java)
        pokreniDetalje.putExtra("movie_title","Pulp Fiction")
        launchActivity<MovieDetailActivity>(pokreniDetalje)
        onView(withId(R.id.movie_title)).perform(click())
        Intents.intended(hasExtra(SearchManager.QUERY, "Pulp Fiction" + " trailer"))
        Intents.release()
    }
    @Test
    fun testFavorites(){
        val pokreniApp = Intent(ApplicationProvider.getApplicationContext(),MainActivity::class.java)
        launchActivity<MainActivity>(pokreniApp)
        val lista = getFavoriteMovies()
        for(movie in lista)
            onView(withId(R.id.favoriteMovies)).perform(
                scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(withText(movie.title))
                )
            )
    }
    @Test
    fun testSEND() {
        val intent = Intent()
        intent.putExtra(Intent.EXTRA_TEXT, "Neki tekst")
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.`package` = "com.example.cineaste"
        launchActivity<MainActivity>(intent).use {
            onView(withId(R.id.searchText)).check(matches(withText("Neki tekst")))
        }
    }

}
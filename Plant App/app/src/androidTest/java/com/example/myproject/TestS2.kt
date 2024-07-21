package com.example.myproject

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.BitmapFactory
import android.provider.MediaStore
//ispravite paket tako da odgovara nazivu paketa kojeg imate u projektu
//ovdje mozete dodati i import klasa ako su u drugom paketu
//glavnu aktivnost imenujte tako da ima naziv MainActivity (ovo je defaultni naziv)
//svi id-evi i podaci koji se koriste u testu su iz postavke i takvi trebaju biti i u vasem projektu
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.hamcrest.number.OrderingComparison.greaterThan
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import androidx.test.espresso.assertion.ViewAssertions.matches
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.internal.matchers.TypeSafeMatcher

@RunWith(AndroidJUnit4::class)
class TestS2 {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(NovaBiljkaActivity::class.java)

    @get:Rule
    val activityRule = ActivityScenarioRule(NovaBiljkaActivity::class.java)

    @Test
    fun testValidacijaImeBiljke() {
        // Unosimo prazan naziv biljke
        onView(withId(R.id.nazivET)).perform(replaceText(""))

        // Klik na dugme za validaciju
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())

        // Provjeravamo da li se prikazuje odgovarajuća greška
        onView(withId(R.id.nazivET)).check(matches(hasErrorText("Naziv mora imati između 2 i 20 znakova")))
    }

    @Test
    fun testValidacijaPorodicaBiljke() {
        // Unosimo neispravnu porodicu biljke
        onView(withId(R.id.porodicaET)).perform(replaceText(""))

        // Klik na dugme za validaciju
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())

        // Provjeravamo da li se prikazuje odgovarajuća greška
        onView(withId(R.id.porodicaET)).check(matches(hasErrorText("Porodica mora imati između 2 i 20 znakova")))
    }

    @Test
    fun testValidacijaMedicinskoUpozorenjeBiljke() {
        // Unosimo prazno medicinsko upozorenje
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(replaceText(""))

        // Klik na dugme za validaciju
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())

        // Provjeravamo da li se prikazuje odgovarajuća greška
        onView(withId(R.id.medicinskoUpozorenjeET)).check(matches(hasErrorText("Medicinsko upozorenje mora imati između 2 i 20 znakova")))
    }

    @Test
    fun testValidacijaDuploJelo() {
        // Dodajemo dva ista jela u listu
        onView(withId(R.id.jeloET)).perform(replaceText("Grah"))
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajJeloBtn)).perform(click()) // Dodaj prvo jelo
        onView(withId(R.id.scrollView)).perform(swipeDown())
        onView(withId(R.id.jeloET)).perform(replaceText("grah"))
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajJeloBtn)).perform(click()) // Dodaj drugo jelo (ista kao prvo, ali malo slovo)

        // Klik na dugme za dodavanje biljke
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())

        // Provjeravamo da li se prikazuje odgovarajuća greška
        onView(withId(R.id.jeloET)).check(matches(hasErrorText("Ne možete dodati dva ista jela")))
    }

    @Test
    fun testValidacijaNitiJednoJeloUListi() {
        onView(withId(R.id.scrollView)).perform(swipeUp())
        // Klik na dugme za dodavanje biljke bez dodavanja jela u listu
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())

        // Provjeravamo da li se prikazuje odgovarajuća greška
        onView(withId(R.id.jeloET)).check(matches(hasErrorText("Dodajte barem jedno jelo")))
    }

    @Test
    fun testValidacijaNitiJednaIzabranaMedicinskaKorist() {
        // Klik na dugme za validaciju
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())

        // Provjeravamo da li se prikazuje odgovarajuća greška
        onView(withId(R.id.medicinskaKoristLVT)).check(matches(hasErrorText("Morate izabrati barem jednu medicinsku korist")))
    }

    @Test
    fun testValidacijaNitiJedanIzabranKlimatskiTip() {
        // Klik na dugme za validaciju
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())

        // Provjeravamo da li se prikazuje odgovarajuća greška
        onView(withId(R.id.klimatskiTipLVT)).check(matches(hasErrorText("Morate izabrati barem jedan klimatski tip")))
    }

    @Test
    fun testValidacijaNitiJedanIzabranZemljisniTip() {
        // Klik na dugme za validaciju
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())

        // Provjeravamo da li se prikazuje odgovarajuća greška
        onView(withId(R.id.zemljisniTipLVT)).check(matches(hasErrorText("Morate izabrati barem jedan zemljisni tip")))
    }

    @Test
    fun testValidacijaNijeIzabranProfilOkusa() {
        // Klik na dugme za validaciju
        onView(withId(R.id.scrollView)).perform(swipeUp())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())

        // Provjeravamo da li se prikazuje odgovarajuća greška
        onView(withId(R.id.profilOkusaLVT)).check(matches(hasErrorText("Morate izabrati profil okusa")))
    }
    private fun withImage(expectedResourceID: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>(){
            override fun matchesSafely(item: View?): Boolean{
                if (item is ImageView){
                    val expectedBitmap = BitmapFactory.decodeResource(item.resources, expectedResourceID)
                    val imageViewBitmap = (item as ImageView).drawable.toBitmap()
                    return imageViewBitmap.sameAs(expectedBitmap)
                }
                return false
            }

            override fun describeTo(description: Description?) {
                description?.appendText("with image drawable from resource id: ")
                description?.appendValue(expectedResourceID)
            }
        }
    }
    @Test
    fun testValidacijaOtvaranjeKamere(){
        Intents.init()
            val bitmapSlika = BitmapFactory.decodeResource(getInstrumentation().targetContext.resources, R.drawable.biljka)
            val rezData = Intent().apply {
                putExtra("data",bitmapSlika)
            }
            val rez = Instrumentation.ActivityResult(Activity.RESULT_OK, rezData)
            intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(rez)
            onView(withId(R.id.scrollView)).perform(swipeUp())
            onView((withId(R.id.uslikajBiljkuBtn))).perform(click())
            onView(withId(R.id.slikaIV)).check(matches(withImage(R.drawable.biljka)))
            Intents.release()
    }
}

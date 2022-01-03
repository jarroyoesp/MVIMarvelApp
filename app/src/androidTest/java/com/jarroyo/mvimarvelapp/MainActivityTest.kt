package com.jarroyo.mvimarvelapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import com.jarroyo.mvimarvelapp.presentation.main.activity.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.com.jarroyo.marvel.data.repository.FakeDataRepositoryImpl

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun GIVEN_data_in_repository_WHEN_listFragmentIsShown_THEN_show_data() {
        FakeDataRepositoryImpl.resultList = mockCharacterList
        hiltRule.inject()
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.nav_view)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_character_list_textinput_search)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_character_list_rv)).check(matches(isDisplayed()))

        FakeDataRepositoryImpl.resultList = mutableListOf()
    }

    @Test
    fun GIVEN_NO_data_in_repository_WHEN_listFragmentIsShown_THEN_show_empty_info() {
        hiltRule.inject()
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.nav_view)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_character_list_textinput_search)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_character_list_layout_empty)).check(matches(isDisplayed()))
    }


    @Test
    fun GIVEN_NO_favorites_WHEN_favoriteFragmentIsShown_THEN_show_empty_info() {
        hiltRule.inject()
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.navigation_favorite)).perform(click())

        onView(withId(R.id.nav_view)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_favorite_layout_no_content)).check(matches(isDisplayed()))
    }


    @Test
    fun GIVEN_favorites_WHEN_favoriteFragmentIsShown_THEN_show_rv() {
        FakeDataRepositoryImpl.favoriteList = mockCharacterList
        hiltRule.inject()
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.navigation_favorite)).perform(click())

        onView(withId(R.id.nav_view)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_favorite_list_rv)).check(matches(isDisplayed()))


        FakeDataRepositoryImpl.favoriteList = mutableListOf()
    }
}
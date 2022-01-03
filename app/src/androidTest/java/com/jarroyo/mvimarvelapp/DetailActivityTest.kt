package com.jarroyo.mvimarvelapp

import android.content.Intent
import android.os.Bundle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.runner.AndroidJUnit4
import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.presentation.detail.activity.DetailActivity
import com.jarroyo.mvimarvelapp.presentation.detail.activity.DetailActivity.Companion.ARG_ITEM
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.com.jarroyo.marvel.data.repository.FakeDataRepositoryImpl


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class DetailActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun before() {
        FakeDataRepositoryImpl.reset()
    }

    @Test
    fun GIVEN_data_detail_WHEN_showDetail_THEN_view_is_shown() {
        hiltRule.inject()

        val scenario = ActivityScenario.launch<DetailActivity>(createIntent())

        onView(withId(R.id.fragment_detail_toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.fragment_detail_tv_description)).check(matches(isDisplayed()))

    }

    @Test
    fun GIVEN_favorite_WHEN_showDetail_THEN_favorite_is_marked() {
        FakeDataRepositoryImpl.isFavorite = true
        hiltRule.inject()

        val scenario = ActivityScenario.launch<DetailActivity>(createIntent())

        onView(withId(R.id.fragment_detail_toolbar)).check(matches(isDisplayed()))
        onView(withTagValue(equalTo(R.drawable.ic_favorite))).check(matches(isDisplayed()))

    }

    @Test
    fun GIVEN_NOfavorite_WHEN_showDetail_THEN_favorite_is_NOTmarked() {
        FakeDataRepositoryImpl.isFavorite = false
        hiltRule.inject()

        val scenario = ActivityScenario.launch<DetailActivity>(createIntent())

        onView(withId(R.id.fragment_detail_toolbar)).check(matches(isDisplayed()))
        onView(withTagValue(equalTo(R.drawable.ic_favorite_unselected))).check(matches(isDisplayed()))

    }

    @Test
    fun GIVEN_noBundle_WHEN_showDetail_THEN_showError() {
        FakeDataRepositoryImpl.isFavorite = false
        hiltRule.inject()

        val scenario = ActivityScenario.launch(DetailActivity::class.java)

        onView(withId(R.id.activity_detail_layout_no_content)).check(matches(isDisplayed()))
    }

    private fun createIntent(): Intent {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            DetailActivity::class.java
        )
        val bundle = Bundle()
        bundle.putParcelable(ARG_ITEM, UiModel(id = 1, name = "name", description = "Description", imageHomeUrl = "image"))
        intent.putExtras(bundle)
        return intent
    }
}
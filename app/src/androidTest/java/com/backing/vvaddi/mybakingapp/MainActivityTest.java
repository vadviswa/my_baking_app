package com.backing.vvaddi.mybakingapp;


import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private static final String RECIPE_NAME_1 = "Nutella Pie";
    private static final String RECIPE_NAME_2 = "Brownies";
    private static final String RECIPE_NAME_3 = "Yellow Cake";
    private static final String RECIPE_NAME_4 = "Cheesecake";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void showRecipe() {
        onView(Matchers.allOf(
                withText(RECIPE_NAME_1),
                withId(R.id.recipe_title)))
                .check(matches(isDisplayed()));

        onView(Matchers.allOf(
                withText(RECIPE_NAME_2),
                withId(R.id.recipe_title)))
                .check(matches(isDisplayed()));

        onView(Matchers.allOf(
                withText(RECIPE_NAME_3),
                withId(R.id.recipe_title)))
                .check(matches(isDisplayed()));

        onView(Matchers.allOf(
                withText(RECIPE_NAME_4),
                withId(R.id.recipe_title)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void showReceipeDetails() {

        onView(Matchers.allOf(
                withText(RECIPE_NAME_2),
                withId(R.id.recipe_title)))
                .check(matches(isDisplayed()))
                .perform(ViewActions.click());

        onView(Matchers.allOf(withText("Ingredients"), withId(R.id.ingredients_title)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void showVideoReceipe() {
        onView(Matchers.allOf(
                withText(RECIPE_NAME_1),
                withId(R.id.recipe_title)))
                .check(matches(isDisplayed()))
                .perform(ViewActions.click());

        onView(Matchers.allOf(withText("Ingredients"),
                withId(R.id.ingredients_title)))
                .check(matches(isDisplayed()));

        onView(withId(R.id.ingredients_container)).perform(ViewActions.swipeUp());

        onView(Matchers.allOf(
                withId(R.id.shortDescription),
                withText("Prep the cookie crust.")))
                .perform(ViewActions.click());

        onView(Matchers.allOf(
                withId(R.id.video_short_description),
                withText("Prep the cookie crust.")))
                .check(matches(isDisplayed()));

        onView(withId(R.id.recipe_video)).check(matches(isDisplayed()));
    }


}

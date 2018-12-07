package com.backing.vvaddi.mybakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.backing.vvaddi.mybakingapp.MainActivity;
import com.backing.vvaddi.mybakingapp.R;
import com.backing.vvaddi.mybakingapp.model.Ingredient;
import com.backing.vvaddi.mybakingapp.model.Recipe;
import com.backing.vvaddi.mybakingapp.ui.fragment.RecipeDetailFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences prefs =
                context.getSharedPreferences("MyPref", 0);
        String name = prefs.getString("recipe_name", "");
        Set<String> strings = prefs.getStringSet("ingredients", new HashSet<String>());

        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);
        remoteView.setTextViewText(R.id.widget_recipe_name, name);
        remoteView.removeAllViews(R.id.widget_ingredients_container);
        populateIngredients(remoteView, strings, context);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteView);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        remoteView.setOnClickPendingIntent(R.id.widget_parent_container, pendingIntent);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private static void populateIngredients(RemoteViews containerView,
                                            Set<String> ingredients, Context context) {
        for (String fullDescription : ingredients) {
            RemoteViews mIngredientView = new RemoteViews(context.getPackageName(), R.layout.widget_ingredient_item);
            mIngredientView.setTextViewText(R.id.app_widget_ingredient_name, fullDescription);
            containerView.addView(R.id.widget_ingredients_container, mIngredientView);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


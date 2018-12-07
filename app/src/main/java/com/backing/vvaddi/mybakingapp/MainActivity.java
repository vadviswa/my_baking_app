package com.backing.vvaddi.mybakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.backing.vvaddi.mybakingapp.model.Ingredient;
import com.backing.vvaddi.mybakingapp.model.Recipe;
import com.backing.vvaddi.mybakingapp.ui.adapter.RecipeAdapter;
import com.backing.vvaddi.mybakingapp.ui.fragment.RecipeDetailFragment;
import com.backing.vvaddi.mybakingapp.ui.fragment.VideoFragment;
import com.backing.vvaddi.mybakingapp.widget.BakingWidgetProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.Preferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {

    public static final String RECIPE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static final String RECIPE_FRAGMENT_TAG = "recipeDetailFragment";
    public static final String VIDEO_FRAGMENT_TAG = "videoFragment";

    private Unbinder unbinder;
    private RequestQueue queue;
    private Recipe recipe;
    private LinearLayoutManager layoutManager;
    private final String RECYCLER_VIEW_POSITION = "recycler_position";
    private final String RECYCLER_VIEW_DATASET = "recycler_data";
    private boolean oncreate = false;

    @BindView(R.id.recipe_recylerview)
    RecyclerView recyclerView;

    private RecipeAdapter adapter;

    @BindView(R.id.loading_indicator)
    ProgressBar progressBar;

    @BindView(R.id.recipe_fragment)
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);
        unbinder = ButterKnife.bind(this);
        layoutManager = new GridLayoutManager(this, calculateNoOfColumns());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecipeAdapter(this, new ArrayList<Recipe>(), this);
        recyclerView.setAdapter(adapter);

        if (savedInstanceState != null && savedInstanceState.containsKey(RECYCLER_VIEW_DATASET)) {
            restorePreviousState(savedInstanceState);
        } else {
            retrieveRecipeMasterList();
        }
        oncreate = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.GONE);
    }

    private void retrieveRecipeMasterList() {

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RECIPE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        final List<Recipe> movieList = Recipe.parseJSON(response);
                        adapter.refresh(movieList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Fragment recipeDetail = getSupportFragmentManager().findFragmentByTag(RECIPE_FRAGMENT_TAG);
        Fragment videoFragment = getSupportFragmentManager().findFragmentByTag(VIDEO_FRAGMENT_TAG);
        if (recipeDetail != null && videoFragment == null) {
            recyclerView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (recipe == null) {
            recipe = adapter.getRecipes().get(0);
        }
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("recipe_name", recipe.getName());
        Set<String> ingredients = new HashSet<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            String fullDescription = ingredient.getQuantity() + " " + ingredient.getMeasure() + " " + ingredient.getIngredient() + "\n";
            ingredients.add(fullDescription);
        }
        editor.putStringSet("ingredients", ingredients);
        editor.commit();

        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), BakingWidgetProvider.class));
        BakingWidgetProvider myWidget = new BakingWidgetProvider();
        myWidget.onUpdate(this, AppWidgetManager.getInstance(this), ids);
    }

    @Override
    public void onItemClick(int index) {
        recipe = adapter.getRecipes().get(index);
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RecipeDetailFragment.RECIPE, recipe);
        recipeDetailFragment.setArguments(bundle);


        recyclerView.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.VISIBLE);

        if (getResources().getBoolean(R.bool.isLarge)) {

            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, recipeDetailFragment, RECIPE_FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();

            Bundle videoBundle = new Bundle();
            videoBundle.putParcelableArrayList(VideoFragment.STEP_VIDEO_RECEIPE, recipe.getSteps());
            videoBundle.putInt(VideoFragment.STEP_INDEX, 0);
            VideoFragment videoFragment = new VideoFragment();
            videoFragment.setArguments(videoBundle);
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_container, videoFragment, VIDEO_FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();

        } else {
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, recipeDetailFragment, RECIPE_FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void restorePreviousState(Bundle savedInstanceState) {
        // getting recyclerview position
        Parcelable mListState = savedInstanceState.getParcelable(RECYCLER_VIEW_POSITION);
        // getting recyclerview items
        ArrayList<Recipe> mDataset = savedInstanceState.getParcelableArrayList(RECYCLER_VIEW_DATASET);
        // Restoring adapter items
        adapter.refresh(mDataset);
        // Restoring recycler view position
        recyclerView.getLayoutManager().onRestoreInstanceState(mListState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        // save recyclerview position
        outState.putParcelable(RECYCLER_VIEW_POSITION, listState);
        // save recyclerview items
        outState.putParcelableArrayList(RECYCLER_VIEW_DATASET, adapter.getRecipes());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private int calculateNoOfColumns() {
        if (getResources().getBoolean(R.bool.isLarge))
            return 2;
        else
            return 1;
    }

}

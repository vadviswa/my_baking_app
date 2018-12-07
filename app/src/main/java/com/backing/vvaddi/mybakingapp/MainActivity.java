package com.backing.vvaddi.mybakingapp;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.backing.vvaddi.mybakingapp.model.Recipe;
import com.backing.vvaddi.mybakingapp.ui.adapter.RecipeAdapter;
import com.backing.vvaddi.mybakingapp.ui.fragment.RecipeDetailFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {


    private static final String RECIPE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private Unbinder unbinder;
    private RequestQueue queue;
    private LinearLayoutManager layoutManager;
    private final String RECYCLER_VIEW_POSITION = "recycler_position";
    private final String RECYCLER_VIEW_DATASET = "recycler_data";

    @BindView(R.id.recipe_recylerview)
    RecyclerView recyclerView;

    private RecipeAdapter adapter;

    @BindView(R.id.loading_indicator)
    ProgressBar progressBar;

    @BindView(R.id.fragment_container)
    FrameLayout frameLayout;


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
    public void onItemClick(int index) {
        final Recipe recipe = adapter.getRecipes().get(index);
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RecipeDetailFragment.RECIPE, recipe);
        recipeDetailFragment.setArguments(bundle);

        recyclerView.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, recipeDetailFragment, "recipeDetailFragment")
                .addToBackStack(null)
                .commit();
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

    public void restorePreviousState(Bundle savedInstanceState) {
        // getting recyclerview position
        Parcelable listState = savedInstanceState.getParcelable(RECYCLER_VIEW_POSITION);
        // getting recyclerview items
        ArrayList<Recipe> dataset = savedInstanceState.getParcelableArrayList(RECYCLER_VIEW_DATASET);
        // Restoring adapter items
        adapter.refresh(dataset);
        // Restoring recycler view position
        recyclerView.getLayoutManager().onRestoreInstanceState(listState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private int calculateNoOfColumns() {
        if (getResources().getBoolean(R.bool.isLarge))
            return 4;
        else
            return 1;
    }

}

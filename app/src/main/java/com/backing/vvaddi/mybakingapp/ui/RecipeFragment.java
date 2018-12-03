package com.backing.vvaddi.mybakingapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.backing.vvaddi.mybakingapp.R;
import com.backing.vvaddi.mybakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeFragment extends Fragment implements RecipeAdapter.ListItemClickListener {

    private static final String RECIPE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private Unbinder unbinder;
    private RequestQueue queue;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.recipe_recylerview)
    RecyclerView recyclerView;

    private RecipeAdapter adapter;

    @BindView(R.id.loading_indicator)
    ProgressBar progressBar;

    public RecipeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(this.getActivity());
        View fragmentView = inflater.inflate(R.layout.recipe_fragment, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);
        retrieveRecipeMasterList();
        return fragmentView;
    }


    @Override
    public void onResume() {
        super.onResume();

        layoutManager = new GridLayoutManager(this.getContext(), calculateNoOfColumns());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecipeAdapter(this.getContext(), new ArrayList<Recipe>(), this);
        recyclerView.setAdapter(adapter);

        retrieveRecipeMasterList();
    }

    private void retrieveRecipeMasterList() {

        progressBar.setVisibility(View.VISIBLE);

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

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, recipeDetailFragment, "recipeDetailFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private int calculateNoOfColumns() {
        if (getResources().getBoolean(R.bool.isLarge))
            return 4;
        else
            return 1;
    }
}

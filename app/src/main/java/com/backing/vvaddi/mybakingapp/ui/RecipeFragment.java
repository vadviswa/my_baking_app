package com.backing.vvaddi.mybakingapp.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.backing.vvaddi.mybakingapp.model.Recipe;
import com.backingr.vvaddi.mybakingapp.R;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private RequestQueue queue;

    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(this.getActivity());
        createPopularMoviesQuery();
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    private void createPopularMoviesQuery() {
        final String popularMoviesUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

        //progressBar.setVisibility(View.VISIBLE);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, popularMoviesUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String moviesResponse) {
                        final List<Recipe> movieList = Recipe.parseJSON(moviesResponse);
/*                        final Hashtable<String, PopularMovie> movieMap = new Hashtable<>();
                        progressBar.setVisibility(View.INVISIBLE);
                        errorTextView.setVisibility(View.GONE);
                        if (movieList != null && !movieList.isEmpty()) {
                            adapter.refreshPopularMovies(movieList);
                        } else {
                            errorTextView.setVisibility(View.VISIBLE);
                        }*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
/*                        progressBar.setVisibility(View.INVISIBLE);
                        errorTextView.setVisibility(View.VISIBLE);*/
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}

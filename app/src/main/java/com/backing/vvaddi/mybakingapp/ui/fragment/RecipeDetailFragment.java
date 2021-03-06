package com.backing.vvaddi.mybakingapp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.backing.vvaddi.mybakingapp.MainActivity;
import com.backing.vvaddi.mybakingapp.R;
import com.backing.vvaddi.mybakingapp.model.Recipe;
import com.backing.vvaddi.mybakingapp.model.Step;
import com.backing.vvaddi.mybakingapp.ui.adapter.IngredientAdapter;
import com.backing.vvaddi.mybakingapp.ui.adapter.StepsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeDetailFragment extends Fragment implements StepsAdapter.VideoClickListener {

    public static final String RECIPE = "receipe";
    private Unbinder unbinder;
    private Recipe recipe = null;

    @BindView(R.id.ingredients)
    RecyclerView ingredientsRecyclerview;

    @BindView(R.id.steps_recyclerview)
    RecyclerView stepsRecyclerView;

    IngredientAdapter ingredientAdapter;
    StepsAdapter stepsAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            recipe = savedInstanceState.getParcelable(RECIPE);
        else if (getArguments() != null)
            recipe = getArguments().getParcelable(RECIPE);
        if (recipe == null)
            return;
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(recipe.getName());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);

        ingredientsRecyclerview.setLayoutManager(linearLayoutManager);
        ingredientAdapter = new IngredientAdapter(getActivity().getApplicationContext(), recipe.getIngredients());
        ingredientsRecyclerview.setAdapter(ingredientAdapter);

        LinearLayoutManager linearLayout = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        stepsRecyclerView.setLayoutManager(linearLayout);
        stepsAdapter = new StepsAdapter(getActivity().getApplicationContext(), recipe.getSteps(), this);
        stepsRecyclerView.setAdapter(stepsAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_detail_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("receipe", recipe);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(getResources().getString(R.string.bakingTime));
        unbinder.unbind();
    }

    @Override
    public void onItemClick(int index) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(VideoFragment.STEP_VIDEO_RECEIPE, stepsAdapter.getSteps());
        bundle.putInt(VideoFragment.STEP_INDEX, index);
        VideoFragment videoFragment = new VideoFragment();
        videoFragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(MainActivity.VIDEO_FRAGMENT_TAG);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (getResources().getBoolean(R.bool.isLarge)) {
            if (fragment != null) {
                fragmentTransaction.remove(fragment);
            }
            fragmentTransaction
                    .add(R.id.fragment_detail_container, videoFragment, MainActivity.VIDEO_FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
        } else {
            fragmentTransaction
                    .replace(R.id.fragment_container, videoFragment, MainActivity.VIDEO_FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
        }
    }
}

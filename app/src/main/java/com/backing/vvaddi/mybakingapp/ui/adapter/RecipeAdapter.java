package com.backing.vvaddi.mybakingapp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.backing.vvaddi.mybakingapp.R;
import com.backing.vvaddi.mybakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private ArrayList<Recipe> recipes;
    private Context context;
    private final ListItemClickListener listener;

    public interface ListItemClickListener {
        void onItemClick(int index);
    }

    public RecipeAdapter(@NonNull Context context, @NonNull ArrayList<Recipe> recipes, @NonNull ListItemClickListener listener) {
        this.context = context;
        this.recipes = recipes;
        this.listener = listener;
    }

    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.receipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        final Recipe recipe = recipes.get(position);

        holder.title.setText(recipe.getName());
        holder.servings.setText(context.getResources().getString(R.string.serves) + " " + recipe.getServings());
        if (TextUtils.isEmpty(recipe.getImage())) {
            Picasso.with(context).load(R.drawable.receipe_placeholder).into(holder.imageView);
        } else {
            Picasso.with(context).load(recipe.getId()).into(holder.imageView);
        }
    }

    public void refresh(List<Recipe> recipeList) {
        recipes.clear();
        recipes.addAll(recipeList);
        notifyDataSetChanged();
    }

    /**
     * @return Return the size of your dataset (invoked by the layout manager)
     */
    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_title)
        TextView title;

        @BindView(R.id.recipe_servings)
        TextView servings;

        @BindView(R.id.recipe_image)
        ImageView imageView;


        public RecipeViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            listener.onItemClick(clickedPosition);
        }
    }
}

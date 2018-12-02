package com.backing.vvaddi.mybakingapp.ui;

import com.backing.vvaddi.mybakingapp.R;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.backing.vvaddi.mybakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdaptor extends RecyclerView.Adapter<RecipeAdaptor.RecipeViewHolder> {

    private ArrayList<Recipe> recipes;
    private Context context;
    private final ListItemClickListener listener;

    public interface ListItemClickListener {
        void onItemClick(int index);
    }

    public RecipeAdaptor(@NonNull Context context, @NonNull ArrayList<Recipe> recipes, @NonNull ListItemClickListener listener) {
        this.context = context;
        this.recipes = recipes;
        this.listener = listener;
    }

    @Override
    public RecipeAdaptor.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.receipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        final Recipe recipe = recipes.get(position);

        holder.title.setText(recipe.getName());
        holder.servings.setText(context.getResources().getString(R.string.serves) + " " + recipe.getServings());
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

        private TextView title;
        private TextView servings;

        public RecipeViewHolder(View view) {
            super(view);
            title = itemView.findViewById(R.id.recipe_title);
            servings = itemView.findViewById(R.id.recipe_servings);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            listener.onItemClick(clickedPosition);
        }
    }
}

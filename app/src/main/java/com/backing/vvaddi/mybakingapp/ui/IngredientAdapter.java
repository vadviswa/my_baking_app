package com.backing.vvaddi.mybakingapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.backing.vvaddi.mybakingapp.R;
import com.backing.vvaddi.mybakingapp.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<Ingredient> ingredients;
    private Context context;


    public IngredientAdapter(@NonNull Context context, @NonNull List<Ingredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.ingredients, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        final Ingredient ingredient = ingredients.get(position);

        holder.quantity.setText(ingredient.getQuantity());
        holder.measure.setText(ingredient.getMeasure());
        holder.description.setText(ingredient.getIngredient());
    }

    /**
     * @return Return the size of your dataset (invoked by the layout manager)
     */
    @Override
    public int getItemCount() {
        return ingredients.size();
    }


    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.quantity)
        TextView quantity;

        @BindView(R.id.measure)
        TextView measure;

        @BindView(R.id.description)
        TextView description;

        public IngredientViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

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
import com.backing.vvaddi.mybakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {

    private List<Step> steps;
    private Context context;


    public StepsAdapter(@NonNull Context context, @NonNull List<Step> steps) {
        this.context = context;
        this.steps = steps;
    }

    @Override
    public StepsAdapter.StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.steps, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        final Step step = steps.get(position);

        holder.shortDescription.setText(step.getShortDescription());
        holder.description.setText(step.getDescription());
    }


    /**
     * @return Return the size of your dataset (invoked by the layout manager)
     */
    @Override
    public int getItemCount() {
        return steps.size();
    }


    public class StepViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.shortDescription)
        TextView shortDescription;

        @BindView(R.id.description)
        TextView description;

        public StepViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

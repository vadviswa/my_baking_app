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
import com.backing.vvaddi.mybakingapp.model.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {

    private ArrayList<Step> steps;
    private Context context;
    private VideoClickListener videoClickListener;

    public interface VideoClickListener {
        void onItemClick(int index);
    }

    public StepsAdapter(@NonNull Context context, @NonNull ArrayList<Step> steps, @NonNull VideoClickListener clickListener) {
        this.context = context;
        this.steps = steps;
        this.videoClickListener = clickListener;
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
        if (TextUtils.isEmpty(step.getVideoURL())) {
            holder.thumbNailURL.setVisibility(View.GONE);
        } else {
            if (!TextUtils.isEmpty(step.getThumbnailURL()))
                Picasso.with(context).load(step.getThumbnailURL()).into(holder.thumbNailURL);
        }
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    /**
     * @return Return the size of your dataset (invoked by the layout manager)
     */
    @Override
    public int getItemCount() {
        return steps.size();
    }


    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.shortDescription)
        TextView shortDescription;

        @BindView(R.id.description)
        TextView description;

        @BindView(R.id.thumbNail)
        ImageView thumbNailURL;

        public StepViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            thumbNailURL.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            videoClickListener.onItemClick(clickedPosition);
        }
    }
}

package com.backing.vvaddi.mybakingapp.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.backing.vvaddi.mybakingapp.MainActivity;
import com.backing.vvaddi.mybakingapp.R;
import com.backing.vvaddi.mybakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class VideoFragment extends Fragment implements View.OnClickListener {

    public static final String STEP_VIDEO_RECEIPE = "stepsVideo";
    public static final String STEP_INDEX = "stepIndex";

    private Unbinder unbinder;
    private ArrayList<Step> steps;
    private int stepIndex;
    private SimpleExoPlayer player;

    @BindView(R.id.recipe_video)
    SimpleExoPlayerView exoPlayer;

    @BindView(R.id.video_short_description)
    TextView shortDescription;

    @BindView(R.id.video_description)
    TextView description;

    @BindView(R.id.next_step)
    Button nextStepButton;

    @BindView(R.id.previous_step)
    Button previousStepButton;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelable(STEP_VIDEO_RECEIPE);
            stepIndex = savedInstanceState.getInt(STEP_INDEX);
        } else if (getArguments() != null) {
            steps = getArguments().getParcelableArrayList(STEP_VIDEO_RECEIPE);
            stepIndex = getArguments().getInt(STEP_INDEX, -1);
        }

        if (steps == null)
            return;
        if (stepIndex == -1 || stepIndex >= steps.size())
            stepIndex = 0;

        initializePlayer();
        nextStepButton.setOnClickListener(this);
        previousStepButton.setOnClickListener(this);
        if (steps != null) {
            Step step = steps.get(stepIndex);
            shortDescription.setText(step.getShortDescription());
            description.setText(step.getDescription());
            if (TextUtils.isEmpty(step.getVideoURL())) {
                exoPlayer.setVisibility(View.GONE);
            } else {
                exoPlayer.setVisibility(View.VISIBLE);
                setVideoReceipe(Uri.parse(step.getVideoURL()));
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receipe_video, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (steps != null) {
            Step step = steps.get(stepIndex);
            shortDescription.setText(step.getShortDescription());
            description.setText(step.getDescription());
            setVideoReceipe(Uri.parse(step.getVideoURL()));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        releasePlayer();
    }

    private void initializePlayer() {
        if (player == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
        }
    }

    private void setVideoReceipe(Uri mediaUri) {
        exoPlayer.setPlayer(player);
        Context context = getActivity();
        String userAgent = Util.getUserAgent(context, "ClassicalMusicQuiz");
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                context, userAgent), new DefaultExtractorsFactory(), null, null);
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
    }

    private void releasePlayer() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(VideoFragment.STEP_VIDEO_RECEIPE, steps);
        if (v.getId() == R.id.next_step)
            bundle.putInt(VideoFragment.STEP_INDEX, stepIndex + 1);
        else if (v.getId() == R.id.previous_step)
            bundle.putInt(VideoFragment.STEP_INDEX, stepIndex - 1);

        VideoFragment videoFragment = new VideoFragment();
        videoFragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment existingFragment = fragmentManager.findFragmentByTag(MainActivity.VIDEO_FRAGMENT_TAG);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (existingFragment != null) {
            fragmentTransaction.remove(existingFragment);
        }

        if (getResources().getBoolean(R.bool.isLarge)) {
            fragmentTransaction
                    .add(R.id.fragment_detail_container, videoFragment, MainActivity.VIDEO_FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
        } else {
            fragmentTransaction
                    .add(R.id.fragment_container, videoFragment, MainActivity.VIDEO_FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
        }
    }
}

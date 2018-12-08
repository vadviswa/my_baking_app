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
    public static final String PLAYER_POSITION = "player_position";

    private Unbinder unbinder;
    private ArrayList<Step> steps;
    private int stepIndex;
    private SimpleExoPlayer player;
    private long position;

    @BindView(R.id.recipe_video)
    SimpleExoPlayerView exoPlayer;

    @BindView(R.id.video_short_description)
    TextView shortDescription;

    @BindView(R.id.video_description)
    TextView description;

    @BindView(R.id.next_step)
    @Nullable
    Button nextStepButton;

    @BindView(R.id.previous_step)
    @Nullable
    Button previousStepButton;


    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        if (getArguments() != null) {
            steps = getArguments().getParcelableArrayList(STEP_VIDEO_RECEIPE);
            stepIndex = getArguments().getInt(STEP_INDEX, -1);
            position = 0;
        }
        if (steps == null)
            return;
        if (stepIndex == -1 || stepIndex >= steps.size())
            stepIndex = 0;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receipe_video, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelableArrayList(STEP_VIDEO_RECEIPE);
            stepIndex = savedInstanceState.getInt(STEP_INDEX);
            position = savedInstanceState.getLong(PLAYER_POSITION, 0);
        }
        initializePlayer();
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
        if (nextStepButton != null)
            nextStepButton.setOnClickListener(this);
        if (previousStepButton != null)
            previousStepButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STEP_VIDEO_RECEIPE, steps);
        outState.putInt(STEP_INDEX, stepIndex);
        outState.putLong(PLAYER_POSITION, player.getCurrentPosition());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void initializePlayer() {
        if (player == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            player.seekTo(position);
        }
    }

    private void hideSystemUi() {
        exoPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void setVideoReceipe(Uri mediaUri) {
        exoPlayer.setPlayer(player);
        Context context = getActivity();
        String userAgent = Util.getUserAgent(context, "BackingApp");
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

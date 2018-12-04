package com.backing.vvaddi.mybakingapp.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class VideoFragment extends Fragment {

    public static final String VIDEO_RECEIPE = "video_receipe";

    private Unbinder unbinder;
    private Step step;
    private SimpleExoPlayer player;

    @BindView(R.id.recipe_video)
    SimpleExoPlayerView exoPlayer;

    @BindView(R.id.video_short_description)
    TextView shortDescription;

    @BindView(R.id.video_description)
    TextView description;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            step = savedInstanceState.getParcelable(VIDEO_RECEIPE);
        else if (getArguments() != null)
            step = getArguments().getParcelable(VIDEO_RECEIPE);
        initializePlayer();
        if (step != null) {
            shortDescription.setText(step.getShortDescription());
            description.setText(step.getDescription());
            setVideoReceipe(Uri.parse(step.getVideoURL()));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receipe_video, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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

}

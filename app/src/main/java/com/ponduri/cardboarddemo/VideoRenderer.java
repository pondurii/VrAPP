package com.ponduri.cardboarddemo;

/**
 * Created by leela.ponduri on 4/25/2016.
 */
import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.util.Log;

import org.rajawali3d.cardboard.RajawaliCardboardRenderer;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.StreamingTexture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;

public class VideoRenderer extends RajawaliCardboardRenderer  {

    // Context mContext;
    VideoScreen  videoScreen;
    String videopath;
    private MediaPlayer mMediaPlayer;
    private StreamingTexture mVideoTexture;

    public VideoRenderer(Activity activity) {
        super(activity.getApplicationContext());
        videoScreen = (VideoScreen) activity;
    }

    @Override
    protected void initScene() {

        //mMediaPlayer = MediaPlayer.create(getContext(),
        //       R.raw.video);
       /* File file = new File(videopath);
        Uri uri = Uri.fromFile(file);
        Log.d("bis", "uri= " + uri.toString());*/
        mMediaPlayer = MediaPlayer.create(getContext(),  R.raw.video);

        mMediaPlayer.setLooping(true);

        mVideoTexture = new StreamingTexture("sintelTrailer", mMediaPlayer);
        Material material = new Material();
        material.setColorInfluence(0);
        try {
            material.addTexture(mVideoTexture);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }

        Sphere sphere = new Sphere(50, 64, 32);
        sphere.setScaleX(-1);
        sphere.setMaterial(material);

        getCurrentScene().addChild(sphere);

        getCurrentCamera().setPosition(Vector3.ZERO);

        getCurrentCamera().setFieldOfView(75);

        mMediaPlayer.start();

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("bis", "video completed");
                mp.stop();
              //  mainActivity.finish();
            }
        });
        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                Log.d("bis", "video seek completed");
                mp.stop();
            //    mainActivity.finish();
            }
        });

    }

    @Override
    protected void onRender(long ellapsedRealtime, double deltaTime) {
        super.onRender(ellapsedRealtime, deltaTime);
        mVideoTexture.update();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMediaPlayer != null)
            mMediaPlayer.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMediaPlayer != null)
            mMediaPlayer.start();
    }

    @Override
    public void onRenderSurfaceDestroyed(SurfaceTexture surfaceTexture) {
        super.onRenderSurfaceDestroyed(surfaceTexture);
        mMediaPlayer.stop();
        mMediaPlayer.release();
    }
}

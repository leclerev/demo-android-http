package com.structit.apiclient;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.structit.apiclient.data.PlayItem;
import com.structit.apiclient.data.access.DataHandler;
import com.structit.apiclient.service.ApiService;
import com.structit.apiclient.service.MusicService;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private DataHandler mDataHandler;
    private MediaPlayer mMediaPlayer;


    private String mPlayListName;
    private static int mPlayListId = -1;
    private int lastPlayedID = 10;

    public Button mPlayButton = null;
    public Button mStopButton = null;
    public Intent musicIntent;
    private int playId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(LOG_TAG, "Creating...");

        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String url = intent.getStringExtra("url");

        TextView textviewApiId = (TextView)findViewById(R.id.api_id);
        textviewApiId.setText(id);

        TextView textviewApiUrl = (TextView)findViewById(R.id.api_url);
        textviewApiUrl.setText(url);

        mDataHandler = new DataHandler(this);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mPlayListName = intent.getStringExtra("playlist");
        mPlayListId = intent.getIntExtra("playlistid", -1);
    }

    @Override
    protected void onStart() {
        Log.i(LOG_TAG, "Starting...");

        super.onStart();

        if(mPlayListId > -1) {
            mDataHandler.open();
            ArrayList<PlayItem> playList = mDataHandler.getPlayList();
            mDataHandler.close();

            LinearLayout playListLayout = (LinearLayout) findViewById(R.id.playListLinearLayout);
            playListLayout.removeAllViews();

            if (!playList.isEmpty()) {
                LayoutInflater inflater = this.getLayoutInflater();

                for(PlayItem item:playList) {
                    // ActivityItem Layout
                    RelativeLayout playItemLayout =
                            (RelativeLayout) inflater.inflate(R.layout.play_item, null);
                    playListLayout.addView(playItemLayout);

                    TextView playNameTextView =
                            (TextView) playItemLayout.findViewById(R.id.playName);
                    playNameTextView.setText(item.getName());

                    TextView playAuthorTextView =
                            (TextView) playItemLayout.findViewById(R.id.playAuthor);
                    playAuthorTextView.setText(item.getAuthor());

                    TextView playRecordTextView =
                            (TextView) playItemLayout.findViewById(R.id.playRecord);
                    playRecordTextView.setText(item.getRecord());

                    PlayOnClickListener listener = new PlayOnClickListener(this, item.getId());
                    playItemLayout.setOnClickListener(listener);

                    //mPlayButton.setOnClickListener(this );
                    //mStopButton.setOnClickListener(this );

                    musicIntent = new Intent(this, MusicService.class);

                }

                LinearLayout buttons = (LinearLayout) findViewById(R.id.id_control_buttons);
                mPlayButton= (Button) buttons.findViewById(R.id.id_play_button);
                mStopButton = (Button) buttons.findViewById(R.id.id_stop_button);

                mPlayButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playId = lastPlayedID;
                        musicIntent.putExtra("playId", playId);
                        startService(musicIntent);
                        finish();
                    }
                });

                mStopButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stopService(musicIntent);
                        finish();
                    }
                });
            } // Else do nothing
        } else {
            Intent intent = new Intent(this, ApiService.class);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Intent intent = new Intent(this, ApiService.class);
        stopService(intent);
    }

    /*public void play(int playId) {
        this.mDataHandler.open();
        String filename = this.mDataHandler.getPlayFile(playId);
        this.mDataHandler.close();

        if(filename.length() > 0) {
            try {
                File file = new File(getFilesDir(), filename);
                this.mMediaPlayer.setDataSource(getApplicationContext(),
                        Uri.fromFile(file));
                this.mMediaPlayer.prepare();
                this.mMediaPlayer.start();
            } catch (Exception ex) {
                Log.e(LOG_TAG, "Unable to play sound");
            }
        } else {
            Log.d(LOG_TAG, "No file found for play: " + playId);
        }
    }*/

    @Override
    public void onClick(View view ) {
        Log.i(LOG_TAG, "itrm id =" + playId );




     /*   if (view == mPlayButton)
        {
            startService(musicIntent);
            finish();
        } else if (view  == mStopButton) {
            stopService(musicIntent);
            finish();
        }*/

    }
}

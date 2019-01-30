package com.example.attendance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity {
    Button pause,prev,next;
    SeekBar seek;
     TextView label;
     static MediaPlayer mymp;
     int position;
     ArrayList<File> mysongs;
     Thread updateseek;
     String sname;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
           if(item.getItemId() == R.id.home){
               onBackPressed();
           }


        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("New Api")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
       pause=(Button)findViewById(R.id.pause);
       prev=(Button)findViewById(R.id.prev);
       next=(Button)findViewById(R.id.next);
       label=(TextView)findViewById(R.id.label);
       seek=(SeekBar)findViewById(R.id.seek);
        getSupportActionBar().setTitle("Now Playing");

       getSupportActionBar().setDisplayShowHomeEnabled(true);



        updateseek = new Thread() {
            @Override
            public void run() {
                int runtime = mymp.getDuration();
                int currentPosition = 0;
                int adv = 0;
                while ((adv = ((adv = runtime - currentPosition) < 500)?adv:500) > 2) {
                    try {
                        currentPosition = mymp.getCurrentPosition();
                        if (seek != null) {
                            seek.setProgress(currentPosition);
                        }
                        sleep(adv);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        seek.setProgress(runtime);
                        break;
                    }

                }
            }
        };

        if(mymp!=null){
           mymp.stop();
           mymp.release();
       }
        Intent intent=getIntent();
         Bundle bundle=intent.getExtras();

         mysongs=(ArrayList) bundle.getParcelableArrayList("songs");
         sname=mysongs.get(position).getName().toString();
         String songname=intent.getStringExtra("song name");
         label.setText(songname);
         label.setSelected(true);


         position=bundle.getInt("pos",0);
         Uri u=Uri.parse(mysongs.get(position).toString());
         mymp=MediaPlayer.create(getApplicationContext(),u);
         mymp.start();
         seek.setMax(mymp.getDuration());
         updateseek.start();
         seek.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
         seek.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);

         seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
             @Override
             public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

             }

             @Override
             public void onStartTrackingTouch(SeekBar seekBar) {

             }

             @Override
             public void onStopTrackingTouch(SeekBar seekBar) {
                   mymp.seekTo(seekBar.getProgress());
             }
         });



    }
    public void pause(View v){
       seek.setMax(mymp.getDuration());
       if(mymp.isPlaying()){
           pause.setBackgroundResource(R.drawable.playicon);
           mymp.pause();
       }
       else
       {
           pause.setBackgroundResource(R.drawable.pauseicon);
           mymp.start();
       }

    }
    public  void next(View v){
        mymp.stop();
        mymp.release();
        position=((position+1)%mysongs.size());

        Uri u=Uri.parse(mysongs.get(position).toString());
        mymp=MediaPlayer.create(getApplicationContext(),u);
        sname=mysongs.get(position).getName().toString();
        label.setText(sname);
        mymp.start();
    }
    public  void prev(View v){
        mymp.stop();
        mymp.release();
        position=(position-1<0)?(mysongs.size()-1):position-1;

        Uri u=Uri.parse(mysongs.get(position).toString());
        mymp=MediaPlayer.create(getApplicationContext(),u);
        sname=mysongs.get(position).getName().toString();
        label.setText(sname);
        mymp.start();
    }





}

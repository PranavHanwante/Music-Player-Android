package com.example.pranav.mymusicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.BufferUnderflowException;

public class MainActivity extends AppCompatActivity {

    ListView lvMusic;
    Button btnStop,btnPause;
    ImageButton ibtnSkipBack,ibtnSkipForward,ibtnPrevious,ibtnNext;
    MediaPlayer mp; /*  gana baja ne ke liye jo hume chaiye usse kheteh h media player*/
    String name[],path[];   /* name[] sub gane ka naam , path[] sub gane ka location */
    int cs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lvMusic = findViewById(R.id.lvMusic);
        btnStop = findViewById(R.id.btnStop);
        btnPause = findViewById(R.id.btnPause);

        ibtnSkipBack = findViewById(R.id.ibtnSkipBack);
        ibtnSkipForward = findViewById(R.id.ibtnSkipForward);
        ibtnPrevious = findViewById(R.id.ibtnPrevious);
        ibtnNext = findViewById(R.id.ibtnNext);


        mp = new MediaPlayer();


        /*List view bana ne ke liye 3 steps, 1st Array List( YE case me string array liya h),
                        2nd Array Adapter, 3rd set adapter*/

        /*Gana chaiye toh kaha seh milega ? CR seh*/

        /*EXTERNAL_CONTENT_URI matlab External mermory ke gaane*/

        /*If your external memory doesnt have song you can put INTERNAL_CONTENT_URI also*/


        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null ,
                null    ,null  );

        int i=0;
        if(cursor.getCount()>0) {
            name = new String[cursor.getCount()];  /*String[Size aayega] Agar aapke pass 5 gaane h toh array of 5 elements banega*/
            path = new String[cursor.getCount()];


            cursor.moveToFirst();
            do {
                name[i] = cursor.getString(
                        cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));    /*Gane ka naam aayega*/
                path[i] = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));   /*Data is were your
                                                                        Song is located Gane ka Location*/
                i++;


            } while (cursor.moveToNext());
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,name);

        lvMusic.setAdapter(arrayAdapter);   /*It will dis your song on the screen*/





        lvMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*onItemClick me apko gane ka position milega wo position use kar ke ap wo gane ko click kar sakteh h*/
                try{
                    cs=position;
                    String p = path[position];/*user ne jo gana click kiya uska mila postion, postion seh mila gane ka path*/
                    mp.reset();                 /*media player ko reset kiya*/
                    mp.setDataSource(p);    /*Gane ko yaha seh load karna h */
                    mp.prepare();              /*Preapare kar ke */
                    mp.start();             /*gane ko start*/
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying())
                    mp.stop();
                    btnPause.setText("Pause");
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying())
                {
                    mp.pause();
                    btnPause.setText("Resume");
                }
                else{
                    mp.start();
                    btnPause.setText("Pause");

                }
            }
        });

        ibtnSkipBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.seekTo(mp.getCurrentPosition()-300);
            }
        });

        ibtnSkipForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.seekTo(mp.getCurrentPosition()+300);
            }
        });

        ibtnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ns = cs-1;
                if(ns<0)
                    Toast.makeText(MainActivity.this, "First Song", Toast.LENGTH_SHORT).show();
                else{
                    try{
                        cs=ns;
                        String p = path[ns];
                        mp.reset();
                        mp.setDataSource(p);    /*Gane ko yaha seh load karna h */
                        mp.prepare();              /*Preapare kar ke */
                        mp.start();             /*gane ko start*/
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                    }
                }


        });


        ibtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ns = cs;
                int z=name.length;
                if(ns==z-1)
                {
                    Toast.makeText(MainActivity.this, "This is the last Song", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    try{
                        int ns2 = cs+1;
                        cs=ns2;
                        String p = path[ns2];
                        mp.reset();
                        mp.setDataSource(p);    /*Gane ko yaha seh load karna h */
                        mp.prepare();              /*Preapare kar ke */
                        mp.start();             /*gane ko start*/
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }
            }


        });



    }
}

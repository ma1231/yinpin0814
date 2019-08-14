package com.example.yinpin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.example.yinpin.getAllsongs.getAllSongs;

public class MainActivity extends AppCompatActivity {
    private audiodecoder decoder;//8.7
    //implements  View.OnClickListener
    // private Audiodecodeandplay decoder;//8.6
    // private mythread decodeandplaythread;//8.7
    public static boolean ispause = true;
    public static final String SAMPLE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/music4.aac";
    //public static final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/music4.aac";
    public static boolean pause = true;
    /* @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

         //decodeandplaythread = new mythread();//8.7
         Button play = (Button) findViewById(R.id.play);//此处未完善
         Button stop = (Button) findViewById(R.id.stop);
         Button pause = (Button) findViewById(R.id.pause);
         Button continuer = (Button) findViewById(R.id.continuer);
         play.setOnClickListener(this);
         stop.setOnClickListener(this);
         pause.setOnClickListener(this);
         continuer.setOnClickListener(this);
 //
         if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
             ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                     Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
         }

     }

     public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
         switch (requestCode) {
             case 1:
                 if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                     Toast.makeText(this, "拒绝权限将无法应用程序", Toast.LENGTH_SHORT).show();
                     finish();
                 }
                 break;
             default:
         }
     }


     //
     @Override
     public void onClick(View view) {
         //decoder = new Audiodecodeandplay();//8.6
         decoder = new audiodecoder();//8.6
         switch (view.getId()) {
             case R.id.play:
                 if (ispause) {

                     Log.d("ispause", "play");
                   // decodeandplaythread.start();//8.7
                     decoder.excute(SAMPLE);//8.7
                     //decoder.start();
                     Log.d("xn0806", String.valueOf(ispause)+"abc");
                     ispause = false;
                 }
                 break;
             case R.id.stop:
                if (!ispause) {
                 Log.d("xn08061", "123A");
                     //decoder.setrunning();
                     Log.d("xn08061", "123B");
                    ispause = true;
                 }

                 break;
             case R.id.pause:
                 if(pause) {
                     pause = false;
                 }
                 Log.d("xn-诺", "pause");
                 break;

             case R.id.continuer:
                 if(!pause) {
                     pause = true;
                     decoder.setrunning();

                 }
                 Log.d("xn-continuerA", String.valueOf(pause));
                 break;

             default:
                 break;

         }

         //   decodeandplaythread.start();
     }
 */
    private List<song1> songList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取本地音乐
        songList = getAllSongs(MainActivity.this);
        //自定义适配器
        SongAdapter adapter = new SongAdapter(MainActivity.this, R.layout.song1_item, songList);
        //创建ListView
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                song1 songSelected = songList.get(position);
                Toast.makeText(MainActivity.this,songSelected.getTitle(),Toast.LENGTH_SHORT).show();//提醒方式
                //调用MusicActivity
                Intent intent = new Intent(MainActivity.this,MymusicActivity.class);
                intent.putExtra("Song_position",new Gson().toJson(position));//传递这首歌的位置
                intent.putExtra("Song",new Gson().toJson(songSelected));//传递这首歌的具体信息
                intent.putExtra("Song_list",new Gson().toJson(songList));//传递整个歌曲列表
                //启动活动
                startActivity(intent);
            }
        });
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "拒绝权限将无法应用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
}
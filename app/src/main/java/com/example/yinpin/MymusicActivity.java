package com.example.yinpin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import static com.example.yinpin.R.drawable.continuer;

public class MymusicActivity extends AppCompatActivity implements View.OnClickListener  {
    private static song1 songSelected;
    private static song1 songPrevious;
    private static int songPosition;
    private static int isHaveConnect=0;
    private static List<song1> songList ;
    private boolean isMusicPlaying;
    private boolean isMusicSwitch;
    //private audiodecoder decoder;//8.7
    public static boolean ispause = true;
    public static final String SAMPLE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/music4.aac";
    public static boolean pause = true;
    public static int i=1;
    public static int j=1;
    int mSampleRate;
    int channel;
    int samplerate=44100;//采样率44100
    int changelConfig= AudioFormat.CHANNEL_OUT_STEREO;//信道数

    private MediaCodec mediaDecode;
    private MediaFormat format;
    private MediaExtractor Extractor;
    private MediaCodec.Callback mCallback;
    private AudioTrack mPlayer;

    private Button play ;
    private Button stop ;
    private Button pause1 ;
    private Button continuer ;
    private Button back ;
    private Button left ;
    private Button right ;

    private TextView title ;
    private TextView  author ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymusic);
        Intent intent = getIntent();
        String JsonData = intent.getStringExtra("Song");
        songSelected = new Gson().fromJson(JsonData, song1.class); //接收选中的歌曲信息
        String JsonData2 = intent.getStringExtra("Song_position");
        songPosition = new Gson().fromJson(JsonData2, int.class); //接收选中的歌曲在列表中的位置
        String JsonData3 = intent.getStringExtra("Song_list");
        songList = new Gson().fromJson(JsonData3, new TypeToken<List<song1>>(){}.getType()); //接收整个歌曲列表


         play = (Button) findViewById(R.id.play);//此处未完善
         stop = (Button) findViewById(R.id.stop);
         pause1 = (Button) findViewById(R.id.pause1);
         //continuer = (Button) findViewById(R.id.continuer);
         back = (Button) findViewById(R.id.button_back);
        left = (Button) findViewById(R.id.button_left);
         right = (Button) findViewById(R.id.button_right);
        title = (TextView) findViewById(R.id.textTitle);
          author = (TextView) findViewById(R.id.textAuthor);
        //title.setText(songSelected.getTitle());
        //author.setText(songSelected.getSinger());
        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        pause1.setOnClickListener(this);
       // continuer.setOnClickListener(this);
        back.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);

       if(isHaveConnect==0){
            isHaveConnect=1;
            ispause=false;
            excute(songSelected.getFileUrl());
        }else{

             // title.setText(songSelected.getTitle());
              // author.setText(songSelected.getSinger());
           if (songPrevious.getTitle()!=songSelected.getTitle()){
                isMusicSwitch=true;
            }else{
               isMusicSwitch=false;
            }
           /*if(!ispause) {
               ispause=true;
               excute(songSelected.getFileUrl());

              *//* if ( isMusicSwitch) {
                   //切歌+正在播放
                   ispause = false;
                   excute(songSelected.getFileUrl());

               } else if ( !isMusicSwitch) {
                   //相同+正在播放
                   ispause = false;
                   excute(songSelected.getFileUrl());
               }*//*
           }*/
            if(ispause&&isMusicSwitch){
               //切歌+已经停止播放
               ispause=false;
               excute(songSelected.getFileUrl());
           }
           else if(ispause&&!isMusicSwitch){
               ispause=false;
               excute(songSelected.getFileUrl());
           }
        }
        songPrevious = songSelected;


     /* if (!ispause) {

            Log.d("xn08061", "123A");
            Log.d("xn08061", "123B");
            ispause = true;
        }
        ispause=false;
        excute(songSelected.getFileUrl());*/


    }

    @Override
    public void onClick(View view) {
        //decoder = new Audiodecodeandplay();//8.6
        //decoder = new audiodecoder();//8.6
        switch (view.getId()) {
            case R.id.play:
                   if (ispause) {
                       excute( songSelected.getFileUrl());
                       // decoder.excute(SAMPLE);//8.7
                       Log.d("xn0806", String.valueOf(ispause)+"abc");
                       ispause = false;
                   }
                break;
            case R.id.stop:
                if (!ispause) {
                    Log.d("xn08061", "123A");
                    Log.d("xn08061", "123B");
                    ispause = true;
                    pause=true;
                }
                pause=true;
                break;
            case R.id.pause1:
               if(i%2==1){
                if(pause) {
                    pause = false;
                    i++;
                   pause1.setBackground(getDrawable(R.drawable.continuer));
                }
               }
              else if(i%2==0){
                   if(!pause) {
                       pause = true;
                       //decoder.setrunning();
                       pause1.setBackground(getDrawable(R.drawable.pause));

                   }
                   i++;
               }

                Log.d("xn-诺", "pause");
                break;

            /*case R.id.continuer:
                if(!pause) {
                    pause = true;
                    //decoder.setrunning();

                }
                Log.d("xn-continuerA", String.valueOf(pause));
                break;*/
            case R.id.button_back:
                ispause=true;
                finish();
                break;
            case R.id.button_left:
                if (!ispause) {
                    Log.d("xn08061", "123A");
                    Log.d("xn08061", "123B");
                    ispause = true;
                }
                Log.e("R.id.button_left:","Head In");
                Log.e("R.id.button_left:",String.valueOf(songPosition));
                if(songPosition > 0) {
                    songPosition = songPosition - 1;
                }
                songSelected = songList.get(songPosition);
                title.setText(songSelected.getTitle());
                author.setText(songSelected.getSinger());
                Log.e("R.id.button_left:",songSelected.getTitle());
                //decoder.excute(SAMPLE);
                Log.e("R.id.button_left:","Play");
                break;
            case R.id.button_right:
                if (!ispause) {

                    Log.d("xn08061", "123A");
                    Log.d("xn08061", "123B");
                    ispause = true;
                }
                Log.e("R.id.button_rights:","Head In");
                Log.e("R.id.button_rights:",String.valueOf(songPosition));
                    if (songPosition < songList.size() - 1) {
                        songPosition = songPosition + 1;
                    }
                    songSelected = songList.get(songPosition);
                   title.setText(songSelected.getTitle());
                   author.setText(songSelected.getSinger());
                Log.e("R.id.button_right:",songSelected.getTitle());
                //decoder.excute(SAMPLE);
                Log.e("R.id.button_right:","Play");
                break;
            default:
                break;

        }

        //   decodeandplaythread.start();
    }
    public  void excute(String path){
        {

            try {
                title.setText(songSelected.getTitle());
                author.setText(songSelected.getSinger());
                Extractor = new MediaExtractor();
                Extractor.setDataSource(path);

                for (int i = 0; i < Extractor.getTrackCount(); i++) {
                    format = Extractor.getTrackFormat(i);//获取指定（index）的通道格式
                    String mime = format.getString(MediaFormat.KEY_MIME);//媒体数据格式
                    if (mime.startsWith("audio/")) {

                        Extractor.selectTrack(i);//选择此音频轨道
                        mSampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);//获取当前音频的采样率
                        Log.d("xn", String.valueOf(mSampleRate));
                        channel = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT); //获取当前帧的通道数
                        int minBufferSize = AudioTrack.getMinBufferSize(samplerate, changelConfig, AudioFormat.ENCODING_PCM_16BIT);

                        mPlayer = new AudioTrack(AudioManager.STREAM_MUSIC, samplerate, changelConfig, AudioFormat.ENCODING_PCM_16BIT, minBufferSize, AudioTrack.MODE_STREAM);
                        {
                            MediaCodecList codecList = new MediaCodecList(MediaCodecList.ALL_CODECS);//8.2
                            String decoderName = codecList.findDecoderForFormat(format);//8.2
                            mediaDecode = MediaCodec.createByCodecName(decoderName);//8.2
                            MediaMetadataRetriever mmr = new MediaMetadataRetriever();//8.11
                            mmr.setDataSource(path);//8.11
                            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);//播放时长//8.11
                            Log.d("许诺666", duration);//8.11
                            /* MediaMetadataRetriever mmr = new MediaMetadataRetriever(); try { mmr.setDataSource(path);
                            String strDuration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                            long duration = Long.valueOf(strDuration);Log.d("许诺666", String.valueOf(duration)); }catch (Exception e){ }*/

                            // mediaDecode = MediaCodec.createDecoderByType(mime);//创建Decode解码器 mediaDecode = MediaCodec.createDecoderByType(mime);
                            //Log.d("debug123456789","aaa");
                            {
                                mediaDecode.setCallback(new MediaCodec.Callback() {
                                    @Override
                                    public void onInputBufferAvailable(MediaCodec mediaCodec, int id) {

                                        Log.d("xn", "onInputBufferAvailable");
                                        // int inputIndex = mediaDecode.dequeueInputBuffer(-1);//获取可用的inputBuffer -1代表一直等待，0表示不等待 建议-1,避免丢帧
                                        if (!ispause) //8.7
                                        {
                                            ByteBuffer inputBuffer = mediaDecode.getInputBuffer(id);

                                            //inputBuffer.clear();
                                            // fill inputBuffer with valid data
                                            //mediaDecode.queueInputBuffer(i, …);
                                            {

                                                {
                                                    int readresult = Extractor.readSampleData(inputBuffer, 0);//从MediaExtractor中读取一帧待解的数据

                                                    //小于0 代表所有数据已读取完成
                                                    if (readresult < 0) {
                                                        mediaDecode.queueInputBuffer(id, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                                                    } else {

                                                        mediaDecode.queueInputBuffer(id, 0, readresult, Extractor.getSampleTime(), 0);//插入一帧待解码的数据
                                                        Log.d("xn-readresult", String.valueOf(readresult));
                                                        if(pause) {
                                                            Extractor.advance();//MediaExtractor移动到下一取样处

                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    }

                                    @Override
                                    public void onOutputBufferAvailable(MediaCodec mediaCodec, int id, MediaCodec.BufferInfo bufferInfo) {
                                        Log.d("xn", "onOutputBufferAvailable");
                                        //if(!ispause)
                                        {
                                            ByteBuffer outputBuffer = mediaDecode.getOutputBuffer(id);
                                            Log.d("xn-outputBuffer", "ByteBuffer outputBuffer");
                                            MediaFormat outputFormat = mediaDecode.getOutputFormat(id);
                                            Log.d("xn-format", "format == outputFormat");
                                            {
                                                if (/*format == outputFormat &&*/ outputBuffer != null && bufferInfo.size > 0) {
                                                    byte[] mbuffer = new byte[bufferInfo.size];//BufferInfo内定义了此数据块的大小
                                                    Log.d("xn-mbuffer", String.valueOf(mbuffer));
                                                    outputBuffer.get(mbuffer);//将Buffer内的数据取出到字节数组中
                                                    mPlayer.play();
                                                    // mPlayer.pause();
                                                    if(pause)
                                                    {
                                                        mPlayer.write(mbuffer, 0, bufferInfo.size);
                                                    }

                                                }
                                                mediaDecode.releaseOutputBuffer(id, false);
                                            }

                                        }
                                    }

                                    @Override
                                    public void onError(MediaCodec mediaCodec, MediaCodec.CodecException e) {
                                        // if(!ispause)
                                        {
                                            Log.d("xn", "onError");
                                        }
                                    }

                                    @Override
                                    public void onOutputFormatChanged(MediaCodec mediaCodec, MediaFormat mediaFormat) {
                                        // if(!ispause)
                                        {
                                            Log.d("xn-onOutput", "onOutputFormatChanged");
                                            format = mediaFormat;
                                        }
                                    }
                                });
                            }
                        }
                        mediaDecode.configure(format, null, null, 0);//8.2
                    }
                }
                //mediaDecode.configure(format, null, null, 0);
                //mediaDecode.start();//8.2
                format =  mediaDecode.getOutputFormat();

                mediaDecode.start();//8.7
                Log.d("xn-诺"," mPlayer.pause()");

                // release();//8.2

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

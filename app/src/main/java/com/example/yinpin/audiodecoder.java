package com.example.yinpin;


import android.content.Context;
import android.database.Cursor;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static com.example.yinpin.MainActivity.SAMPLE;
import static com.example.yinpin.MainActivity.ispause;
import static com.example.yinpin.MainActivity.pause;
import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;

public class audiodecoder {
    int mSampleRate;
    int channel;
    int samplerate=44100;//采样率44100
    int changelConfig=AudioFormat.CHANNEL_OUT_STEREO;//信道数

    private MediaCodec mediaDecode;
    private MediaFormat format;
    private MediaExtractor Extractor;
    private MediaCodec.Callback mCallback;
    private AudioTrack mPlayer;
    //private mythread0807  thread0807;


    public void setrunning(){
        Log.d("xn","setrunning");



    }

    public  void excute(String path){

            try {
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



    public void release() {
/*
        if (mediaDecode != null) {

            mediaDecode.stop();
            mediaDecode.release();
            mediaDecode = null;
        }
        if (Extractor != null) {
            Extractor.release();
            Extractor = null;
        }

        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        */
        Log.d("xn-0805","release");
    }






}

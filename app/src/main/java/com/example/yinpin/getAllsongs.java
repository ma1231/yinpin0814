package com.example.yinpin;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class getAllsongs {

    public static List<song1> getAllSongs(Context context) {
        Log.e("getAllSongs:","Head In");

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        Log.e("getAllSongs:","In 0");
        List<song1> songs = new ArrayList<song1>();

        Log.e("getAllSongs:","In 1");

        for (int i = 0; i < cursor.getCount(); i++) {
            song1 mp3Info = new song1();                               //新建一个歌曲对象,将从cursor里读出的信息存放进去,直到取完cursor里面的内容为止.
            cursor.moveToNext();

            Log.e("getAllSongs:","In 2");
            long id = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));	//音乐id

            String title = cursor.getString((cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE)));//音乐标题

            String artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));//艺术家

            int duration = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));//时长

            int size = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE));	//文件大小

            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));	//文件路径

            String album = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM)); //唱片图片

            long album_id = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)); //唱片图片ID

            int isMusic = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));//是否为音乐

            if (isMusic != 0 && duration/(1000 * 60) >= 1) {		//只把1分钟以上的音乐添加到集合当中
                //mp3Info.setId(id);
                mp3Info.setTitle(title);
                Log.e("getAllSongs:Title",title);
                mp3Info.setSinger(artist);
                Log.e("getAllSongs:artist",artist);
                mp3Info.setDuration(duration);
                //mp3Info.setSize(size);
                mp3Info.setFileUrl(url);
                mp3Info.setAlbum(album);
                //mp3Info.setAlbum_id(album_id);
                songs.add(mp3Info);
            }
        }
        return songs;

    }

}

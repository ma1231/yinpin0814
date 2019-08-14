package com.example.yinpin;

import android.util.Log;

public class mythread0807 extends Thread {
    @Override
    public void run() {
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

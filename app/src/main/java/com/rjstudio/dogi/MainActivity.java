package com.rjstudio.dogi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.rjstudio.dogi.Utilities.TimeUtilty;
import com.rjstudio.dogi.Utilities.VoiceUtility;

import java.time.Instant;

public class MainActivity extends Activity {

    private TextView tv_timeDisplay;
    private ImageView iv_emoji;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initialization();
//        Intent intent = new Intent(this,Main2Activity.class);
//        startActivity(intent);
        //VoiceUtility.getInstance(this).playTest(getApplicationContext());
//        try {
////            VoiceUtility.getInstance(this).getCurrentTime(21,20,20,0);
//            VoiceUtility.getInstance(this).numberTest();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    private void initialization()
    {

//        tv_timeDisplay = findViewById(R.id.tv_time);
//        iv_emoji = findViewById(R.id.iv_emoji);

        mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        tv_timeDisplay.setText(TimeUtilty.getInstance().getCurrentTime());
                }
            }
        };
        new TimeThread().start();
    }

    class TimeThread extends Thread{

        private Message message;

        @Override
        public void run() {

            do {

                try {
                    Thread.sleep(1000);
                    message = new Message();
                    message.what = 1;
                    mHandler.sendMessage(message);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (true);
        }
    }
}

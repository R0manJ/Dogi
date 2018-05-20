package com.rjstudio.dogi;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.rjstudio.dogi.R;
import com.rjstudio.dogi.Utilities.TimeUtilty;
import com.rjstudio.dogi.Utilities.VoiceUtility;

import static android.content.ContentValues.TAG;

public class Main2Activity extends Activity {

    private Handler mHandler;
    private TextView tv_time;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private int limit = 51;
    private int keepTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Log.d(TAG, "Main2Activity onCreate: ");
        tv_time = findViewById(R.id.tv_time);
        keepTime = 0;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            try {
                tellTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        tellTime();
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (keepTime == 20)
                {
                    keepTime = 0;
                    finish();
                }
                else {
                    keepTime ++;
                }
                tv_time.setText(TimeUtilty.getInstance().getCurrentTime());
                if (limit <=255){
                    limit += 1;
                }
                else {
                    limit = 51;
                }
                setScreen(limit);
            }
        };
        new TimeThread().start();
        powerManager = (PowerManager) this.getSystemService(this.POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,"TEST");
        wakeLock.acquire();


        //tellTime();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void tellTime(){
        try {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        VoiceUtility.getInstance(getApplicationContext()).getCurrentTime(
                                TimeUtilty.getInstance().getCurrentHour(),
                                TimeUtilty.getInstance().getCurrentMinuter(),
                                TimeUtilty.getInstance().getCurrentSecond(),
                                0
                        );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setScreen(int p)
    {
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        float f = p/255.0F;
        layoutParams.screenBrightness = f;
        window.setAttributes(layoutParams);
    }

    class TimeThread extends Thread{
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    mHandler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (true);
        }
    }
}

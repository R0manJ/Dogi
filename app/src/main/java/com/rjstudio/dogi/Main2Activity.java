package com.rjstudio.dogi;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.rjstudio.dogi.R;
import com.rjstudio.dogi.Utilities.TimeUtilty;

public class Main2Activity extends Activity {

    private Handler mHandler;
    private TextView tv_time;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private int limit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tv_time = findViewById(R.id.tv_time);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                tv_time.setText(TimeUtilty.getInstance().getCurrentTime());
                if (limit <=255){
                    limit += 1;
                }
                else {
                    limit = 1;
                }
                setScreen(limit);
            }
        };
        new TimeThread().start();
        powerManager = (PowerManager) this.getSystemService(this.POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,"TEST");
        wakeLock.acquire();

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

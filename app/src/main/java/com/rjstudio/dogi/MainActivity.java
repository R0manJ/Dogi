package com.rjstudio.dogi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rjstudio.dogi.Utilities.BluetoothUtility;
import com.rjstudio.dogi.bean.Bluetooth;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {

    private TextView tv_timeDisplay;
    private ImageView iv_emoji;
    private Handler mHandler;
    private ListView lv_menu;
    private BluetoothUtility btUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();
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

        tv_timeDisplay = findViewById(R.id.tv_time);
        iv_emoji = findViewById(R.id.iv_emoji);
        initialMenu();
        btUtility = new BluetoothUtility(this);
//        mHandler = new Handler()
//        {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                switch (msg.what){
//                    case 1:
//                        tv_timeDisplay.setText(TimeUtilty.getInstance().getCurrentTime());
//                }
//            }
//        };
//        new TimeThread().start();
    }

    private void initialMenu()
    {
        lv_menu = findViewById(R.id.lv_menu);

        List<String> menu = Arrays.asList(new String[]{"Bluetooth","About"});
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menu);

        lv_menu.setAdapter(myAdapter);
        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        Toast.makeText(getApplicationContext(),"BT",Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(),"About",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });



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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btUtility.killReceiver();
    }
}

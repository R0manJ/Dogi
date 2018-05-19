package com.rjstudio.dogi;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.rjstudio.dogi.Adapter.BluetoothListAdapter;
import com.rjstudio.dogi.Utilities.BTUtility;
import com.rjstudio.dogi.Utilities.BluetoothServerTest;
import com.rjstudio.dogi.Utilities.BluetoothUtility;
import com.rjstudio.dogi.Bean.Bluetooth;
import com.rjstudio.dogi.Utilities.BluetoothUtilityTest;
import com.rjstudio.dogi.Utilities.TimeUtilty;
import com.rjstudio.dogi.Utilities.VoiceUtility;

import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    private TextView tv_timeDisplay;
    private ImageView iv_emoji;
    private ListView lv_menu;
    private BluetoothUtility btUtility;
    private DrawerLayout dl_content;
    private BluetoothListAdapter bluetoothListAdapter;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1)
            {
                bluetoothListAdapter.notifyDataSetChanged();
//                Log.d(TAG, "Attention pleas! Device set had changed!");
            }

            switch (msg.what)
            {
                case 1:
                    // Time display...
                    tv_timeDisplay.setText(TimeUtilty.getInstance().getCurrentTime());
                    break;
                case 2:
                    Log.d(TAG, "BT handle is "+msg.obj);
                    tv_timeDisplay.setText(msg.obj.toString());
                    break;
                //接收到语音的指令处理
                case 54:
                    switch (Integer.decode(msg.obj+"")){
                        case 1:
                            Toast.makeText(getApplicationContext(),"Turn on light",Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(getApplicationContext(),"Turn off light",Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                        case 5:
                            break;
                        case 6:
                            break;
                        case 7:
                            //温湿度：
                            String value;
                            String number = "";
                            String number2;
                            Log.d(TAG, "摄氏度 ， 湿度 : "+msg.obj);
                            switch ((int)msg.obj)
                            {
                                case 238:
                                    receiveTime ++;
                                    break;

                                default:
                                    if (receiveTime == 1)
                                    {
                                        number = msg.obj+"";
                                        receiveTime ++;
                                    }
                                    else if (receiveTime == 2)
                                    {
                                        tv_temperature.setText(number + "." + msg.obj + "摄氏度");
                                    }
                                    else if (receiveTime == 3)
                                    {
                                        number = msg.obj+"";
                                        receiveTime ++;
                                    }
                                    else if (receiveTime == 4)
                                    {
                                        tv_humidity.setText(number + "."+msg.obj + "% RH ");
                                    }

                                    break;
                                case 255:
                                    receiveTime = 0;
                                    break;

                            }
                            break;
                        case 8:
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
                            break;
                    }
                case 98:
                    tv_timeDisplay.setText(msg.obj.toString());
                    break;
                case 99:
                    Log.d(TAG, "handleMessage: BT socket is connected.");
                    bluetoothUtilityTest.sendMessageTest("Test complete!");

                    break;

            }
        }
    };
    private List<Bluetooth> list;
    private List<Bluetooth> popupWindowsList;
    private ListView lv_bt;
    private BluetoothUtilityTest bluetoothUtilityTest;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private int receiveTime;
    private TextView tv_humidity;
    private TextView tv_temperature;

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
//
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        BluetoothServerTest blue = new BluetoothServerTest(mHandler);
        blue.start();

        dl_content = findViewById(R.id.dl_main);
        tv_timeDisplay = findViewById(R.id.tv_time);
        iv_emoji = findViewById(R.id.iv_emoji);
        initialMenu();
        btUtility = new BluetoothUtility(this,mHandler);
        popupWindowsList = btUtility.getBluetoothDevice();
        bluetoothListAdapter = new BluetoothListAdapter(getApplicationContext(),popupWindowsList);


        AnimationDrawable frameAnim = (AnimationDrawable) getResources().getDrawable(R.drawable.eyeing);
        iv_emoji.setBackgroundDrawable(frameAnim);
        frameAnim.start();

        powerManager = (PowerManager) this.getSystemService(this.POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "TEST");
        wakeLock.acquire();

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
        tv_temperature = findViewById(R.id.tv_temperature);
        tv_humidity = findViewById(R.id.tv_humidity);
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
                        dl_content.closeDrawer(Gravity.LEFT);
                        BTPopWindows();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(),"About",Toast.LENGTH_LONG).show();
                        dl_content.closeDrawer(Gravity.LEFT);

                        break;
                }
            }
        });

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btUtility.killReceiver();
    }

    private void BTPopWindows()
    {
        View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.popwindows,null,false);
        lv_bt = contentView.findViewById(R.id.lv_bt);
        Button bt_search = contentView.findViewById(R.id.bt_search);


        Log.d(TAG, "BTPopWindows: BTD"+popupWindowsList.size());
        lv_bt.setAdapter(bluetoothListAdapter);
        lv_bt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d(TAG, "onItemClick: "+bluetoothListAdapter.getItem(position));
                Bluetooth device = (Bluetooth) bluetoothListAdapter.getItem(position);
                //Toast.makeText(getApplication(),device.getName() + "---" +device.getAddress(),Toast.LENGTH_SHORT).show();
//                bluetoothUtilityTest = new BluetoothUtilityTest(mHandler,device.getAddress());
//                bluetoothUtilityTest.start();

//                Toast.makeText(getApplicationContext(),device.getName() + "---" +device.getAddress(),Toast.LENGTH_SHORT).show();
//                Log.d("BT Click", "onItemClick: "+device.getName() + "---" +device.getAddress());

                BTUtility btUtility = new BTUtility(mHandler);
                btUtility.clientConnect(device.getAddress());
                btUtility.start();
                btUtility.writeToClientOutput(7+"");

            }
        });

        Log.d(TAG, "BTPopWindows: Creating");

        PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,200,true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);





        try {
            popupWindow.showAtLocation(contentView,Gravity.LEFT,0,1);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Searching...",Toast.LENGTH_LONG).show();
                btUtility.searchBT();
                bluetoothListAdapter.notifyDataSetChanged();

            }
        });
    }
}

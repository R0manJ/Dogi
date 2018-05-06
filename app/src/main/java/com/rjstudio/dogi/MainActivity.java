package com.rjstudio.dogi;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.rjstudio.dogi.Utilities.BluetoothUtility;
import com.rjstudio.dogi.Bean.Bluetooth;

import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    private TextView tv_timeDisplay;
    private ImageView iv_emoji;
    private Handler mHandler;
    private ListView lv_menu;
    private BluetoothUtility btUtility;
    private DrawerLayout dl_content;
    private BluetoothListAdapter bluetoothListAdapter;

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

        dl_content = findViewById(R.id.dl_main);
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
        ListView lv_BT = contentView.findViewById(R.id.lv_bt);
        Button bt_search = contentView.findViewById(R.id.bt_search);


        List<Bluetooth> list = btUtility.getNewBluetooths();
        Log.d(TAG, "BTPopWindows: BTD"+list.size());
        bluetoothListAdapter = new BluetoothListAdapter(getApplicationContext(),list);
        lv_BT.setAdapter(bluetoothListAdapter);
        lv_BT.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        Log.d(TAG, "BTPopWindows: Creating");

        PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,200,true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);





        popupWindow.showAtLocation(contentView,Gravity.LEFT,0,1);

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

package com.rjstudio.dogi.Utilities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by r0man on 2018/5/13.
 */

public class BluetoothServerTest extends Thread {

    private static final String S_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private final UUID testUUID = UUID.fromString(S_UUID);
    private  BluetoothServerSocket bluetoothServerSocket;
    private final BluetoothAdapter bluetoothAdapter;
    private Handler handler;
    private  Message msg;

    public BluetoothServerTest(Handler handler) {
        super();
        msg = new Message();
        this.handler = handler;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            bluetoothServerSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("server",testUUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        try {
            Log.d(TAG, "run: ServerSocket");
            BluetoothSocket bluetoothSocket = bluetoothServerSocket.accept();
            InputStream is = bluetoothSocket.getInputStream();
            while (true)
            {
                int a = 0;
                while ( (a = is.read()) != -1)
                {
                    Log.d(TAG, "server :" + (char)a);
                    msg = new Message();
                    msg.obj = (char)a;
                    msg.what = 98;
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

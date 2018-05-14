package com.rjstudio.dogi.Utilities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.rjstudio.dogi.Bean.Bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by r0man on 2018/5/13.
 */

public class BluetoothUtilityTest extends Thread {
    //无论是BluetoothSocket，还是BluetoothServerSocket，都需要一个UUID（全局唯一标识符,Universally Unique Identifier）
    // UUID相当于Socket的端口，而蓝牙地址相当于Socket的IP。
    private static final String S_UUID = "00001101-0000-1000-8000-00805F9B34FB";

    private BluetoothAdapter mBluetoothAdapter;
    private List<String> bluetoothDevices = new ArrayList<String>();
    private final UUID testUUID = UUID.fromString(S_UUID);

    private BluetoothSocket clientSocket;
    private BluetoothDevice bluetoothDevice;
    private OutputStream outputStream;
    private InputStream inputStream;
    private Handler mHandler;
    private String clientDeviceAddress;
    private Message msg;
    private BluetoothServerSocket serviceSocket;

    public BluetoothUtilityTest(BluetoothAdapter mBluetoothAdapter) {
        this.mBluetoothAdapter = mBluetoothAdapter;
    }

    public BluetoothUtilityTest(Handler handler , BluetoothAdapter bluetoothAdapter , String deviceAddress) {
        this.mBluetoothAdapter = bluetoothAdapter;
        this.mHandler = handler;
        this.clientDeviceAddress = deviceAddress;
    }

    public BluetoothUtilityTest(Handler handler,String deviceAddress)
    {
        this.mHandler = handler;
        this.clientDeviceAddress = deviceAddress;
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            connet();
            startService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //广播里把ListView的适配器 notifyDataSetChanged();
    //ListView 的 OnClick 事件， 可以用getItem ， 获取到对象。


    //Q： 连接设备的时候是否要取消蓝牙搜索？

    //获取远程设备getRemoteDevice(address);

    //创建客户端 device.createRfcommSocketToServiceRecord(UUID);

    //阻塞线程 connect.

    //连接成功获取 输入输出流。

    //结束时关闭即可。

    private void connet() throws IOException {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(clientDeviceAddress);
        if (mBluetoothAdapter.isDiscovering())
        {
            mBluetoothAdapter.cancelDiscovery();
        }
        if (clientSocket == null)
        {
            clientSocket = device.createInsecureRfcommSocketToServiceRecord(testUUID);
        }

    }


    @Override
    public void run() {
        super.run();

//        try{
//            serviceSocket.accept();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//

        try {
            clientSocket.connect();


            outputStream = clientSocket.getOutputStream();
            inputStream = clientSocket.getInputStream();
            msg = new Message();
            msg.what = 99;
            mHandler.sendMessage(msg);

            // InputStream 。read 返回的是ASCII码 。 返回-1表示读取工作结束。

            while(true)
            {
                int b = 0;
                while((b = inputStream.read()) != -1)
                {
                    Log.d(TAG, "ReadStream "+clientDeviceAddress+"Char ;" +(char)b + "INT : "+b);
                    msg = new Message();
                    msg.obj = (char)b;
                    msg.what = 2;
                    mHandler.sendMessage(msg);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void sendMessage(String content)
    {
        if (clientSocket == null || outputStream == null)
        {
            Log.d(TAG, "ClientSocket or outputStream is null.");
            return;
        }
        byte[] contents = content.getBytes();
        try {
            outputStream.write(contents);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessageTest(String content)
    {

        Log.d(TAG, "sendMessageTest: 开始测试");
        if (clientSocket == null || outputStream == null)
        {
            Log.d(TAG, "ClientSocket or outputStream is null.");
            return;
        }
        for (int i = 0 ; i < 10 ; i++)
        {
            Log.d(TAG, "sendMessageTest: 测试 "+ i);
            byte[] a = (i+"").getBytes();
            try {
                outputStream.write(a);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        byte[] contents = content.getBytes();
        try {
            Log.d(TAG, "sendMessageTest: "+content);
            outputStream.write(contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "sendMessageTest:" + contents);
    }

    public void startService()
    {
        try {
            serviceSocket = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("Server",testUUID);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.rjstudio.dogi.Utilities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by r0man on 2018/5/14.
 * version: 2.0
 */

public class BTUtility {

    private Handler handler;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private BluetoothServerSocket bluetoothServerSocket;
    private BluetoothDevice bluetoothDevice;
    private static final String S_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private UUID serial_UUID = UUID.fromString(S_UUID);

    private InputStream clientInputStream;
    private OutputStream clientOutputStream;
    private InputStream serverInputStream;
    private OutputStream serverOutputStream;

    private boolean isServerOutputStreamIsNull = true;
    private boolean isClientOutputStreamIsNull = true;

    private Message msg;
    private BluetoothSocket bluetoothSocketOfClient;

    public BTUtility(Handler handler) {
            this.handler = handler;
            this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void serverAccept()
    {
        try {
            bluetoothServerSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("Server",serial_UUID);
            new ServerSocket().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToServerOutput(String content)
    {
        byte[] contents = content.getBytes();
        if (isServerOutputStreamIsNull)
        {
            Log.d(TAG, "ServerOutputStream is null");
            return;
        }
        try {
            serverOutputStream.write(contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendMessageToHandler(int status_code , String contents,Handler handler)
    {
        msg = new Message();
        msg.what = status_code;
        msg.obj = contents;
        handler.sendMessage(msg);
    }

    private void clientConnect(String bluetoothDeviceAddress)
    {
        this.bluetoothDevice = bluetoothAdapter.getRemoteDevice(bluetoothDeviceAddress);
        try {
            bluetoothSocketOfClient = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(serial_UUID);
            new ClientSocket().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToClient
    private class ServerSocket extends Thread{

        public ServerSocket() {
            super();

        }

        @Override
        public void run() {
            super.run();
            try {
                bluetoothSocket = bluetoothServerSocket.accept();
                sendMessageToHandler(0,"BluetoothSocket has accepted!",handler);
                serverInputStream = bluetoothSocket.getInputStream();
                sendMessageToHandler(1,"ServerInputStream has got! ",handler);
                if ( (serverOutputStream = bluetoothSocket.getOutputStream()) != null){
                    isServerOutputStreamIsNull = false;
                }
                sendMessageToHandler(2,"ServerOutputStream has got!",handler);
                while (true)
                {
                    int a = 0;
                    sendMessageToHandler(3,"Listening server InputStream",handler);
                    while( ( a = serverInputStream.read()) != -1 )
                    {
                        // Receive contents
                        Log.d(TAG, "Receive is "+ (char)a );
                        sendMessageToHandler(4,a+"",handler);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ClientSocket extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                bluetoothSocketOfClient.connect();
                sendMessageToHandler(50 ,"Device has connected!", handler);
                clientInputStream = bluetoothSocketOfClient.getInputStream();
                sendMessageToHandler(51,"Device inputStream has got!",handler);

                if ( (clientOutputStream = bluetoothSocketOfClient.getOutputStream()) != null)
                {
                    isClientOutputStreamIsNull = false;
                    sendMessageToHandler(52,"Device outputStream has got!",handler);

                }
                while (true)
                {
                    sendMessageToHandler(53,"Client InputStream is listening!",handler);
                    int a = 0;
                    while( (a = clientInputStream.read()) !=-1)
                    {
                        Log.d(TAG, "Recvier is"+(char)a);
                        sendMessageToHandler(54,a+"",handler);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}

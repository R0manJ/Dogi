package com.rjstudio.dogi.Utilities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.rjstudio.dogi.Bean.Bluetooth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by r0man on 2018/4/29.
 */

public class BluetoothUtility {

    public final static String CONNECTED_BLUETOOTH = "connectedBluetooth";
    public final static String NEW_BLUETOOTH = "newBluetooth";

    private static BluetoothAdapter bluetoothAdapter;
    private final Context context;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive");
            String action = intent.getAction();
            switch (action)
            {
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    connectedBluetooths.clear();
                    newBluetooths.clear();
                    Log.d(TAG, "onReceive start");

//                    mSearchListener.startSearch();
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Bluetooth bluetooth = new Bluetooth();
                    bluetooth.setName(device.getName());
                    bluetooth.setAddress(device.getAddress());
                    Log.d(TAG, "onReceive found  " + device.getName() + "-" + device.getAddress() +"---"+newBluetooths.size() +"----"+connectedBluetooths.size());

//                    bluetooth.setType(device.getType()); // API 18
//                    bluetooth.setUuid(device.getUuids()); // API 15
//                    short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_DEVICE);
 //                   Log.d("info",device.getName()+":"+rssi);


                    if (device.getBondState() == BluetoothDevice.BOND_BONDED)
                    {
                        for (Bluetooth bluetooth1 : connectedBluetooths)
                        {
                            if (bluetooth1.getAddress().equals(bluetooth.getAddress()))
                            {
                                return;
                            }

                        }
                        connectedBluetooths.add(bluetooth);
                    }
                    else
                    {
                        Log.d(TAG, "onReceive: New dev");
                        for (Bluetooth bluetooth1 : newBluetooths)
                        {
                            if(bluetooth1.getAddress().equals(bluetooth.getAddress())) {
                                Log.d(TAG, "onReceive: ---");
                                return;
                            }

                        }
                        newBluetooths.add(bluetooth);
                        Log.d(TAG, "onReceive: Add - size "+newBluetooths.size());
                    }

                    Log.d(TAG, "onReceive: Found + newBT :"+newBluetooths.size() + " ConnBT : "+connectedBluetooths.size());
                    //mSearchListener.whileSearch(device);
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Map<String,List<Bluetooth>> bluetoothMap = new HashMap<String,List<Bluetooth>>();
                    bluetoothMap.put(CONNECTED_BLUETOOTH,connectedBluetooths);
                    bluetoothMap.put(NEW_BLUETOOTH,newBluetooths);
                    Log.d(TAG, "onReceive: stop");
                    for (Bluetooth bluetooth1 : connectedBluetooths){
                        Log.d(TAG, "Connect Name : "+bluetooth1.getName() + "Address : "+bluetooth1.getAddress());

                    }

                    for (Bluetooth bluetooth1 : newBluetooths)
                    {
                        Log.d(TAG, "Name : "+bluetooth1.getName() + "Address : "+bluetooth1.getAddress());
                    }
                    Log.d(TAG, "onReceive: newDev "+newBluetooths.size() + "-- Conn" + connectedBluetooths.size());
                    Toast.makeText(context,"Total "+newBluetooths.size() +"  devices . ",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private List<Bluetooth> newBluetooths;
    private List<Bluetooth> connectedBluetooths;

    public BluetoothUtility(Context context) {
        super();
        this.context = context;
        initialization();
    }

    private void initialization()
    {
        Log.d(TAG, "initialing");
        connectedBluetooths = new ArrayList<Bluetooth>();
        newBluetooths = new ArrayList<Bluetooth>();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null)
        {
            if (!bluetoothAdapter.isEnabled())
            {
                Toast.makeText(context,"Bluetooth is off,turn on BT!",Toast.LENGTH_LONG).show();
                Intent enabledBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                context.startActivity(enabledBtIntent);
            }
            else
            {
                Toast.makeText(context,"Bluetooth had start.",Toast.LENGTH_LONG).show();
            }


        }
        Log.d(TAG, "initialization: register");
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mReceiver,filter);
        searchBT();
        //bluetoothAdapter.startDiscovery();




    }

    public void killReceiver()
    {
        context.unregisterReceiver(mReceiver);
        bluetoothAdapter.cancelDiscovery();
    }

    public List<Bluetooth> getNewBluetooths() {
        return newBluetooths;
    }

    public void setNewBluetooths(List<Bluetooth> newBluetooths) {
        this.newBluetooths = newBluetooths;
    }

    public List<Bluetooth> getConnectedBluetooths() {
        return connectedBluetooths;
    }

    public void setConnectedBluetooths(List<Bluetooth> connectedBluetooths) {
        this.connectedBluetooths = connectedBluetooths;
    }


    public List<Bluetooth> getBluetoothDevice()
    {
        List<Bluetooth> mDevices = new ArrayList<Bluetooth>();
        Log.d(TAG, "getBluetoothDevice: Add new Bluetooths successful? "+mDevices.addAll(newBluetooths));
        Log.d(TAG, "getBluetoothDevice: Add connected Device successful? " + mDevices.addAll(connectedBluetooths));
        return mDevices;
    }

    public void searchBT()
    {
        try{
//            bluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "searchBT: startDiscorvery -  "+bluetoothAdapter.startDiscovery());
            Log.d(TAG, "searchBT: isDiscorvery - "+bluetoothAdapter.isDiscovering());

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        if (bluetoothAdapter.isDiscovering())
        {
            Toast.makeText(context,"Searching...",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.d(TAG, "searchBT: cancel Discovery "+bluetoothAdapter.cancelDiscovery());
            Log.d(TAG, "searchBT: ?"+bluetoothAdapter.isEnabled());
            Toast.makeText(context,"Search fail...",Toast.LENGTH_SHORT).show();
        }

    }
}

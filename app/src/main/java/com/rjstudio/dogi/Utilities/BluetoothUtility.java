package com.rjstudio.dogi.Utilities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.rjstudio.dogi.bean.Bluetooth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            String action = intent.getAction();
            switch (action)
            {
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    connectedBluetooths.clear();
                    newBluetooths.clear();
//                    mSearchListener.startSearch();
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Bluetooth bluetooth = new Bluetooth();
                    bluetooth.setName(device.getName());
                    bluetooth.setAddress(device.getAddress());
//                    bluetooth.setType(device.getType()); // API 18
//                    bluetooth.setUuid(device.getUuids()); // API 15
                    short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_DEVICE);
                    Log.d("info",device.getName()+":"+rssi);
                    if (device.getBondState() == BluetoothDevice.BOND_BONDED)
                    {
                        for (Bluetooth bluetooth1 : connectedBluetooths)
                        {
                            if (bluetooth1.getAddress().equals(bluetooth.getAddress()))
                            {
                                return;
                            }
                            connectedBluetooths.add(bluetooth);
                        }
                    }
                    else
                    {
                        for (Bluetooth bluetooth1 : newBluetooths)
                        {
                            if(bluetooth1.getAddress().equals(bluetooth.getAddress()))
                            {
                                return;
                            }
                            //newBlueTooths.add(bluetooth);
                        }
                    }
                    //mSearchListener.whileSearch(device);
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Map<String,List<Bluetooth>> bluetoothMap = new HashMap<String,List<Bluetooth>>();
                    bluetoothMap.put(CONNECTED_BLUETOOTH,connectedBluetooths);
                    bluetoothMap.put(NEW_BLUETOOTH,newBluetooths);
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
                Toast.makeText(context,"Bluetooth is start fail.",Toast.LENGTH_LONG).show();
            }


        }
    }

}

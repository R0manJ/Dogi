package com.rjstudio.dogi.Adapter;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.rjstudio.dogi.Bean.Bluetooth;
import com.rjstudio.dogi.R;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by r0man on 2018/5/6.
 */

public class BluetoothListAdapter extends BaseAdapter {



    private final List<Bluetooth> list;
    private Context mContext;

    public BluetoothListAdapter(Context context,List<Bluetooth> list ) {
        super();
        this.list = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item,null);
        TextView tv = view.findViewById(R.id.it_tv);
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: "+list.get(position).getName()+"list size"+list.size());
//            }
//        });
        tv.setText(list.get(position).getName());
        return view;
    }




//    private class ViewHolder{
//        private View view;
//        private String tag;
//        private View
//        public ViewHolder(View view , String tag,Context context) {
//            super();
//            LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,null);
//        }
//
//        public View getView() {
//            return view;
//        }
//
//        public void setView(View view) {
//            this.view = view;
//        }
//
//        public String getTag() {
//            return tag;
//        }
//
//        public void setTag(String tag) {
//            this.tag = tag;
//        }
//    }
}

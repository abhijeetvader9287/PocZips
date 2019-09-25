package com.sunmi.codescanner.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sunmi.codescanner.R;
import com.sunmi.codescanner.ScanConfig;

import java.util.ArrayList;
import java.util.HashMap;

public class StartActivity extends Activity {

    private TextView mScanResult;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        innitView();
    }

    private void innitView() {
        mScanResult = (TextView) findViewById(R.id.scan_result);
        mListView = (ListView) findViewById(R.id.list_view);

    }


    //在外面先定义，ViewHolder静态类
    static class ViewHolder {
        public TextView type;
        public TextView value;
    }

    static class MyAdapter extends BaseAdapter {
        ArrayList<HashMap<String, String>> data;
        Context context;
        LayoutInflater mLayoutInflater;

        public MyAdapter(Context context, ArrayList<HashMap<String, String>> data) {
            this.data = data;
            this.context = context;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mLayoutInflater.inflate(R.layout.list_item, null);
                holder.type = (TextView) convertView.findViewById(R.id.scan_type);
                holder.value = (TextView) convertView.findViewById(R.id.scan_str);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.type.setText(String.format(context.getString(R.string.scan_type), data.get(position).get(ScanConfig.TYPE)));
            holder.value.setText(String.format(context.getString(R.string.scan_string), data.get(position).get(ScanConfig.VALUE)));
            return convertView;
        }
    }

    private static final int START_SCAN = 0x0001;

    public void startScan(View view) {
        // 为外部调用提供
        Intent intent = new Intent();
        intent.setAction("com.sunmi.scan");
        intent.setPackage("com.sunmi.codescanner");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setClassName("com.sunmi.sunmiqrcodescanner", "com.sunmi.sunmiqrcodescanner.activity.ScanActivity");
//        // PPI_1920_1080 = 0X0001;PPI_1280_720 = 0X0002;PPI_640_480 = 0X0003;
//        // 当前分辨率 默认 640*480
//        intent.putExtra("CURRENT_PPI", 0X0003);
//        // 扫描完成声音提示  默认true
//        intent.putExtra("PLAY_SOUND", true);
//        // 扫描完成震动 默认false
//        intent.putExtra("PLAY_VIBRATE", false);
//        // 识别反色二维码 默认true
//        intent.putExtra("IDENTIFY_INVERSE_QR_CODE", true);
//        // 识别画面中多个二维码 默认false
//        intent.putExtra("IDENTIFY_MORE_CODE", true);
//        // 是否显示设置按钮 默认true
//        intent.putExtra("IS_SHOW_SETTING", true);
//        // 是否显示选择相册按钮 默认true
//        intent.putExtra("IS_SHOW_ALBUM", true);
        startActivityForResult(intent, START_SCAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == START_SCAN && data != null) {
            Bundle bundle = data.getExtras();
            ArrayList<HashMap<String, String>> result = (ArrayList<HashMap<String, String>>) bundle.getSerializable("data");
            MyAdapter myAdapter = new MyAdapter(this, result);
            mScanResult.setVisibility(View.VISIBLE);
            mListView.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

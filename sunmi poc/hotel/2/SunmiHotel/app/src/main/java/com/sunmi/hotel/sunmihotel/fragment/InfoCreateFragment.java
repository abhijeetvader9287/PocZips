package com.sunmi.hotel.sunmihotel.fragment;

import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.sunmi.hotel.sunmihotel.BaseFragment;
import com.sunmi.hotel.sunmihotel.R;
import com.sunmi.hotel.sunmihotel.utils.TimeUtils;
import com.sunmi.hotel.sunmihotel.bean.HotelUser;
import com.sunmi.hotel.sunmihotel.model.RoomModel;
import com.sunmi.hotel.sunmihotel.present.CountDownPresent;

public class InfoCreateFragment extends BaseFragment implements CountDownPresent.CountDownCallback {
    private TextView tvCountDown;
    private TextView timeMsg;
    private TextView orderName;
    private TextView orderDate;
    private TextView orderType;
    private TextView orderTel;
    private TextView orderNumber;
    private TextView goNext;
    private HotelUser hotelUser;
    public void setHotelUser(HotelUser hotelUser) {
        this.hotelUser = hotelUser;
    }
    @Override
    protected int getViewId() {
        return R.layout.fragment_info_create;
    }

    @Override
    protected void initView(View view) {

        tvCountDown = (TextView) view.findViewById(R.id.tv_count_down);
        timeMsg = (TextView) view.findViewById(R.id.time_msg);
        orderName = (TextView) view.findViewById(R.id.order_name);
        orderDate = (TextView) view.findViewById(R.id.order_date);
        orderType = (TextView) view.findViewById(R.id.order_type);
        orderTel = (TextView) view.findViewById(R.id.order_tel);
        orderNumber = (TextView) view.findViewById(R.id.order_number);
        goNext = (TextView) view.findViewById(R.id.goNext);
    }



    @Override
    protected void initData() {
        hotelUser.setPhoneNum(mParam1);
        orderName.setText(hotelUser.getName());
        orderDate.setText(TimeUtils.getNowDateShort());
        orderType.setText(RoomModel.getRoom());
        orderTel.setText(mParam1);
        orderNumber.setText(SystemClock.uptimeMillis()+"");
    }

    @Override
    protected void initAction() {
        goNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext();
            }
        });
        CountDownPresent.getInstance().setCountDownCallback(this);
        CountDownPresent.getInstance().setStartTime(30);
    }

    private void goNext() {
        replaceContent((InfoFragment) InfoFragment.newInstance(new InfoFragment(), orderType.getText().toString(), null));
    }


    @Override
    public void onTick(long millisUntilFinished) {
        tvCountDown.setText(""+millisUntilFinished);
    }

    @Override
    public void onSuccess() {
        backTopFragment();
    }
}

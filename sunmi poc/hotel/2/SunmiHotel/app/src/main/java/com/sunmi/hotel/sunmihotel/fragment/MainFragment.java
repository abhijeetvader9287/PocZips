package com.sunmi.hotel.sunmihotel.fragment;

import android.view.View;
import android.widget.TextView;

import com.sunmi.hotel.sunmihotel.BaseFragment;
import com.sunmi.hotel.sunmihotel.R;
import com.sunmi.hotel.sunmihotel.dialog.Create;

public class MainFragment extends BaseFragment implements View.OnClickListener {

    private TextView hotelStayIn;


    @Override
    protected int getViewId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView(View view) {
        hotelStayIn = (TextView) view.findViewById(R.id.hotel_stay_in);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initAction() {
        hotelStayIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hotel_stay_in:
                IDCardFragment idCardFragment = (IDCardFragment) IDCardFragment.newInstance(new IDCardFragment(), null, null);
                replaceContent(idCardFragment, false);
                break;
        }
    }
}

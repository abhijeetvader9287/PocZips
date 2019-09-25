package com.sunmi.hotel.sunmihotel.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunmi.hotel.sunmihotel.BaseActivity;
import com.sunmi.hotel.sunmihotel.BaseFragment;
import com.sunmi.hotel.sunmihotel.MainActivity;
import com.sunmi.hotel.sunmihotel.R;
import com.sunmi.hotel.sunmihotel.bean.HotelUser;
import com.sunmi.hotel.sunmihotel.dialog.Create;
import com.sunmi.hotel.sunmihotel.dialog.CustomDialog;
import com.sunmi.hotel.sunmihotel.model.RoomModel;
import com.sunmi.hotel.sunmihotel.present.CountDownPresent;
import com.sunmi.hotel.sunmihotel.present.MiCardPresent;
import com.sunmi.mifarecard.IMifareCard;

import java.util.Random;
import java.util.logging.Handler;

public class InfoFragment extends BaseFragment implements MiCardPresent.MiCardCallBack, View.OnClickListener, CountDownPresent.CountDownCallback {

    private TextView confirm;
    private TextView tvCountDown;
    private TextView timeMsg;
    private ImageView ivInfoType;
    private TextView goNext;
    private TextView msg;
    private TextView tips;

    private MiCardPresent miCardPresent;

    private HotelUser hotelUser;
    boolean isSuccess;
    ObjectAnimator showBg = new ObjectAnimator();
    ObjectAnimator animator = new ObjectAnimator();


    byte[] cardNo = new byte[32];//卡号
    byte[] bytes = new byte[128];//卡数据

    public void setHotelUser(HotelUser hotelUser) {
        this.hotelUser = hotelUser;
    }

    public void setMifareCard(IMifareCard mService) {
        if (mService != null) {
            miCardPresent = new MiCardPresent(mService, this);
        }
    }


    @Override
    protected int getViewId() {
        return R.layout.fragment_info;
    }

    @Override
    protected void initView(View view) {
        confirm = (TextView) view.findViewById(R.id.confirm);

        tvCountDown = (TextView) view.findViewById(R.id.tv_count_down);
        timeMsg = (TextView) view.findViewById(R.id.time_msg);
        ivInfoType = (ImageView) view.findViewById(R.id.iv_info_type);


        goNext = (TextView) view.findViewById(R.id.goNext);

        msg = (TextView) view.findViewById(R.id.msg);

        tips = (TextView) view.findViewById(R.id.tips);

    }

    @Override
    protected void initData() {
        switch (RoomModel.getRoom(getContext(), mParam1)) {
            case RoomModel.one:
                ivInfoType.setImageResource(R.drawable.ic_onebed);
                break;
            case RoomModel.two:
                ivInfoType.setImageResource(R.drawable.ic_twobed);
                break;
            default:
                ivInfoType.setImageResource(R.drawable.ic_bigbed);
                break;
        }

//        SpannableString spannableString = new SpannableString(confirm.getText());
//        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6000")), 5, confirm.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        confirm.setText(spannableString);
        confirm.setText(setNumColor(getString(R.string.info_num_hotel) + new Random().nextInt(1000)));

//        SpannableString spannableString1 = new SpannableString(tips.getText());
//        spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6000")), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6000")), 9, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tips.setText(setNumColor(tips.getText().toString()));

        showBg.setDuration(300);
        showBg.setFloatValues(1.0f, 0.0f, 1.0f);
        showBg.setTarget(null);
        showBg.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                if (Math.abs(animatedValue) < 0.02f) {
                    confirm.setText(getString(R.string.info_hotel_success1));
                    msg.setText(getString(R.string.info_hotel_success2));
                }
                confirm.setAlpha(animatedValue);
                msg.setAlpha(animatedValue);
                ivInfoType.setAlpha(animatedValue);

            }
        });

        animator.setDuration(300).setFloatValues(0.0f, 1.0f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                goNext.setAlpha(animatedValue);
            }
        });
    }

    public static SpannableStringBuilder setNumColor(String str) {
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        for (int i = 0; i < str.length(); i++) {
            char a = str.charAt(i);
            if (a >= '0' && a <= '9') {
                style.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6000")), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                style.setSpan(new RelativeSizeSpan(3.0f), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
        }
        return style;
    }


    @Override
    protected void initAction() {
        goNext.setOnClickListener(this);
        if (miCardPresent != null) {
            miCardPresent.onCardIssuingAndCardReader();
        }

        CountDownPresent.getInstance().setCountDownCallback(this);
        CountDownPresent.getInstance().setStartTime(30);
    }


    @Override
    public void onTick(long millisUntilFinished) {
        tvCountDown.setText("" + millisUntilFinished);
    }

    @Override
    public void onFail(final String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(getString(R.string.tips_micard_send_fail));
            }
        });
    }

    @Override
    public void onGetCardNo(byte[] cardNo) {
        this.cardNo = cardNo;
        hotelUser.setMiCard(cardNo);
        if (miCardPresent != null || !isDetached()) {
            miCardPresent.giveCard();
        }
    }

    @Override
    public void onGetCardInfo(byte[] cardInfo) {
        bytes = cardInfo;
        hotelUser.setMiCardInfo(bytes);
    }

    @Override
    public void onMicardSuccess() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity) getActivity()).soundPresent.startGun();
                isSuccess = true;
                confirm.setText(getString(R.string.info_hotel_success1));
                msg.setText(getString(R.string.info_hotel_success2));
                ivInfoType.setImageResource(R.drawable.ic_out);
                goNext.setVisibility(View.VISIBLE);
                tips.setVisibility(View.GONE);
//        showBg.start();
//        animator.start();
            }
        });

    }

    @Override
    public void onSuccess() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isSuccess) {
                    backTopFragment();
                } else {
                    showDialog();
                }
            }
        });

    }

    @Override
    public void onDetach() {
        miCardPresent.setCardCallBack(null);
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goNext:
                backTopFragment();
                break;
        }
    }

    CustomDialog dialog;

    void showDialog() {
        if (dialog == null) {
            dialog = Create.createDialog(getContext(), R.drawable.ic_room, getString(R.string.dialog_no_micard), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    backTopFragment();
                }
            }, new CustomDialog.DialogOver() {
                @Override
                public void over() {
                    backTopFragment();
                }
            });
            dialog.show();
        } else {
            dialog.show();
        }
    }
}

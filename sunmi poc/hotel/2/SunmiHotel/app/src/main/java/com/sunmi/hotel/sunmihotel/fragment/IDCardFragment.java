package com.sunmi.hotel.sunmihotel.fragment;

;

import android.animation.Animator;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Outline;
import android.os.Handler;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sunmi.hotel.sunmihotel.BaseActivity;
import com.sunmi.hotel.sunmihotel.BaseFragment;

import com.sunmi.hotel.sunmihotel.MainActivity;
import com.sunmi.hotel.sunmihotel.R;
import com.sunmi.hotel.sunmihotel.bean.HotelUser;
import com.sunmi.hotel.sunmihotel.dialog.Create;
import com.sunmi.hotel.sunmihotel.dialog.CustomDialog;
import com.sunmi.hotel.sunmihotel.present.CountDownPresent;
import com.sunmi.hotel.sunmihotel.present.IDCardPresent;
import com.sunmi.hotel.sunmihotel.view.CircularProgressView;
import com.sunmi.idcardservice.IDCardInfo;
import com.sunmi.idcardservice.IDCardServiceAidl;


public class IDCardFragment extends BaseFragment implements IDCardPresent.IDCardCallBack, CountDownPresent.CountDownCallback {

    private IDCardPresent idCardPresent;

    private HotelUser hotelUser;
    private TextView tvCountDown;
    private CircularProgressView circularProgressView;
    private FrameLayout animSuccess;
    private FrameLayout circularRoot;
    private TextView TitleText;
    private TextView timeMsg;
    private TextView tips;

    @Override
    protected int getViewId() {
        return R.layout.fragment_idcard;
    }


    @Override
    protected void initView(View view) {
        tvCountDown = view.findViewById(R.id.tv_count_down);

        TitleText = (TextView) view.findViewById(R.id.TitleText);
        timeMsg = (TextView) view.findViewById(R.id.time_msg);
        tips = (TextView) view.findViewById(R.id.tips);


        circularProgressView = (CircularProgressView) view.findViewById(R.id.circularProgressView);

        circularRoot = (FrameLayout) view.findViewById(R.id.circular_root);


        animSuccess = (FrameLayout) view.findViewById(R.id.anim_success);
        animSuccess.setClipToOutline(true);
        animSuccess.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0
                        , view.getMeasuredWidth(),
                        view.getMeasuredWidth(),
                        view.getMeasuredWidth() / 2);
            }
        });
    }

    @Override
    protected void initData() {

    }

    public void setStop(){
        super.setStop();
        idCardPresent.cancelAutoReading();
    }
    public void setResume(){
        super.setResume();
        idCardPresent.startReadCard();
    }


    public void setIdCardServiceAidl(IDCardServiceAidl idCardServiceAidl) {
        if (idCardServiceAidl != null) {
            idCardPresent = new IDCardPresent(idCardServiceAidl, this);
        }

    }

    @Override
    protected void initAction() {
        if (idCardPresent != null) {
            idCardPresent.startReadCard();
        }
        if (MainActivity.isDebug) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    showAnim();
                }
            },2000);
        }

        CountDownPresent.getInstance().setCountDownCallback(this);
        CountDownPresent.getInstance().setStartTime(120);
    }

    void goNext() {
       postDelayed(new Runnable() {
            @Override
            public void run() {
                replaceContent(FaceFragment.newInstance(new FaceFragment(), null, null));
            }
        }, 3000);
    }

    void showAnim() {
        circularProgressView.startAnim(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                TitleText.setText(R.string.idcard_scan_success);
                timeMsg.setText(R.string.idcard_scan_next);
                tvCountDown.setVisibility(View.GONE);
                animSuccess.setVisibility(View.VISIBLE);
                ((MainActivity)getActivity()).soundPresent.startIdcardSuccess();
                goNext();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void onFail(String msg) {

    }

    @Override
    public void getCardData(IDCardInfo info, int code) {
        Log("身份证 ==" + info.toString());
        hotelUser.setIDCard(info.getIdCard());
        hotelUser.setName(info.getName());
        hotelUser.setIdcard_face(info.getImageAddress());
        timeMsg.post(new Runnable() {
            @Override
            public void run() {
                TitleText.setText(R.string.idcard_scan);
                tips.setVisibility(View.GONE);
                showAnim();
            }
        });

    }

    public void setHotelUser(HotelUser hotelUser) {
        this.hotelUser = hotelUser;
    }



    @Override
    public void onTick(long millisUntilFinished) {
        tvCountDown.setText("" + millisUntilFinished);
    }

    @Override
    public void onSuccess() {
        setStop();
        showDialog();
    }

    CustomDialog dialog;

    void showDialog() {
        if (dialog == null) {
            dialog = Create.createDialog(getContext(), R.drawable.ic_card, getString(R.string.dialog_no_idcard), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    backTopFragment();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    setResume();
                    CountDownPresent.getInstance().setStartTime(60);
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

    @Override
    public void onDestroy() {
        idCardPresent.cancelAutoReading();
        super.onDestroy();
    }
}

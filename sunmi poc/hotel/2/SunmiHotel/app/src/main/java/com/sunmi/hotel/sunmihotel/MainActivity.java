package com.sunmi.hotel.sunmihotel;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunmi.hotel.sunmihotel.bean.HotelUser;
import com.sunmi.hotel.sunmihotel.dialog.Create;
import com.sunmi.hotel.sunmihotel.dialog.CustomDialog;
import com.sunmi.hotel.sunmihotel.fragment.FaceFragment;
import com.sunmi.hotel.sunmihotel.fragment.IDCardFragment;
import com.sunmi.hotel.sunmihotel.fragment.InfoCreateFragment;
import com.sunmi.hotel.sunmihotel.fragment.InfoFragment;
import com.sunmi.hotel.sunmihotel.fragment.MainFragment;
import com.sunmi.hotel.sunmihotel.fragment.PhoneFragment;
import com.sunmi.hotel.sunmihotel.present.CountDownPresent;
import com.sunmi.hotel.sunmihotel.present.SoundPresent;
import com.sunmi.hotel.sunmihotel.present.StateLampPresent;
import com.sunmi.idcardservice.IDCardServiceAidl;
import com.sunmi.mifarecard.IMifareCard;
import com.sunmi.statuslampmanager.IStateLamp;

import java.util.Locale;

public class MainActivity extends BaseActivity {
    private ImageView goHome;
    private ImageView rootBg;
    private ImageView process;
    private TextView tips;
    public static boolean isDebug = false;

    public IMifareCard mifareCard = null;

    public IDCardServiceAidl idCardServiceAidl = null;
    private IStateLamp stateLamp = null;

    MainFragment mainFragment;

    public HotelUser hotelUser = new HotelUser();

    public CountDownPresent countDownPresent;

    public StateLampPresent stateLampPresent;

    public SoundPresent soundPresent;

    ObjectAnimator showBg = new ObjectAnimator();
    ObjectAnimator hiddenBg = new ObjectAnimator();


    boolean isOk, isProcess;

    @Override
    protected int getViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        goHome = findViewById(R.id.go_home);
        rootBg = (ImageView) findViewById(R.id.rootBg);
        process = (ImageView) findViewById(R.id.process);
        tips = (TextView) findViewById(R.id.tips);


    }

    @Override
    protected void initData() {
        stateLampPresent = new StateLampPresent();
        mainFragment = new MainFragment();
        addContent(MainFragment.newInstance(mainFragment, null, null), false);
    }

    @Override
    protected void initAction() {
        goHome.setOnClickListener(this);
        connectLedService();
        countDownPresent = CountDownPresent.getInstance();

        soundPresent = new SoundPresent(this);

        showBg.setDuration(getResources().getInteger(R.integer.fragment_replace_time));
        showBg.setFloatValues(0.0f, 1.0f);
        showBg.setTarget(rootBg);
        showBg.setPropertyName("alpha");

        hiddenBg.setDuration(getResources().getInteger(R.integer.fragment_replace_time));
        hiddenBg.setFloatValues(1.0f, 0.0f);
        hiddenBg.setTarget(rootBg);
        hiddenBg.setPropertyName("alpha");

        findViewById(R.id.logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDebug = !isDebug;
                findViewById(R.id.logo).setAlpha(isDebug ? 0.5f : 1f);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go_home:
                showDialog();
                break;
        }

    }

    CustomDialog dialog;

    void showDialog() {
        stopProcess();
        if (dialog == null) {
            dialog = Create.createDialog(this, R.drawable.ic_empty, getString(R.string.dialog_gohome_tips), getString(R.string.dialog_gohome), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    backTopFragment();
                }
            }, getString(R.string.dialog_cancel), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            }, new CustomDialog.DialogOver() {
                @Override
                public void over() {
                    backTopFragment();
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    resumeProcess();
                }
            });
            dialog.show();
        } else {
            dialog.show();
        }

    }

    private void connectLedService() {
        Intent intent = new Intent();
        intent.setPackage("com.sunmi.statuslampmanager");
        intent.setAction("com.sunmi.statuslamp.service");
        bindService(intent, conLed, BIND_AUTO_CREATE);
    }

    private ServiceConnection conLed = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            stateLamp = IStateLamp.Stub.asInterface(service);
            stateLampPresent.setStateLamp(stateLamp);
            bindIDCard();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            showToast("led failed");
            stateLamp = null;
        }
    };

    void bindIDCard() {
        Intent intent = new Intent();
        intent.setPackage("com.sunmi.idcardservice");
        intent.setAction("com.sunmi.idcard");
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }


    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log("身份证读取服务连接成功，开始识别");
            idCardServiceAidl = IDCardServiceAidl.Stub.asInterface(service);
            connectService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log("身份证读取服务连接失败");
            showToast("idCard failed");
            idCardServiceAidl = null;
        }
    };

    private ServiceConnection con = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log("micard服务连接成功");
            mifareCard = IMifareCard.Stub.asInterface(service);
            showToast(getString(R.string.tips_services_success));
            isOk = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            showToast("micard failed");
            mifareCard = null;
            isOk = false;
        }
    };

    private void connectService() {
        Intent intent = new Intent();
        intent.setPackage("com.sunmi.mifarecard");
        intent.setAction("com.sunmi.MifareCardService");
        startService(intent);
        bindService(intent, con, BIND_AUTO_CREATE);
    }

    @Override
    public void replaceContent(BaseFragment fragment, boolean addToBackStack) {
        if (isOk || isDebug) {
            super.replaceContent(fragment, addToBackStack);
        }
    }

    @Override
    public void backTopFragment() {
        super.backTopFragment();
        countDownPresent.setCountDownCallback(null);
        replaceContent(mainFragment, false);
        cancelProcess();
        stateLampPresent.closeAllLamp();
        hotelUser = new HotelUser();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stateLampPresent.closeAllLamp();
        stopProcess();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeProcess();
    }

    @Override
    protected void onDestroy() {
        if (idCardServiceAidl != null) {
            try {
                idCardServiceAidl.cancelAutoReading();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            unbindService(mConnection);
        }
        if (mifareCard != null) {
            unbindService(con);
        }

        if (stateLamp != null) {
            unbindService(conLed);
        }
        countDownPresent.cancel();
        soundPresent.close();
        super.onDestroy();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentAttach(BaseFragment baseFragment) {
        if (!isOk && !isDebug) {
            return;
        }
        stateLampPresent.closeAllLamp();
        if (baseFragment instanceof InfoFragment) {
            stateLampPresent.startCardLed();
            process.setImageResource(R.drawable.crumbs_03);
            ((InfoFragment) baseFragment).setMifareCard(mifareCard);
            ((InfoFragment) baseFragment).setHotelUser(hotelUser);
        } else if (baseFragment instanceof IDCardFragment) {
            soundPresent.startIdcard();
            startProcess();
            stateLampPresent.startIdLed();
            ((IDCardFragment) baseFragment).setIdCardServiceAidl(idCardServiceAidl);
            ((IDCardFragment) baseFragment).setHotelUser(hotelUser);
        } else if (baseFragment instanceof FaceFragment) {
            soundPresent.startFace();
            stateLampPresent.startCameraLed();
        } else if (baseFragment instanceof PhoneFragment) {
            soundPresent.startPhone();
            process.setImageResource(R.drawable.crumbs_02);
        } else if (baseFragment instanceof InfoCreateFragment) {
            soundPresent.startCheck();
            ((InfoCreateFragment) baseFragment).setHotelUser(hotelUser);
        }
    }

    private void startProcess() {
        isProcess = true;
        tips.setTextColor(Color.parseColor("#99333C4F"));
        goHome.setVisibility(View.VISIBLE);
        process.setVisibility(View.VISIBLE);
        process.setImageResource(R.drawable.crumbs_01);

        hiddenBg.start();

    }

    private void cancelProcess() {
        isProcess = false;
        tips.setTextColor(Color.parseColor("#99FFFFFF"));
        goHome.setVisibility(View.GONE);
        process.setImageResource(R.drawable.crumbs_01);
        process.setVisibility(View.INVISIBLE);

        showBg.start();

    }

    public void stopProcess() {
        getFirstFragment().setStop();
        isProcess = false;
        countDownPresent.setStop(true);
    }

    public void resumeProcess() {
        getFirstFragment().setResume();
        isProcess = true;
        countDownPresent.setStop(false);
    }



}

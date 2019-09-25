package com.sunmi.hotel.sunmihotel.present;

import android.content.Intent;
import android.os.RemoteException;

import com.sunmi.idcardservice.CardCallback;
import com.sunmi.idcardservice.IDCardInfo;
import com.sunmi.idcardservice.IDCardServiceAidl;

public class IDCardPresent {
    private IDCardServiceAidl idCardServiceAidl = null;
    IDCardCallBack idCardCallBack;

    public interface IDCardCallBack {
        void onFail(String msg);

        void getCardData(IDCardInfo info, int code);
    }


    public IDCardPresent(IDCardServiceAidl mService, IDCardCallBack idCardCallBack) {
        this.idCardServiceAidl = mService;
        this.idCardCallBack = idCardCallBack;
    }


    public void startReadCard() {
        try {
            idCardServiceAidl.readCardAuto(cardCallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void cancelAutoReading() {
        try {
            idCardServiceAidl.cancelAutoReading();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    CardCallback cardCallback = new CardCallback.Stub() {
        @Override
        public void getCardData(final IDCardInfo info, final int code) throws RemoteException {
            if (code == 10) {
                try {
                    idCardServiceAidl.cancelAutoReading();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                idCardCallBack.getCardData(info,code);
            } else if (code != 1 && code != -10) {
                idCardCallBack.onFail("身份证读取异常 ==" + code);
            }
        }
    };


}

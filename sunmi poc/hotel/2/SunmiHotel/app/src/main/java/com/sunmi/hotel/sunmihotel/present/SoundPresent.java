package com.sunmi.hotel.sunmihotel.present;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.sunmi.hotel.sunmihotel.R;

public class SoundPresent {

    SoundPool soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);

    public SoundPresent(Context context) {
        soundPool.load(context, R.raw.check, 1);// 1
        soundPool.load(context, R.raw.face, 1);// 1
        soundPool.load(context, R.raw.gun, 1);// 1
        soundPool.load(context, R.raw.idcard, 1);// 1
        soundPool.load(context, R.raw.idcard_success, 1);// 1
        soundPool.load(context, R.raw.phone, 1);// 1
    }

    public void startIdcard() {
        soundPool.play(4, 1, 1, 10, 0, 1);
    }

    public void startIdcardSuccess() {
        soundPool.play(5, 1, 1, 10, 0, 1);
    }

    public void startFace() {
        soundPool.play(2, 1, 1, 10, 0, 1);
    }

    public void startCheck() {
        soundPool.play(1, 1, 1, 10, 0, 1);
    }

    public void startPhone() {
        soundPool.play(6, 1, 1, 10, 0, 1);
    }

    public void startGun(){
        soundPool.play(3, 1, 1, 10, 0, 1);
    }

    public void close(){
        soundPool.release();
    }

}

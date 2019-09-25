package com.sunmi.hotel.sunmihotel.model;

import android.content.Context;

import com.sunmi.hotel.sunmihotel.R;

import java.util.Random;

public class RoomModel {

    public static final int one = R.string.room_one;

    public static final int two = R.string.room_two;

    public static final int all = R.string.room_all;

    public static int getRoom() {
        Random random = new Random();
        switch (random.nextInt(4)) {
            case 1:
                return one;
            case 2:
                return two;
            default:
                return all;
        }
    }

    public static int getRoom(Context context, String room) {
        if (context.getString(one).equals(room)) {
            return one;
        } else if (context.getString(two).equals(room)) {
            return two;

        }
        return all;

    }

}

package com.example.broadcastreceivertest;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        // 리시버 상태 체크
        //Toast.makeText(context, "Event !!!", Toast.LENGTH_SHORT).show();

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (phoneNumber == null ) {
            Log.i("call-state", " : NULL");
        } else {
            if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
                Toast.makeText(context, "call Active", Toast.LENGTH_SHORT).show();
            } else if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
                Toast.makeText(context, "No call", Toast.LENGTH_SHORT).show();
            } else if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
                Log.d("리시버", "전화 벨 울리는 중");
                Log.d("Phone number :", phoneNumber);
                Toast.makeText(context, "Ringing State", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, phoneNumber, Toast.LENGTH_SHORT).show();
            }
        }

    }
}

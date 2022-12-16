package com.example.broadcastreceivertest.kotlin

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.PhoneNumberUtils
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast

class IncomingCallBroadcastReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive()")


        // 리시버 상태 체크
        //Toast.makeText(context, "Event !!!", Toast.LENGTH_SHORT).show();
        val telephonyManager =
            context.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
        val phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)

        if (phoneNumber == null) {
            Log.i("call-state", " : NULL")
        } else {
            if (TelephonyManager.EXTRA_STATE_OFFHOOK == state) { // 통화중
                Toast.makeText(context, "call Active", Toast.LENGTH_SHORT).show()
            } else if (TelephonyManager.EXTRA_STATE_IDLE == state) { // 종료
                Toast.makeText(context, "No call", Toast.LENGTH_SHORT).show()
            } else if (TelephonyManager.EXTRA_STATE_RINGING == state) {
                Log.d("리시버", "전화 벨 울리는 중")
                Log.d("Phone number :", phoneNumber)
                val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                val phoneNumber = PhoneNumberUtils.formatNumber(incomingNumber)
//                val serviceIntent = Intent(context, CallingService::class.java)
                val serviceIntent = Intent(context, MyService::class.java)
                serviceIntent.putExtra("phone_number", phoneNumber)
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(serviceIntent)
                }else {
                    context.startService(serviceIntent)
                }
            }
        }

    }


    companion object {
        private const val TAG = "PHONE_STATE";
        var mLastState : String? = null
    }
}
package com.example.broadcastreceivertest.kotlin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Display
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.example.broadcastreceivertest.R


class MyService : Service() {

    private var mWindowManager: WindowManager? = null
    private var windowManager: WindowManager? = null
    var params: WindowManager.LayoutParams? = null
    var rootView: View? = null
    val channelId : String = "com.example.broadcastreceivertest"
    val channelName : String = "Service Channel"

    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT
            )

//            var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            manager.createNotificationChannel(channel)
        }

//        val notificationBuilder = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.mipmap.ic_launcher)
//            .setContentTitle("title")
//            .setContentText("text")
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setCategory(NotificationCompat.CATEGORY_CALL)
//
//        val notification = notificationBuilder.build();
//
//        startForeground(1, notification) // 추가


        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val display: Display = windowManager!!.getDefaultDisplay()

        val width = (display.width * 0.9).toDouble() //Display 사이즈의 90%

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params =  WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        or  WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,

                PixelFormat.TRANSLUCENT);

            params!!.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP;
            params!!.x = 0;
            params!!.y = 100;
            mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager?;
            mWindowManager!!.addView(rootView, params)
        }else {
            params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        or  WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        or  WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        or  WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT)
        }


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MyService", "서비스 동작")
        if(intent == null) {
            return Service.START_STICKY // 서비스가 종료 되었을 때도 다시 자동으로 실행
        }else {
            processCommand(intent)
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun processCommand(intent: Intent?) {
        // 전달 받은 데이터 가져오기
        val phoneNumber = intent?.getStringExtra("phone_number")

        if (phoneNumber != null) {
            Log.d("폰넘버", phoneNumber)
        }else {
            Log.d("폰넘버", "안찍힘")
        }
        val showIntent : Intent = Intent(applicationContext, PopupActivity::class.java)

        showIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        showIntent.putExtra("call_number", phoneNumber)
        startActivity(showIntent) // 액티비티로 보냄

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}
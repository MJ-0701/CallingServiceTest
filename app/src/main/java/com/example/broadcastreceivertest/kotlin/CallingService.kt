package com.example.broadcastreceivertest.kotlin


import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.widget.TextView
import androidx.core.app.NotificationCompat
import butterknife.ButterKnife
import butterknife.InjectView
import butterknife.OnClick
import com.example.broadcastreceivertest.R


class CallingService : Service() {

    var rootView: View? = null

    @SuppressLint("NonConstantResourceId")
    @InjectView(R.id.txtText)
    var tv_call_number: TextView? = null

    private var windowManager: WindowManager? = null

    private var mWindowManager: WindowManager? = null
    private val mChatHeadView: View? = null
    var params: WindowManager.LayoutParams? = null

    val channelId : String = "com.example.broadcastreceivertest"
    val channelName : String = "Service Channel"


    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT
            )

            var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("title")
            .setContentText("text")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)

        val notification = notificationBuilder.build();

        startForeground(1, notification) // 추가


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



        val layoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        rootView = layoutInflater.inflate(R.layout.dialog_custom, null)
        ButterKnife.inject(this, rootView)
        setDraggable()
    }


    private fun setDraggable() {
        rootView!!.setOnTouchListener(object : OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params!!.x
                        initialY = params!!.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        return true
                    }
                    MotionEvent.ACTION_UP -> return true
                    MotionEvent.ACTION_MOVE -> {
                        params!!.x = initialX + (event.rawX - initialTouchX).toInt()
                        params!!.y = initialY + (event.rawY - initialTouchY).toInt()
                        if (rootView != null) windowManager!!.updateViewLayout(rootView, params)
                        return true
                    }
                }
                return false
            }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        windowManager!!.addView(rootView, params)
        setExtra(intent)

        if (!TextUtils.isEmpty(call_number)) {
            Log.d("세팅1", call_number.toString())
            tv_call_number?.text = call_number
        }else {
            Log.d("세팅2", call_number.toString())
        }
        return START_REDELIVER_INTENT
    }


    private fun setExtra(intent: Intent?) {
        if (intent == null) {
            removePopup()
            return
        }
        call_number = intent.getStringExtra(EXTRA_CALL_NUMBER)
    }


    override fun onBind(intent: Intent?): IBinder? {
        // Not used
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        removePopup()
    }


    @SuppressLint("NonConstantResourceId", "ResourceType")
//    @OnClick(R.id.cancel_button)
    fun removePopup() {
//        if (rootView != null && windowManager != null)
//            windowManager!!.removeView(rootView)

        var inflater : LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val inflate = inflater.inflate(R.id.cancel_button, null)
        inflate.setOnClickListener {

        }
    }




    companion object {
        const val EXTRA_CALL_NUMBER = "call_number"
        var call_number: String? = null
    }


}
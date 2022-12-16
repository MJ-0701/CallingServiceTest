package com.example.broadcastreceivertest.kotlin

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.broadcastreceivertest.R


class MainActivity : AppCompatActivity() {

    private val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 100

    var txtResult: TextView? = null

//    var toggleButton : ToggleButton = findViewById(R.id.toggleButton);



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()
//        val toggleButton : ToggleButton = findViewById(R.id.toggleButton);
//        toggleButton.setOnClickListener(View.OnClickListener {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {   // 오레오 이상일 경우
//                if (!Settings.canDrawOverlays(this)) {              // 체크
//                    val intent = Intent(
//                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                        Uri.parse("package:$packageName")
//                    )
//                    startForegroundService(Intent(this@MainActivity, CallingService::class.java))
//                    startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
//                } else {
//                    // TODO :: 권한 없을시 로직
//                }
//            } else {
//                startService(Intent(this@MainActivity, CallingService::class.java))
//            }
//
//        })

        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.READ_CALL_LOG
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity, arrayOf(
                    Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE
                ), 1
            )
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {   // 오레오 이상일 경우
            if (!Settings.canDrawOverlays(this)) {              // 체크
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startForegroundService(Intent(this@MainActivity, CallingService::class.java))
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
            } else {
                // TODO :: 권한 없을시 로직
            }
        } else {
            startService(Intent(this@MainActivity, CallingService::class.java))
        }
    }


}
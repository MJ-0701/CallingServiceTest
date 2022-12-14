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
//        checkPermission()
        val toggleButton : ToggleButton = findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(View.OnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {   // 마시멜로우 이상일 경우
                if (!Settings.canDrawOverlays(this)) {              // 체크
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                    startForegroundService(Intent(this@MainActivity, CallingService::class.java))
//                    startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
                } else {
                    startService(Intent(this@MainActivity, CallingService::class.java))
                }
            } else {
                startService(Intent(this@MainActivity, CallingService::class.java))
            }

        })


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

//    fun checkPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
//            if (!Settings.canDrawOverlays(this)) {              // 다른앱 위에 그리기 체크
//                val uri: Uri = Uri.fromParts("package", packageName, null)
//                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri)
//                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
//
//            } else {
//                startMain()
//            }
//        } else {
//            startMain()
//        }
//    }

    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(this)) {              // 체크
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
            } else {
                startService(Intent(this@MainActivity, CallingService::class.java))
            }
        } else {
            startService(Intent(this@MainActivity, CallingService::class.java))
        }
    }


    @TargetApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // TODO 동의를 얻지 못했을 경우의 처리
            } else {
                startService(Intent(this@MainActivity, CallingService::class.java))
            }
        }
    }

    /* REQ_CODE_OVERLAY_PERMISSION는 임의로 정한 상수
   onActivityResult(int requestCode, int resultCode, Intent data)에서 requestCode로 받을 때 사용함 */





//    @TargetApi(Build.VERSION_CODES.M)
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
//            if (!Settings.canDrawOverlays(this)) {
//                finish()
//            } else {
//                startMain()
//            }
//        }
//    }

//    private fun startMain() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(Intent(this, CallingService::class.java))
//        }else {
//            startService(Intent(this, CallingService::class.java))
//        }
//    }


}
package com.example.broadcastreceivertest.kotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import com.example.broadcastreceivertest.R


class PopupActivity : Activity() {

    var txtText: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("테스트", "첫번째")


        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_custom)

        //UI 객체생성
        txtText = findViewById<View>(R.id.txtText) as TextView

        //데이터 가져오기
        val intent = intent
        val data = intent.getStringExtra("call_number")
        txtText!!.text = data
    }


    override fun onNewIntent(intent: Intent?) { // 이미 액티비티가 만들어져 있는 상태라고 하면 onCreate()가 호출되지 않고 onNewIntent가 호출됨.
        Log.d("테스트", "두번째")

        processCommand(intent)
        super.onNewIntent(intent)
    }


    private fun processCommand(intent : Intent?) {
        if(intent != null) {
            var phoneNumber : String? = intent.getStringExtra("call_number")
        }
    }

    //확인 버튼 클릭
    fun mOnClose(v: View?) {
        //데이터 전달하기
        val intent = Intent()
        intent.putExtra("result", "Close Popup")
        setResult(RESULT_OK, intent)
        //액티비티(팝업) 닫기
        finish()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //바깥레이어 클릭시 안닫히게
        return event.action != MotionEvent.ACTION_OUTSIDE
    }

    override fun onBackPressed() {
        //안드로이드 백버튼 막기
        return
    }
}
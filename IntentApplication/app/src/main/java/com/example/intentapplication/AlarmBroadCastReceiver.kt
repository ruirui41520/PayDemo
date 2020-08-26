package com.example.intentapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if ("alarm" == it.action) {
                Toast.makeText(context, "闹钟开始啦", Toast.LENGTH_LONG).show()
                // todo operate alarm event
            }
        }
    }
}
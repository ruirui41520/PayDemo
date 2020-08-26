package com.example.intentapplication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_alarm.*

class AlarmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
        checkPermission()
        timer.setOnClickListener {
            startTimer("begin",10)
        }
        alarm.setOnClickListener { createAlarm("create alarm",13,35) }
        file.setOnClickListener { selectImage() }
        call.setOnClickListener { dialPhoneNumber("13019491167") }
    }

    private fun createAlarm(message: String, hour: Int = 13, minutes: Int = 25) {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, message)
            putExtra(AlarmClock.EXTRA_HOUR, hour)
            putExtra(AlarmClock.EXTRA_MINUTES, minutes)
        }
        if (intent.resolveActivity(packageManager) != null) startActivity(intent)
    }

    private fun startTimer(message:String,seconds:Int = 50) {
        val intent = Intent(AlarmClock.ACTION_SET_TIMER).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE,message)
            putExtra(AlarmClock.EXTRA_LENGTH,seconds)
            putExtra(AlarmClock.EXTRA_SKIP_UI,true)
        }
        if (intent.resolveActivity(packageManager) != null)startActivity(intent)
    }

    private fun selectImage(){
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        if (intent.resolveActivity(packageManager)!= null) startActivityForResult(intent,200)
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    baseContext,
                    Manifest.permission.SET_ALARM
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.SET_ALARM
                    ), 100
                )
            }
        }
    }

    private fun dialPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            100 ->
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    Toast.makeText(baseContext, "success", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(baseContext, "failed", Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==200 && resultCode ==Activity.RESULT_OK){
            data?.let {
                val thumbnail = it.getParcelableArrayExtra("data") as Bitmap
                val uri = it.data as Uri
                Toast.makeText(baseContext,uri.path, Toast.LENGTH_LONG)
            }
        }
    }
}
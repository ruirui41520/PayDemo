package com.example.intentapplication

import android.app.Application
import android.content.Context

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

    companion object{
        var mContext: BaseApplication? = null

    }

}
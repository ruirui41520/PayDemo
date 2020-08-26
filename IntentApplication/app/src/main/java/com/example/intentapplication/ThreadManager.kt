package com.example.intentapplication

import android.annotation.TargetApi
import android.os.*
import android.os.AsyncTask.THREAD_POOL_EXECUTOR
import java.util.concurrent.Executor
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ThreadManager {
    companion object {
        var NETWORK_EXECUTOR: Executor? = null
        private var SUB_THREAD_HANDLER: Handler? = null
        private var MAIN_THREAD_HANDLER: Handler? = null
        private var SUB_THREAD: HandlerThread? = null

        private fun isHoneycomb(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        fun initNetworkExecutor(): Executor? {
            var result: Executor? = null
            if (isHoneycomb()) {
                result = THREAD_POOL_EXECUTOR
            } else {
                var temp: Executor? = null
                try {
                    val field = AsyncTask::class.java.getDeclaredField("sExecutor")
                    field.isAccessible = true
                    temp = field.get(null) as Executor
                } catch (e: Exception) {
                    temp = ThreadPoolExecutor(
                        1,
                        0,
                        0,
                        TimeUnit.SECONDS,
                        LinkedBlockingDeque<Runnable>()
                    )
                }
                result = temp
            }
            return result
        }

        fun getSubThreadHandler(): Handler? {
            if (SUB_THREAD_HANDLER == null) {
                synchronized(ThreadManager::class.java) {
                    SUB_THREAD = HandlerThread("ZZ_SUB")
                    SUB_THREAD!!.start()
                    SUB_THREAD_HANDLER =
                        Handler(SUB_THREAD!!.looper)
                }
            }
            return SUB_THREAD_HANDLER
        }

        fun executeOnNetWorkThread(run: Runnable?) {
            NETWORK_EXECUTOR!!.execute(run)
        }

        fun getMainThreadHandler(): Handler? {
            if (MAIN_THREAD_HANDLER == null) {
                synchronized(
                    ThreadManager::class.java
                ) {
                    MAIN_THREAD_HANDLER = Handler(Looper.getMainLooper())
                }
            }
            return MAIN_THREAD_HANDLER
        }
    }

    init {
        NETWORK_EXECUTOR = initNetworkExecutor()
    }
}
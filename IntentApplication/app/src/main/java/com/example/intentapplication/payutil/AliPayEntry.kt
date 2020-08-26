package com.example.intentapplication.payutil

import android.app.Activity
import com.alipay.sdk.app.PayTask
import com.example.intentapplication.ThreadManager

class AliPayEntry : PayEntry() {
    private var mModel: String? = null
    var mActivity: Activity? = null
    companion object {
        // 订单支付成功
        const val CODE_9000 = 9000

        // 正在处理中
        const val CODE_8000 = 8000

        // 订单支付失败
        const val CODE_4000 = 4000

        // 用户中途取消
        const val CODE_6001 = 6001

        // 网络出现错误
        const val CODE_6002 = 6002

        const val ALI_TYPE = 1

        const val weiXinType = 2

        private var instance: AliPayEntry? = null

        @Synchronized
        fun getInstance(): AliPayEntry {
            if (instance == null) {
                instance = AliPayEntry()
            }
            return instance!!
        }
    }

    override fun setModel(obj: Any?) {
        mModel = obj as String
    }

    override fun pay() {
        if (isPaying()) return
        payStart()
        ThreadManager.executeOnNetWorkThread(Runnable {
            val payTask = PayTask(mActivity)
            val result = payTask.payV2(mModel, true)
            val payResult = PayResult(result)
            payResult(payResult)
        })

    }

    private fun payResult(payResult: PayResult) {
        var code = CODE_4000
        try {
            code = payResult.resultStatus?.trim()?.toInt()!!
        } catch (e: Exception) {
        }
        ThreadManager.getMainThreadHandler()?.post(Runnable {
            notifyResult(code, payResult.result)
        })
    }

    override fun notifyResult(errCode: Int, result: String?) {
        payEnd()
        for (listener in mListeners) {
            (listener.get() as OnPayListener).onPayResult(ALI_TYPE, errCode, result)
        }
    }
}
package com.example.intentapplication.payutil

import android.content.Context
import android.widget.Toast
import com.example.intentapplication.BaseApplication
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

class WeiXinPayEntry : PayEntry() {
    private var mApi: IWXAPI? = null
    private var mModel: WeiXinPayModel? = null

    init {
        // WEI_XIN_APP_ID 个人app_id
        mApi = WXAPIFactory.createWXAPI(BaseApplication.mContext, WEI_XIN_APP_ID, true)
        val result = mApi!!.registerApp(WEI_XIN_APP_ID)
        if (result) {
            Toast.makeText(BaseApplication.mContext, "success", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(BaseApplication.mContext, "failed", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val WEI_XIN_TYPE = 2
        private var instance: WeiXinPayEntry? = null
        const val WEI_XIN_APP_ID = "id"

        class WeiXinPayModel {
            var appid: String? = null
            var partnerid: String? = null
            var prepayid: String? = null
            var packageValue: String? = null
            var noncestr: String? = null
            var timestamp: String? = null
            var sign: String? = null
        }

        fun isWeiXinAvailable(context: Context): Boolean {
            val packageManager = context.packageManager
            val pinfo = packageManager.getInstalledPackages(0)
            if (pinfo != null) {
                for (i in pinfo.indices) {
                    val pn = pinfo[i].packageName
                    if (pn == "com.tencent.mm") {
                        return true
                    }
                }
            }
            return false
        }

        fun getInstance(): WeiXinPayEntry? {
            if (instance == null) {
                synchronized(WeiXinPayEntry::class.java) {
                    instance = WeiXinPayEntry()
                }
            }
            return instance
        }

    }


    override fun setModel(obj: Any?) {
        obj?.let {
            mModel = it as WeiXinPayModel
        }
    }

    override fun pay() {
        if (isPaying())return
        payStart()
        mModel?.apply {
            val payReq = PayReq()
            payReq.appId = appid
            payReq.partnerId = partnerid
            payReq.prepayId = prepayid
            payReq.packageValue = packageValue
            payReq.nonceStr = noncestr
            payReq.timeStamp = timestamp
            payReq.sign = sign
            if (mApi?.sendReq(payReq) == true) Toast.makeText(BaseApplication.mContext,"send pay success",Toast.LENGTH_LONG).show()
        }
    }

    override fun notifyResult(errCode: Int, result: String?) {
        payEnd()
        for (listener in mListeners){
            (listener.get() as PayEntry.OnPayListener).onPayResult(WEI_XIN_TYPE, errCode, result)
        }
    }
}
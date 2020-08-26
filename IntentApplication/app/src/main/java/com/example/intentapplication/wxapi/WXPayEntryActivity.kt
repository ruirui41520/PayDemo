package com.example.intentapplication.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.intentapplication.payutil.WeiXinPayEntry
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory

class WXPayEntryActivity:Activity(), IWXAPIEventHandler {
    companion object{
        private val TAG = "WXPayEntryActivity"
    }
    private var api: IWXAPI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = WXAPIFactory.createWXAPI(this,WeiXinPayEntry.WEI_XIN_APP_ID)
        api!!.handleIntent(intent,this)
        api!!.registerApp(WeiXinPayEntry.WEI_XIN_APP_ID)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        api!!.handleIntent(intent, this)
    }

    override fun onResp(p0: BaseResp?) {
        p0?.apply {
            WeiXinPayEntry.getInstance()?.notifyResult(errCode,"")
            if (type == ConstantsAPI.COMMAND_PAY_BY_WX) finish()
        }
    }

    override fun onReq(p0: BaseReq?) {
    }

}
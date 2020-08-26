package com.example.intentapplication

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.example.intentapplication.payutil.AliPayEntry
import com.example.intentapplication.payutil.PayEntry
import com.example.intentapplication.payutil.WeiXinPayEntry
import kotlinx.android.synthetic.main.activity_pay.*

class PayActivity : Activity(), PayEntry.OnPayListener {
    private var aliPayEntry: AliPayEntry? = null
    private var weiXinPayEntry: WeiXinPayEntry? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)
        aliPayEntry = AliPayEntry.getInstance()
        weiXinPayEntry = WeiXinPayEntry.getInstance()
        ali_pay.setOnClickListener { aliPay() }
    }

    private fun aliPay() {
        aliPayEntry?.apply {
            // todo get model from server
            setModel("pay info")
            registerListener(this@PayActivity)
            mActivity = this@PayActivity
            pay()
        }
    }

    private fun wePay() {
        weiXinPayEntry?.apply {
            registerListener(this@PayActivity)
            setModel("WeiXinPayModel")
            pay()
        }
    }

    override fun onPayResult(type: Int, errCode: Int, result: String?) {
        when (type) {
            AliPayEntry.ALI_TYPE ->
                if (errCode == AliPayEntry.CODE_9000) {
                    Toast.makeText(this, "支付宝支付成功", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "支付宝支付失败", Toast.LENGTH_SHORT).show()
                }
            WeiXinPayEntry.WEI_XIN_TYPE ->
                Toast.makeText(baseContext, "dd", Toast.LENGTH_LONG).show()

        }
    }
}
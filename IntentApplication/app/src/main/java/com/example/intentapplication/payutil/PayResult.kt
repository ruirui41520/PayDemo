package com.example.intentapplication.payutil

import android.text.TextUtils

class PayResult {

    var resultStatus: String? = null
    var result: String? = null
    var memo: String? = null

    constructor(rawResult: Map<String?, String?>?) {
        if (rawResult == null) {
            return
        }
        for (key in rawResult.keys) {
            when {
                TextUtils.equals(key, "resultStatus") -> {
                    resultStatus = rawResult[key]
                }
                TextUtils.equals(key, "result") -> {
                    result = rawResult[key]
                }
                TextUtils.equals(key, "memo") -> {
                    memo = rawResult[key]
                }
            }
        }
    }
}
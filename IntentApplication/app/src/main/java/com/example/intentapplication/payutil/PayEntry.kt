package com.example.intentapplication.payutil

import java.lang.ref.WeakReference
import java.util.*

abstract class PayEntry {
    interface OnPayListener {
        fun onPayResult(type: Int, errCode: Int, result: String?)
    }
    private var mPaying = false
    var mListeners : ArrayList<WeakReference<OnPayListener>>

    constructor(){
        mListeners = ArrayList<WeakReference<OnPayListener>>()
    }

    fun registerListener(listener: OnPayListener?){
        listener?.let {
            for (wr in mListeners){
                if (wr.get() ==listener)
                    return
            }
        }
        mListeners.add(WeakReference<OnPayListener>(listener))
    }

    fun unregisterListener(listener: OnPayListener?) {
        if (listener != null) {
            for (wr in mListeners) {
                if (wr.get() == null || wr.get() === listener) {
                    mListeners.remove(wr)
                    break
                }
            }
        }
    }

    protected fun isPaying(): Boolean {
        return mPaying
    }

    protected fun payStart() {
        mPaying = true
    }

    protected fun payEnd() {
        mPaying = false
    }

    abstract fun setModel(obj: Any?)

    abstract fun pay()

    abstract fun notifyResult(errCode: Int, result: String?)

}
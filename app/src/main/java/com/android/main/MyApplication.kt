package com.android.main

import android.app.Application
import com.llj.baselib.IOTLib
import com.llj.baselib.bean.UserConfigBean

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val bean = UserConfigBean(
            userId = "19203",
            appKey = "d711918c84",
            deviceId = "25849",
            clientId = "1206",
            clientSecret = "c9183edb25"
        )
        IOTLib.init(applicationContext, bean)
        list.add(ReserveDataBean(IOTLib.getUcb().deviceId.toInt(), (2..8).random(),(1..3).random()))
        list.add(ReserveDataBean(IOTLib.getUcb().deviceId.toInt() - 2, (2..8).random(),(1..3).random()))
    }

    companion object {
        var list = mutableListOf<ReserveDataBean>()
    }
}
package com.android.main

import androidx.lifecycle.MutableLiveData
import com.android.main.ui.MainActivity
import com.llj.baselib.IOTLib
import com.llj.baselib.IOTViewModel

class MainVM:IOTViewModel() {

    val reserveNumber = MutableLiveData<Int>(IOTLib.getSP(MainActivity.USER).getInt(MainActivity.USER_NUMBER,4))
    val absentNumber = MutableLiveData<Int>(IOTLib.getSP(MainActivity.USER).getInt(MainActivity.USER_ABSENT,1))

    val devState = MutableLiveData<Boolean>(false)

    fun reserveDev(){
//        sendOrderToDevice("A")
    }

    fun cancelDev(){
//        sendOrderToDevice("B")
    }

}
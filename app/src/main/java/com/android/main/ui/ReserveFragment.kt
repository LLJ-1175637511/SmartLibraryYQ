package com.android.main.ui

import com.android.main.MyApplication
import com.android.main.R
import com.android.main.ReserveFragmentInterf
import com.android.main.ReserveInterf
import com.android.main.databinding.FragmentReserveBinding

class ReserveFragment private constructor(): BaseDialog<FragmentReserveBinding>(),ReserveFragmentInterf{

    override fun getLayoutId() = R.layout.fragment_reserve

    private var callBack: ReserveInterf? = null

    fun initDialogListener(callBack: ReserveInterf) {
        this.callBack = callBack
    }

    override fun initCreateView() {
        super.initCreateView()
        mDataBinding.ivQuit.setOnClickListener {
            destroyDialog()
        }
        mDataBinding.recylerView.apply {
            val l = MyApplication.list
            adapter = ReserveRV(l,this@ReserveFragment)
        }
    }

    companion object {
        @JvmStatic
        fun instance() = ReserveFragment()
    }

    override fun selectId(i: Int) {
        callBack?.selectId(i)
        destroyDialog()
    }

}
package com.android.main.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.android.main.MainVM
import com.android.main.R
import com.android.main.ReserveInterf
import com.android.main.databinding.ActivityMainBinding
import com.android.main.databinding.DialogSelectSureBinding
import com.llj.baselib.IOTLib
import com.llj.baselib.IOTViewModel
import com.llj.baselib.save
import com.llj.baselib.ui.IOTMainActivity
import com.llj.baselib.utils.ToastUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : IOTMainActivity<ActivityMainBinding>(), ReserveInterf {

    override fun getLayoutId() = R.layout.activity_main

    private val vm by viewModels<MainVM>()

    private var reserveDialog: AlertDialog? = null
    private var reserveJob: Job? = null

    override fun init() {
        super.init()
//        vm.connect(this, MainDataBean::class.java)
        initMainView()
    }

    private fun initMainView() {
        initToolbar()
        vm.reserveNumber.observe(this) {
            if (it == null) return@observe
            mDataBinding.tvNumber.text = it.toString()
            IOTLib.getSP(USER).save {
                putInt(USER_NUMBER, it)
            }
        }
        vm.absentNumber.observe(this) {
            if (it == null) return@observe
            mDataBinding.tvAbsent.text = it.toString()
            IOTLib.getSP(USER).save {
                putInt(USER_ABSENT, it)
            }
        }
        mDataBinding.tvReserve.setOnClickListener {
            val f = ReserveFragment.instance()
            f.initDialogListener(this)
            showDialog(f, "reserve")
        }
    }

    private fun initToolbar() {
        mDataBinding.toolbar.apply {
            toolbarBaseTitle.text = getString(R.string.app_name)
            toolbarBase.inflateMenu(R.menu.toolbar_menu)
            toolbarBase.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.quit_app -> {
                        startCommonActivity<LoginActivity>()
                        finish()
                    }
                }
                false
            }
        }
    }

    override fun offDevLine() {
        vm.devState.postValue(false)
    }

    override fun onDevLine() {
        vm.devState.postValue(true)
    }

    override fun realData(data: Any?) {
        //??????????????????????????????
        cancelReserve()
        vm.reserveNumber.postValue(vm.reserveNumber.value!! + 1)
        ToastUtils.toastShort("???????????????????????????????????????")
    }

    override fun webState(state: IOTViewModel.WebSocketType) {

    }

    override fun selectId(i: Int) {
        vm.reserveDev()
        buildDialog()
    }

    @SuppressLint("ResourceAsColor")
    private fun buildDialog() {
        val reserveBinding = DataBindingUtil.inflate<DialogSelectSureBinding>(
            layoutInflater,
            R.layout.dialog_select_sure,
            null,
            false
        )
        reserveBinding.lifecycleOwner?.let { lifecycleOwner ->
            vm.devState.observe(lifecycleOwner){
                if (it == null) return@observe
                if (it) {
                    reserveBinding.tvDevState.setTextColor(R.color.greenDark)
                    reserveBinding.tvDevState.text = "??????"
                }else{
                    reserveBinding.tvDevState.setTextColor(R.color.red)
                    reserveBinding.tvDevState.text = "??????"
                }
            }
        }
        reserveDialog = AlertDialog.Builder(this)
            .setView(reserveBinding?.root)
            .setCancelable(false)
            .create()
        reserveBinding?.tvCanncel?.setOnClickListener {
            vm.cancelDev()
            cancelReserve()
        }
        //??????????????????
        reserveJob = lifecycleScope.launch {
            val allTime = 60
            repeat(allTime) {
                val t = "??????${allTime - it}s???????????????????????????"
                reserveBinding?.tvMessage?.text = t
                delay(1000)
            }
            ToastUtils.toastShort("??????????????????????????????")
            vm.absentNumber.postValue(vm.absentNumber.value!! + 1)
            cancelReserve()
        }
        reserveDialog?.show()
    }

    private fun cancelReserve(){
        reserveJob?.cancel()
        reserveJob = null
        reserveDialog?.cancel()
    }

    private fun showDialog(df: DialogFragment, tag: String) {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        val prev: Fragment? = supportFragmentManager.findFragmentByTag(tag)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        df.show(ft, tag)
    }

    companion object {
        const val USER = "USER"
        const val USER_NUMBER = "USER_NUMBER"
        const val USER_ABSENT = "USER_ABSENT"
    }
}
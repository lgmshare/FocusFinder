package com.aomiga.focus.ui

import android.Manifest
import android.content.Intent
import com.aomiga.focus.app.BaseActivity
import com.aomiga.focus.databinding.StartPermissionActivityBinding
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.PermissionUtils.FullCallback


class StartPermissionActivity : BaseActivity<StartPermissionActivityBinding>() {

    override fun onBuildBinding(): StartPermissionActivityBinding {
        return StartPermissionActivityBinding.inflate(layoutInflater)
    }

    override fun onInitView() {
        binding.btnAgree.setOnClickListener {
            requestPermission()
        }
        binding.btnCancel.setOnClickListener {
            ActivityUtils.finishAllActivities()
        }
    }

    private fun requestPermission() {
        PermissionUtils.permission(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE).callback(object : FullCallback {
            override fun onGranted(granted: MutableList<String>) {
                startActivity(Intent(this@StartPermissionActivity, MainActivity::class.java))
                finish()
            }

            override fun onDenied(deniedForever: MutableList<String>, denied: MutableList<String>) {
                if (deniedForever.isNotEmpty()) {
                    showTipsDialog("Please enable mobile camera permission in settings") {
                        start()
                    }
                }
            }
        }).request()
    }

    override fun onBackPressed() {
    }

    //跳转
    private fun start() {
        PermissionUtils.launchAppDetailsSettings()
    }
}
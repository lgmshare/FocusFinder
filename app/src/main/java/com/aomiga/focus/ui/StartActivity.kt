package com.aomiga.focus.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.aomiga.focus.app.BaseActivity
import com.aomiga.focus.databinding.StartActivityBinding

class StartActivity : BaseActivity<StartActivityBinding>() {
    override fun onBuildBinding(): StartActivityBinding {
        return StartActivityBinding.inflate(layoutInflater)
    }

    override fun onInitView() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                startActivity(Intent(this@StartActivity, StartPermissionActivity::class.java))
            } else {
                startActivity(Intent(this@StartActivity, MainActivity::class.java))
            }
            finish()
        } else {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                || checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
            ) {
                startActivity(Intent(this@StartActivity, StartPermissionActivity::class.java))
            } else {
                startActivity(Intent(this@StartActivity, MainActivity::class.java))
            }
            finish()
        }
    }
}
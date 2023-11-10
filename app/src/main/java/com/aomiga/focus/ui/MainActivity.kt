package com.aomiga.focus.ui

import android.content.Intent
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.aomiga.focus.R
import com.aomiga.focus.app.BaseActivity
import com.aomiga.focus.databinding.MainActivityBinding
import com.aomiga.focus.utils.GlideEngine
import com.blankj.utilcode.util.LogUtils
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.style.BottomNavBarStyle
import com.luck.picture.lib.style.PictureSelectorStyle
import com.luck.picture.lib.style.SelectMainStyle
import com.luck.picture.lib.style.TitleBarStyle


class MainActivity : BaseActivity<MainActivityBinding>() {

    override fun onBuildBinding(): MainActivityBinding {
        return MainActivityBinding.inflate(layoutInflater)
    }

    override fun onInitView() {
        binding.run {
            btnMagnifier.setOnClickListener {
                log(1)
                startActivity(Intent(this@MainActivity, CaptureActivity::class.java))
            }

            btnEnlarger.setOnClickListener {
                log(2)
                pickImage()
            }
        }
    }

    private fun pickImage() {
        val whiteTitleBarStyle = TitleBarStyle()
        whiteTitleBarStyle.titleBackgroundColor = ContextCompat.getColor(this@MainActivity, R.color.ps_color_white)
        whiteTitleBarStyle.titleDrawableRightResource = R.drawable.ic_arrow_down_24
        whiteTitleBarStyle.titleLeftBackResource = R.mipmap.ps_ic_black_back
        whiteTitleBarStyle.titleTextColor = ContextCompat.getColor(this@MainActivity, R.color.ps_color_black)
        whiteTitleBarStyle.titleCancelTextColor = ContextCompat.getColor(this@MainActivity, R.color.ps_color_53575e)
        whiteTitleBarStyle.isDisplayTitleBarLine = true

        val whiteBottomNavBarStyle = BottomNavBarStyle()
        whiteBottomNavBarStyle.bottomNarBarBackgroundColor = Color.parseColor("#EEEEEE")
        whiteBottomNavBarStyle.bottomPreviewSelectTextColor = ContextCompat.getColor(this@MainActivity, R.color.ps_color_53575e)

        whiteBottomNavBarStyle.bottomPreviewNormalTextColor = ContextCompat.getColor(this@MainActivity, R.color.ps_color_9b)
        whiteBottomNavBarStyle.bottomPreviewSelectTextColor = ContextCompat.getColor(this@MainActivity, R.color.ps_color_0077F6)
        whiteBottomNavBarStyle.isCompleteCountTips = false
        whiteBottomNavBarStyle.bottomEditorTextColor = ContextCompat.getColor(this@MainActivity, R.color.ps_color_53575e)
        whiteBottomNavBarStyle.bottomOriginalTextColor = ContextCompat.getColor(this@MainActivity, R.color.ps_color_53575e)

        val selectMainStyle = SelectMainStyle()
        selectMainStyle.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.ps_color_white)
        selectMainStyle.isDarkStatusBarBlack = true
        selectMainStyle.selectNormalTextColor = ContextCompat.getColor(this@MainActivity, R.color.ps_color_9b)
        selectMainStyle.selectTextColor = ContextCompat.getColor(this@MainActivity, R.color.ps_color_0077F6)
        selectMainStyle.previewSelectBackground = R.drawable.ps_white_preview_selector
        selectMainStyle.selectBackground = R.drawable.ps_checkbox_selector
        selectMainStyle.setSelectText(R.string.ps_done_front_num)
        selectMainStyle.mainListBackgroundColor = ContextCompat.getColor(this@MainActivity, R.color.ps_color_white)

        val selectorStyle = PictureSelectorStyle()
        selectorStyle.setTitleBarStyle(whiteTitleBarStyle)
        selectorStyle.setBottomBarStyle(whiteBottomNavBarStyle)
        selectorStyle.setSelectMainStyle(selectMainStyle)

        PictureSelector.create(this@MainActivity)
            .openGallery(SelectMimeType.ofImage())
            .isQuickCapture(false)
            .isAutoVideoPlay(false)
            .isDisplayCamera(false)
            .setMaxSelectNum(1)
            .setSelectorUIStyle(selectorStyle)
            .setImageEngine(GlideEngine.createGlideEngine())
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>) {
                    val localMedia = result[0]
                    val path: String = localMedia.availablePath
                    startActivity(Intent(this@MainActivity, MagnifierActivity::class.java).apply {
                        putExtra("path", path)
                    })
                }

                override fun onCancel() {

                }
            })
    }

    private fun log(type: Int) {
        when (type) {
            1 -> {
                LogUtils.d("FocusFinder", "capture")
            }

            2 -> {
                LogUtils.d("FocusFinder", "pick image")
            }
        }
    }
}
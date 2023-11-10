package com.aomiga.focus.app

import android.app.ActionBar
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.aomiga.focus.databinding.DialogSimpleBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    lateinit var binding: VB

    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = onBuildBinding()
        setContentView(binding.root)
        onInitView()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    abstract fun onBuildBinding(): VB

    abstract fun onInitView()

    fun showTipsDialog(text: String, confirmCallback: (() -> Unit)) {
        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
        val rootBinding = DialogSimpleBinding.inflate(layoutInflater)
        dialog = Dialog(this).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            setContentView(rootBinding.root)
        }

        rootBinding.tvContent.text = text

        rootBinding.btnConfirm.setOnClickListener {
            dialog?.dismiss()
            confirmCallback.invoke()
        }
        rootBinding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
        dialog?.window?.also {
            it.attributes?.also { attr ->
                attr.width = ActionBar.LayoutParams.MATCH_PARENT
                attr.height = ActionBar.LayoutParams.WRAP_CONTENT
                it.attributes = attr
            }
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setGravity(Gravity.BOTTOM)
        }
        dialog?.show()
    }

    override fun onPause() {
        super.onPause()
        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
        dialog = null
    }


}
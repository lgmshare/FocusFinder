package com.aomiga.focus.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.aomiga.focus.R
import com.aomiga.focus.app.BaseActivity
import com.aomiga.focus.databinding.ImagePreviewActivityBinding
import com.aomiga.focus.utils.Utils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.ToastUtils
import com.github.chrisbanes.photoview.OnScaleChangedListener
import com.otaliastudios.cameraview.PictureResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class ImagePreviewActivity : BaseActivity<ImagePreviewActivityBinding>() {

    private var saveSuccess = false

    companion object {
        var pictureResult: PictureResult? = null
    }

    override fun onBuildBinding(): ImagePreviewActivityBinding {
        return ImagePreviewActivityBinding.inflate(layoutInflater)
    }

    override fun onInitView() {
        val result = pictureResult ?: run {
            finish()
            return
        }
        val imageView = findViewById<ImageView>(R.id.image)

        try {
            result.toBitmap(5120, 5120) { bitmap -> imageView.setImageBitmap(bitmap) }
        } catch (e: UnsupportedOperationException) {
            imageView.setImageDrawable(ColorDrawable(Color.GREEN))
            Toast.makeText(this, "Can't preview this format: " + result.format, Toast.LENGTH_LONG).show()
        }
        if (result.isSnapshot) {
            // Log the real size for debugging reason.
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeByteArray(result.data, 0, result.data.size, options)
            if (result.rotation % 180 != 0) {
                Log.e("PicturePreview", "The picture full size is ${result.size.height}x${result.size.width}")
            } else {
                Log.e("PicturePreview", "The picture full size is ${result.size.width}x${result.size.height}")
            }
        }

        binding.image.minimumScale = 1f
        binding.image.maximumScale = 8.0f
        binding.image.setOnScaleChangeListener(object : OnScaleChangedListener {
            override fun onScaleChange(scaleFactor: Float, focusX: Float, focusY: Float) {
                Log.d("PicturePreviewActivity", "scaleFactor:" + binding.image.scale)
                binding.seekBar.progress = ((binding.image.scale * 100).toInt() - 100)
            }
        })

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnDownload.setOnClickListener {
            lifecycleScope.launch {
                launch {
                    disableView(false)
                    disableClick(false)
                    result.toBitmap(2156, 2156) { bitmap ->
                        if (bitmap != null) {
                            saveSuccess = true
                            saveImage(bitmap)
                            ToastUtils.showLong(R.string.save_tips)
                        } else {
                            saveSuccess = false
                            disableView(true)
                            disableClick(true)
                            ToastUtils.showLong(R.string.save_failed_tips)
                        }
                    }
                }
                launch {
                    delay(3500)
                    if (saveSuccess) {
                        startActivity(Intent(this@ImagePreviewActivity, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2) {
                    var scaleFloat = ((p1.toFloat() + 100) / 100).toFloat()
                    if (scaleFloat < 1) {
                        scaleFloat = 1f
                    }
                    if (scaleFloat > 8) {
                        scaleFloat = 8.0f
                    }
                    binding.image.scale = scaleFloat
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        binding.btnZoomIn.setOnClickListener {
            toggleZoom(true)
        }

        binding.btnZoomOut.setOnClickListener {
            toggleZoom(false)
        }
    }

    private fun toggleZoom(div: Boolean) {
        var scaleFloat = binding.image.scale
        if (div) {
            if (scaleFloat > 1) {
                scaleFloat -= 1
            }
        } else {
            if (scaleFloat < 8) {
                scaleFloat += 1
            }
        }

        if (scaleFloat < 1) {
            scaleFloat = 1f
        } else if (scaleFloat > 8) {
            scaleFloat = 8.0f
        }

        Log.d("PicturePreviewActivity", "scaleFloat:$scaleFloat")
        binding.image.scale = scaleFloat
        binding.seekBar.progress = ((scaleFloat * 100).toInt() - 100)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isChangingConfigurations) {
            pictureResult = null
        }
    }

    private fun saveImage(bitmap: Bitmap): Uri? {
        val path = PathUtils.getFilesPathExternalFirst() + "/capture/" + System.currentTimeMillis().toString() + ".jpg"
        FileUtils.createOrExistsFile(path)
        val imageFile: File = FileUtils.getFileByPath(path)
        LogUtils.d("FocusFinder", path)
        try {
            val fos = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Utils.insertImageToAlbum(this@ImagePreviewActivity, imageFile)
    }

    private fun disableView(boolean: Boolean) {
        binding.btnZoomOut.isEnabled = boolean
        binding.btnZoomIn.isEnabled = boolean
    }

    private fun disableClick(boolean: Boolean) {
        binding.btnBack.isEnabled = boolean
        binding.btnDownload.isEnabled = boolean
        binding.seekBar.isEnabled = boolean
    }
}
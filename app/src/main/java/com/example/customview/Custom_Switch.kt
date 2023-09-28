package com.example.customview

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.media.MediaPlayer
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.M)
class Custom_Switch @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr) {

    private val imageView: ImageView
    private val switch: Switch
    private val cameraManager: CameraManager =
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    init {
        orientation= VERTICAL
        gravity=Gravity.CENTER
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.PersonView)
        val image = typedArray.getResourceId(R.styleable.PersonView_image, 0)
        val text = typedArray.getString(R.styleable.PersonView_text)
        typedArray.recycle()

        imageView = ImageView(context).apply {
            layoutParams = LayoutParams(250.dp, 250.dp)
            if (image != 0) setImageResource(image)
        }

        switch = Switch(context).apply {
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                setMargins(0, 0, 0, 60.dp)

                setThumbResource(R.drawable.thumb)
                setTrackResource(R.drawable.track)
                textOn="on"
                textOff="off"
                showText=true
                setOnCheckedChangeListener{
                    _,ischeaked->
                    val player = MediaPlayer.create(context,R.raw.ring)
                    player.start()

                    if (isChecked){
                        imageView.setImageResource(R.drawable.img)
                       turnOnFlashlight()
                    }
                    else {
                        imageView.setImageResource(R.drawable.img_1)
                        tag=0
                        turnOffFlashlight()
                    }
                }
            }
        }
        addView(switch)
        addView(imageView)

    }

    fun setImage(resId: Int) {
        imageView.setImageResource(resId)
    }

    fun getIscheaked():Boolean{
        return switch.isChecked
    }



    fun turnOnFlashlight() {
        try {
            val cameraIdList = cameraManager.cameraIdList
            val cameraId = cameraIdList[0] // Assume the first camera id is the back camera
            cameraManager.setTorchMode(cameraId, true)
        } catch (e: CameraAccessException) {
            // Handle exceptions
        }
    }

    fun turnOffFlashlight() {
        try {
            val cameraIdList = cameraManager.cameraIdList
            val cameraId = cameraIdList[0] // Assume the first camera id is the back camera
            cameraManager.setTorchMode(cameraId, false)
        } catch (e: CameraAccessException) {
            // Handle exceptions
        }
    }

}



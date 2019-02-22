package gcatech.net.scannercore

import android.content.Context
import android.hardware.Camera
import android.view.SurfaceHolder
import android.view.SurfaceView

class CameraPreview @JvmOverloads  constructor(context:Context,private val camera:Camera,private val previewCallback: Camera.PreviewCallback,private val autoFocusCallback: Camera.AutoFocusCallback)
    : SurfaceView(context) , SurfaceHolder.Callback {

    init {
        holder.addCallback(this)
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        camera.stopPreview()
        camera.setPreviewDisplay(holder)
        camera.setPreviewCallback(previewCallback)
        camera.startPreview()
        camera.autoFocus(autoFocusCallback)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        camera.stopPreview()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        camera.setPreviewDisplay(holder)
    }

}
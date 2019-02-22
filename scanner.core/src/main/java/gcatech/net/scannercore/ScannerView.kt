package gcatech.net.scannercore

import android.content.Context
import android.hardware.Camera
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.os.Handler
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import gcatech.net.scannercore.config.ConfigScannerView
import gcatech.net.scannercore.handler.IScannerNotify
import kotlinx.android.synthetic.main.scanner_view.view.*


class ScannerView @JvmOverloads  constructor(context: Context?,attrs: AttributeSet?,  defStyleAttr: Int = 0 )
    : RelativeLayout(context,attrs,defStyleAttr) {

    private  var  camera:Camera
    private  var  cameraPreview : CameraPreview? = null
    private  var  autoFocusHandler : Handler
    private var  previewing : Boolean = true
    private var processDetect = false
    private var isListener = false
    private  var readyfocus = false
    private lateinit var  scannerNotify : IScannerNotify
    private var detector  : FirebaseVisionBarcodeDetector
    private  var  metadata : FirebaseVisionImageMetadata? = null
    private  lateinit var  image : FirebaseVisionImage
    private  var  size : Camera.Size? = null
    private lateinit var result : Task<List<FirebaseVisionBarcode>>
    private var data : ByteArray? = null


    init {
        LayoutInflater.from(context).inflate(R.layout.scanner_view, this, true)
        autoFocusHandler = Handler()
        camera = Camera.open()
        camera.setDisplayOrientation(90)

        val options = FirebaseVisionBarcodeDetectorOptions.Builder().setBarcodeFormats(
                        FirebaseVisionBarcode.FORMAT_QR_CODE,
                        FirebaseVisionBarcode.FORMAT_PDF417).build()

        detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options)
    }


    public fun start(scannerNotify:IScannerNotify,config : ConfigScannerView?) {
        this.scannerNotify = scannerNotify
        initCamera()
    }

    private fun initCamera(){
        frameCameraPreview.removeAllViews()
        camera.release()
        camera = Camera.open()
        camera.setDisplayOrientation(90)

        val parameter = camera.parameters
        size =  parameter.previewSize

        parameter.setPictureSize(size?.width!!,size?.height!!)
        parameter.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
        camera.parameters = parameter

        metadata = FirebaseVisionImageMetadata.Builder()
                .setWidth(size?.width!!)
                .setHeight(size?.height!!)
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                .build()
        cameraPreview = CameraPreview(context!!, camera, previewCb, autoFocusCB)
        frameCameraPreview.addView(cameraPreview)
        camera.startPreview()
    }

    public fun restart(){
        if(!previewing){
            previewing = true
            processDetect = false
            initCamera()
        }
    }


    private fun stop(){
        if(previewing){
            camera.stopPreview()
            camera.setPreviewCallback(null)
            previewing = false
            cameraPreview = null
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop()
    }


    private var previewCb: Camera.PreviewCallback = Camera.PreviewCallback { data, _ ->
        try {
            if(!readyfocus){
                this.data = data
            }
        }
        catch (ex : Exception){
            scannerNotify.resultScanner(ex.message.toString())
        }

    }


    private val autoFocusCB: Camera.AutoFocusCallback = Camera.AutoFocusCallback { success, _ ->

        if(success && !isListener && data != null){
            readyfocus = success
            isListener = true
            image = FirebaseVisionImage.fromByteArray(data!!, metadata!!)
            result = detector.detectInImage(image)
            result.addOnSuccessListener { barcode  ->
                if(barcode?.size!! > 0){
                    val rawValue = barcode[0].rawValue
                    scannerNotify.resultScanner(rawValue.toString())
                    stop()
                }
                isListener = false
                readyfocus = false
            }.addOnFailureListener {
                scannerNotify.resultScanner("Error Scanner")
                isListener = false
                readyfocus = false
            }
        }
        autoFocusHandler.postDelayed(doAutoFocus, 1000)
    }

    private val doAutoFocus = Runnable {
        if (previewing){
            try {
                camera.autoFocus(autoFocusCB)
            }catch (ex:Exception){
             Log.e("Error",ex.message)
            }
        }
    }
}
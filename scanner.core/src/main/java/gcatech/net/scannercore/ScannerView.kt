package gcatech.net.scannercore

import android.content.Context
import android.hardware.Camera
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.os.Handler
import android.util.Log
import android.view.View
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import gcatech.net.scannercore.config.ConfigScannerView
import gcatech.net.scannercore.config.ModeScannerType
import gcatech.net.scannercore.handler.IScannerNotify
import kotlinx.android.synthetic.main.scanner_view.view.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap




class ScannerView @JvmOverloads  constructor(context: Context?,attrs: AttributeSet?,  defStyleAttr: Int = 0 )
    : RelativeLayout(context,attrs,defStyleAttr) {

    private lateinit var  camera:Camera
    private  var  cameraPreview : CameraPreview? = null
    private  var  autoFocusHandler : Handler
    private var  previewing : Boolean = true
    private var processDetect = false
    private var isListener = false
    private  var readyfocus = false
    private lateinit var  scannerNotify : IScannerNotify
    private var detectorCodeBar  : FirebaseVisionBarcodeDetector? = null
    private var detectorOcr  : FirebaseVisionTextRecognizer? = null
    private  var  metadata : FirebaseVisionImageMetadata? = null
    private  lateinit var  image : FirebaseVisionImage
    private  var  size : Camera.Size? = null
    private lateinit var result : Task<List<FirebaseVisionBarcode>>
    private var data : ByteArray? = null
    private var dataPreview : ByteArray? = null
    private var hatInit = false
    private var config : ConfigScannerView? = null
    private var frame : View? =  null
    private  lateinit var  bmp : Bitmap

    init {
        LayoutInflater.from(context).inflate(R.layout.scanner_view, this, true)
        autoFocusHandler = Handler()
    }


    fun start(scannerNotify:IScannerNotify,config : ConfigScannerView?, frame : View?) {
        this.frame = frame
        this.config = config
        when(config?.scannerType){
            ModeScannerType.CodeBar ->{
                val options = FirebaseVisionBarcodeDetectorOptions.Builder().setBarcodeFormats(
                        FirebaseVisionBarcode.FORMAT_PDF417).build()
                detectorCodeBar = FirebaseVision.getInstance().getVisionBarcodeDetector(options)
            }
            ModeScannerType.Ocr ->{
                detectorOcr = FirebaseVision.getInstance().onDeviceTextRecognizer
            }
        }

        this.scannerNotify = scannerNotify
        if(!hatInit){
            camera = Camera.open()
            hatInit = true
        }
        initCamera()
    }


    private fun initCamera(){
        frameCameraPreview.removeAllViews()
        camera.release()
        camera = Camera.open()

        camera.setDisplayOrientation(0)

        val parameter = camera.parameters
        size =  parameter.previewSize

        parameter.setPictureSize(size?.width!!,size?.height!!)
        parameter.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
        camera.parameters = parameter

      /*  if(frame == null){
            metadata = FirebaseVisionImageMetadata.Builder()
                    .setWidth(size?.width!!)
                    .setHeight(size?.height!!)
                    .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                    .setRotation(FirebaseVisionImageMetadata.ROTATION_0)
                    .build()
        }*/

        cameraPreview = CameraPreview(context!!, camera, previewCb, autoFocusCB)
        frameCameraPreview.addView(cameraPreview)
        camera.startPreview()
    }

    fun restart(){
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
                this.dataPreview = data
            }
        }
        catch (ex : Exception){
            scannerNotify.resultScanner(ex.message.toString(),bmp)
        }

    }
    private val autoFocusCB: Camera.AutoFocusCallback = Camera.AutoFocusCallback { success, _ ->

        if(success && !isListener){
            readyfocus = success
            isListener = true
            this.camera.takePicture(null,null) { bytes: ByteArray, camera ->
                camera.startPreview()
                data = bytes
                val bm = BitmapFactory.decodeByteArray(data!!,0,data?.size!!)
                bmp = if(frame != null) {
                    Bitmap.createBitmap(bm, frame?.x?.toInt()!!,frame?.y?.toInt()!!,
                            frame?.width!!, frame?.height!!)
                } else{
                    bm
                }

                //  image = FirebaseVisionImage.fromByteArray(bytes, metadata!!)
                image = FirebaseVisionImage.fromBitmap(bmp)
                decoder()
            }
        }
        autoFocusHandler.postDelayed(doAutoFocus, 1000)
    }

    private  fun  decoder(){
        when(config?.scannerType){
            ModeScannerType.CodeBar ->{
                decoderCodeBar()
            }
            ModeScannerType.Ocr ->{
                decoderOrc()
            }
        }

    }

    private fun decoderCodeBar(){
        result = detectorCodeBar?.detectInImage(image)!!
        result.addOnSuccessListener { barcode  ->
            if(barcode?.size!! > 0){
                val rawValue = barcode[0].rawValue
                stop()
                scannerNotify.resultScanner(rawValue.toString(),bmp)
            }
            isListener = false
            readyfocus = false
        }.addOnFailureListener {
            scannerNotify.resultScanner("Error Scanner",bmp)
            isListener = false
            readyfocus = false
        }
    }

    private fun decoderOrc(){
        detectorOcr?.processImage(image)
                ?.addOnSuccessListener { textResult ->
                    if(textResult != null && !textResult.text.isNullOrEmpty()){
                        stop()
                        scannerNotify.resultScanner(textResult.text,bmp)
                    }
                    isListener = false
                    readyfocus = false
                }
                ?.addOnFailureListener {
                    isListener = false
                    readyfocus = false
                }
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
package gcatech.net.scanner.activity

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import gcatech.net.scanner.R
import gcatech.net.scannercore.config.ConfigScannerView
import gcatech.net.scannercore.handler.IScannerNotify
import kotlinx.android.synthetic.main.scanner_activivy.*
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.content.Intent
import android.graphics.Bitmap
import gcatech.net.scanner.InputMode
import gcatech.net.scanner.config.InputType
import gcatech.net.scannercore.config.ModeScannerType
import java.util.*
import java.io.ByteArrayOutputStream


class ScannerActivity : AppCompatActivity(), IScannerNotify {

    private  var inputType : InputType? = null
    private  var iteration : Int = 0
    private  var macthcString : ArrayList<String>? = null
    private  var pictures : ArrayList<ByteArray>? = null

    companion object {
        const val  permissionRequest : Int = 100
        private  val typeMap : Map<InputType, ModeScannerType> = mapOf(Pair(InputType.CodeBar,ModeScannerType.CodeBar)
                ,Pair(InputType.Ocr,ModeScannerType.Ocr))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanner_activivy)
        val intent = intent
        val extras = intent.extras
        inputType = extras?.get(InputMode.typeModeKey) as InputType
        macthcString = arrayListOf()
        pictures = arrayListOf()
        this.parentView.viewTreeObserver.addOnGlobalLayoutListener {
            val height = this.parentView.height *0.9f
            val wight = height*(1.7f/1f)
            val layoutParams = gosh.layoutParams
            layoutParams.height = height.toInt()
            layoutParams.width = wight.toInt()
            gosh.layoutParams = layoutParams
        }
        getPermission()
    }

    private fun getPermission(){
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),permissionRequest)
        }
        else{
            startScanner()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode ==permissionRequest &&  grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startScanner()
        } else {
            this.finish()
        }
    }

    private  fun startScanner(){
        val config =  ConfigScannerView()
        config.scannerType = typeMap[inputType!!]!!
        scanner.start(this,config,gosh)
    }

    override fun resultScanner(result: String,  bm: Bitmap?) {
        iteration++
        val resizedBitmap = Bitmap.createScaledBitmap(
                bm, 450*(1.7f/1f).toInt(), 450, false)
        val bStream = ByteArrayOutputStream()
        resizedBitmap?.compress(Bitmap.CompressFormat.PNG, 60, bStream)
        val byteArray = bStream.toByteArray()
        if(iteration == 1 || !inputType?.isReplaceIteration!!){
            macthcString?.add(result)
            pictures?.add(byteArray!!)
        }else if(inputType?.isReplaceIteration!!)
        {
            macthcString?.set(0,result)
            pictures?.set(0,byteArray!!)
        }

       if(inputType?.iteration == iteration){
           val intent = Intent()
           intent.putExtra(InputMode.resultString, macthcString)
           intent.putExtra(InputMode.picturesResult, pictures)
           intent.putExtra(InputMode.typeModeKey, inputType)
           setResult(Activity.RESULT_OK, intent)
           finish()
        }
    }

}
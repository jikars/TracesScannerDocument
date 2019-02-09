package gcatech.net.qrscanner

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import gcatech.net.qrscanner.handler.QRResultHandler
import gcatech.net.qrscanner.providerqr.IQRProvider
import kotlinx.android.synthetic.main.view_qrscanner.view.*

class QRScannerView   @JvmOverloads  constructor( context: Context?, private val  attrs: AttributeSet?,private val  defStyleAttr: Int = 0 )
    : RelativeLayout(context,attrs,defStyleAttr) {

    private lateinit var scannerProvider : IQRProvider

    init {
        LayoutInflater.from(context).inflate(R.layout.view_qrscanner, this, true)
    }

    fun setConfig(qrResultHandler: QRResultHandler,providerQR: EnumProviderQR ){
        if(containQR.visibility != View.VISIBLE){
            containQR.visibility = View.VISIBLE
            scannerProvider =  providerQR.instanceView(context,attrs,defStyleAttr)
            scannerProvider.setQRResultHandler(qrResultHandler)
            if(scannerProvider is View){
                scannerProvider.setQRResultHandler(qrResultHandler)
                containQR.addView( scannerProvider as View, 0, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT))
            }
        }
    }

    fun startScanner() {
        if (containQR.visibility == View.VISIBLE) {
            scannerProvider.startScanner()
        }
    }

    fun startFlash(start:Boolean) {
        if (containQR.visibility == View.VISIBLE) {
            scannerProvider.startFlash(start)
        }
    }

    fun enabledAutoFocus(enabled:Boolean){
        if(containQR.visibility  == View.VISIBLE){
            scannerProvider.enabledAutoFocus(enabled)
        }
    }

    fun stopScanner(){
        if(containQR.visibility  == View.VISIBLE){
            scannerProvider.stopScanner()
        }
    }
}
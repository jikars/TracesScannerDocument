package gcatech.net.qrscanner.providerqr

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import gcatech.net.qrscanner.R
import gcatech.net.qrscanner.handler.QRResultHandler
import kotlinx.android.synthetic.main.view_zxingscannerprovier.view.*
import me.dm7.barcodescanner.zxing.ZXingScannerView

internal class ZXingScannerView @JvmOverloads  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int = 0 )
    : RelativeLayout(context,attrs,defStyleAttr), IQRProvider, ZXingScannerView.ResultHandler {

    override var qrResultHandler: QRResultHandler? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_zxingscannerprovier, this, true)
    }

    override fun startScanner() {
        if(qrResultHandler != null){
            scanner.setIsBorderCornerRounded(false)
            scanner.setResultHandler(this)
            scanner.setFormats(listOf(BarcodeFormat.PDF_417))
            scanner.startCamera()
        }
    }

    override fun handleResult(result: Result?) {
        qrResultHandler?.resultQR(result?.toString()!!)
    }

    override fun startFlash(start:Boolean) {
        scanner.flash = start
    }

    override fun enabledAutoFocus(enabled:Boolean) {
        scanner.setAutoFocus(enabled)
    }

    override fun stopScanner() {
        scanner.stopCamera()
    }


}

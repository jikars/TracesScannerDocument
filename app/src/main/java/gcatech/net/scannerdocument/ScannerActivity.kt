package gcatech.net.scannerdocument

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import gcatech.net.qrscanner.EnumProviderQR
import gcatech.net.qrscanner.handler.QRResultHandler
import kotlinx.android.synthetic.main.scanner_activity.*

class ScannerActivity : AppCompatActivity(), QRResultHandler {
    override fun resultQR(result: String) {
        Toast.makeText(this,result,Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanner_activity)
        scannerQR.setConfig(this,EnumProviderQR.ZXingScanner)
        scannerQR.startScanner()
        scannerQR.enabledAutoFocus(true)
    }
}

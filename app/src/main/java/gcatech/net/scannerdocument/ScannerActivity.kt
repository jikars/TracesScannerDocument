package gcatech.net.scannerdocument

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import gcatech.net.scanner.config.InputType
import gcatech.net.scannercore.handler.IScannerNotify
import gcatech.net.scannerui.models.document.colombia.CitizenshipCardCol
import kotlinx.android.synthetic.main.scanner_activity.*
import android.content.Intent
import gcatech.net.scannerui.config.ConfigScannerView
import gcatech.net.scannerui.mach.colombia.CitizenshipCardMachColCodeBar


class ScannerActivity : AppCompatActivity() {

     companion object {
         const val  CODE_REQUEST = 10
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanner_activity)
        val config = ConfigScannerView()
        config.type = CitizenshipCardCol::class
        config.machTypeDocument = mutableMapOf()
        config.machTypeDocument[InputType.CodeBar] = CitizenshipCardMachColCodeBar()
        scanner.start(config, CODE_REQUEST,this)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        scanner.onActivityResult(requestCode, resultCode, data)
    }

}

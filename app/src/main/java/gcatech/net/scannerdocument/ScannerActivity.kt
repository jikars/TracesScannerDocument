package gcatech.net.scannerdocument

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import gcatech.net.scannercore.handler.IScannerNotify
import kotlinx.android.synthetic.main.scanner_activity.*



class ScannerActivity : AppCompatActivity(), IScannerNotify {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanner_activity)
        scanner.start(this,null)
        btnRestart.setOnClickListener{
            scanner.restart()
        }
    }

    override fun resultScanner(result: String) {
        Toast.makeText(this,result,Toast.LENGTH_LONG).show()
    }

}

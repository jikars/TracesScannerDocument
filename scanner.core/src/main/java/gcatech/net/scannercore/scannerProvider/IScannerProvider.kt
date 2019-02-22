package gcatech.net.scannercore.scannerProvider
import android.util.Size
import gcatech.net.scannercore.config.ConfigScannerView
import gcatech.net.scannercore.handler.IScannerNotify

interface IScannerProvider {
    fun Start(scannerNotify: IScannerNotify, config : ConfigScannerView, orientation : Int,formatPicture : String , size : Size)
    fun Stop()
    fun ScanningPicture(file: ByteArray)
}
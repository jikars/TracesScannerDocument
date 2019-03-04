package gcatech.net.scannercore.handler

import android.graphics.Bitmap

interface IScannerNotify {
    fun resultScanner(result : String, bm: Bitmap?)
}
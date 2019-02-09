package gcatech.net.qrscanner.providerqr

import gcatech.net.qrscanner.handler.QRResultHandler

interface  IQRProvider {
    var qrResultHandler : QRResultHandler?
    fun startScanner()
    fun startFlash(start:Boolean)
    fun enabledAutoFocus(enabled:Boolean)
    fun stopScanner()
    fun setQRResultHandler(qrResultHandler : QRResultHandler){
        this.qrResultHandler = qrResultHandler
    }
}
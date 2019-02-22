package gcatech.net.scannerui.config

import gcatech.net.scanner.config.ScannerConfig

class ScannerDocumentConfig<T> {
    lateinit var  scannerConfig: List<ScannerConfig<T>>
    lateinit var  title : String
    var hasFingerPrint : Boolean = false
}
package gcatech.net.scanner.config

import gcatech.net.scannercore.config.FormatScanner
import gcatech.net.scannercore.config.ModeScannerType

enum class InputType(var mode: ModeScannerType?, var formats : List<FormatScanner>?){
    CodeBar(ModeScannerType.CodeBar, arrayListOf(FormatScanner.Pdf417)),
    Ocr(ModeScannerType.Ocr,null),
    CodeBarOcr(ModeScannerType.CodeBar,arrayListOf(FormatScanner.Pdf417)),
    WebService(null,null)
}
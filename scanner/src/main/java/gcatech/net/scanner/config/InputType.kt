package gcatech.net.scanner.config

import gcatech.net.scannercore.config.FormatScanner
import gcatech.net.scannercore.config.ModeScannerType
import java.io.Serializable

enum class InputType(val mode: ModeScannerType?, val formats : List<FormatScanner>?,val keyString : String, val iteration : Int, val isReplaceIteration : Boolean) : Serializable{
    CodeBar(ModeScannerType.CodeBar, arrayListOf(FormatScanner.Pdf417),"CodeBar",1,false),
    Ocr(ModeScannerType.Ocr,null,"Ocr",2,false),
    WebService(null,null,"WebServiceValue",1,false)
}
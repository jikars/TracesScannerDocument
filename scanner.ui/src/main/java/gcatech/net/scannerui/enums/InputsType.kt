package gcatech.net.scannerui.enums

import gcatech.net.scanner.config.InputType

enum class InputsType (val inputs : Map<InputType,Float>) {
    Ocr(mutableMapOf(Pair(InputType.Ocr,1.0f))),
    CodeBar(mutableMapOf(Pair(InputType.CodeBar,1.0f))),
    WebService(mutableMapOf(Pair(InputType.WebService,1.0f))),
    OcrCodeBar(mutableMapOf(Pair(InputType.Ocr,0.5f), Pair(InputType.CodeBar,0.5f))),
    OcrCodeBarWebService(mutableMapOf(Pair(InputType.Ocr,0.4f), Pair(InputType.CodeBar,0.3f),Pair(InputType.CodeBar,0.3f)))
}
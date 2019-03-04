package gcatech.net.scannerui.models.document.colombia

import gcatech.net.scannerui.annotation.TypeScanner
import gcatech.net.scannerui.annotation.UiConfig
import gcatech.net.scannerui.enums.InputsType
import gcatech.net.scannerui.enums.SideDocument
import gcatech.net.scannerui.models.document.ModelMach


class CitizenshipCardCol : ModelMach() {

    @UiConfig("name",true)
    @TypeScanner(InputsType.OcrCodeBar)
    var name : String? = null

    @UiConfig("numberIdentifier")
    var numberIdentifier : String? = null

    @TypeScanner(InputsType.OcrCodeBar)
    @UiConfig("birthDate")
    var birthDate : String? = null


    @TypeScanner(InputsType.OcrCodeBarWebService)
    @UiConfig("rh")
    var rh : String? = null

    @TypeScanner(InputsType.OcrCodeBar)
    @UiConfig("gender")
    var gender : String? = null

    @UiConfig("datePlaceOfExpedition")
    @TypeScanner(InputsType.OcrCodeBar)
    var datePlaceOfExpedition : String? = null

}
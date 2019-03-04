package gcatech.net.scannerui.models.document

import gcatech.net.scannerui.enums.SideDocument

abstract class ModelMach {
    var arrayPicture : Map<SideDocument, ByteArray>? = null
}
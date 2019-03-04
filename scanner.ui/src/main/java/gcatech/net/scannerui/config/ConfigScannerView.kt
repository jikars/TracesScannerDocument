package gcatech.net.scannerui.config

import gcatech.net.scanner.Mach
import gcatech.net.scanner.config.InputType
import kotlin.reflect.KClass

class ConfigScannerView{
    lateinit var   machTypeDocument  :   MutableMap<InputType, Mach>
    lateinit var  type : KClass<*>
}
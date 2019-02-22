package gcatech.net.scanner.config

import gcatech.net.scannerinterpreter.MachResponseConfig

class ScannerConfig<T>  {
    lateinit  var inputType: InputType
    var  matchResponseConfig: MachResponseConfig<T>? = null
}
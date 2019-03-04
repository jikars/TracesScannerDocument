package gcatech.net.scannerui.annotation

import gcatech.net.scannerui.enums.InputsType

@Repeatable
@Target(AnnotationTarget.PROPERTY)
annotation  class TypeScanner(val inputType: InputsType)



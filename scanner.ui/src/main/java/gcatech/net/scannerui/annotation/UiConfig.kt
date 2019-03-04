package gcatech.net.scannerui.annotation

@Target(AnnotationTarget.PROPERTY)
annotation class UiConfig(val name: String, val isBackPropierty : Boolean = false)
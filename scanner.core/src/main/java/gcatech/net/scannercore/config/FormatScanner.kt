package gcatech.net.scannercore.config

enum class FormatScanner(val typeScanner : ModeScannerType) {
    Pdf417(ModeScannerType.CodeBar),
    Qr(ModeScannerType.CodeBar)
}
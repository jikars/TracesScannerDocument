package gcatech.net.qrscanner

import android.content.Context
import android.util.AttributeSet
import gcatech.net.qrscanner.providerqr.IQRProvider
import gcatech.net.qrscanner.providerqr.ZXingScannerView

enum class EnumProviderQR : IEnumProviderQR {
    ZXingScanner{
        override fun instanceView(context: Context?,    attrs: AttributeSet?, defStyleAttr: Int  ): IQRProvider {
            return ZXingScannerView(context, attrs, defStyleAttr)
        }
    }
}
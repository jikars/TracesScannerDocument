package gcatech.net.qrscanner

import android.content.Context
import android.util.AttributeSet
import gcatech.net.qrscanner.providerqr.IQRProvider

interface IEnumProviderQR  {
    fun instanceView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int  ): IQRProvider
}
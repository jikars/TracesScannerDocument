package gcatech.net.scannerui.utils

import android.content.Context


class UtilsUi {
    fun getStringResourceByName(context: Context,name: String): String {
        val packageName = context.packageName
        val resId =context.resources.getIdentifier(name, "string", packageName)
        return context.getString(resId)
    }
}
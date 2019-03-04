package gcatech.net.scannerui.mach.colombia

import gcatech.net.scanner.Mach
import gcatech.net.scanner.model.ScannerResponse
import gcatech.net.scannerui.enums.SideDocument
import gcatech.net.scannerui.models.document.colombia.CitizenshipCardCol
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CitizenshipCardMachColCodeBar : Mach() {

    private var charsSpace  = arrayListOf("(\u0000){1,}")
    private var secuenceSubstring  = arrayListOf("O+","O-","B+","B-","A+","A-")
    private  var hasValue : Boolean = false

    override fun maching(machResult: ArrayList<String>, pictures: ArrayList<ByteArray>): Any {

        val text = machResult.joinToString { it }

        var list : MutableList<String>? = null
        secuenceSubstring.forEach {
            if (text.contains(it)) {
                if(!hasValue){
                    val mach = text.substring(0,text.indexOf(it )+ it.length)
                    charsSpace.forEach {
                        if(!hasValue){
                            list =  mach.split(it.toRegex()).toMutableList()
                            hasValue = list?.size!! >= 5
                        }
                    }
                }
            }
        }

        list?.removeAt(0)
        list?.removeAt(0)

        val cc =  list?.get(0)?.replace("[A-Za-z]".toRegex(),"")?.takeLast(10)?.replaceFirst("^0+(?!$)".toRegex(), "")

        val lastMap = list?.last()

        val name =  list?.get(0)?.split(cc!!)?.last() + " " +  list?.get(1)!! +
                " " +  list?.get(2)!! + " " +  if(list?.count()!! >= 4)  list?.get(3)!! else ""

        val gender = lastMap!![1]

        val date = lastMap.substring(2,10)

        val rh = lastMap.substring(lastMap.length-2, lastMap.length)

        val inputFormat = SimpleDateFormat("yyyyMMdd", Locale.US)
        val outputFormat = SimpleDateFormat("dd/MMM/yyyy",Locale.US)
        val dateOut = inputFormat.parse(date)
        val birthDate = outputFormat.format(dateOut)

        val citizenshipCardCol= CitizenshipCardCol()
        citizenshipCardCol.numberIdentifier = cc
        citizenshipCardCol.name =name
        citizenshipCardCol.gender = gender.toString()
        citizenshipCardCol.rh = rh
        citizenshipCardCol.birthDate = birthDate
        citizenshipCardCol.arrayPicture = mapOf(Pair(SideDocument.Back,pictures.first()))

        return citizenshipCardCol
    }

}


package gcatech.net.scanner

import android.content.Intent
import gcatech.net.scanner.activity.ScannerActivity
import gcatech.net.scanner.config.InputType
import android.app.Activity
import kotlin.Unit




class InputMode {

    private  lateinit  var context : Activity
    private  lateinit  var inputType: InputType
    private   var codeRequest: Int = 0

    companion object {
        private val values = mutableMapOf<InputType, (_context:Activity, _codeRequest : Int) -> Unit>()
        const val  typeModeKey = "Type"
        const val  resultString  = "ResultString"
        const val  picturesResult  = "PictureResult"
    }

    init {
        if(values.isEmpty()){
            values[InputType.CodeBar] = {  it,it2 ->
                run {
                    val intent = Intent(it, ScannerActivity::class.java)
                    intent.putExtra(typeModeKey, InputType.CodeBar)
                    it.startActivityForResult(intent, it2)
                }
            }


            values[InputType.Ocr] = {  it,it2 ->
                run {
                    val intent = Intent(it, ScannerActivity::class.java)
                    intent.putExtra(typeModeKey, InputType.Ocr)
                    it.startActivityForResult(intent, it2)
                }
            }
        }
    }

    fun  resolve(context: Activity, inputType: InputType, codeRequest : Int){
        this.context = context
        this.inputType = inputType
        this.codeRequest = codeRequest
        values[this.inputType]?.invoke(context,codeRequest)
    }

}
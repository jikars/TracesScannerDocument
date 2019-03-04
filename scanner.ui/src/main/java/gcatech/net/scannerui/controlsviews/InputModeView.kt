package gcatech.net.scannerui.controlsviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import gcatech.net.scanner.config.InputType
import gcatech.net.scannerui.R
import kotlinx.android.synthetic.main.input_mode_view.view.*

class InputModeView @JvmOverloads  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int = 0 )
    : RelativeLayout(context,attrs,defStyleAttr) {

    lateinit var  inputType : InputType
    private  lateinit var  inputField : InputFieldDocumentView
    private   var  value : String? = null

    init{
        LayoutInflater.from(context).inflate(R.layout.input_mode_view, this, true)
    }


    fun  start(inputType : InputType, inputField :  InputFieldDocumentView){
        this.inputType  = inputType
        this.inputField = inputField
        btnMod.text = inputType.keyString
        btnMod.setOnClickListener{
            if(value != null){
                inputField.changeSelect(inputType,value!!)
            }
        }
    }


    fun setValue( value : String?){
        if(value != null){
            this.value = value
            this.isEnabled = true
        }
    }
}


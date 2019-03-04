@file:Suppress("UNCHECKED_CAST")

package gcatech.net.scannerui.controlsviews

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import gcatech.net.scanner.config.InputType
import gcatech.net.scannerui.R
import gcatech.net.scannerui.annotation.TypeScanner
import gcatech.net.scannerui.annotation.UiConfig
import gcatech.net.scannerui.utils.UtilsUi
import kotlinx.android.synthetic.main.input_field_document_view.view.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation


class InputFieldDocumentView  @JvmOverloads  constructor(context: Context?, attrs: AttributeSet?,defStyleAttr: Int = 0 )
    : ConstraintLayout(context,attrs,defStyleAttr) {

    private lateinit var  field: KProperty1<*,*>
    private var  utilUi: UtilsUi
    private lateinit var scannerOptionView:ScannerDocumentsView
    private lateinit var typeScanner :  MutableMap<InputType,*>
    private var inputModes : MutableMap<InputType,InputModeView>
    private var valueSelect  : String? = null
    private   var inputTypeSelect : InputType? = null

    var  value: Any? = null

    init{
        LayoutInflater.from(context).inflate(R.layout.input_field_document_view, this, true)
        utilUi = UtilsUi()
        inputModes = mutableMapOf()
    }

    fun start(field :  KProperty1<*,*>,scannerOptionView:ScannerDocumentsView,typeScanner : MutableMap<InputType,*>) {
        this.typeScanner = typeScanner
        this.scannerOptionView = scannerOptionView
        this.field = field
        val annotation = field.findAnnotation<UiConfig>()

        val annotationType = field .findAnnotation<TypeScanner>()
        titleInput.text = utilUi.getStringResourceByName(context,annotation?.name!!)

        annotationType?.inputType?.inputs?.forEach{
            if(typeScanner.contains((it.key))){
                val inputTypeFiled = InputModeView(context,null,0)
                inputTypeFiled.start(it.key,this)
                inputTypeFiled.isEnabled = false
                inputModes[it.key] = inputTypeFiled
                inputModeContainer.addView(inputTypeFiled)
            }
        }
    }


    fun setValue(type : InputType){
        val obj = scannerOptionView.mapResultFromType[type]
        val value  = field.call(obj).toString()
        inputModes[type]?.setValue(value)
        if(valueSelect == null){
            valueSelect = value
            inputProp.setText(value, TextView.BufferType.EDITABLE)
            changeEdit(valueSelect)
            inputTypeSelect = type
        }
    }

    fun changeSelect(type : InputType, value: String){
        inputTypeSelect = type
        valueSelect = value
        changeEdit(valueSelect)
    }

    private  fun changeEdit(changeText:String?){
        if(changeText != null){
            inputProp.setText(changeText, TextView.BufferType.EDITABLE)
        }
    }
}
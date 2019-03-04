package gcatech.net.scannerui.controlsviews

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import gcatech.net.scanner.InputMode
import gcatech.net.scanner.Mach
import gcatech.net.scanner.config.InputType
import gcatech.net.scanner.model.ScannerResponse
import gcatech.net.scannerui.R
import gcatech.net.scannerui.annotation.UiConfig
import gcatech.net.scannerui.config.ConfigScannerView
import gcatech.net.scannerui.enums.SideDocument
import gcatech.net.scannerui.models.document.ModelMach
import kotlinx.android.synthetic.main.scanner_document_view.view.*
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

@Suppress("UNCHECKED_CAST")
class ScannerDocumentsView  @JvmOverloads  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int = 0 )
    : RelativeLayout(context,attrs,defStyleAttr) {

    internal  var mapResultFromType  = mutableMapOf<InputType, ModelMach>()
    private var codeRequest : Int = 0
    private lateinit var machTypeDocument : MutableMap<InputType, Mach>
    private lateinit var type: KClass<*>
    private var inputFieldDocumentViews : ArrayList<InputFieldDocumentView>


    init{
        LayoutInflater.from(context).inflate(R.layout.scanner_document_view, this, true)
        inputFieldDocumentViews = arrayListOf()
    }

    fun  start (typeConfig : ConfigScannerView, codeRequest:Int, activity: Activity){
        this.type = typeConfig.type
        this.codeRequest = codeRequest
        this.machTypeDocument = typeConfig.machTypeDocument
        this.machTypeDocument.forEach{
            val sca = ScannerOptionView(context,null,this.codeRequest)
            sca.start(it.key,this.codeRequest,activity)
            scannerOptions.addView(sca)
        }

        this.type.declaredMemberProperties.filter { it.annotations.filter { it.annotationClass ==  UiConfig::class }.count()>0 }.reversed().forEach{
            val field = InputFieldDocumentView(context,null,this.codeRequest)
            field.start(it,this,machTypeDocument)
            if(!inputFieldDocumentViews.contains(field)){
                inputFieldDocumentViews.add(field)
            }
            bottomPart.addView(field)
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        if(requestCode == codeRequest && resultCode == Activity.RESULT_OK){
            val inputType =  data?.extras?.get(InputMode.typeModeKey) as InputType?
            val value =  data?.extras?.getStringArrayList(InputMode.resultString) as ArrayList<String>
            val pictures =  data.extras?.getSerializable(InputMode.picturesResult) as ArrayList<ByteArray>
            if(inputType != null){
                mapResultFromType[inputType] = machTypeDocument[inputType]?.maching(value,pictures)!! as ModelMach
                setValuesScanner(inputType)
                setPictures(inputType)
            }
        }
    }

    private  fun setPictures(inputType: InputType){
        mapResultFromType[inputType]?.arrayPicture?.forEach{
            val bmp = BitmapFactory.decodeByteArray(it.value,0, it.value.size)
            when(it.key){
                SideDocument.Back->{
                    documentBack.setImageBitmap(bmp)
                }
                SideDocument.Front->{
                    documentFront.setImageBitmap(bmp)
                }
            }
        }
    }

    private fun setValuesScanner(inputType: InputType){
        inputFieldDocumentViews.forEach{
            it.setValue(inputType)
        }
    }
}
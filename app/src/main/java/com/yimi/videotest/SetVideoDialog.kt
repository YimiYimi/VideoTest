package com.yimi.videotest


import android.app.AlertDialog
import android.content.Context
import android.view.*
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.set_video_dialog.*

class SetVideoDialog(context: Context?) : AlertDialog(context), View.OnClickListener {

    var call: clickCallBack? = null;
    var text:TextView? = null
    var editVideo: EditText? = null;
    var buttonOk: View? = null;
    var buttonCancle: View? = null

    constructor(context: Context?, title: String, content: String, yesCallBack: clickCallBack)
            : this(context, title, content, yesCallBack, false) {
    }

    constructor(context: Context?, title: String, content: String, yesCallBack: clickCallBack, isEdit: Boolean) : this(context) {
        call = yesCallBack
        if (isEdit){
            editVideo?.visibility=View.VISIBLE
            editVideo?.requestFocus()                       //光标聚焦编辑框
            editVideo?.hint =  content
            text?.text = title
        }

    }

    init {
        val inflate = LayoutInflater.from(context).inflate(R.layout.set_video_dialog, null);
        setView(inflate)
        //设置点击别的区域不关闭页面
        setCancelable(false)

        text = inflate.findViewById<TextView>(R.id.dialog_text)
        editVideo = inflate.findViewById<EditText>(R.id.edit_video)
        buttonOk = inflate.findViewById<View>(R.id.button_ok)
        buttonCancle = inflate.findViewById<View>(R.id.button_cancle)

        buttonOk?.setOnClickListener(this)
        buttonCancle?.setOnClickListener{dismiss()}

//        findViewById<EditText>(R.id.edit_video).text ?: "https://vfx.mtime.cn/Video/2019/03/19/mp4/190319222227698228.mp4"
    }


    override fun onClick(p0: View?) {
        call?.yesClick(this);
    }

    interface clickCallBack {
        fun yesClick(dialog:SetVideoDialog)
    }

}
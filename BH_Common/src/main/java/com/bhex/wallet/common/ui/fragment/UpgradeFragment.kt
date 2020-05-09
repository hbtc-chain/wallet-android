package com.bhex.wallet.common.ui.fragment


import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.util.DisplayMetrics
import android.view.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bhex.lib.uikit.util.PixelUtils

import com.bhex.wallet.common.R

/**
 * @author gongdongyang
 * 升级对话框
 */
class UpgradeFragment : DialogFragment() {

    private var content:String? = null

    private var dialogOnClickListener:DialogOnClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val style = STYLE_NO_TITLE
        val theme = R.style.centerDialogStyle
        setStyle(style,theme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val mRootView =  inflater.inflate(R.layout.fragment_upgrade, container, false)
        val contentView = mRootView.findViewById<AppCompatTextView>(R.id.tv_upgrade_content)
        contentView.text = content

        mRootView.findViewById<AppCompatButton>(R.id.btn_cancel).setOnClickListener{
            dismiss()
        }

        mRootView.findViewById<AppCompatButton>(R.id.btn_confirm).setOnClickListener{
            dismiss()
            dialogOnClickListener?.onDialogNegativeClickListener(it)
        }
        return mRootView
    }

    override fun onStart() {
        super.onStart()

        val window:Window? = dialog!!.window
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val dm  = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(dm)

        var lp:WindowManager.LayoutParams = window.attributes
        lp.gravity = Gravity.CENTER;

        lp.width = dm.widthPixels -PixelUtils.dp2px(context,48f);
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT

        window.attributes = lp

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var dialog:Dialog =  super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        return dialog
    }

    companion object{
        fun showUpgradeDialog(content:String,listener: DialogOnClickListener):UpgradeFragment{
            val fragment:UpgradeFragment = UpgradeFragment()
            fragment.dialogOnClickListener = listener
            return  fragment
        }
    }

    interface DialogOnClickListener{
        fun onDialogNegativeClickListener(v:View)
    }
}

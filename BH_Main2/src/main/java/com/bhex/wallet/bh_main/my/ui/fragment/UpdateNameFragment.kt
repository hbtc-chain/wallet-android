package com.bhex.wallet.bh_main.my.ui.fragment


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.bhex.lib.uikit.util.PixelUtils
import com.bhex.wallet.bh_main.R


/**
 * @author gongdongyang
 * 2020年5月15日22:41:51
 * 修改用户名称
 */
class UpdateNameFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val style = STYLE_NO_TITLE
        setStyle(style,theme)
    }

    override fun onCreateView(inflater: LayoutInflater, container:
        ViewGroup?,savedInstanceState: Bundle?): View?{

        val mRootView = inflater.inflate(R.layout.fragment_update_name, container, false)
        mRootView.findViewById<AppCompatButton>(R.id.btn_cancel).setOnClickListener{
            dismiss();
        }
        mRootView.findViewById<AppCompatButton>(R.id.btn_confirm).setOnClickListener{
            dismiss();
            updateUserName();
        }
        return mRootView
    }

    override fun onStart() {
        super.onStart()
        val window:Window = dialog!!.window
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //获取屏幕的宽度
        val dm  = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(dm)

        var lp:WindowManager.LayoutParams = window.attributes
        lp.gravity = Gravity.CENTER

        lp.width = dm.widthPixels-PixelUtils.dp2px(activity,48f)
        lp.height = PixelUtils.dp2px(activity, 185f)
        lp.windowAnimations = R.style.centerDialogStyle
        window.attributes = lp
    }

    /**
     * 修改用户名称
     */
    fun updateUserName(){

    }

    companion object{
        fun showFragment():UpdateNameFragment{
            val fragment = UpdateNameFragment()
            return fragment
        }
    }
}

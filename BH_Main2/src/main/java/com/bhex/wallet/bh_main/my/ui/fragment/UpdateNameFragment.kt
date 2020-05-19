package com.bhex.wallet.bh_main.my.ui.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.*
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bhex.lib.uikit.util.PixelUtils
import com.bhex.lib.uikit.widget.InputView
import com.bhex.network.base.LoadDataModel
import com.bhex.network.base.LoadingStatus
import com.bhex.network.utils.ToastUtils
import com.bhex.wallet.bh_main.R
import com.bhex.wallet.common.manager.BHUserManager
import com.bhex.wallet.common.ui.fragment.UpgradeFragment
import com.bhex.wallet.common.viewmodel.WalletViewModel

/**
 * @author gongdongyang
 * 2020年5月15日22:41:51
 * 修改用户名称
 */
class UpdateNameFragment : DialogFragment() {
    var dialogOnClickListener: DialogOnClickListener? = null

    val wallet = BHUserManager.getInstance().currentBhWallet
    var walletViewModel:WalletViewModel? = null

    var walletNameInput: InputView?=null

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
            updateUserName();
        }

        walletNameInput = mRootView.findViewById(R.id.inp_wallet_name)
        walletNameInput?.inputString = wallet.name

        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        walletViewModel = ViewModelProviders.of(this).get(WalletViewModel::class.java)
        walletViewModel!!.mutableLiveData.observe(this, Observer {ldm->
            updateUserCallBack(ldm)
        })
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
        var name:String = walletNameInput!!.inputStringNoTrim
        if(TextUtils.isEmpty(name)){
            ToastUtils.showToast(getString(R.string.hint_input_username))
            return
        }
        wallet.name = name;
        walletViewModel!!.updateWalletName(this,wallet)

    }
    companion object{
        fun showFragment(listener:DialogOnClickListener):UpdateNameFragment{
            val fragment = UpdateNameFragment()
            fragment.dialogOnClickListener = listener
            return fragment
        }
    }

    /**
     * 更新用户名回调
     */
    fun updateUserCallBack(ldm:LoadDataModel<Any>){
        dismiss();
        if(ldm.loadingStatus == LoadingStatus.SUCCESS){
            if(dialogOnClickListener!=null){
                dialogOnClickListener?.onDialogNegativeClickListener(null)
            }
        }else{

        }
    }

    interface DialogOnClickListener{
        fun onDialogNegativeClickListener(v:View?)
    }
}

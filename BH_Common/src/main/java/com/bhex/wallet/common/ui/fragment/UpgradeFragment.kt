package com.bhex.wallet.common.ui.fragment


import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.view.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import com.bhex.lib.uikit.util.PixelUtils
import com.bhex.wallet.common.R
import com.bhex.wallet.common.download.ApkDownLoadService
import com.bhex.wallet.common.download.DownloadInfo
import com.bhex.wallet.common.manager.MMKVManager
import com.bhex.wallet.common.model.BHPhoneInfo
import com.bhex.wallet.common.model.UpgradeInfo
import java.io.File


/**
 * @author gongdongyang
 * 升级对话框
 */
class UpgradeFragment : DialogFragment() {

    //private var content:String? = null

    private var dialogOnClickListener:DialogOnClickListener? = null

    private var upgradeInfo:UpgradeInfo?=null

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
        contentView.text = upgradeInfo?.newFeatures
        mRootView.findViewById<AppCompatButton>(R.id.btn_cancel).setOnClickListener{
            dismiss()
        }

        mRootView.findViewById<AppCompatButton>(R.id.btn_confirm).setOnClickListener{
            dismiss()
            startUpdate();
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MMKVManager.getInstance().mmkv().encode("update_cancel_time", System.currentTimeMillis());
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var dialog:Dialog =  super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        return dialog
    }

    companion object{
        fun showUpgradeDialog(upgradeInfo: UpgradeInfo,listener: DialogOnClickListener):UpgradeFragment{
            val fragment = UpgradeFragment()
            fragment.dialogOnClickListener = listener
            fragment.upgradeInfo = upgradeInfo
            return  fragment
        }
    }

    interface DialogOnClickListener{
        fun onDialogNegativeClickListener(v:View)
    }

    fun startUpdate(){
        var intent:Intent = Intent(activity, ApkDownLoadService::class.java)
        var downloadInfo = DownloadInfo(upgradeInfo!!.downloadUrl,BHPhoneInfo.appVersion)

        val file = File(context!!.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),downloadInfo.getApkFileName())
        if(file.exists()){
            file.exists()
        }
        downloadInfo.apkLocalPath = file.absolutePath
        intent.putExtra("taskInfo", downloadInfo);
        context!!.startService(intent)
    }
}

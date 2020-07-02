package com.bhex.wallet.mnemonic.ui.fragment;


import android.graphics.Bitmap;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageHelper;
import androidx.appcompat.widget.AppCompatImageView;

import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.network.mvx.base.BaseFragment;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.MD5;
import com.bhex.tools.utils.QREncodUtil;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.enums.BH_BUSI_TYPE;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;

import butterknife.BindView;

/**
 * @author gongdongyang
 * 二维码导出
 * 2020-5-17 00:10:02
 */
public class ExportQRFragment extends BaseFragment {

    @BindView(R2.id.btn_show_qr)
    AppCompatButton btn_show_qr;

    @BindView(R2.id.iv_hidden)
    AppCompatImageView iv_hidden;

    @BindView(R2.id.iv_qr)
    AppCompatImageView iv_qr;

    BHWallet mCurrentWallet;

    private String flag;
    private String inptPwd;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_export_qr;
    }

    @Override
    protected void initView() {
        mCurrentWallet = BHUserManager.getInstance().getCurrentBhWallet();
        flag = getArgumentValue(ExportTextFragment.KEY_FLAG);
        inptPwd = getArgumentValue(BHConstants.INPUT_PASSWORD);

    }

    @Override
    protected void addEvent() {
        btn_show_qr.setOnClickListener(v -> {
            showQR();
        });
    }

    /**
     * 显示二维码
     */
    private void showQR() {
        String content = "";
        if(BH_BUSI_TYPE.备份私钥.value.equals(flag)){
            content = BHUserManager.getInstance().getOriginContext(mCurrentWallet.privateKey, MD5.md5(inptPwd));
        }else{
            content = mCurrentWallet.keystorePath;
        }

        Bitmap bitmap = QREncodUtil.createQRCode(content,
                PixelUtils.dp2px(getYActivity(),210),PixelUtils.dp2px(getYActivity(),210),
                null);

        iv_qr.setImageBitmap(bitmap);
        iv_qr.setVisibility(View.VISIBLE);
        iv_hidden.setVisibility(View.INVISIBLE);
        btn_show_qr.setVisibility(View.INVISIBLE);
    }

    private String getArgumentValue(String key){
        String result = "";
        if(getArguments()!=null){
            result = getArguments().getString(key,"1");
        }
        return result;
    }
}

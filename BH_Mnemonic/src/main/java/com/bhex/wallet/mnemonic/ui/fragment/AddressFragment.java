package com.bhex.wallet.mnemonic.ui.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhex.lib.uikit.widget.RecycleViewDivider;
import com.bhex.lib.uikit.widget.util.ColorUtil;
import com.bhex.lib.uikit.widget.util.PixelUtils;
import com.bhex.network.app.BaseApplication;
import com.bhex.network.mvx.base.BaseDialogFragment;
import com.bhex.network.utils.ToastUtils;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;
import com.bhex.wallet.mnemonic.adapter.AddressAdapter;
import com.bhex.wallet.mnemonic.ui.item.BHWalletItem;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author gongdongyang
 * 地址对话框
 * 2020-3-20 15:04:27
 */
public class AddressFragment extends BaseDialogFragment implements AddressAdapter.AddressCheckChangeListener {

    @BindView(R2.id.recycler_address)
    RecyclerView recycler_address;
    @BindView(R2.id.btn_cancel)
    MaterialButton btn_cancel;
    @BindView(R2.id.btn_confirm)
    MaterialButton btn_confirm;

    private List<BHWalletItem> mData;

    private AddressAdapter mAddressAdapter;

    private AddressChangeListener changeListener;


    public AddressFragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayout() {
        return R.layout.fragment_address;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mData = getAllBHWalletItem();

        mAddressAdapter = new AddressAdapter(R.layout.item_address,mData);
        mAddressAdapter.setCheckChangeListener(this);

        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);

        RecycleViewDivider ItemDecoration = new RecycleViewDivider(
                getContext(),LinearLayoutManager.VERTICAL,
                1,
                ColorUtil.getColor(getContext(),R.color.gray_E7ECF4));

        recycler_address.addItemDecoration(ItemDecoration);

        recycler_address.setLayoutManager(lm);

        recycler_address.setAdapter(mAddressAdapter);

        /*mAddressAdapter.setOnItemChildClickListener((adapter, view1, position) -> {
            if(view1.getId()==R.id.ck_ok){
                ToastUtils.showToast("view1");
                AppCompatCheckedTextView ck = (AppCompatCheckedTextView)view1;

                ck.setChecked(!ck.isChecked());
            }
        });*/

    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getAttributes().windowAnimations = R.style.bottomDialogStyle;

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;

        params.width = ViewGroup.LayoutParams.MATCH_PARENT;

        params.width = dm.widthPixels;
        params.height = PixelUtils.dp2px(BaseApplication.getInstance(), 285);
        window.setAttributes(params);
    }


    @OnClick({R2.id.btn_cancel, R2.id.btn_confirm})
    public void onViewClicked(View view) {
        if(view.getId() == R.id.btn_cancel){
            dismiss();
        }else if(view.getId() == R.id.btn_confirm){
            int position = getSelectPosition();
            if(changeListener!=null){
                changeListener.onItemClick(position);
            }
            dismiss();
        }
    }

    @Override
    public void onCheckedChanged(int position, boolean isChecked) {
        for (int i = 0; i < mData.size(); i++) {
            BHWalletItem bhItem = mData.get(i);
            bhItem.isDefault = BHWalletItem.UNSELECTED;
        }
        BHWalletItem item = mData.get(position);
        item.isDefault = BHWalletItem.SELECTED;
        mAddressAdapter.notifyDataSetChanged();
    }

    public List<BHWalletItem> getAllBHWalletItem(){
        List<BHWalletItem> result = null;
        List<BHWallet> origin = BHUserManager.getInstance().getAllWallet();
        if(origin!=null && origin.size()>0){
            result = new ArrayList<>();
            for (BHWallet tmp:origin) {
                BHWalletItem item = BHWalletItem.makeBHWalletItem(tmp);
                result.add(item);
            }
        }
        return result;
    }

    //计算选中的item
    public int getSelectPosition(){
        int position = 0;
        for (int i = 0; i < mData.size(); i++) {
            BHWalletItem item = mData.get(i);
            if(item.isDefault==BHWalletItem.SELECTED){
                return i;
            }
        }
        return position;
    }

    public interface AddressChangeListener{
        public void onItemClick(int position);
    }

    public void setChangeListener(AddressChangeListener changeListener) {
        this.changeListener = changeListener;
    }
}

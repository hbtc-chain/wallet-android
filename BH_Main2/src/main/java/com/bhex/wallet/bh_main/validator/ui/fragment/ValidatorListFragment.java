package com.bhex.wallet.bh_main.validator.ui.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bhex.wallet.bh_main.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ValidatorListFragment extends Fragment {


    public ValidatorListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_validator_list, container, false);
    }

}

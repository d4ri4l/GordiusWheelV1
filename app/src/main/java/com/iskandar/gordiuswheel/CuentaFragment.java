package com.iskandar.gordiuswheel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CuentaFragment extends Fragment {
    View vista;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista=inflater.inflate(R.layout.fragment_cuenta, container, false);
        textView=(TextView)vista.findViewById(R.id.textView2);
        SharedPreferences prefe=getActivity().getSharedPreferences("datos",Context.MODE_PRIVATE);

        textView.setText(prefe.getString("NameUser",""));

        return vista;
    }
}

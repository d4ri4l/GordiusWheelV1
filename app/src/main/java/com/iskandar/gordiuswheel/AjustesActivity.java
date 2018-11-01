package com.iskandar.gordiuswheel;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class AjustesActivity extends AppCompatActivity {

    Switch Theme;
    int ThemeCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        Theme =(Switch)findViewById(R.id.swTheme);

        Theme.setChecked(false);
        Theme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {

                } else {

                }
            }
        });
    }
}

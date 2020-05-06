package com.wpz.obddemo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.wpz.obddemo.obd.reader.activity.ConfigActivity;
import com.wpz.obddemo.obd.reader.activity.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.list_bluetooth)
    ListView listBluetooth;
    @BindView(R.id.tv_go_main)
    TextView tvGoMain;
    @BindView(R.id.tv_go_config)
    TextView tvGoConfig;

    private final static int PERMISSION_CODE = 01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        } else {
        }
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_go_main, R.id.tv_go_config})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.tv_go_main:
                Log.e("SplashActivity", "onViewClicked: 12121");
                intent.setClass(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_go_config:
                intent.setClass(this, ConfigActivity.class);
                startActivity(intent);
                break;
        }
    }
}

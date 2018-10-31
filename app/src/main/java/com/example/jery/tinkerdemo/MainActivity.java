package com.example.jery.tinkerdemo;

import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jery.tinkerdemo.tinker.Utils;
import com.meituan.android.walle.WalleChannelReader;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        checkPermision();
//        showChannel();
    }

    private void showChannel() {
        String channel = WalleChannelReader.getChannel(this.getApplicationContext());
        tv.setText(TextUtils.isEmpty(channel) ? "" : channel);
    }

    public void onTinkerClick(View view) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip";
        File file = new File(path);
        if (file.exists()) {
            Toast.makeText(this, "补丁已经存在", Toast.LENGTH_SHORT).show();
            TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), path);
        } else {
            Toast.makeText(this, "补丁已经不存在", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length == 2 && grantResults[0] == 0 && grantResults[1] == 0) {
            Toast.makeText(this, "获取权限成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "获取权限失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPermision() {
        if (Build.VERSION.SDK_INT >= 23) {
            int hasRStoragePermission = this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE");
            int hasWStoragePermission = this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
            if (hasRStoragePermission != 0 || hasWStoragePermission != 0) {
                this.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
                return;
            }
        }
    }


    //Tinker相关配置
    protected void onResume() {
        super.onResume();
        Utils.setBackground(false);

    }

    //Tinker相关配置
    @Override
    protected void onPause() {
        super.onPause();
        Utils.setBackground(true);
    }

}

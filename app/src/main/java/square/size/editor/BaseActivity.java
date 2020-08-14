package square.size.editor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.bean.Permissions;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;
import com.x.codeX.agent.AdAgent;

public abstract class BaseActivity extends AppCompatActivity {
    @Keep
    public AdAgent adAgent;

    protected abstract int attachLayout();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(attachLayout());
        initAD();
    }

    public void checkPer(String permission){
        SoulPermission.getInstance().checkAndRequestPermission(permission, new CheckRequestPermissionListener() {
            @Override
            public void onPermissionOk(Permission permission) {
                PermissionOk(permission);
            }

            @Override
            public void onPermissionDenied(Permission permission) {
                PermissionDenied(permission);
            }
        });
    }

   public void PermissionOk(Permission permission){

   }
    public void PermissionDenied(Permission permission){

    }
    private void initAD() {
        adAgent = new AdAgent.Builder(this)
//                .setAdInfo("small", (ViewGroup) findViewById(R.id.inner_ad_holder))
                .build();
        adAgent.loadAd();
    }

    public void goMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

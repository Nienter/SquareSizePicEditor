package square.size.editor;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.x.codeX.agent.AdAgent;

public class BaseActivity extends AppCompatActivity {
    @Keep
    public AdAgent adAgent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAD();
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

package square.size.editor;

import android.content.Intent;
import android.os.Bundle;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import square.size.editor.adapter.ImageAdapter;

public class ImageActivity extends BaseActivity {
    private List<String> paths = null;
    @BindView(R.id.bt_back)
    AppCompatImageButton bt_back;
    @BindView(R.id.tv_title)
    AppCompatTextView tv_title;
    @BindView(R.id.bt_next)
    AppCompatImageButton bt_next;
    @BindView(R.id.ry_image)
    RecyclerView ry_image;
    private ImageAdapter adapter;

    @Override
    protected int attachLayout() {
        return R.layout.activity_image;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String dirPath = getIntent().getStringExtra("path");
        File file = new File(dirPath);
        paths = Arrays.asList(file.list(getFileterImage()));
        adapter = new ImageAdapter(paths);
        ry_image.setLayoutManager(new GridLayoutManager(this, 3));
        ry_image.setAdapter(adapter);
    }

    private FilenameFilter getFileterImage() {
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg")
                        || filename.endsWith(".png")
                        || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        };
        return filenameFilter;
    }
    @OnClick(R.id.bt_back)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.bt_next)
    public void onNext() {
        Intent intent = new Intent(this, HandleActivity.class);
        startActivity(intent);
        finish();
    }
}

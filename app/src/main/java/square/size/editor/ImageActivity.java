package square.size.editor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import square.size.editor.adapter.ImageAdapter;
import square.size.editor.adapter.SelectorAdapter;

public class ImageActivity extends BaseActivity implements ImageAdapter.OnSelectorListener {
    private List<File> paths = null;
    @BindView(R.id.bt_back)
    AppCompatImageButton bt_back;
    @BindView(R.id.tv_title)
    AppCompatTextView tv_title;
    @BindView(R.id.bt_next)
    AppCompatImageButton bt_next;
    @BindView(R.id.ry_image)
    RecyclerView ry_image;
    private ImageAdapter adapter;
    private String dirPath;//目录文件夹
    @BindView(R.id.ry_selector)
    RecyclerView ry_selector;
    @BindView(R.id.rl_bottom)
    RelativeLayout rl_bottom;
    private List<File> selectorFile = new ArrayList<>();
    private SelectorAdapter selectorAdapter;

    @Override
    protected int attachLayout() {
        return R.layout.activity_image;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        dirPath = getIntent().getStringExtra("paths");
        File file = new File(dirPath);
        paths = Arrays.asList(file.listFiles(getFileterImage()));
        adapter = new ImageAdapter(paths, this);
        ry_image.setLayoutManager(new GridLayoutManager(this, 3));
        ry_image.setAdapter(adapter);
        selectorAdapter = new SelectorAdapter(selectorFile, this);
        ry_selector.setAdapter(selectorAdapter);
        initView();
    }

    private void initView() {
        tv_title.setText(new File(dirPath).getName());
        if (Constants.from.equals(Constants.GALLERY)) {
            bt_next.setVisibility(View.GONE);
            rl_bottom.setVisibility(View.GONE);
        }else if(Constants.from.equals(Constants.COLLAGE)){
            bt_next.setVisibility(View.VISIBLE);
            rl_bottom.setVisibility(View.VISIBLE);
        }
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
        List<Bitmap> bitmaps = new ArrayList<>();
        for (File file:selectorFile
             ) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            bitmaps.add(bitmap);
        }
        Constants.setCacheBitmaps(bitmaps);
        Intent intent = new Intent(this, HandleActivity.class);
        intent.putExtra("type", 0);
        intent.putExtra("borderSize", selectorFile.size());
        intent.putExtra("themeId", 0);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSelectorAdd(File file) {
        selectorFile.add(file);
        selectorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSelectorRemove(File file) {
        selectorFile.remove(file);
        selectorAdapter.notifyDataSetChanged();
    }

}

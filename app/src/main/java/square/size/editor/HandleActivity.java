package square.size.editor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBoxBlurFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilterGroup;
import square.size.editor.adapter.ColorAdapter;
import square.size.editor.adapter.PatternAdapter;
import square.size.editor.adapter.PuzzleAdapter;
import square.size.editor.puzz.PuzzleUtils;
import square.size.editor.puzz.view.PuzzleLayout;
import square.size.editor.puzz.view.SquarePuzzleView;

public class HandleActivity extends BaseActivity implements ColorAdapter.OnItemColorClickListener, PatternAdapter.onPatternListener, SeekBar.OnSeekBarChangeListener, PuzzleAdapter.OnItemClickListener {
    private static final String TAG = "HandleActivity";
    @BindView(R.id.vary)
    SquarePuzzleView mSquarePuzzleView;
    private PuzzleLayout puzzleLayout;
    @BindView(R.id.bt_back)
    AppCompatImageButton bt_back;
    @BindView(R.id.bt_next)
    AppCompatImageButton bt_next;
    @BindView(R.id.tv_title)
    AppCompatTextView tv_title;
    @BindView(R.id.tv_crop)
    TextView tv_crop;
    @BindView(R.id.tv_color)
    TextView tv_color;
    @BindView(R.id.tv_adjust)
    TextView tv_adjust;
    @BindView(R.id.tv_filter)
    TextView tv_filter;
    @BindView(R.id.tv_pattern)
    TextView tv_pattern;
    @BindView(R.id.tv_blur)
    TextView tv_blur;
    @BindView(R.id.handle_bottom)
    LinearLayout handle_bottom;
    @BindView(R.id.ry_color)
    RecyclerView ry_color;
    @BindView(R.id.hs)
    HorizontalScrollView hs;
    @BindView(R.id.ry_patten)
    RecyclerView ry_pattern;
    private PatternAdapter patternAdapter;
    @BindView(R.id.seek_blur)
    SeekBar seekBar_blur;
    @BindView(R.id.ll_bottom)
    LinearLayout ll_bottom;
    @BindView(R.id.ry_grid)
    RecyclerView ry_grid;
    @BindView(R.id.tv_grid)
    TextView tv_grid;
    @BindView(R.id.tv_border)
    TextView tv_border;
    @BindView(R.id.seek_border)
    SeekBar seek_border;

    private PuzzleAdapter puzzleAdapter;
    private String[] colors = new String[]{"#FFFFFF", "#CCCCCC", "#9A9A9A", "#FFB9D3", "#FFA891", "#FF8888", "#FF6379", "#FFCE6E", "#7AD9C4"};
    private List<String> colorString = new ArrayList<>();
    private ColorAdapter colorAdapter;
    private List<View> views = new ArrayList<>();
    private List<Drawable> patterns = new ArrayList<>();
    private GPUImage gpuImage;
    private List<View> visibleView = new ArrayList<>();
    Bitmap resource;
    private List<Drawable> endDrawable = new ArrayList<>();

    @Override
    protected int attachLayout() {
        return R.layout.activity_handle;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initPuzzle();
        initView();
    }

    private void initPuzzle() {
        puzzleAdapter = new PuzzleAdapter();
        puzzleAdapter.setOnItemClickListener(this);
        ry_grid.setAdapter(puzzleAdapter);
        puzzleAdapter.refreshData(PuzzleUtils.getAllPuzzleLayouts(), null);
        ry_grid.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                puzzleAdapter.refreshData(PuzzleUtils.getAllPuzzleLayouts(), null);
            }
        });
        resource = BitmapFactory.decodeResource(getResources(), R.drawable.aaa);
        int borderSize = getIntent().getIntExtra("borderSize", 1);
        puzzleLayout = PuzzleUtils.getPuzzleLayout(1, borderSize, 0);
        mSquarePuzzleView.setBackgroundColor(Color.parseColor(colors[0]));
        mSquarePuzzleView.setPuzzleLayout(puzzleLayout);
        for (int i = 0; i < borderSize; i++) {
            mSquarePuzzleView.addPiece(Constants.getCacheBitmaps().get(i));
        }
        Constants.cacheBitmaps.clear();
        getEndDrawable();
        BitmapDrawable drawable = (BitmapDrawable) endDrawable.get(0);
        Constants.cache = drawable.getBitmap();
        mSquarePuzzleView.setPiecePadding(10);
        mSquarePuzzleView.setSelected(0);
    }

    public List<Drawable> getEndDrawable() {
        for (int i = 0; i < mSquarePuzzleView.getPuzzlePieces().size(); i++) {
            endDrawable.clear();
            endDrawable.add(mSquarePuzzleView.getPuzzlePieces().get(i).getDrawable());
        }
        return endDrawable;
    }

    private void initView() {
        gpuImage = new GPUImage(this);
        colorString = Arrays.asList(colors);
        colorAdapter = new ColorAdapter(colorString, this);
        initPatterns();
        seekBar_blur.setOnSeekBarChangeListener(this);
        seek_border.setOnSeekBarChangeListener(this);
        patternAdapter = new PatternAdapter(patterns, this);
        ry_pattern.setAdapter(patternAdapter);
        tv_title.setText("");
        ry_color.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        ry_color.setAdapter(colorAdapter);
        views.add(ry_color);
        visibleView.add(ry_color);
        visibleView.add(ry_pattern);
        visibleView.add(seekBar_blur);
        visibleView.add(ry_grid);
        visibleView.add(seek_border);
        bt_next.setImageResource(R.drawable.ic_save);
    }

    public void setVisibleView(View view) {
        for (View v : visibleView
        ) {
            v.setVisibility(View.GONE);
        }
        view.setVisibility(View.VISIBLE);
    }

    private void initPatterns() {
        Drawable bitmap = getDrawable(R.drawable.bg1);
        Drawable bitmap1 = getDrawable(R.drawable.bg2);
        Drawable bitmap2 = getDrawable(R.drawable.bg3);
        Drawable bitmap3 = getDrawable(R.drawable.bg4);
        Drawable bitmap4 = getDrawable(R.drawable.bg5);
        Drawable bitmap5 = getDrawable(R.drawable.bg6);

        patterns.add(bitmap);
        patterns.add(bitmap1);
        patterns.add(bitmap2);
        patterns.add(bitmap3);
        patterns.add(bitmap4);
        patterns.add(bitmap5);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goMain();
        }
        return super.onKeyDown(keyCode, event);
    }

    private static final int CROP = 1002;
    private static final int ADJUST = 1003;

    @OnClick(R.id.tv_crop)
    public void crop() {
        if (mSquarePuzzleView.hasPieceSelected()) {
            Intent intent = new Intent(this, CutActivity.class);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) mSquarePuzzleView.getHandlingPiece().getDrawable();
            Constants.cache = bitmapDrawable.getBitmap();
            startActivityForResult(intent, CROP);
        } else {

            Intent intent = new Intent(this, CutActivity.class);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) mSquarePuzzleView.getHandlingPiece().getDrawable();
            Constants.cache = bitmapDrawable.getBitmap();
            startActivityForResult(intent, CROP);
        }

    }

    @OnClick(R.id.tv_adjust)
    public void setTv_adjust() {
        Intent intent = new Intent(this, AdjustActivity.class);
        startActivityForResult(intent, ADJUST);
    }

    @OnClick(R.id.tv_color)
    public void color() {
        if (!tv_color.isSelected()) {
            resetStatus(tv_color);
            setVisibleView(ry_color);
            ry_color.post(new Runnable() {
                @Override
                public void run() {
                    ry_color.getChildAt(0).setSelected(true);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CROP && resultCode == -1) {//使用静态常量传递bitmap
            mSquarePuzzleView.replace(Constants.cache, "");
        }
        if (requestCode == ADJUST && resultCode == -1) {//使用静态常量传递bitmap
            mSquarePuzzleView.replace(Constants.cache, "");
        }
        if (requestCode == FILTER && resultCode == -1) {//使用静态常量传递bitmap
            mSquarePuzzleView.replace(Constants.cache, "");
        }
        getEndDrawable();
    }

    public void resetStatus(View v) {
        for (int i = 0; i < ll_bottom.getChildCount(); i++) {
            ll_bottom.getChildAt(i).setSelected(false);
        }
        v.setSelected(true);
    }

    @Override
    public void onItemColorClick(int position, String s) {
        for (int i = 0; i < ry_color.getChildCount(); i++) {
            LinearLayout ll = (LinearLayout) ry_color.getChildAt(i);
            ll.setSelected(false);
        }
        LinearLayout ll = (LinearLayout) ry_color.getChildAt(position);
        ll.setSelected(true);
        mSquarePuzzleView.setBackgroundColor(Color.parseColor(s));
    }

    private GPUImageBrightnessFilter gpuImageBrightnessFilter;
    private GPUImageContrastFilter gpuImageContrastFilter;
    private GPUImageFilterGroup gpuImageFilterGroup = new GPUImageFilterGroup();
    private static final int FILTER = 1004;

    @OnClick(R.id.tv_filter)
    public void setTv_filter() {
        Intent intent = new Intent(this, FilterActivity.class);
        startActivityForResult(intent, FILTER);
    }

    @OnClick(R.id.tv_pattern)
    public void setTv_pattern() {
        if (!tv_pattern.isSelected()) {
            resetStatus(tv_pattern);
            setVisibleView(ry_pattern);
        }
    }

    GPUImageBoxBlurFilter gpuImageGaussianBlurFilter = new GPUImageBoxBlurFilter();


    @OnClick(R.id.tv_blur)
    public void setTv_blur() {
        resetStatus(tv_blur);
        setVisibleView(seekBar_blur);

    }

    @Override
    public void onPattern(int pos) {
        mSquarePuzzleView.setBackground(patterns.get(pos));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.getId() == R.id.seek_blur) {
            gpuImage.setFilter(gpuImageGaussianBlurFilter);
            gpuImageGaussianBlurFilter.setBlurSize(progress);
            gpuImage.setImage(Constants.cache);
            gpuImage.requestRender();
            Bitmap bitmap = gpuImage.getBitmapWithFilterApplied(Constants.cache);
            if (bitmap != null) {
                mSquarePuzzleView.setBackground(new BitmapDrawable(bitmap));
            }
        } else if (seekBar.getId() == R.id.seek_border) {
            mSquarePuzzleView.setPiecePadding(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onItemClick(PuzzleLayout puzzleLayout, int themeId) {
        mSquarePuzzleView.setBackgroundColor(Color.parseColor(colors[0]));
        getEndDrawable();
        mSquarePuzzleView.setPuzzleLayout(puzzleLayout);
        mSquarePuzzleView.addDrawablePieces(endDrawable);

        mSquarePuzzleView.setPiecePadding(10);
//        puzzleAdapter.refreshData(PuzzleUtils.getAllPuzzleLayouts(), null);

    }

    @OnClick(R.id.tv_border)
    public void setTv_border() {
        resetStatus(tv_border);
        setVisibleView(seek_border);
    }

    @OnClick(R.id.tv_grid)
    public void setTv_grid() {
        resetStatus(tv_grid);
        setVisibleView(ry_grid);
    }

    @OnClick(R.id.bt_back)
    public void setBt_back() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.bt_next)
    public void setBt_next() {
        File file = Tools.getNewFile(this, getString(R.string.app_name));
        Tools.savePuzzle(mSquarePuzzleView, file, 100, new Tools.Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(HandleActivity.this, R.string.savasucc, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(HandleActivity.this, SaveActivity.class);
                intent.putExtra("path", file.getPath());
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed() {
                Toast.makeText(HandleActivity.this, R.string.savefail, Toast.LENGTH_LONG).show();
            }
        });
    }
}

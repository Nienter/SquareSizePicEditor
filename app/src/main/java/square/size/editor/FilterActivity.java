package square.size.editor;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageLevelsFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageMonochromeFilter;
import square.size.editor.adapter.FilterAdapter;

public class FilterActivity extends BaseActivity implements WorkThread.ITask, FilterAdapter.OnItemClickListener {
    private ProgressDialog progressBar;
    @BindView(R.id.gpu)
    GPUImageView gpuImageView;
    @BindView(R.id.ry_filter)
    RecyclerView ry_filter;
    @BindView(R.id.bt_next)
    AppCompatImageButton bt_next;
    @BindView(R.id.bt_back)
    AppCompatImageButton bt_back;
    @BindView(R.id.tv_title)
    AppCompatTextView tv_title;
    private WorkThread myWork;
    private GPUImageHueFilter gpuImageHueFilter = new GPUImageHueFilter();
    private GPUImageGrayscaleFilter gpuImageGrayscaleFilter = new GPUImageGrayscaleFilter();
    private GPUImageColorInvertFilter gpuImageColorInvertFilter = new GPUImageColorInvertFilter();
    private GPUImageMonochromeFilter gpuImageMonochromeFilter = new GPUImageMonochromeFilter();
    private GPUImageLevelsFilter gpuImageLevelsFilter = new GPUImageLevelsFilter();
    private GPUImageHighlightShadowFilter gpuImageHighlightShadowFilter = new GPUImageHighlightShadowFilter();
    private Bitmap bitmap1, bitmap2, bitmap3, bitmap4, bitmap5, bitmap6;
    private FilterAdapter filterAdapter;
    private List<GPUImageFilter> filters = new ArrayList<>();

    @Override
    protected int attachLayout() {
        return R.layout.activity_filter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();

    }

    private GPUImage gpuImage, gpuImage1, gpuImage2, gpuImage3, gpuImage4, gpuImage5;

    private void initView() {
        tv_title.setText(getString(R.string.filter));
        progressBar = new ProgressDialog(this);
        myWork = new WorkThread();
        filters.add(gpuImageHueFilter);
        filters.add(gpuImageGrayscaleFilter);
        filters.add(gpuImageColorInvertFilter);
        filters.add(gpuImageMonochromeFilter);
        filters.add(gpuImageLevelsFilter);
        filters.add(gpuImageHighlightShadowFilter);
        gpuImageView.setImage(Constants.cache);
        initRecycle();
    }

    private void initRecycle() {
        gpuImage = new GPUImage(this);
        gpuImage.setImage(Constants.cache);
        gpuImage1 = new GPUImage(this);
        gpuImage1.setImage(Constants.cache);
        gpuImage2 = new GPUImage(this);
        gpuImage2.setImage(Constants.cache);
        gpuImage3 = new GPUImage(this);
        gpuImage3.setImage(Constants.cache);
        gpuImage4 = new GPUImage(this);
        gpuImage4.setImage(Constants.cache);
        gpuImage5 = new GPUImage(this);
        gpuImage5.setImage(Constants.cache);
        progressBar.show();
        myWork.postTask(this);
    }

    @OnClick(R.id.bt_back)
    public void setBt_back() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @OnClick(R.id.bt_next)
    public void setBt_next() {
        Constants.cache = gpuImageView.getGPUImage().getBitmapWithFilterApplied();
        setResult(-1);
        finish();
    }

    @Override
    public Object onDo(Object... args) {
        gpuImage.setFilter(gpuImageHueFilter);
        bitmap1 = gpuImage.getBitmapWithFilterApplied();
        gpuImage1.setFilter(gpuImageGrayscaleFilter);
        bitmap2 = gpuImage1.getBitmapWithFilterApplied();
        gpuImage2.setFilter(gpuImageColorInvertFilter);
        bitmap3 = gpuImage2.getBitmapWithFilterApplied();
        gpuImage3.setFilter(gpuImageMonochromeFilter);
        bitmap4 = gpuImage3.getBitmapWithFilterApplied();
        gpuImage4.setFilter(gpuImageLevelsFilter);
        bitmap5 = gpuImage4.getBitmapWithFilterApplied();
        gpuImage5.setFilter(gpuImageHighlightShadowFilter);
        bitmap6 = gpuImage5.getBitmapWithFilterApplied();
        return null;
    }

    @Override
    public void onResult(Object ret) {
        if (progressBar.isShowing()) {
            progressBar.dismiss();
        }
        List<Bitmap> bitmaps = new ArrayList<>();
        bitmaps.add(bitmap1);
        bitmaps.add(bitmap2);
        bitmaps.add(bitmap3);

        bitmaps.add(bitmap4);
        bitmaps.add(bitmap5);
//        bitmaps.add(bitmap6);

        filterAdapter = new FilterAdapter(bitmaps, this);
        ry_filter.setAdapter(filterAdapter);

    }

    @Override
    public void onItemClick(int pos) {
        gpuImageView.setFilter(filters.get(pos));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bitmap1.recycle();
        bitmap2.recycle();
        bitmap3.recycle();
        bitmap4.recycle();
        bitmap5.recycle();
        bitmap6.recycle();
    }
}

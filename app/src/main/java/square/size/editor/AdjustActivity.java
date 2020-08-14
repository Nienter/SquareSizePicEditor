package square.size.editor;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageVignetteFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageWhiteBalanceFilter;

public class AdjustActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {
    @BindView(R.id.seek)
    SeekBar seekBar;
    @BindView(R.id.tv_contrast)
    TextView tv_contrast;
    @BindView(R.id.tv_bringhtness)
    TextView tv_brightness;
    @BindView(R.id.tv_saturation)
    TextView tv_saturation;
    @BindView(R.id.tv_exposure)
    TextView tv_exposure;
    @BindView(R.id.tv_warmth)
    TextView tv_warmth;
    @BindView(R.id.tv_vignette)
    TextView tv_vignette;
    @BindView(R.id.gpu)
    GPUImageView gpuImageView;
    @BindView(R.id.ll_han)
    LinearLayout ll_han;

    @BindView(R.id.bt_back)
    AppCompatImageButton bt_back;
    @BindView(R.id.bt_next)
    AppCompatImageButton bt_next;
    @BindView(R.id.tv_title)
    AppCompatTextView tv_title;


    private GPUImageBrightnessFilter gpuImageBrightnessFilter = new GPUImageBrightnessFilter();
    private GPUImageContrastFilter gpuImageContrastFilter = new GPUImageContrastFilter();
    private GPUImageSaturationFilter gpuImageSaturationFilter = new GPUImageSaturationFilter();
    private GPUImageExposureFilter gpuImageExposureFilter = new GPUImageExposureFilter();
    private GPUImageWhiteBalanceFilter gpuImageWhiteBalanceFilter = new GPUImageWhiteBalanceFilter();
    private GPUImageVignetteFilter gpuImageVignetteFilter = new GPUImageVignetteFilter();
    private GPUImageFilterGroup gpuImageFilterGroup = new GPUImageFilterGroup();

    @Override
    protected int attachLayout() {
        return R.layout.activity_adjust;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        tv_title.setText(getString(R.string.adjust));
        Bitmap bitmap = Constants.cache;
        gpuImageView.setImage(bitmap);

        seekBar.setOnSeekBarChangeListener(this);
    }

    @OnClick(R.id.tv_bringhtness)
    public void setTv_brightness() {
//        gpuImage.setFilter(gpuImageBrightnessFilter);
        if (!gpuImageFilterGroup.getFilters().contains(gpuImageBrightnessFilter)) {
            gpuImageFilterGroup.addFilter(gpuImageBrightnessFilter);
        }
        reset(ll_han, tv_brightness);
    }

    @OnClick(R.id.tv_contrast)
    public void setTv_contrast() {
        if (!gpuImageFilterGroup.getFilters().contains(gpuImageContrastFilter)) {
            gpuImageFilterGroup.addFilter(gpuImageContrastFilter);
        }
        reset(ll_han, tv_contrast);
    }

    @OnClick(R.id.tv_saturation)
    public void setTv_saturation() {
        if (!gpuImageFilterGroup.getFilters().contains(gpuImageSaturationFilter)) {
            gpuImageFilterGroup.addFilter(gpuImageSaturationFilter);
        }
        reset(ll_han, tv_saturation);
    }

    @OnClick(R.id.tv_exposure)
    public void setTv_exposure() {
        if (!gpuImageFilterGroup.getFilters().contains(gpuImageExposureFilter)) {
            gpuImageFilterGroup.addFilter(gpuImageExposureFilter);
        }
        reset(ll_han, tv_exposure);
    }

    @OnClick(R.id.tv_warmth)
    public void setTv_warmth() {
        if (!gpuImageFilterGroup.getFilters().contains(gpuImageWhiteBalanceFilter)) {
            gpuImageFilterGroup.addFilter(gpuImageWhiteBalanceFilter);
        }
        reset(ll_han, tv_warmth);
    }

    @OnClick(R.id.tv_vignette)
    public void setTv_vignette() {
        if (!gpuImageFilterGroup.getFilters().contains(gpuImageVignetteFilter)) {
            gpuImageFilterGroup.addFilter(gpuImageVignetteFilter);
        }
        reset(ll_han, tv_vignette);
    }

    public void reset(ViewGroup view, View v) {
        for (int i = 0; i < view.getChildCount(); i++) {
            view.getChildAt(i).setSelected(false);
        }
        v.setSelected(true);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (tv_brightness.isSelected()) {
            float a = 2 * progress / 100f - 1;
            gpuImageBrightnessFilter.setBrightness(a);
            gpuImageView.setFilter(gpuImageFilterGroup);

        } else if (tv_contrast.isSelected()) {
            float a = 4 * progress / 100f;
            gpuImageContrastFilter.setContrast(a);
            gpuImageView.setFilter(gpuImageFilterGroup);
        } else if (tv_saturation.isSelected()) {
            float a = 2 * progress / 100f;
            gpuImageSaturationFilter.setSaturation(a);
            gpuImageView.setFilter(gpuImageFilterGroup);
        }else if (tv_exposure.isSelected()) {
            float a = 20 * progress / 100f - 10;
            gpuImageExposureFilter.setExposure(a);
            gpuImageView.setFilter(gpuImageFilterGroup);
        }else if (tv_warmth.isSelected()) {
            float a = 3000 * progress / 100f - 4000;
            gpuImageWhiteBalanceFilter.setTemperature(a);
            gpuImageView.setFilter(gpuImageFilterGroup);
        }else if (tv_vignette.isSelected()) {
            float a = 1 * progress / 100f;
            PointF pointF = new PointF();
            pointF.x = 0.5f;
            pointF.y = 0.5f;
            gpuImageVignetteFilter.setVignetteCenter(pointF);
            gpuImageVignetteFilter.setVignetteEnd(a);
            gpuImageVignetteFilter.setVignetteStart(a);
            gpuImageView.setFilter(gpuImageFilterGroup);
        }
        gpuImageView.requestRender();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @OnClick(R.id.bt_back)
    public void setBt_back() {
        setResult(RESULT_CANCELED);
    }

    @OnClick(R.id.bt_next)
    public void setBt_next() {
        Constants.cache = gpuImageView.getGPUImage().getBitmapWithFilterApplied();
        setResult(-1);
        finish();
    }
}

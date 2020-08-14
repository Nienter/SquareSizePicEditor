package square.size.editor;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageVignetteFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageWhiteBalanceFilter;

public enum FilterType {
    BRIGHTNESS(-1f, 1f, new GPUImageBrightnessFilter()),
    CONTRAST(0f, 4f,new GPUImageContrastFilter()),
    SATURATION(0, 2,new GPUImageSaturationFilter()),
    WARMTH(3000, 7000,new GPUImageWhiteBalanceFilter()),
    EXPOSURE(-4, 4,new GPUImageExposureFilter()),
    VIGNETTE(0, 0, new GPUImageVignetteFilter());


    FilterType(float i, float i2, GPUImageFilter imagefilter) {

    }
}

package square.size.editor;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final String GALLERY = "gallery";
    public static final String COLLAGE = "collage";
    public static Bitmap cache = null;
    public static String from = null;
    public static List<Bitmap> cacheBitmaps = new ArrayList<>();

    public static List<Bitmap> getCacheBitmaps() {
        return cacheBitmaps;
    }

    public static void setCacheBitmaps(List<Bitmap> cacheBitmaps) {
        Constants.cacheBitmaps = cacheBitmaps;
    }
}

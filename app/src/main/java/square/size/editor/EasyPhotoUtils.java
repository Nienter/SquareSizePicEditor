//package square.size.editor;
//
//import android.app.Activity;
//
//import com.huantansheng.easyphotos.EasyPhotos;
//
//public class EasyPhotoUtils {
//    public static void startSingle(Activity context) {
//        EasyPhotos.createAlbum(context, true, GlideEngine.getInstance())
//                .setFileProviderAuthority(context.getString(R.string.pio))
//                .setCount(1)
//                .setPuzzleMenu(true)
//                .start(1);
//    }
//
//    public static void startMul(Activity context, int count) {
//        EasyPhotos.createAlbum(context, true, GlideEngine.getInstance())
//                .setFileProviderAuthority(context.getString(R.string.pio))
//                .setCount(count)
//                .start(1);
//    }
//
//    public static void startCamera(Activity activity) {
//        EasyPhotos.createCamera(activity)
//                .setFileProviderAuthority(activity.getString(R.string.pio))//参数说明：见下方`FileProvider的配置`
//                .start(1);
//    }
//}

package square.size.editor;

import java.util.ArrayList;
import java.util.List;

public class SdkUtils {
    private static List<SdkObject> sdkObjects = new ArrayList<>();

    public static void addSdkPlugin(SdkObject sdkObject) {
        sdkObjects.add(sdkObject);
    }


    public static void init(){
        for (SdkObject sdk:
             sdkObjects) {
            sdk.init();
        }
    }
    public static void destroy(){
        for (SdkObject sdk:
                sdkObjects) {
            sdk.destroy();
        }
    }
}

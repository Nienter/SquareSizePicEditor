package square.size.editor;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class CanvasLayout extends FrameLayout {
    public CanvasLayout(Context context) {
        this(context, null);
    }

    public CanvasLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CanvasLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setStaticTransformationsEnabled(true);
    }
}

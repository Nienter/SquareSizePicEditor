package square.size.editor.puzz.straight.layout;

import android.util.Log;

import square.size.editor.puzz.view.Line;

/**
 * 2
 */
public class TwoStraightLayout extends NumberStraightLayout {
    private float mRadio = 1f / 2;

    public TwoStraightLayout(int theme) {
        super(theme);
    }

    public TwoStraightLayout(float radio, int theme) {
        super(theme);
        if (mRadio > 1) {
            Log.e(TAG, "CrossLayout: the radio can not greater than 1f");
            mRadio = 1f;
        }
        mRadio = radio;
    }

    @Override
    public int getThemeCount() {
        return 10;
    }

    @Override
    public void layout() {
        switch (theme) {
            case 0:
                addLine(0, Line.Direction.HORIZONTAL, mRadio);
                break;
            case 1:
                addLine(0, Line.Direction.VERTICAL, mRadio);
                break;
            case 2:
                addLine(0, Line.Direction.HORIZONTAL, 1f / 3);
                break;
            case 3:
                addLine(0, Line.Direction.HORIZONTAL, 2f / 3);
                break;
            case 4:
                addLine(0, Line.Direction.VERTICAL, 1f / 3);
                break;
            case 5:
                addLine(0, Line.Direction.VERTICAL, 2f / 3);
                break;
            case 6:
                addLine(0, Line.Direction.VERTICAL, 1f / 4);
                break;
            case 7:
                addLine(0, Line.Direction.VERTICAL, 3f / 4);
                break;
            case 8:
                addLine(0, Line.Direction.HORIZONTAL, 1f / 4);
                break;
            case 9:
                addLine(0, Line.Direction.HORIZONTAL, 3f / 4);
                break;
            default:
                addLine(0, Line.Direction.HORIZONTAL, mRadio);
                break;
        }
    }
}

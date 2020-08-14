package square.size.editor.puzz.straight.layout;


/**
 * 只有一片
 */
public class NormalLayout extends NumberStraightLayout {


    public NormalLayout(int theme) {
        super(theme);
    }

    @Override
    public int getThemeCount() {
        return 1;
    }

    @Override
    public void layout() {

    }
}

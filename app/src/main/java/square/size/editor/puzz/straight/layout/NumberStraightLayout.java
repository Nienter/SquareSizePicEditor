package square.size.editor.puzz.straight.layout;

import android.util.Log;

import square.size.editor.puzz.straight.StraightPuzzleLayout;

/**
 * @author wupanjie
 */
public abstract class NumberStraightLayout extends StraightPuzzleLayout {
  static final String TAG = "NumberStraightLayout";
  protected int theme;

  public NumberStraightLayout(int theme) {
    if (theme >= getThemeCount()) {
      Log.e(TAG, "NumberStraightLayout: the most theme count is "
          + getThemeCount()
          + " ,you should let theme from 0 to "
          + (getThemeCount() - 1)
          + " .");
    }
    this.theme = theme;
  }

  public abstract int getThemeCount();

  public int getTheme() {
    return theme;
  }
}

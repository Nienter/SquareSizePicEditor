package square.size.editor;

public class PuzzleBean {
    private int type;
    private int borderSize;
    private int themeId;

    public PuzzleBean(int type, int borderSize, int themeId) {
        this.type = type;
        this.borderSize = borderSize;
        this.themeId = themeId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(int borderSize) {
        this.borderSize = borderSize;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }
}

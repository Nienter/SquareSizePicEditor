package square.size.editor;

public class ImageFilter {
    private float value;//seekbar的值
    private boolean isSelected;
    private FilterType filterType;
    public float getValue() {
        return value;
    }

    public ImageFilter(float value, boolean isSelected, FilterType filterType) {
        this.value = value;
        this.isSelected = isSelected;
        this.filterType = filterType;
    }
    public void setValue(float value) {
        this.value = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }
}

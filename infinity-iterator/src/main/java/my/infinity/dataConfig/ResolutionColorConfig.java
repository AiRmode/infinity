package my.infinity.dataConfig;

/**
 * Created by alexey on 21.08.16.
 */
public class ResolutionColorConfig implements DataConfig {
    private volatile int height;
    private volatile int width;
    private volatile byte depth;//in bytes!
    private volatile byte minValue;
    private volatile byte maxValue;

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public byte getDepth() {
        return depth;
    }

    public void setDepth(byte depth) {
        this.depth = depth;
    }

    @Override
    public byte getElementMinValue() {

        return minValue;
    }

    public void setMinValue(byte minValue) {
        this.minValue = minValue;
    }

    @Override
    public byte getElementMaxValue() {
        return maxValue;
    }

    public void setMaxValue(byte maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResolutionColorConfig that = (ResolutionColorConfig) o;

        if (height != that.height) return false;
        if (width != that.width) return false;
        if (depth != that.depth) return false;
        if (minValue != that.minValue) return false;
        return maxValue == that.maxValue;

    }

    @Override
    public int hashCode() {
        int result = height;
        result = 31 * result + width;
        result = 31 * result + (int) depth;
        result = 31 * result + (int) minValue;
        result = 31 * result + (int) maxValue;
        return result;
    }
}

package my.infinity;

/**
 * Created by alexey on 24.08.16.
 */
public class TempColorModel implements Comparable<TempColorModel> {
    private int r;
    private int g;
    private int b;

    public TempColorModel(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TempColorModel that = (TempColorModel) o;

        if (r != that.r) return false;
        if (g != that.g) return false;
        return b == that.b;

    }

    @Override
    public int hashCode() {
        int result = r;
        result = 31 * result + g;
        result = 31 * result + b;
        return result;
    }

    @Override
    public int compareTo(TempColorModel o) {
        return 0;
    }
}

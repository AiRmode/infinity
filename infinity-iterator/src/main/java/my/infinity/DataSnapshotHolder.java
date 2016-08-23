package my.infinity;

import my.infinity.dataConfig.DataConfig;

import java.util.Arrays;
import java.util.Stack;

/**
 * Created by alexey on 22.08.16.
 */
public class DataSnapshotHolder {
    private Byte[] array;
    private Stack<FrameItem> stack;
    private DataConfig dataConfig;

    public DataSnapshotHolder(Byte[] array, Stack<FrameItem> stack, DataConfig dataConfig) {
        this.array = array;
        this.stack = stack;
        this.dataConfig = dataConfig;
    }

    public Byte[] getArray() {
        return array;
    }

    public void setArray(Byte[] array) {
        this.array = array;
    }

    public Stack<FrameItem> getStack() {
        return stack;
    }

    public void setStack(Stack<FrameItem> stack) {
        this.stack = stack;
    }

    public DataConfig getDataConfig() {
        return dataConfig;
    }

    public void setDataConfig(DataConfig dataConfig) {
        this.dataConfig = dataConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataSnapshotHolder that = (DataSnapshotHolder) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(array, that.array)) return false;
        if (stack != null ? !stack.equals(that.stack) : that.stack != null) return false;
        return !(dataConfig != null ? !dataConfig.equals(that.dataConfig) : that.dataConfig != null);

    }

    @Override
    public int hashCode() {
        int result = array != null ? Arrays.hashCode(array) : 0;
        result = 31 * result + (stack != null ? stack.hashCode() : 0);
        result = 31 * result + (dataConfig != null ? dataConfig.hashCode() : 0);
        return result;
    }
}

package my.infinity;

import java.util.Arrays;
import java.util.Stack;

/**
 * Created by alexey on 22.08.16.
 */
public class DataSnapshotHolder {
    private Byte[] array;
    private Stack<FrameItem> stack;

    public DataSnapshotHolder(Byte[] array, Stack<FrameItem> stack) {
        this.array = array;
        this.stack = stack;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataSnapshotHolder that = (DataSnapshotHolder) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(array, that.array)) return false;
        return !(stack != null ? !stack.equals(that.stack) : that.stack != null);

    }

    @Override
    public int hashCode() {
        int result = array != null ? Arrays.hashCode(array) : 0;
        result = 31 * result + (stack != null ? stack.hashCode() : 0);
        return result;
    }
}

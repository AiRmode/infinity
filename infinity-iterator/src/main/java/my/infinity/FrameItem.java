package my.infinity;

import java.io.Serializable;

/**
 * Created by alexey on 18.08.16.
 */
public class FrameItem implements Serializable {
    public volatile int index;
    public volatile byte value;

    public FrameItem(int index, byte value) {
        this.index = index;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FrameItem frameItem = (FrameItem) o;

        if (index != frameItem.index) return false;
        return value == frameItem.value;

    }

    @Override
    public int hashCode() {
        int result = index;
        result = 31 * result + value;
        return result;
    }
}

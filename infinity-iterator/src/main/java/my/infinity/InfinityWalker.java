package my.infinity;

import my.infinity.dataConfig.DataConfig;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Stack;

/**
 * Created by alexey on 11.08.16.
 */
public class InfinityWalker implements Runnable, Serializable {
    private volatile Byte[] sourceArray;
    private volatile Byte[] filledArray;
    @SuppressWarnings("all")
    private volatile int height;
    @SuppressWarnings("all")
    private volatile int width;
    @SuppressWarnings("all")
    private volatile byte depth;//in bytes!

    private volatile byte minValue;
    @SuppressWarnings("all")
    private volatile byte maxValue;

    private volatile DataConfig dataConfig;

    private volatile Stack<FrameItem> stack;

    public InfinityWalker(DataConfig dataConfig) {
        this.dataConfig = dataConfig;
    }

    @PostConstruct
    public void init() {
        this.height = dataConfig.getHeight();
        this.width = dataConfig.getWidth();
        this.depth = dataConfig.getDepth();
        this.minValue = dataConfig.getElementMinValue();
        this.maxValue = dataConfig.getElementMaxValue();

        //define array
        sourceArray = new Byte[height * width * depth];
        filledArray = new Byte[height * width * depth];
        //calc last index
        //fill array with init and target data
        Arrays.fill(sourceArray, minValue);
        Arrays.fill(filledArray, maxValue);
        //init stack
        stack = new Stack<>();
    }

    public static void main(String[] args) {
//        InfinityWalker infinityWalker = new InfinityWalker();
//        infinityWalker.walkStack(0);
    }

    @Override
    public void run() {
        walkStack(0);
    }

    public void walkStack(int startingIndex) {
        for (int index = 0; index <= sourceArray.length; index++) {
            FrameItem frame = new FrameItem(index, minValue);
            stack.push(frame);
        }
        while (!stack.isEmpty()) {
            if (Thread.interrupted()) {
                break;
            }
            FrameItem frame = stack.peek();
            if (frame.index == sourceArray.length) {
//                System.out.println(Arrays.toString(sourceArray));
                storeArrayCopy();
                stack.pop();
                continue;
            }

            if (frame.value < filledArray[frame.index]) {
                sourceArray[frame.index] = ++frame.value;
                int nextIndex = frame.index;
                while (nextIndex != sourceArray.length) {
                    FrameItem nextFrame = new FrameItem(nextIndex + 1, minValue);
                    stack.push(nextFrame);
                    nextIndex++;
                }
            } else {
                sourceArray[frame.index] = minValue;
                stack.pop();
            }
        }
    }

    private void storeArrayCopy() {
        Byte[] copy = new Byte[sourceArray.length];
        System.arraycopy(sourceArray, 0, copy, 0, sourceArray.length);
        DataSnapshotHolder dataSnapshotHolder = new DataSnapshotHolder(copy, stack, dataConfig);
        DataSnapshotStorage.getMap().put(dataConfig, dataSnapshotHolder);
    }

    public Stack<FrameItem> getStack() {
        return stack;
    }

    public Byte[] getSourceArray() {
        return sourceArray;
    }

    public DataConfig getDataConfig() {
        return dataConfig;
    }
}

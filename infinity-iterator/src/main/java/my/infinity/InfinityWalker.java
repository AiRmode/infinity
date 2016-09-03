package my.infinity;

import my.infinity.dataConfig.DataConfig;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Arrays;
import java.util.Stack;

/**
 * Created by alexey on 11.08.16.
 */
public class InfinityWalker implements Runnable, Serializable {
    public static final int SAVE_INTERVAL_IN_ITERATIONS = 100000;
    public static final int START_NEW_INFINITY_WALK = 0;
    public static final int CONTINUE_FROM_BACKUP_STATE = -1;
    public static final String BACKUP_FILENAME_SUFFIX = "backupState.out";
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

    private volatile int backupCounter;

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
        String fileName = getBackupFileName();
        File file = new File(fileName);
        if (file.exists()) {
            deserialize(file);//continue from previous backup
            walkStack(CONTINUE_FROM_BACKUP_STATE);
        } else {
            walkStack(START_NEW_INFINITY_WALK);//start new walk
        }
    }

    private String getBackupFileName() {
        return dataConfig.getConfigName() + BACKUP_FILENAME_SUFFIX;
    }

    public void walkStack(int startingIndex) {
        if (startingIndex == START_NEW_INFINITY_WALK) {
            for (int index = 0; index <= sourceArray.length; index++) {
                FrameItem frame = new FrameItem(index, minValue);
                stack.push(frame);
            }
        }
        while (!stack.isEmpty()) {
            if (Thread.interrupted()) {
                break;
            }
            FrameItem frame = stack.peek();
            if (frame.index == sourceArray.length) {
//                System.out.println(Arrays.toString(sourceArray));
                storeArrayCopy();
                if (isBackupTime()) {
                    String file = getBackupFileName();
                    serialize(new File(file));
                }
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

    private boolean isBackupTime() {
        if (backupCounter == SAVE_INTERVAL_IN_ITERATIONS) {
            backupCounter = 0;
            return true;
        } else {
            backupCounter++;
            return false;
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

    public void serialize(File file) {
        try (OutputStream fos = new FileOutputStream(file.getName());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this);
            oos.flush();
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    public void deserialize(File file) {
        try (FileInputStream fis = new FileInputStream(file.getName());
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            InfinityWalker walker = (InfinityWalker) ois.readObject();
            restoreFromSerialized(walker);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void restoreFromSerialized(InfinityWalker walker) {
        this.sourceArray = walker.sourceArray;
        this.filledArray = walker.filledArray;
        this.dataConfig = walker.dataConfig;
        this.height = walker.height;
        this.width = walker.width;
        this.depth = walker.depth;
        this.minValue = walker.minValue;
        this.maxValue = walker.maxValue;
        this.stack = walker.stack;
    }
}

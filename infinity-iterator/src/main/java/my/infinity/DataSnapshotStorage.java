package my.infinity;

import my.infinity.dataConfig.DataConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by alexey on 21.08.16.
 */
public class DataSnapshotStorage {

    private final static Map<DataConfig, DataSnapshotHolder> map = new ConcurrentHashMap<>();

    public static Map<DataConfig, DataSnapshotHolder> getMap() {
        return map;
    }
}

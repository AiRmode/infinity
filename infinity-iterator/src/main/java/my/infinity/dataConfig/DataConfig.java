package my.infinity.dataConfig;

import java.io.Serializable;

/**
 * Created by alexey on 21.08.16.
 */
public interface DataConfig extends Serializable{
    int getHeight();

    int getWidth();

    byte getDepth();

    byte getElementMinValue();

    byte getElementMaxValue();
}

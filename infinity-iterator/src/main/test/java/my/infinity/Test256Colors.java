package my.infinity;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by alexey on 25.08.16.
 */
public class Test256Colors {
    @Test
    public void distinctColorsTest() {
        Set<TempColorModel> tempColorModelSet = new HashSet<>();
        for (int c = Byte.MIN_VALUE; c <= Byte.MAX_VALUE; c++) {
            int r1, g1, b1, x;
            x = c + (-128);
            r1 = (x & 0xE0);
            g1 = ((x & 0x1C) << 3);
            b1 = ((x & 0x3) << 6);
            TempColorModel tempColorModel = new TempColorModel(r1, g1, b1);
            if (tempColorModelSet.contains(tempColorModel)) {
                System.out.println("duplicate");
            } else {
                tempColorModelSet.add(tempColorModel);
            }
        }
        int size = -Byte.MIN_VALUE + Byte.MAX_VALUE + 1;
        Assert.assertEquals(tempColorModelSet.size(), size);
    }
}

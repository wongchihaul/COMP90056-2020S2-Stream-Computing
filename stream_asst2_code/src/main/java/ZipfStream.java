import org.apache.commons.math3.distribution.ZipfDistribution;

import java.util.ArrayList;

/**
 * @Author: Zhihao Huang
 * @StudentID: 1052452
 * @Date: 2020/10/18
 */
public class ZipfStream {
    private final ArrayList<Integer> zipfStream = new ArrayList<>();

    public ZipfStream(int universe, double exponent, int len) {
        ZipfDistribution zipf = new ZipfDistribution(universe, exponent);
        for (int i = 0; i < len; i++) {
            zipfStream.add(zipf.sample());
        }
    }

    public ArrayList<Integer> getZipStream() {
        return zipfStream;
    }
}

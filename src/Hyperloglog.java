import java.util.ArrayList;
import java.util.Arrays;

public class Hyperloglog implements DistinctCount {
    private final int b;
    private int RawE;

    public Hyperloglog(int b) {
        this.b = b;
    }
    public int getRawE(){ return this.RawE;}
    public long compute(ArrayList<Long> elements) {
        int m = 1 << b;
        int[] M = new int[m];
        Hash h = new Hash();
        for (long v : elements) {
            String x = h.h2b(v);
            int j = 1 + Integer.parseInt(x.substring(0, b),2);  //the first b bits of x
            String w = x.substring(b);  //the rest bits of x
            int cnt = 0;
            for (int i = 0;i < w.length(); i++) {
                if(w.charAt(i) == '0') cnt++;
            }
            M[j - 1] = Math.max(M[j - 1], cnt + 1);
        }
        double alpha_m;
        switch (m) {
            case 16:
                alpha_m = 0.673;
                break;
            case 32:
                alpha_m = 0.697;
                break;
            case 64:
                alpha_m = 0.709;
                break;
            default:
                alpha_m = 0.7213 / (1 + 1.079 / m);
        }

        int E = (int) (alpha_m * Math.pow(m, 2) * Math.pow(Arrays.stream(M).mapToDouble(v -> Math.pow(2, -v)).sum(), -1));
        this.RawE = E;
        int V = (int) Arrays.stream(M).filter(v -> v == 0).count();
        if (E <= 2.5 * m) {
            return (V != 0) ? (int) (m * Math.log((double)m / V)) : E;
        }
        if (E <= Math.pow(2, 32) / 30) {
            return E;
        } else {
            return (int) (-Math.pow(2, 32) * Math.log(1 - E / Math.pow(2, 32)));
        }
    }
}

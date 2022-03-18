import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: Zhihao Huang
 * @StudentID: 1052452
 * @Date: 2020/10/18
 */
public class StickySampling {
    private double s;               //support threshold
    private double epsilon;         //error tolerance
    private double delta;           //probability of failure
    private int r;              //sampling rate
    private int t;                  //window size
    private int wb;                 //window boundaries
    private int N;                  // the current length of the stream
    private boolean firstTime;
    private HashMap<Object, Integer> tracked;
    private ArrayList<Object> outputs;

    public StickySampling(double s, double delta){
        this.s = s;
        this.epsilon = s / 10;
        this.delta = delta;
        this.r = 1;
        this.t = (int) (1 / epsilon * Math.log(1 / (s * delta)) / Math.log(2));
        this.wb = t;
        this.N = 0;
        this.firstTime = true;
        tracked = new HashMap<>();
        outputs = new ArrayList<>();
    }

    public static boolean flipCoin(double p) {                   //successful with probability p
        return Math.random() < p;
    }

    public ArrayList<Object> output() {
        tracked.forEach((k, v) -> {
            if (v > (this.s - this.epsilon) * this.N) {
                outputs.add(k);
            }
        });
        return getOutputs();
    }

    public void add(Object x) {
        this.N++;
        Set<Object> marked = new HashSet<>();
        if (tracked.size() < wb) {
            if (tracked.containsKey(x)) {
                if (tracked.get(x) > 0) {
                    tracked.replace(x, tracked.get(x) + 1);
                }
            } else {
                if (flipCoin(1.0 / r)) {
                    tracked.put(x, 1);
                }
            }
        }  else {
            if (firstTime) {
                firstTime = false;
            } else {
                t *= 2;
            }
            r *= 2;
            wb += t;
            for (Object key : tracked.keySet()) {
                while (flipCoin(0.5)) {
                    tracked.replace(key, tracked.get(key) - 1);
                    if (tracked.get(key) == 0) {
                        marked.add(key);
                        break;
                    }
                }
            }
            for (Object y : marked) {
                tracked.remove(y);
            }
        }
    }


    public double getS() {
        return s;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public double getDelta() {
        return delta;
    }

    public int getR() {
        return r;
    }

    public int getT() {
        return t;
    }

    public int getN() {
        return N;
    }

    public HashMap<Object, Integer> getTracked() {
        return tracked;
    }

    public ArrayList<Object> getOutputs() {
        return outputs;
    }
}

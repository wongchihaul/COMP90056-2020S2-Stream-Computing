import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: Zhihao Huang
 * @StudentID: 1052452
 * @Date: 2020/10/18
 */
public class LossyCounting {
    private double s;               //support threshold
    private double epsilon;         //error tolerance
    private int N;                  //the current length of the stream
    private int w;                  //windowSize
    private int bucketID;                  //bucketID
    private HashMap<Object, Integer> tracked;
    private ArrayList<Object> outputs;

    public LossyCounting(double s){
        this.s = s;
        this.epsilon = s / 10;
        this.N = 0;
        this.w = (int) (1 / epsilon + 1);      //floor(1/e)
        this.bucketID = 1;                         // bucket = floor(N/w)
        tracked = new HashMap<>();
        outputs = new ArrayList<>();
    }

    public void add(Object x) {
        N++;

        if (tracked.containsKey(x)) {
            tracked.replace(x, tracked.get(x) + 1);
        } else {
            tracked.put(x, bucketID + 1);
        }
        if (N % w == 0) {
            bucketID = N / w + 1;

            Set<Object> marked = new HashSet<>();
            tracked.replaceAll((key, v) -> v - 1);

            for (Object k : tracked.keySet()) {
                if (tracked.get(k) <= bucketID) {
                    marked.add(k);
                }
            }
            for (Object k : marked) {
                tracked.remove(k);
            }
        }
    }

    public ArrayList<Object> output(){
        tracked.forEach((k,v) -> {
            if(v > (this.s - this.epsilon) * this.N){
                outputs.add(k);
            }
        });
        return getOutputs();
    }

    public double getS() {
        return s;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public int getN() {
        return N;
    }

    public int getW() {
        return w;
    }

    public int getBucketID() {
        return bucketID;
    }

    public HashMap<Object, Integer> getTracked() {
        return tracked;
    }

    public ArrayList<Object> getOutputs() {
        return outputs;
    }
}

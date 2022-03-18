import java.util.*;

/**
 * @Author: Zhihao Huang
 * @StudentID: 1052452
 * @Date: 2020/10/18
 */
public class SpaceSaving {
    private double s;               //support threshold
    private int t;                  //num of monitored elements
    private int N;                  // the current length of the stream
    private HashMap<Object,Integer> tracked;
    private ArrayList<Object> outputs;

    public SpaceSaving(double s){
        this.s = s;
        this.t = (int) (1 / s);
        this.N = 0;
        tracked = new HashMap<>();
        outputs = new ArrayList<>();
    }

    public void add(Object x){
        this.N ++;

        if(tracked.containsKey(x)){
            tracked.replace(x, tracked.get(x) + 1);
        } else {
            if(tracked.size() < t){
                tracked.put(x, 1);
            } else{
                int minCount = removeKeyWithMinVal(tracked);
                tracked.put(x, minCount + 1);
            }
        }
    }

    // remove key with min value and return the value
    public int removeKeyWithMinVal(HashMap<Object, Integer> map){
        List<HashMap.Entry<String,Integer>> list = new ArrayList(map.entrySet());
        list.sort(Comparator.comparingInt(Map.Entry::getValue));
        Object keyWithMinVal = list.get(0).getKey();
        Integer val = list.get(0).getValue();
        map.remove(keyWithMinVal);
        return val;
    }

    public ArrayList<Object> output(){
        outputs.addAll(tracked.keySet());
        return outputs;
    }

    public double getS() {
        return s;
    }

    public int getT() {
        return t;
    }

    public int getN() {
        return N;
    }

    public Map<Object, Integer> getTracked() {
        return tracked;
    }

    public ArrayList<Object> getOutputs() {
        return outputs;
    }
}

import java.util.HashSet;

public class baseline implements DistinctCount{
    private static HashSet<Integer> hset = new HashSet<>();
    public baseline() {
    }

    public int compute(int[] elements) {
        for(int e : elements) {
            hset.add(e);
        }
        return hset.size();
    }
}

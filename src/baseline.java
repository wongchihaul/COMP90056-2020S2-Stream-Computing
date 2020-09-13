import java.util.HashSet;

public class baseline implements DistinctCount{
    private static HashSet<Long> hset = new HashSet<>();
    public baseline() {
    }

    public long compute(int[] elements) {
        for(long e : elements) {
            hset.add(e);
        }
        return hset.size();
    }
}

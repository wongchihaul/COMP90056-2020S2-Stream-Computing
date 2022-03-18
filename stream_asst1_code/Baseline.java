import java.util.ArrayList;

public class Baseline implements DistinctCount{
    public Baseline() { }

    public long compute(ArrayList<Long> elements) {
        return elements.stream().distinct().count();
    }
}

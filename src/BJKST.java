import java.util.Comparator;
import java.util.PriorityQueue;

public class BJKST implements DistinctCount {
    private int n;      //universe {1,2,...,n}
    private double eps;  //epsilon

    public BJKST(int range, double eps) {
        this.n = range;
        this.eps = eps;
    }

    public long compute(int[] elements) {
        int t = (int) Math.ceil(96 / Math.pow(eps, 2));
        PriorityQueue<Long> pq = new PriorityQueue<>((i1, i2) -> (int) (i2 - i1));
        LongHash h = new LongHash();
        for (int x:elements) {
            long hx = h.h2u(x, (long)Math.pow(n,3));
            if(pq.size() < t){
                pq.add(hx);
            } else {
                if(hx < pq.peek() && !pq.contains(hx)){
                    pq.poll();
                    pq.add(hx);
                }
            }
        }
//        System.out.println("t --> " + t);
//        System.out.println("n^3 --> " + (long)Math.pow(n , 3));
//        System.out.println("max --> " + pq.peek());
        return (long) (t * Math.pow(n , 3) / pq.peek());
    }
}

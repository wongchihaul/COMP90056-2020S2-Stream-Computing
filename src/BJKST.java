import java.util.PriorityQueue;

public class BJKST implements DistinctCount {
    private int n;      //universe {1,2,...,n}
    private double eps;  //epsilon

    public BJKST(int range, double eps) {
        this.n = range;
        this.eps = eps;
    }

    public int compute(int[] elements) {
        int t = (int) Math.ceil(96 / Math.pow(eps, 2));
        PriorityQueue<Integer> pq = new PriorityQueue<>((i1, i2) -> i2 - i1);
        Hash h = new Hash();
        for (int x:elements) {
            int hx = h.h2u(x, (int)Math.pow(n,3));
            if(pq.size() < t){
                pq.add(hx);
            } else {
                if(hx < pq.peek() && !pq.contains(hx)){
                    pq.poll();
                    pq.add(hx);
                }
            }
        }
        int maxInQueue = pq.peek();
        return (int) (t * Math.pow(n , 3) / maxInQueue);
    }
}

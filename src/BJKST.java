import java.util.PriorityQueue;

public class BJKST implements DistinctCount {
    private int n;      //universe {1,2,...,n}
    private double eps;  //epsilon

    public BJKST(int elementLen, double eps) {
        this.n = elementLen;
        this.eps = eps;
    }

    public long compute(int[] elements) {
        int t = (int) Math.ceil(96 / Math.pow(eps, 2));
        PriorityQueue<Long> pq = new PriorityQueue<>((i1, i2) -> (int) (i2 - i1));
        Hash h = new Hash();
        for (int x:elements) {
            long hx = h.h2u(x, (int)Math.pow(n,3));
            if(pq.size() < t){
                pq.add(hx);
            } else {
                if(hx < pq.peek() && !pq.contains(hx)){
                    pq.poll();
                    pq.add(hx);
                }
            }
        }
        long maxInQueue = pq.peek();
        System.out.println(maxInQueue);
        return (long) (t * Math.pow(n,3) / maxInQueue);
    }
}

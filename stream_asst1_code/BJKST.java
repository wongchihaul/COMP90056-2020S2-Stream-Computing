import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class BJKST implements DistinctCount {
    private int n;      //universe {1,2,...,n}
    private double eps;  //epsilon


    public BJKST(int universe, double eps) {
        this.n = universe;
        this.eps = eps;
    }

    public long compute(ArrayList<Long> elements) {
        int t = (int) Math.ceil(96 / Math.pow(eps, 2));         // t smallest hash value
        Hash h = new Hash();
        PriorityQueue<Long> pq = new PriorityQueue<>(Collections.reverseOrder());      //Max Heap tracks t-th smallest hash value that ever seen so far
        for (long x:elements) {
            long hx = h.h2u(x, (long)Math.pow(n,3));
            if(pq.size() < t){
                pq.add(hx);
            }
            else {
                if (hx < pq.peek() && !pq.contains(hx)) {
                    pq.poll();
                    pq.add(hx);
                }
            }
        }
        return (long) (t * Math.pow(n,3) / pq.peek());                //Biggest value in MaxHeap with size t is the t-th smallest value.
    }
}

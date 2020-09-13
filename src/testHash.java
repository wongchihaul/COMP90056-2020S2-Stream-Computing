import java.util.PriorityQueue;

public class testHash {
    private int p = 24593; //smaller than 2^15
    private int a,b;		// only use for hash tables < 24593 in size

    public testHash(){
        a=StdRandom.uniform(p-1)+1;	// choose random parameters
        b=StdRandom.uniform(p);
    }
    public int h2u(int x,int r){

        int y = (a*x+b) % p;
        return y % r;
    }

    public static int h_basic(Object key){	    // if you only want the
        return (key.hashCode() & 0x0000ffff);   // lower 16 bits
    }



    public static void main(String args[]){
//        testHash h = new testHash();
//
//        int h2 = h.h2u(100,1000000);
//        System.out.println(h2);
        int[] testArr = new int[] {1,3,5,3,4,1,3,7,5,9,3,6,7,9,7,2};
        int t = 3;
        PriorityQueue<Integer> pq = new PriorityQueue<>((i1, i2) -> (int) (i2 - i1));
        for(int x : testArr) {
            if(pq.size() < t) {
                pq.add(x);
            }
            else{
                if(x < pq.peek() && !pq.contains(x)){
                    pq.poll();
                    pq.add(x);
                }
            }
        }
        System.out.println(pq);
    }
}

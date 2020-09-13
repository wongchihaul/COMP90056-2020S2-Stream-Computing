public class LongHash {
    private long p1 = 4398046511119L;  // slightly larger than 2^42 for BJKST
//    private int p1 = 1073741789; //smaller than 2^30
    private long p2 = 4294967653L; // slightly larger than 2^32 for hyperLoglog
    private long a1,b1;		// only use for hash tables < 1321123 in size
    private long a2,b2;

    public LongHash(){
        a1 = (long) (StdRandom.uniform(0,p1 - 1) + 1);
        b1 = (long) StdRandom.uniform(0,p1);
        a2 = (long) (StdRandom.uniform(0,p2 - 1) + 1);
        b2 = (long) StdRandom.uniform(0,p2);
//        System.out.println("a1: " + a1 + ";b1: " + b1 + ";p1: " + p1 +"a2: " + a2 + ";b2: " + b2 + ";p2: " + p2);
    }
    public long h2u(int x,long range){
        long prod = a1 *x;
        prod += b1;
        long y = prod % p1;
        return y % range;
    }
    public String h2b(int x) {
        long prod = a2 *(long)x;
        prod += b2;
        long y = prod % p2;
        long hashValue = y % 2147483647;
        return addZeroLHS(Long.toBinaryString(hashValue & 4294967295L),32);
    }


    public String addZeroLHS(String str, int strLen) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strLen - str.length(); i++) {
            sb.append("0");
        }
        sb.append(str);
        return sb.toString();
    }
}

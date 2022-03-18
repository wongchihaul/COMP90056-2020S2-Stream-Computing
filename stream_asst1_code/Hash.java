import java.math.BigInteger;

public class Hash {
//    private long p1 = 2199023255579L;  // slightly larger than 2^41, for BJKST
    private long p1 =2305843009213693951L;      //2^61 - 1 for BJKST
    private long p2 = 6074001001L; // 2^32 < p2 < 2^33, for hyperLoglog
    private long a1,b1;		// only use for hash tables < 1321123 in size
    private long a2,b2;     //  only use for hash tables < 2^32 in size

    public Hash(){
        StdRandom.setSeed(0);
        a1 = (long) (StdRandom.uniform(0,p1 - 1) + 1);
        b1 = (long) StdRandom.uniform(0,p1);
        a2 = (long) (StdRandom.uniform(0,p2 - 1) + 1);
        b2 = (long) StdRandom.uniform(0,p2);

    }

    /**
     * Two-family hash function
     * @param x
     * @param range
     * @return
     */
//    public long h2u(long x,long range){
//        long prod = a1 *x;
//        prod += b1;
//        long y = prod % p1;
//        return y % range;
//    }
    public long h2u(long x, long range){              // a * x may exceed range of Long so we must use such a Number class with bigger interval
        BigInteger big_a1 = new BigInteger(String.valueOf(a1));                     // x < 2^20, then x^3 < 2^60, then 2 * x^3 < 2^61 - 1
        BigInteger big_x = new BigInteger(String.valueOf(x));
        BigInteger big_b1 = new BigInteger(String.valueOf(b1));
        BigInteger big_p1 = new BigInteger(String.valueOf(p1));
        BigInteger big_prod = big_a1.multiply(big_x);                   //prod = a1 * x
        big_prod = big_prod.add(big_b1);
        long y = big_prod.divideAndRemainder(big_p1)[1].longValue();                          // store Quotient in [0] and Remainder in [1]; then y = prod % p1;
        return y % range;
    }



    public String h2b(long x) {
        long prod = a2 * x;
        prod += b2;
        long y = prod % p2;
        long hashValue = y % (long)(Math.pow(2,32));
        return addZeroLHS(Long.toBinaryString(hashValue & 4294967295L),32);     //get lower 32-bit after hash
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

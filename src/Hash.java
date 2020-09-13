// Hash.java
// Hash class
// awirth for COMP90056
// Aug 2017-20

public class Hash{
	private long p1 = 2305843009213693952L ; // 2^61 - 1
	private long p2 = 4294968827L; // slightly lager than 2^32
	private long a1,b1;		// only use for hash tables < 1321123 in size //for h2u
	private long a2,b2;		//for h2b

	public Hash(){
//		a=StdRandom.uniform(p1 -1)+1;
//		b=StdRandom.uniform(p1); // changed from p-1
		a1 = (long) (StdRandom.uniform(0,p1-1) + 1);
		b1 = (long) (StdRandom.uniform(0,p1));
	}
	public long h2u(int x,long range){
		long prod = a1 *(long)x;
		prod += b1;
		long y = prod % p1;
		return y % range;
	}
	public String h2b(int x) {
		return addZeroLHS(Long.toBinaryString(h2u(x, p2) & 0xffffffffL),32);
//		return Long.toBinaryString(h2u(x, p2) & 0xffffffffL);
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

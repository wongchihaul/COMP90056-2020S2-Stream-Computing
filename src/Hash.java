// Hash.java
// Hash class
// awirth for COMP90056
// Aug 2017-20

public class Hash{
	private int p = 1073741789; //smaller than 2^30
	private int a,b;		// only use for hash tables < 24593 in size

	public Hash(){
		a=StdRandom.uniform(p-1)+1;
		b=StdRandom.uniform(p); // changed from p-1
	}
	public int h2u(int x,int range){
		long prod = (long)a*(long)x;
		prod += (long)b;
		long y = prod % (long) p;
		int r = (int) y % range;
		//System.out.format("x %12d y %12d r %12d %n", x,y,r);
		return r;
	}
	public String h2b(int x) {
		return addZeroLHS(Integer.toBinaryString(h2u(x,2147483646)),32);
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

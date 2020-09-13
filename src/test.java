public class test {
    public static void main(String[] args) {
        int[] testArray = new int[(int)Math.pow(10,7)];
        StdRandom.setSeed(0);
        for (int i = 0; i < testArray.length; i++) {
            testArray[i] = StdRandom.uniform(483646);
        }
        BJKST bjkst = new BJKST(testArray.length, 0.01);
        hyperLogLog hlog = new hyperLogLog(8);
        baseline bsl = new baseline();
        metricMeasure(bjkst, testArray);
        metricMeasure(hlog, testArray);
        metricMeasure(bsl, testArray);
    }

    public static void metricMeasure (DistinctCount dc, int[] element) {
        Runtime r = Runtime.getRuntime();
        r.gc();
        long start = System.currentTimeMillis();
        long startMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long distinct = dc.compute(element);
        long endMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long end = System.currentTimeMillis();
        System.out.println("Running: " + dc.getClass());
        System.out.println("Number of Distinct Elements is " + distinct);
        System.out.println("TimeCost: "+ (end - start) +"ms");
        System.out.println("MemoryCost: "+ ((endMem - startMem) / (1024 * 1024)) +"MB");
    }
}

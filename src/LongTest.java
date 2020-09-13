import java.util.Arrays;

public class LongTest {
    public static void main(String[] args) {
        int[] testArray = new int[100000];
        StdRandom.setSeed(0);
        int range = (int)(Math.pow(2,31) - 1);
        for (int i = 0; i < testArray.length; i++) {
            testArray[i] = StdRandom.uniform(range);
        }
        BJKST bjkst = new BJKST(testArray.length, 0.1);
        hyperLogLog hlog = new hyperLogLog(16);
        baseline bsl = new baseline();
        metricMeasure(bjkst, testArray, 0.01);
        metricMeasure(hlog, testArray, 0.01);
        metricMeasure(bsl, testArray, 0.01);
    }

    public static void metricMeasure (DistinctCount dc, int[] elements, double delta) {
        Runtime r = Runtime.getRuntime();
        r.gc();
        long start = System.currentTimeMillis();
        long startMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
//        long distinct = dc.compute(elements);
        long distinct = medianTrick(dc, elements, delta);
        long endMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long end = System.currentTimeMillis();
        System.out.println("Running: " + dc.getClass());
        System.out.println("Number of Distinct Elements is " + distinct);
        System.out.println("TimeCost: "+ (end - start) +"ms");
        System.out.println("MemoryCost: "+ ((endMem - startMem) / 1024.0 ) +"KB");
    }

    public static long medianTrick(DistinctCount dc, int[] elements, double delta) {
        int d = (int)(12 * Math.log(1 / delta));
        long[] distincts = new long[d];
        for (int i = 0; i < d; i++) {
            distincts[i] = dc.compute(elements);
        }
        Arrays.sort(distincts);
        return (distincts.length % 2 != 0) ? distincts[distincts.length / 2] : (distincts[distincts.length /2 ] + distincts[distincts.length / 2 - 1]) / 2 ;
    }
}
import java.io.File;
import java.util.*;

public class test {
    public static void main(String[] args) {
        StdRandom.setSeed(0);
//        Question3();
//        Question4();
        Question5();
//        Question6();
//        Question7();
//        Question8();
    }

    public static void Question3(){
        int universe = (int)Math.pow(2,20);           //in order not to go beyond 2^61 - 1
        int numOfDistinct = (int)Math.pow(2,10);
        Hash h = new Hash();
        ArrayList<Integer> elements = new ArrayList<>();
        for (int i = 0; i < numOfDistinct; i++) {
            elements.add((int) h.h2u(i, universe));
        }
        Iterator<Integer> it = elements.stream().sorted().iterator();
        int fallInTheRange = 0;
        for(int j = 0; j < numOfDistinct; j++){
            int e = it.next();
            if(j * universe/numOfDistinct < e && e <= (j+1) * universe/numOfDistinct) {
                fallInTheRange ++;
            }
        }
        System.out.println("For every interval (i*2^11,(i+1)*2^11], where i is in [0,1024], it has " + fallInTheRange*1.0/numOfDistinct +" elements on average");
    }

    public static void Question4(){
        long universe = (long)Math.pow(2,32);
        Hyperloglog hlog = new Hyperloglog(16);
        Baseline bsl = new Baseline();
        for (int i = 1; i < 6; i++) {
            int numOfDistinct = (int)Math.pow(10,i);
            int length = (int)Math.pow(10,6);
            long[] dumpArray = new long[numOfDistinct];
            for (int j = 0; j < numOfDistinct ; j++) {
                dumpArray[j] = (long)StdRandom.uniform(0,universe);
            }
            ArrayList<Long> elements = new ArrayList<>();
            for (int k = 0; k < numOfDistinct; k++) {               //for each distinct element, repeat length/numOfDistinct times
                for(int m = 0; m < length/numOfDistinct; m++){
                    elements.add(dumpArray[k]);
                }
            }
            Collections.shuffle(elements);
            long hlogCardinality = hlog.compute(elements);
            long bslCardinality = bsl.compute(elements);
            System.out.println("HyperLogLog: Number of Distinct Elements is " + hlogCardinality);
            System.out.println("HyperLogLog: Raw E is " + hlog.getRawE());
            System.out.println("Exact Number of Distinct Elements is " + bslCardinality);
            System.out.println("Relative Error is: " + Math.abs(hlogCardinality - bslCardinality) / (bslCardinality * 1.0));
            System.out.println("**********************");
        }
    }

    public static void Question5(){
        int universe = (int)Math.pow(2,20);               //in order not to exceed 2^61 - 1
        BJKST bjkst = new BJKST(universe, 0.05);
        Hyperloglog hlog = new Hyperloglog(16);          // 2^9 = 512, 1.04/sqrt(512) ~= 0.046
        Baseline bsl = new Baseline();
        int d = (int)(12 * Math.log(1 / 0.1));
        System.out.println(" Number of Distinct Elements now may be 10^4");
        ArrayList<Long> bjkst4 = new ArrayList<>();
        ArrayList<Long> hyperloglog4 = new ArrayList<>();
        for (int i = 0; i < d; i++) {
            int numOfDistinct4 = (int)Math.pow(10,4.002);                       //in order to get exact 10^4 distinct elements
            int length4 = (int)Math.pow(10,5);
            long[] dumpArray4 = new long[numOfDistinct4];
            for (int j = 0; j < numOfDistinct4 ; j++) {
                dumpArray4[j] = StdRandom.uniform(0,universe);
            }
            ArrayList<Long> elements4 = new ArrayList<>();
            for (int k = 0; k < numOfDistinct4; k++) {               //for each distinct element, repeat length/numOfDistinct times
                for(int m = 0; m < length4/numOfDistinct4; m++){
                    elements4.add(dumpArray4[k]);
                }
            }
            Collections.shuffle(elements4);
            long bjkstCardinality = bjkst.compute(elements4);
            long hlogCardinality = hlog.compute(elements4);
            bjkst4.add(bjkstCardinality);
            hyperloglog4.add(hlogCardinality);
//            if(i == 0) {System.out.println("Exact Distinct count: " +medianTrick(bsl, elements4,0.05));} else{break;}             //get the exact  number distinct elememts
        }
        System.out.println("==============================");
        System.out.println(" Number of Distinct Elements now may be 10^6");
        ArrayList<Long> bjkst6 = new ArrayList<>();
        ArrayList<Long> hyperloglog6 = new ArrayList<>();
        for (int i = 0; i < d; i++) {
//            int numOfDistinct6 = (int)Math.pow(10,5.02165);         //in order to get exact 10^5 distinct elements
            int numOfDistinct6 = (int)Math.pow(10,6.50852);             //in order to get exact 10^6 distinct elements
            int length6 = (int)Math.pow(10,7);
            long[] dumpArray6 = new long[numOfDistinct6];
            for (int j = 0; j < numOfDistinct6 ; j++) {
                dumpArray6[j] = StdRandom.uniform(universe);
            }
            ArrayList<Long> elements6 = new ArrayList<>();
            for (int k = 0; k < numOfDistinct6; k++) {               //for each distinct element, repeat length/numOfDistinct times
                for(int m = 0; m < length6/numOfDistinct6; m++){
                    elements6.add(dumpArray6[k]);
                }
            }
            Collections.shuffle(elements6);
            long bjkstCardinality = bjkst.compute(elements6);
            long hlogCardinality = hlog.compute(elements6);
            bjkst6.add(bjkstCardinality);
            hyperloglog6.add(hlogCardinality);
//            if(i == 0) {System.out.println("Exact Distinct count: " +medianTrick(bsl, elements6,0.1));} else{break;}          //get the exact  number distinct elememts
        }
        System.out.println("bjkst4: ");
        bjkst4.stream().forEach(v -> System.out.print(v + ","));
        System.out.println();
        System.out.println("bjkst6: ");
        bjkst6.stream().forEach(v -> System.out.print(v + ","));
        System.out.println();
        System.out.println("hyperloglog4: ");
        hyperloglog4.stream().forEach(v -> System.out.print(v + ","));
        System.out.println();
        System.out.println("hyperloglog6: ");
        hyperloglog6.stream().forEach(v -> System.out.print(v + ","));
    }

    public static void Question6(){
        int universe = (int)Math.pow(2,20);  // less than p^(1/3), use this universe in comparison when involving BJKST.
        BJKST bjkst = new BJKST(universe, 0.05);
        Hyperloglog hlog = new Hyperloglog(16);
        Baseline bsl = new Baseline();
        for(int i = 1; i < 5; i++){
            int numOfDistinct = (int)Math.pow(10,i);
            int length = (int)Math.pow(10,6);
            long[] dumpArray = new long[numOfDistinct];
            for (int j = 0; j < numOfDistinct ; j++) {
                dumpArray[j] = StdRandom.uniform(0,universe);
            }
            ArrayList<Long> elements = new ArrayList<>();
            for (int k = 0; k < numOfDistinct; k++) {               //for each distinct element, repeat length/numOfDistinct times
                for(int m = 0; m < length/numOfDistinct; m++){
                    elements.add(dumpArray[k]);
                }
            }
            Collections.shuffle(elements);
            long bjkstCardinality = bjkst.compute(elements);
            long hlogCardinality = hlog.compute(elements);
            long bslCardinality = bsl.compute(elements);
            System.out.println("BJKST: Cardinality is " + bjkstCardinality);
            System.out.println("HyperLogLog: Cardinality is " + hlogCardinality);
            System.out.println("Exact Cardinality is " + bslCardinality);
            System.out.println("************");
        }
    }

    public static void Question7(){
        // Comparison among BJKST, Hyperloglog and Baseline
        System.out.println("------Comparison among BJKST, Hyperloglog and Baseline-----");
        int universe1 = 13000;  // less than p^(1/3), use this universe in comparison when involving BJKST.
        BJKST bjkst1 = new BJKST(universe1, 0.1);
        Hyperloglog hlog1 = new Hyperloglog(16);
        Baseline bsl1 = new Baseline();
        for(int i = 1; i < 5; i++) {
            int numOfDistinct = (int) Math.pow(10, i);
            int length1 = (int) Math.pow(10, 5);
            long[] dumpArray = new long[numOfDistinct];
            for (int j = 0; j < numOfDistinct; j++) {
                dumpArray[j] = StdRandom.uniform(0, universe1);
            }
            ArrayList<Long> elements1 = new ArrayList<>();
            for (int k = 0; k < numOfDistinct; k++) {               //for each distinct element, repeat length/numOfDistinct times
                for (int m = 0; m < length1 / numOfDistinct; m++) {
                    elements1.add(dumpArray[k]);
                }
            }
            Collections.shuffle(elements1);
            memMeasure(bjkst1, elements1);
            memMeasure(hlog1, elements1);
            memMeasure(bsl1, elements1);
        }


        System.out.println("-------Comparison between Hyperloglog and Baseline-----");
        long universe2 = (long)Math.pow(2,32);
        Hyperloglog hlog2 = new Hyperloglog(16);
        Baseline bsl2 = new Baseline();
        for (int i = 1; i < 9; i++) {
            int length2 = (int) Math.pow(10, i);       //Assuming that cardinality == length of stream
            ArrayList<Long> elements2 = new ArrayList<>();
            for (int j = 0; j < length2; j++) {
                elements2.add((long) StdRandom.uniform(0, universe2));
            }
            memMeasure(hlog2, elements2);
            memMeasure(bsl2, elements2);
        }

    }

    public static void Question8(){
        int universe = 13000;  // less than p^(1/3), use this universe in comparison when involving BJKST.
        BJKST bjkst = new BJKST(universe, 0.1);
        Hyperloglog hlog = new Hyperloglog(16);
        Baseline bsl = new Baseline();
        for(int i = 1; i < 5; i++){
            int numOfDistinct = (int)Math.pow(10,i);
            int length = (int)Math.pow(10,6);
            long[] dumpArray = new long[numOfDistinct];
            for (int j = 0; j < numOfDistinct ; j++) {
                dumpArray[j] = StdRandom.uniform(0,universe);
            }
            ArrayList<Long> elements = new ArrayList<>();
            for (int k = 0; k < numOfDistinct; k++) {               //for each distinct element, repeat length/numOfDistinct times
                for(int m = 0; m < length/numOfDistinct; m++){
                    elements.add(dumpArray[k]);
                }
            }
            Collections.shuffle(elements);
            long exactNumOfDistinct = bsl.compute(elements);
            timeMeasure(bjkst, elements, exactNumOfDistinct);
            timeMeasure(hlog, elements, exactNumOfDistinct);
            System.out.println("---------Exact Number of Distinct Elements is " + exactNumOfDistinct + "------------");
        }
    }







    public static void memMeasure (DistinctCount dc, ArrayList<Long> elements) {

        System.gc();
        long startMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long distinct = dc.compute(elements);
//        long distinct = medianTrick(dc, elements, delta);
        long endMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        System.gc();
        System.out.println("Running: " + dc.getClass());
        System.out.println("Number of Distinct Elements is " + distinct);
        System.out.println("MemoryCost: "+ ((endMem - startMem) / 1024.0 ) +"KB");
        System.out.println("********************");
    }

    public static void timeMeasure(DistinctCount dc, ArrayList<Long> elements, long exactNumOfDistinct){
        long start = System.currentTimeMillis();
        long distinct = dc.compute(elements);
        long end = System.currentTimeMillis();
        System.out.println("Running: " + dc.getClass());
        System.out.println("Relative Error is " + Math.abs(distinct - exactNumOfDistinct) / (exactNumOfDistinct * 1.0));
        System.out.println("TimeCost: "+ (end - start) +"ms");
        System.out.println("********************");
    }

    public static long medianTrick(DistinctCount dc, ArrayList<Long> elements, double delta) {
        int d = (int)(12 * Math.log(1 / delta));
        long[] distincts = new long[d];
        for (int i = 0; i < d; i++) {
            distincts[i] = dc.compute(elements);
        }
        Arrays.sort(distincts);
        return (distincts.length % 2 != 0) ? distincts[distincts.length / 2] : (distincts[distincts.length /2 ] + distincts[distincts.length / 2 - 1]) / 2 ;
    }
}


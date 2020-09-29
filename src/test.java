import java.util.*;

public class test {
    public static void main(String[] args) {
//        Question3();
        Question4();
//        Question5();
//        Question6();
//        Question7();
//        Question8();
    }

    public static void Question3(){
        StdRandom.setSeed(0);
        int universe = (int)Math.pow(2,20);           //in order not to go beyond 2^61 - 1
        int numOfDistinct = (int)Math.pow(2,10);
        Hash h = new Hash();
        ArrayList<Integer> elements = new ArrayList<>();        //contains randomly chosen elements
        ArrayList<Integer> hashedElem = new ArrayList<>();      //contains hased elements
        for (int i = 0; i < numOfDistinct; i++) {
            int j = StdRandom.uniform(universe);
            elements.add(j);
            hashedElem.add((int) h.h2u(j, universe));
        }
        elements.stream().sorted().forEach(e -> System.out.print(e + ", "));
        System.out.println();
        hashedElem.stream().sorted().forEach(e -> System.out.print(e + ", "));
    }

    public static void Question4(){
        StdRandom.setSeed(0);
        long universe = (long)Math.pow(2,32);
        Hyperloglog hlog = new Hyperloglog(universe,16);
        Baseline bsl = new Baseline();
        ArrayList<Long> distinctCount = new ArrayList<>();
        ArrayList<Double> RawE_Error = new ArrayList<>();
        ArrayList<Double> CorrectedE_Error = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            int numOfDistinct =  (int) Math.pow(10, i);
//            switch (i) {                                            //in order to get exact (or very close to) 10^4, 10^5,10^6 distinct elements
//                case 4:
//                    numOfDistinct = (int) Math.pow(10, 4.002);
//                    break;
//                case 5:
//                    numOfDistinct = (int) Math.pow(10, 5.02165);
//                    break;
//                case 6:
//                    numOfDistinct = (int) Math.pow(10, 6.50852);
//                    break;
//                default:
//                    numOfDistinct = (int) Math.pow(10, i);
//                    break;
//            }
            ArrayList<Long> elements = new ArrayList<>();
            for (int j = 0; j < numOfDistinct ; j++) {
                elements.add((long)StdRandom.uniform(0,universe));
            }
            Collections.shuffle(elements);
            long hlogCardinality = hlog.compute(elements);
            long bslCardinality = bsl.compute(elements);
            distinctCount.add(bslCardinality);
            RawE_Error.add((hlog.getRawE() - bslCardinality) / (bslCardinality * 1.0));
            CorrectedE_Error.add((hlogCardinality - bslCardinality) / (bslCardinality * 1.0));
        }
        distinctCount.stream().forEach(e -> System.out.print(e + ", "));
        System.out.println();
        RawE_Error.stream().forEach(e -> System.out.print(e + ", "));
        System.out.println();
        CorrectedE_Error.stream().forEach(e -> System.out.print(e + ", "));
    }

    public static void Question5(){
        int universe1 = (int)Math.pow(2,20);               //in order not to exceed 2^61 - 1
        long universe2 = (long)Math.pow(2,32);
        BJKST bjkst = new BJKST(universe1, 0.05);
        Hyperloglog hlog = new Hyperloglog(universe2,16);
        Baseline bsl = new Baseline();
        int d = (int)(1 * Math.log(1 / 0.1));
        ArrayList<Long> bjkst4 = new ArrayList<>();
        ArrayList<Long> hyperloglog4 = new ArrayList<>();
        ArrayList<Long> bjkst6 = new ArrayList<>();
        ArrayList<Long> hyperloglog6 = new ArrayList<>();
        for (int i = 0; i < d; i++) {
            StdRandom.setSeed(i);                                                   // generate different random value arrays in each iteration.

            int numOfDistinct4 = (int)Math.pow(10,4.002);                       //in order to get exact 10^4 distinct elements
            ArrayList<Long> elements4b = new ArrayList<>();
            ArrayList<Long> elements4h = new ArrayList<>();
            for (int j = 0; j < numOfDistinct4 ; j++) {
                elements4b.add((long) StdRandom.uniform(0,universe1));
                elements4h.add((long) StdRandom.uniform(0,universe2));
            }
            long bjkstCardinality4 = bjkst.compute(elements4b);
            long hlogCardinality4 = hlog.compute(elements4h);
            bjkst4.add(bjkstCardinality4);
            hyperloglog4.add(hlogCardinality4);
//            if(i == 0) {System.out.println("Exact Distinct count: " +medianTrick(bsl, elements4,0.05));} else{break;}             //get the exact number distinct elememts


            int numOfDistinct6 = (int)Math.pow(10,6.5081);             //in order to get exact 10^6 distinct elements
            ArrayList<Long> elements6b = new ArrayList<>();
            ArrayList<Long> elements6h = new ArrayList<>();
            for (int j = 0; j < numOfDistinct6 ; j++) {
                elements6b.add((long) StdRandom.uniform(0, universe1));
                elements6h.add((long) StdRandom.uniform(0, universe2));
            }
            long bjkstCardinality6 = bjkst.compute(elements6b);
            long hlogCardinality6 = hlog.compute(elements6h);
            bjkst6.add(bjkstCardinality6);
            hyperloglog6.add(hlogCardinality6);
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
//
//    public static void Question6(){
//        StdRandom.setSeed(0);
//        int universe = (int)Math.pow(2,20);  // less than p^(1/3), use this universe in comparison when involving BJKST.
//        BJKST bjkst = new BJKST(universe, 0.1);
//        Hyperloglog hlog = new Hyperloglog(16);
//        Baseline bsl = new Baseline();
//        ArrayList<Long> distinctCount = new ArrayList<>();
//        ArrayList<Double> BJKST_Error = new ArrayList<>();
//        ArrayList<Double> Hyper_Error = new ArrayList<>();
////        for(int i = 1; i < 7; i++)
//        for(int i = 0; i < 12; i++)
//        {
////            int numOfDistinct = (int)Math.pow(10,i);                      //After running comparison with the distinct counts [10,100,1000,9955,95316,644380],
//            int numOfDistinct = (int)Math.pow(10,4 + 0.1 * i);        //I found that these two algorithm work fine in range 10^4~10^5, so I'd like to have a comparison in this range.
//            int length = (int)Math.pow(10,6);
//            long[] dumpArray = new long[numOfDistinct];
//            for (int j = 0; j < numOfDistinct ; j++) {
//                dumpArray[j] = StdRandom.uniform(0,universe);
//            }
//            ArrayList<Long> elements = new ArrayList<>();
//            for (int k = 0; k < numOfDistinct; k++) {               //for each distinct element, repeat length/numOfDistinct times
//                for(int m = 0; m < length/numOfDistinct; m++){
//                    elements.add(dumpArray[k]);
//                }
//            }
//            Collections.shuffle(elements);
//            long bjkstCardinality = bjkst.compute(elements);
//            long hlogCardinality = hlog.compute(elements);
//            long bslCardinality = bsl.compute(elements);
//            distinctCount.add(bslCardinality);
//            BJKST_Error.add((bjkstCardinality - bslCardinality) * 1.0 / bslCardinality);
//            Hyper_Error.add((hlogCardinality - bslCardinality) * 1.0 / bslCardinality);
//        }
//        distinctCount.stream().forEach(e -> System.out.print(e + ", "));
//        System.out.println();
//        BJKST_Error.stream().forEach(e -> System.out.print(e + ", "));
//        System.out.println();
//        Hyper_Error.stream().forEach(e -> System.out.print(e + ", "));
//    }
//
//    public static void Question7(){
//        StdRandom.setSeed(0);
//        System.out.println("------Comparison among BJKST, Hyperloglog and Baseline-----");
//        int universe1 = (int)Math.pow(2,20);  // less than p^(1/3), use this universe in comparison when involving BJKST.
//        BJKST bjkst1 = new BJKST(universe1, 0.1);
//        Hyperloglog hlog1 = new Hyperloglog(16);
//        Baseline bsl1 = new Baseline();
//        ArrayList<Long> distinctCount = new ArrayList<>();
//        ArrayList<Double> memBaseline = new ArrayList<>();
//        ArrayList<Double> memBJKST = new ArrayList<>();
//        ArrayList<Double> memHyper = new ArrayList<>();
//        for(int i = 1; i < 7; i++) {
//            int numOfDistinct;
//            switch (i) {                                            //in order to get exact (or very close to) 10^4, 10^5,10^6
//                case 4:
//                    numOfDistinct = (int) Math.pow(10, 4.002);
//                    break;
//                case 5:
//                    numOfDistinct = (int) Math.pow(10, 5.02165);
//                    break;
//                case 6:
//                    numOfDistinct = (int) Math.pow(10, 6.50852);
//                    break;
//                default:
//                    numOfDistinct = (int) Math.pow(10, i);
//                    break;
//            }
//            ArrayList<Long> elements1 = new ArrayList<>();
//            for (int j = 0; j < numOfDistinct; j++) {
//                elements1.add((long) StdRandom.uniform(0, universe1));
//            }
//            Collections.shuffle(elements1);
//            double[] resBjkst4 = memMeasure(bjkst1, elements1);
//            double[] resHyper4 = memMeasure(hlog1, elements1);
//            double[] resBsl4 = memMeasure(bsl1, elements1);
//            distinctCount.add((long) resBsl4[1]);
//            memBJKST.add(resBjkst4[0]);
//            memHyper.add(resHyper4[0]);
//            memBaseline.add(resBsl4[0]);
//        }
//
//        distinctCount.stream().forEach(e -> System.out.print(e + ", "));
//        System.out.println();
//        memBaseline.stream().forEach(e -> System.out.print(e + ", "));
//        System.out.println();
//        memHyper.stream().forEach(e -> System.out.print(e + ", "));
//        System.out.println();
//        memBJKST.stream().forEach(e -> System.out.print(e + ", "));
//
//
//        System.out.println("-------Comparison between Hyperloglog and Baseline-----");
//        distinctCount = new ArrayList<>();
//        memBaseline = new ArrayList<>();
//        memBJKST = new ArrayList<>();
//        memHyper = new ArrayList<>();
//        long universe2 = (long)Math.pow(2,32);
//        Hyperloglog hlog2 = new Hyperloglog(16);
//        Baseline bsl2 = new Baseline();
//        for (int i = 1; i < 8; i++) {
//            int length2 = (int) Math.pow(10, i);       //Assuming that cardinality == length of stream
//            ArrayList<Long> elements2 = new ArrayList<>();
//            for (int j = 0; j < length2; j++) {
//                elements2.add((long) StdRandom.uniform(0, universe2));
//            }
//            double[] resHlog = memMeasure(hlog2, elements2);
//            double[] resBsl = memMeasure(bsl2, elements2);
//            distinctCount.add((long) resBsl[1]);
//            memHyper.add(resHlog[0]);
//            memBaseline.add(resBsl[0]);
//        }
//        distinctCount.stream().forEach(e -> System.out.print(e + ", "));
//        System.out.println();
//        memBaseline.stream().forEach(e -> System.out.print(e + ", "));
//        System.out.println();
//        memHyper.stream().forEach(e -> System.out.print(e + ", "));
//        System.out.println();
//        memBJKST.stream().forEach(e -> System.out.print(e + ", "));
//
//    }
//
    public static void Question8(){
        StdRandom.setSeed(0);
        int universe = (int)Math.pow(2,20);  // less than p^(1/3), use this universe in comparison when involving BJKST.
        Baseline bsl = new Baseline();
        ArrayList<Double> timeBJKST = new ArrayList<>();
        ArrayList<Double> timeHyper = new ArrayList<>();
        ArrayList<Double> accBJKST = new ArrayList<>();
        ArrayList<Double> accHyper = new ArrayList<>();
        for (int i = 10; i < 17; i++) {
            Hyperloglog hlog = new Hyperloglog(universe,i);
            int numOfDistinct = (int) Math.pow(10, 3);
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
            long distinct = bsl.compute(elements);
            double[] metricHyper = timeMeasure(hlog, elements);
            timeHyper.add(metricHyper[0] / elements.size() * 1000);
            accHyper.add((metricHyper[1] - distinct) * 1.0 / distinct);
        }
//        for (int i = 0; i < 16; i++) {
//            BJKST bjkst = new BJKST(universe, 0.2 - 0.01 * i);
//            int numOfDistinct = (int) Math.pow(10, 5.02165);
//
//            ArrayList<Long> elements = new ArrayList<>();
//            for (int j = 0; j < numOfDistinct ; j++) {
//                elements.add((long) StdRandom.uniform(0,universe));
//            }
//            long distinct = bsl.compute(elements);
//            double[] metricBJKST = timeMeasure(bjkst, elements);
//            timeBJKST.add(metricBJKST[0] / elements.size() * 1000);
//            accBJKST.add((metricBJKST[1] - distinct) * 1.0 / distinct);
//        }
        timeHyper.stream().forEach(e -> System.out.print(e + ", "));
        System.out.println();
        accHyper.stream().forEach(e -> System.out.print(e + ", "));
        System.out.println();
        timeBJKST.stream().forEach(e -> System.out.print(e + ", "));
        System.out.println();
        accBJKST.stream().forEach(e -> System.out.print(e + ", "));
    }







    public static double[] memMeasure (DistinctCount dc, ArrayList<Long> elements) {

        double avgMem = 0;
        double avgDist = 0;
        double[] res = new double[2];
        for (int i = 0; i < 3; i++) {
            System.gc();
            long startMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
            long distinct = dc.compute(elements);
            long endMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
            avgMem += (endMem - startMem ) / 1024.0;
            avgDist += distinct;
        }
        res[0] = avgMem / 3;
        res[1] = avgDist / 3;
        return res;
    }

    public static double[] timeMeasure(DistinctCount dc, ArrayList<Long> elements){
        double[] res = new double[2];
        double avgTime = 0;
        long avgDistinct = 0;
        for (int i = 0; i < 3; i++) {
            long start = System.currentTimeMillis();
            long distinct = dc.compute(elements);
            long end = System.currentTimeMillis();
            avgDistinct += distinct;
            avgTime += end - start;
        }
        res[0] = avgTime / 3;
        res[1] = avgDistinct / 3;
        return res;

    }

    public static long medianTrick(DistinctCount dc, ArrayList<Long> elements, double delta) {
        int d = (int)(36 * Math.log(1 / delta));
        long[] distincts = new long[d];
        for (int i = 0; i < d; i++) {
            distincts[i] = dc.compute(elements);
        }
        Arrays.sort(distincts);
        return (distincts.length % 2 != 0) ? distincts[distincts.length / 2] : (distincts[distincts.length /2 ] + distincts[distincts.length / 2 - 1]) / 2 ;
    }
}


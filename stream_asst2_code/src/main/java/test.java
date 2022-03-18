import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Zhihao Huang
 * @StudentID: 1052452
 * @Date: 2020/10/18
 */
public class test {
    static final int universe = 2147483646;                // 2^31-1
    static final int lenOfStream = 1000000;               // 1e6

    public static void main(String[] args) {
//        ZipfDistribution();
//        ZipfTheoryHeavyHitters();
//        precision_runtimeANDz();
        mem_time_ANDs();
//        AREandSupport();
    }


    public static void ZipfDistribution() {
        double[] exponents = {1.1, 1.4, 1.7, 2.0};
        for (double exponent : exponents) {
            ZipfStream zipf = new ZipfStream(1000, exponent, 100000);          //len of stream is set to 100
            ArrayList<Integer> zipfStream = zipf.getZipStream();
            HashMap<Integer, Integer> zipfMap = new HashMap<>();
            zipfStream.forEach(k -> {
                if (zipfMap.containsKey(k)) {
                    zipfMap.replace(k, zipfMap.get(k) + 1);
                } else {
                    zipfMap.put(k, 1);
                }
            });
            String fileName = "z=" + exponent + ".csv";
            try {
                writeToCSV(zipfMap, fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void ZipfTheoryHeavyHitters() {
        double threshold = 0.01;
        double sum;
        int cnt;
        double normFreq;
        double[] exponents = {1.1, 1.4, 1.7, 2.0};
        for (double exponent : exponents) {
            sum = 0;
            cnt = 0;
            for (int i = 0; i < 1000; i++) {
                sum += Math.pow(1.0 / (i + 1), exponent);
            }
            do {
                normFreq = Math.pow(1.0 / (cnt + 1), exponent) / sum;
                cnt++;
            } while (normFreq > threshold);
            System.out.println("z: " + exponent + ", frequent items in theoretical: " + cnt);
        }
    }

    public static void precision_runtimeANDz() {
        double[] exponents = {1.1, 1.4, 1.7, 2.0};
        double s = 0.001;

        StickySampling stickySampling = new StickySampling(s, 0.05);
        LossyCounting lossyCounting = new LossyCounting(s);
        SpaceSaving spaceSaving = new SpaceSaving(s);

        ArrayList<Double> stickyPrecs = new ArrayList<>();
        ArrayList<Double> lossyPrecs = new ArrayList<>();
        ArrayList<Double> spacePrecs = new ArrayList<>();
        ArrayList<Double> stickyTimes = new ArrayList<>();
        ArrayList<Double> lossyTimes = new ArrayList<>();
        ArrayList<Double> spaceTimes = new ArrayList<>();


        for (double exponent : exponents) {

            // Get real heavy hitters
            ZipfStream zipf = new ZipfStream(universe, exponent, lenOfStream);
            ArrayList<Integer> zipfStream = zipf.getZipStream();
            HashMap<Integer, Integer> zipfMap = new HashMap<>();
            ArrayList<Object> heavyHitters = new ArrayList<>();
            zipfStream.forEach(k -> {
                if (zipfMap.containsKey(k)) {
                    zipfMap.replace(k, zipfMap.get(k) + 1);
                } else {
                    zipfMap.put(k, 1);
                }
            });
            zipfMap.forEach((k, v) -> {
                if (v > s * lenOfStream) heavyHitters.add(k);
            });

            long startTime, endTime;

            // 1. StickySampling
            startTime = System.currentTimeMillis();
            for (int j = 0; j < zipfStream.size(); j++) {
                stickySampling.add(zipfStream.get(j));
            }
            endTime = System.currentTimeMillis();
            double updateTime1 = (endTime - startTime) * 1.0 / lenOfStream * 1000;            //microsecond
            ArrayList<Object> output1 = stickySampling.output();
            double precision1 = output1.stream().filter(heavyHitters::contains).count() * 1.0 / output1.size();

            // 2.LossyCouting
            startTime = System.currentTimeMillis();
            for (int j = 0; j < zipfStream.size(); j++) {
                lossyCounting.add(zipfStream.get(j));
            }
            endTime = System.currentTimeMillis();
            double updateTime2 = (endTime - startTime) * 1.0 / lenOfStream * 1000;            //microsecond
            ArrayList<Object> output2 = stickySampling.output();
            double precision2 = output2.stream().filter(heavyHitters::contains).count() * 1.0 / output2.size();

            // 3.SpaceSaving
            startTime = System.currentTimeMillis();
            for (int j = 0; j < zipfStream.size(); j++) {
                spaceSaving.add(zipfStream.get(j));
            }
            endTime = System.currentTimeMillis();
            double updateTime3 = (endTime - startTime) * 1.0 / lenOfStream * 1000;            //microsecond
            ArrayList<Object> output3 = stickySampling.output();
            double precision3 = output3.stream().filter(heavyHitters::contains).count() * 1.0 / output3.size();

            // Output
            stickyTimes.add(updateTime1);
            lossyTimes.add(updateTime2);
            spaceTimes.add(updateTime3);
            stickyPrecs.add(precision1);
            lossyPrecs.add(precision2);
            spacePrecs.add(precision3);

        }

        //make it persistent
        try {
            FileWriter fw = new FileWriter("Time_and_Precision_over_Different_z.txt");
            fw.write("z = [1.1, 1.4, .17, 2.0]\n");
            fw.write("StickySampling Precision: ");
            stickyPrecs.forEach(t -> {
                try {
                    fw.write(t + ", ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fw.write("\n");

            fw.write("StickySampling UpdateTime: ");
            stickyTimes.forEach(t -> {
                try {
                    fw.write(t + ", ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fw.write("\n");

            fw.write("LossyCounting Precision: ");
            lossyPrecs.forEach(t -> {
                try {
                    fw.write(t + ", ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fw.write("\n");

            fw.write("LossyCounting UpdateTime: ");
            lossyTimes.forEach(t -> {
                try {
                    fw.write(t + ", ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fw.write("\n");

            fw.write("SpaceSaving Precision: ");
            spacePrecs.forEach(t -> {
                try {
                    fw.write(t + ", ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fw.write("\n");

            fw.write("SpaceSaving UpdateTime: ");
            spaceTimes.forEach(t -> {
                try {
                    fw.write(t + ", ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fw.write("\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void mem_time_ANDs() {
        //maximum number of tracked items vs support
        //runtime vs support
        //(minimum s at least 10^−4)
        //the maximum number of tracked items vs runtime
        double exponent = 1.1;
        double[] support = {0.001, 0.002, 0.003, 0.004, 0.005, 0.006, 0.007, 0.008, 0.009,
                0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09, 0.1};
        ArrayList<Integer> stickyTrackeds = new ArrayList<>();
        ArrayList<Integer> lossyTrackeds = new ArrayList<>();
        ArrayList<Integer> spaceTrackeds = new ArrayList<>();
        ArrayList<Double> stickyTimes = new ArrayList<>();
        ArrayList<Double> lossyTimes = new ArrayList<>();
        ArrayList<Double> spaceTimes = new ArrayList<>();


        for (double s : support) {

            StickySampling stickySampling = new StickySampling(s, 0.05);
            LossyCounting lossyCounting = new LossyCounting(s);
            SpaceSaving spaceSaving = new SpaceSaving(s);

            ZipfStream zipf = new ZipfStream(universe, exponent, lenOfStream);
            ArrayList<Integer> zipfStream = zipf.getZipStream();

            long startTime, endTime;
            int tracked1 = 0, tracked2 = 0, tracked3 = 0;

            // 1. StickySampling
            startTime = System.currentTimeMillis();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < zipfStream.size(); j++) {
                    stickySampling.add(zipfStream.get(j));
                }
                tracked1 += stickySampling.getTracked().size();
            }
            endTime = System.currentTimeMillis();
            double updateTime1 = (endTime - startTime) * 1.0 / lenOfStream * 1000 / 3;            //microsecond
            tracked1 /= 3;

            // 2.LossyCouting
            startTime = System.currentTimeMillis();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < zipfStream.size(); j++) {
                    lossyCounting.add(zipfStream.get(j));
                }
                tracked2 += lossyCounting.getTracked().size();
            }
            endTime = System.currentTimeMillis();
            double updateTime2 = (endTime - startTime) * 1.0 / lenOfStream * 1000 / 3;            //microsecond
            tracked2 /= 3;


            // 3.SpaceSaving
            startTime = System.currentTimeMillis();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < zipfStream.size(); j++) {
                    spaceSaving.add(zipfStream.get(j));
                }
                tracked3 += spaceSaving.getTracked().size();
            }
            endTime = System.currentTimeMillis();
            double updateTime3 = (endTime - startTime) * 1.0 / lenOfStream * 1000 / 3;            //microsecond
            tracked3 /= 3;

            //Add to list
            stickyTrackeds.add(tracked1);
            lossyTrackeds.add(tracked2);
            spaceTrackeds.add(tracked3);
            stickyTimes.add(updateTime1);
            lossyTimes.add(updateTime2);
            spaceTimes.add(updateTime3);
        }
        //make it persistent
        try {
            FileWriter fw = new FileWriter("Time_and_Mem_over_Support.txt");
            fw.write("s from 1e-3 to 1e-1\n");
            fw.write("StickySampling Num of Tracked: ");
            stickyTrackeds.forEach(t -> {
                try {
                    fw.write(t + ", ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fw.write("\n");

            fw.write("StickySampling UpdateTime: ");
            stickyTimes.forEach(t -> {
                try {
                    fw.write(t + ", ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fw.write("\n");

            fw.write("LossyCounting Num of Tracked: ");
            lossyTrackeds.forEach(t -> {
                try {
                    fw.write(t + ", ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fw.write("\n");

            fw.write("LossyCounting UpdateTime: ");
            lossyTimes.forEach(t -> {
                try {
                    fw.write(t + ", ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fw.write("\n");

            fw.write("SpaceSaving Num of Tracked: ");
            spaceTrackeds.forEach(t -> {
                try {
                    fw.write(t + ", ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fw.write("\n");

            fw.write("SpaceSaving UpdateTime: ");
            spaceTimes.forEach(t -> {
                try {
                    fw.write(t + ", ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fw.write("\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void AREandSupport() {
        double exponent = 1.1;
        double[] support = {0.001, 0.002, 0.003, 0.004, 0.005, 0.006, 0.007, 0.008, 0.009,
                0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09, 0.1};

        //calculate the ARE of true heavy hitters in outputs of 3 algos
        ArrayList<Double> stickyAREs = new ArrayList<>();
        ArrayList<Double> lossyAREs = new ArrayList<>();
        ArrayList<Double> spaceAREs = new ArrayList<>();

        for (double s : support) {

            StickySampling stickySampling = new StickySampling(s, 0.05);
            LossyCounting lossyCounting = new LossyCounting(s);
            SpaceSaving spaceSaving = new SpaceSaving(s);

            ZipfStream zipf = new ZipfStream(universe, exponent, lenOfStream);
            ArrayList<Integer> zipfStream = zipf.getZipStream();
            HashMap<Integer, Integer> zipfMap = new HashMap<>();
            ArrayList<Object> heavyHitters = new ArrayList<>();
            zipfStream.forEach(k -> {
                if (zipfMap.containsKey(k)) {
                    zipfMap.replace(k, zipfMap.get(k) + 1);
                } else {
                    zipfMap.put(k, 1);
                }
            });
            //true heavy hitters
            zipfMap.forEach((k, v) -> {
                if (v > s * lenOfStream) heavyHitters.add(k);
            });
            int cnt;
            double res;
            // 1. StickySampling
            for (int j = 0; j < zipfStream.size(); j++) {
                stickySampling.add(zipfStream.get(j));
            }
            ArrayList<Object> output1 = stickySampling.output();
            HashMap<Object, Integer> tracked1 = stickySampling.getTracked();
            cnt = 0;
            res = 0;
            for (Object k : heavyHitters) {
                if (output1.contains(k)) {
                    cnt++;
                    res += Math.abs(tracked1.get(k) - zipfMap.get(k)) * 1.0 / zipfMap.get(k);
                }
            }
//            for (Object k : output1){
//                //false positive
//                if(!heavyHitters.contains(k))   {
//                    cnt++;
//                    res += Math.abs(tracked1.get(k) - zipfMap.get(k)) * 1.0 / zipfMap.get(k);
//                }
//            }
            double are1 = res / cnt;


            // 2.LossyCouting
            for (int j = 0; j < zipfStream.size(); j++) {
                lossyCounting.add(zipfStream.get(j));
            }
            ArrayList<Object> output2 = lossyCounting.output();
            HashMap<Object, Integer> tracked2 = lossyCounting.getTracked();
            cnt = 0;
            res = 0;
            for (Object k : heavyHitters) {
                if (output2.contains(k)) {
                    cnt++;
                    res += Math.abs(tracked2.get(k) - zipfMap.get(k)) * 1.0 / zipfMap.get(k);
                }
            }
//            for (Object k : output2){
//                //false positive
//                if(!heavyHitters.contains(k))   {
//                    cnt++;
//                    res += Math.abs(tracked2.get(k) - zipfMap.get(k)) * 1.0 / zipfMap.get(k);
//                }
//            }
            double are2 = res / cnt;

            // 3.SpaceSaving
            for (int j = 0; j < zipfStream.size(); j++) {
                spaceSaving.add(zipfStream.get(j));
            }
            ArrayList<Object> output3 = spaceSaving.output();
            Map<Object, Integer> tracked3 = spaceSaving.getTracked();
            cnt = 0;
            res = 0;
            for (Object k : heavyHitters) {
                if (output3.contains(k)) {
                    cnt++;
                    res += Math.abs(tracked3.get(k) - zipfMap.get(k)) * 1.0 / zipfMap.get(k);
                }
            }
//            for (Object k : output3){
//                //false positive
//                if(!heavyHitters.contains(k))   {
//                    cnt++;
//                    res += Math.abs(tracked3.get(k) - zipfMap.get(k)) * 1.0 / zipfMap.get(k);
//                }
//            }
            double are3 = res / cnt;

            // saving
            stickyAREs.add(are1);
            lossyAREs.add(are2);
            spaceAREs.add(are3);
        }

        //make it persistent
        try {
            FileWriter fw = new FileWriter("Average_Relative_Error_vs_Support.txt");
//            FileWriter fw = new FileWriter("FP_Average_Relative_Error_vs_Support.txt");
            fw.write("s from 1e-3 to 1e-1\n");

//            fw.write("This is for ARE of False Positive");

            fw.write("StickySampling ARE ");
            stickyAREs.forEach(t -> {
                try {
                    fw.write(t + ", ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fw.write("\n");

            fw.write("LossyCounting ARE ");
            lossyAREs.forEach(t -> {
                try {
                    fw.write(t + ", ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            fw.write("\n");

            fw.write("SpaceSaving ARE ");
            spaceAREs.forEach(t -> {
                try {
                    fw.write(t + ", ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fw.write("\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void writeToCSV(HashMap<Integer, Integer> hashMap, String fileName) throws IOException {
        FileWriter fw = new FileWriter(fileName);
        fw.write("K,V\n");
        fw.flush();
        hashMap.forEach((k, v) -> {
            try {
                synchronized (k) {
                    fw.write(k + "," + v + "\n");
                    fw.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        fw.close();


    }


}

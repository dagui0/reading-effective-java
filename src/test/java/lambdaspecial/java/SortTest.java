package lambdaspecial.java;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SortTest {

    @Test
    public void testQuickSortLambda() {

        QuickSortLambda<Integer> sort = new QuickSortLambda<>();

        List<Integer> original = List.of(3, 6, 8, 10, 1, 2, 1);
        List<Integer> sorted = List.of(1, 1, 2, 3, 6, 8, 10);

        assertEquals(sorted, sort.sort(original));
    }

    @Test
    public void testQuickSortProcedural() {

        QuickSortProcedural<Integer> sort = new QuickSortProcedural<>();

        List<Integer> original = List.of(3, 6, 8, 10, 1, 2, 1);
        List<Integer> sorted = List.of(1, 1, 2, 3, 6, 8, 10);

        assertEquals(sorted, sort.sort(original));
    }


    @Test
    public void testQuickSortArray() {

        QuickSortProcedural<Integer> sort = new QuickSortProcedural<>();

        int[] original = {3, 6, 8, 10, 1, 2, 1};
        int[] sorted = {1, 1, 2, 3, 6, 8, 10};

        sort.sort(original);
        assertArrayEquals(sorted, original);
    }

    private int[] copyArray(int[] array) {
        int[] copy = new int[array.length];
        System.arraycopy(array, 0, copy, 0, array.length);
        return copy;
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    static class Report {
        private final int sample;
        private final int[] data;
        private long basis;
        private final Map<String, Long> times = new TreeMap<>();

        public int[] getDataArray() {
            int[] copy = new int[data.length];
            System.arraycopy(data, 0, copy, 0, data.length);
            return copy;
        }

        public List<Integer> getDataList() {
            List<Integer> list = new ArrayList<>(data.length);
            for (int i : data) {
                list.add(i);
            }
            return list;
        }

        public void setTime(String method, Runnable func) {
            setTime(method, func, false);
        }

        public void setTime(String method, Runnable func, boolean isBasis) {
            long startTime = System.nanoTime();
            func.run();
            long endTime = System.nanoTime();
            long time = endTime - startTime;
            times.put(method, time);
            if (isBasis) {
                basis = time;
            }
        }
    }

    @Test
    public void testPerfomances() {

        final int MAXVALUE = 1000000;
        Random r = new Random();
        int[] samples = { 3, 10, 100, 1000, 10000, 100000, 1000000 };
        Map<Integer, Report> reports = new TreeMap<>();

        QuickSortProcedural<Integer> javaProcedural = new QuickSortProcedural<>();
        QuickSortLambda<Integer> javaLambda = new QuickSortLambda<>();
        lambdaspecial.kotlin.QuickSortProcedural<Integer> kotlinProcedural = new lambdaspecial.kotlin.QuickSortProcedural<>();
        lambdaspecial.kotlin.QuickSortLambda<Integer> kotlinLambda = new lambdaspecial.kotlin.QuickSortLambda<>();

        for (int sample: samples)    {
            int[] array = new int[sample];
            for (int i = 0; i < sample; i++) {
                int rn = r.nextInt(MAXVALUE);
                array[i] = rn;
            }

            Report report = new Report(sample, array);
            reports.put(sample, report);

            // java array
            int[] copiedArray = report.getDataArray();
            report.setTime("java | array", () -> {
                javaProcedural.sort(copiedArray);
            }, true);

            // java list
            List<Integer> copiedList1 = report.getDataList();
            report.setTime("java | list", () -> {
                javaProcedural.sort(copiedList1);
            });

            // java lambda
            List<Integer>copiedList2 = report.getDataList();
            report.setTime("java | lambda", () -> {
                javaLambda.sort(copiedList2);
            });

            // kotlin array
            int[] copiedArray_1 = report.getDataArray();
            report.setTime("kotlin | array", () -> {
                kotlinProcedural.sort(copiedArray_1);
            });

            // kotlin list
            List<Integer> copiedList1_1 = report.getDataList();
            report.setTime("kotlin | list", () -> {
                kotlinProcedural.sort(copiedList1_1);
            });

            // kotlin lambda
            List<Integer>copiedList2_1 = report.getDataList();
            report.setTime("kotlin | lambda", () -> {
                kotlinLambda.sort(copiedList2_1);
            });
        }

        System.out.println("| lang | method | 3 | 10 | 100 | 1k | 10k | 100k | 1M |");
        System.out.println("|------|--------|---|----|-----|----|-----|------|----|");
        List.of("java | array", "java | list", "java | lambda", "kotlin | array", "kotlin | list", "kotlin | lambda")
                .forEach(method -> {
                            System.out.printf("| %s |", method);
                            reports.forEach((sample, report) -> {
                                long basis = report.getBasis();
                                double time = report.getTimes().get(method);
                                System.out.printf(" %.1f |", (time / basis));
                            });
                            System.out.println();
                        }
                );
        System.out.println();

        System.out.println("| lang | method | 3 | 10 | 100 | 1k | 10k | 100k | 1M |");
        System.out.println("|------|--------|---|----|-----|----|-----|------|----|");
        List.of("java | array", "java | list", "java | lambda", "kotlin | array", "kotlin | list", "kotlin | lambda")
                .forEach(method -> {
                        System.out.printf("| %s |", method);
                        reports.forEach((sample, report) -> {
                            double time = report.getTimes().get(method);
                            System.out.printf(" %.3f |", (time / 1_000_000.0));
                        });
                        System.out.println();
                }
        );
        System.out.println();
    }
}

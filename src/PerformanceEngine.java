import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PerformanceEngine {

    public Map<String, Long> evaluate(double[] originalData) {
        Map<String, Long> results = new LinkedHashMap<>();
        List<Sorter> algorithms = new ArrayList<>();

        algorithms.add(new InsertionSort());
        algorithms.add(new ShellSort());
        algorithms.add(new MergeSort());
        algorithms.add(new QuickSort());
        algorithms.add(new HeapSort());

        for (Sorter sorter : algorithms) {
            double[] dataCopy = Arrays.copyOf(originalData, originalData.length);

            long startTime = System.nanoTime();
            sorter.sort(dataCopy);
            long endTime = System.nanoTime();
            long duration = endTime - startTime;

            results.put(sorter.getName(), duration);
        }

        return results;
    }
}
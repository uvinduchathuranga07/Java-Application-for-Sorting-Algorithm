import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PerformanceEngine {

    public String evaluate(double[] originalData) {
        StringBuilder sb = new StringBuilder();
        List<Sorter> algorithms = new ArrayList<>();


        algorithms.add(new InsertionSort());
        algorithms.add(new ShellSort());
        algorithms.add(new MergeSort());
        algorithms.add(new QuickSort());
        algorithms.add(new HeapSort());

        long bestTime = Long.MAX_VALUE;
        String bestAlgo = "";

        sb.append(String.format("%-20s | %-20s\n", "Algorithm", "Time (ns)"));
        sb.append("---------------------------------------------\n");

        for (Sorter sorter : algorithms) {
            double[] dataCopy = Arrays.copyOf(originalData, originalData.length);

            long startTime = System.nanoTime();
            sorter.sort(dataCopy);
            long endTime = System.nanoTime();
            long duration = endTime - startTime;

            sb.append(String.format("%-20s | %-20d\n", sorter.getName(), duration));

            if (duration < bestTime) {
                bestTime = duration;
                bestAlgo = sorter.getName();
            }
        }

        sb.append("\n---------------------------------------------\n");
        sb.append("Best Performance: " + bestAlgo + "\n");

        return sb.toString();
    }
}
public class InsertionSort implements Sorter {
    @Override
    public String getName() {
        return "Insertion Sort";
    }

    @Override
    public void sort(double[] array) {
        int n = array.length;
        for (int i = 1; i < n; ++i) {
            double key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j = j - 1;
            }
            array[j + 1] = key;
        }
    }
}
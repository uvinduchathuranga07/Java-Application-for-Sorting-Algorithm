public class MergeSort implements Sorter {
    @Override
    public String getName() { return "Merge Sort"; }

    @Override
    public void sort(double[] array) {
        if (array.length < 2) return;
        int mid = array.length / 2;
        double[] left = new double[mid];
        double[] right = new double[array.length - mid];

        System.arraycopy(array, 0, left, 0, mid);
        System.arraycopy(array, mid, right, 0, array.length - mid);

        sort(left);
        sort(right);
        merge(array, left, right);
    }

    private void merge(double[] array, double[] left, double[] right) {
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) array[k++] = left[i++];
            else array[k++] = right[j++];
        }
        while (i < left.length) array[k++] = left[i++];
        while (j < right.length) array[k++] = right[j++];
    }
}
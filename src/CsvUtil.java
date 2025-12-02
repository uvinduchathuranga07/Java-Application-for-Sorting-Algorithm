import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {
    
    // Reads the first line to get headers (Column names)
    public static String[] getHeaders(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line != null) {
                return line.split(",");
            }
        }
        return new String[0];
    }

    // Reads a specific column index and converts to double array
    public static double[] getColumnData(File file, int columnIndex) throws IOException, NumberFormatException {
        List<Double> dataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (columnIndex < values.length) {
                    // Clean whitespace and parse
                    String val = values[columnIndex].trim();
                    if (!val.isEmpty()) {
                        dataList.add(Double.parseDouble(val));
                    }
                }
            }
        }
        // Convert List to primitive array
        return dataList.stream().mapToDouble(d -> d).toArray();
    }
}
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Arrays;

public class SortingAppGUI extends JFrame {
    private JButton uploadBtn, sortBtn;
    private JComboBox<String> columnSelector;
    private JTextArea resultArea;
    private JLabel statusLabel;
    private File currentFile;

    public SortingAppGUI() {
        setTitle("Sorting Algorithm Performance Evaluation");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel: Upload and Select
        JPanel topPanel = new JPanel();
        uploadBtn = new JButton("Upload CSV");
        columnSelector = new JComboBox<>();
        sortBtn = new JButton("Run Performance Eval");
        sortBtn.setEnabled(false);

        topPanel.add(uploadBtn);
        topPanel.add(new JLabel("Select Column:"));
        topPanel.add(columnSelector);
        topPanel.add(sortBtn);

        // Center: Results
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Bottom: Status
        statusLabel = new JLabel("Status: Waiting for file...");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(statusLabel);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Event Listeners
        uploadBtn.addActionListener(e -> handleFileUpload());
        sortBtn.addActionListener(e -> runSorting());
    }

    private void handleFileUpload() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try {
                // Member 1 logic: File Handling
                String[] headers = CsvUtil.getHeaders(currentFile);
                columnSelector.removeAllItems();
                for (String header : headers) {
                    columnSelector.addItem(header);
                }
                statusLabel.setText("File loaded: " + currentFile.getName());
                sortBtn.setEnabled(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage());
            }
        }
    }

    private void runSorting() {
        try {
            int colIndex = columnSelector.getSelectedIndex();
            if (colIndex == -1) return;

            // Load data using CsvUtil
            double[] rawData = CsvUtil.getColumnData(currentFile, colIndex);
            
            resultArea.setText("Evaluating Algorithms on " + rawData.length + " records...\n\n");

            // CALL MEMBER 2's LOGIC HERE
            // We pass the data to the Performance Engine
            PerformanceEngine engine = new PerformanceEngine();
            String report = engine.evaluate(rawData);
            
            resultArea.append(report);
            statusLabel.setText("Status: Evaluation Complete.");

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Selected column must be Numeric!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SortingAppGUI().setVisible(true));
    }
}
import helpers.BarChartPanel;
import helpers.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.Map;

public class SortingAppGUI extends JFrame {
    private UIUtils.ModernButton uploadBtn, sortBtn, viewTableBtn, viewGraphBtn;
    private JComboBox<String> columnSelector;
    private JTextArea resultArea;
    private JLabel statusLabel;
    private File currentFile;

    private BarChartPanel chartPanel;
    private JPanel cardContainer;
    private CardLayout cardLayout;

    private UIUtils.RoundedPanel bestAlgoCard;
    private JLabel bestAlgoNameLabel;
    private JLabel bestAlgoTimeLabel;
    private JPanel contentWrapper;

    public SortingAppGUI() {
        setTitle("Algorithm Performance Analyzer");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIUtils.BG_COLOR);
        setContentPane(mainPanel);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIUtils.BG_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 30, 10, 30));

        JLabel titleLabel = new JLabel("Sorting Evaluator");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(UIUtils.TEXT_DARK);


        JPanel titleBlock = new JPanel(new GridLayout(2, 1));
        titleBlock.setOpaque(false);
        titleBlock.add(titleLabel);

        headerPanel.add(titleBlock, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        UIUtils.RoundedPanel controlPanel = new UIUtils.RoundedPanel();
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 12));
        controlPanel.setBorder(new EmptyBorder(5, 15, 5, 15));

        uploadBtn = new UIUtils.ModernButton("Upload File", UIUtils.PRIMARY_COLOR);

        columnSelector = new JComboBox<>();
        columnSelector.setPreferredSize(new Dimension(180, 35));
        columnSelector.setBackground(Color.WHITE);
        columnSelector.setFont(UIUtils.MAIN_FONT);

        sortBtn = new UIUtils.ModernButton("Run Analysis", UIUtils.ACCENT_COLOR);
        sortBtn.setEnabled(false);

        controlPanel.add(uploadBtn);
        controlPanel.add(new JLabel("Target Column:"));
        controlPanel.add(columnSelector);
        controlPanel.add(sortBtn);


        UIUtils.RoundedPanel switchCardPanel = new UIUtils.RoundedPanel();
        switchCardPanel.setBackground(Color.WHITE);
        switchCardPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 12));
        switchCardPanel.setBorder(new EmptyBorder(5, 10, 5, 10));


        viewTableBtn = new UIUtils.ModernButton("Data", Color.GRAY);
        viewGraphBtn = new UIUtils.ModernButton("Graph", Color.GRAY);

        switchCardPanel.add(viewTableBtn);
        switchCardPanel.add(viewGraphBtn);

        switchCardPanel.setMaximumSize(new Dimension(250, 55));
        switchCardPanel.setPreferredSize(new Dimension(250, 55));

        JPanel topContainer = new JPanel(new BorderLayout(15,0));
        topContainer.setOpaque(false);
        topContainer.setBorder(new EmptyBorder(0, 0, 0, 0));
        topContainer.add(controlPanel, BorderLayout.CENTER);
        topContainer.add(switchCardPanel, BorderLayout.EAST);


        topContainer.setMaximumSize(new Dimension(1200, 75));
        topContainer.setPreferredSize(new Dimension(1000, 75));

        bestAlgoCard = createBestPerformerCard();
        bestAlgoCard.setVisible(false);

        cardLayout = new CardLayout();
        cardContainer = new UIUtils.RoundedPanel();
        cardContainer.setLayout(cardLayout);
        cardContainer.setBackground(Color.WHITE);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        resultArea.setBorder(new EmptyBorder(15,15,15,15));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(null);

        chartPanel = new BarChartPanel(null);

        cardContainer.add(scrollPane, "TABLE");
        cardContainer.add(chartPanel, "GRAPH");

        contentWrapper = new JPanel();
        contentWrapper.setOpaque(false);
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setBorder(new EmptyBorder(0, 30, 20, 30));

        contentWrapper.add(topContainer);
        contentWrapper.add(Box.createRigidArea(new Dimension(0, 15)));
        contentWrapper.add(bestAlgoCard);

        contentWrapper.add(Box.createRigidArea(new Dimension(0, 15)));
        contentWrapper.add(cardContainer);

        add(contentWrapper, BorderLayout.CENTER);

        statusLabel = new JLabel("Status: Waiting for input...");
        statusLabel.setBorder(new EmptyBorder(5, 30, 10, 30));
        statusLabel.setForeground(Color.GRAY);
        add(statusLabel, BorderLayout.SOUTH);

        setupListeners();
    }

    private UIUtils.RoundedPanel createBestPerformerCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout(0, 5));
        card.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("  Best Performer");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(UIUtils.TEXT_DARK);
        card.add(title, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));

        bestAlgoNameLabel = new JLabel("N/A");
        bestAlgoNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        bestAlgoNameLabel.setForeground(UIUtils.ACCENT_COLOR);

        bestAlgoTimeLabel = new JLabel("0 ns");
        bestAlgoTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        bestAlgoTimeLabel.setForeground(Color.GRAY);

        contentPanel.add(bestAlgoNameLabel);
        contentPanel.add(bestAlgoTimeLabel);
        card.add(contentPanel, BorderLayout.CENTER);

        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        card.setPreferredSize(new Dimension(1000, 110));

        return card;
    }

    private void setupListeners() {
        uploadBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile();
                try {
                    String[] headers = CsvUtil.getHeaders(currentFile);
                    columnSelector.removeAllItems();
                    for (String h : headers) columnSelector.addItem(h);
                    statusLabel.setText("File Loaded: " + currentFile.getName());
                    sortBtn.setEnabled(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        sortBtn.addActionListener(e -> runAnalysis());

        viewTableBtn.addActionListener(e -> {
            cardLayout.show(cardContainer, "TABLE");
            viewTableBtn.setBackground(UIUtils.PRIMARY_COLOR);
            viewGraphBtn.setBackground(Color.GRAY);
        });

        viewGraphBtn.addActionListener(e -> {
            cardLayout.show(cardContainer, "GRAPH");
            viewGraphBtn.setBackground(UIUtils.PRIMARY_COLOR);
            viewTableBtn.setBackground(Color.GRAY);
        });
    }

    private void runAnalysis() {
        try {
            int colIndex = columnSelector.getSelectedIndex();
            if (colIndex == -1) return;

            double[] data = CsvUtil.getColumnData(currentFile, colIndex);
            statusLabel.setText("Analyzing " + data.length + " rows...");

            PerformanceEngine engine = new PerformanceEngine();
            Map<String, Long> results = engine.evaluate(data);

            String bestAlgo = "";
            long bestTime = Long.MAX_VALUE;
            for (Map.Entry<String, Long> entry : results.entrySet()) {
                if (entry.getValue() < bestTime) {
                    bestTime = entry.getValue();
                    bestAlgo = entry.getKey();
                }
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Performance Report\n==================\n\n");
            results.forEach((k, v) -> sb.append(String.format("%-15s : %,d ns\n", k, v)));
            resultArea.setText(sb.toString());

            chartPanel.setData(results);

            bestAlgoNameLabel.setText(bestAlgo);
            bestAlgoTimeLabel.setText(String.format("in %,d ns", bestTime));

            bestAlgoCard.setVisible(true);

            contentWrapper.revalidate();
            contentWrapper.repaint();

            cardLayout.show(cardContainer, "GRAPH");
            viewGraphBtn.setBackground(UIUtils.PRIMARY_COLOR);
            viewTableBtn.setBackground(Color.GRAY);

            statusLabel.setText("Analysis Complete.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SortingAppGUI().setVisible(true));
    }
}
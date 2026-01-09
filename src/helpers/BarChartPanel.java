package helpers;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class BarChartPanel extends JPanel {
    private Map<String, Long> data;

    public BarChartPanel(Map<String, Long> data) {
        this.data = data;
        setBackground(Color.WHITE);
    }

    public void setData(Map<String, Long> data) {
        this.data = data;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data == null || data.isEmpty()) {
            drawPlaceholder(g);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = 60;
        int barWidth = (width - 2 * padding) / data.size();


        g2.setColor(new Color(240, 240, 240));
        for (int i = 0; i < 5; i++) {
            int y = padding + (i * (height - 2 * padding) / 4);
            g2.drawLine(padding, y, width - padding, y);
        }

        long maxTime = data.values().stream().max(Long::compare).orElse(1L);

        int i = 0;
        for (Map.Entry<String, Long> entry : data.entrySet()) {
            long time = entry.getValue();

            int barHeight = (int) ((double) time / maxTime * (height - 2 * padding));


            int x = padding + (i * barWidth) + 20;
            int y = height - padding - barHeight;
            int actualBarWidth = barWidth - 40;


            g2.setColor(new Color(0, 0, 0, 20));
            g2.fillRoundRect(x + 3, y + 3, actualBarWidth, barHeight, 10, 10);

            g2.setColor(new Color(66, 133, 244));
            g2.fillRoundRect(x, y, actualBarWidth, barHeight, 10, 10);

            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            String timeStr = time + " ns";
            int strWidth = g2.getFontMetrics().stringWidth(timeStr);
            g2.drawString(timeStr, x + (actualBarWidth - strWidth) / 2, y - 5);

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            String name = entry.getKey();
            String shortName = name.replace(" Sort", "");
            int nameWidth = g2.getFontMetrics().stringWidth(shortName);
            g2.drawString(shortName, x + (actualBarWidth - nameWidth) / 2, height - padding + 20);

            i++;
        }
    }

    private void drawPlaceholder(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        String msg = "Execute a sort to view the Time Complexity Graph";
        int w = g.getFontMetrics().stringWidth(msg);
        g.drawString(msg, (getWidth() - w) / 2, getHeight() / 2);
    }
}
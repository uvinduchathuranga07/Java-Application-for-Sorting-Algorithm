package helpers;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UIUtils {
    public static final Color BG_COLOR = new Color(240, 242, 245);
    public static final Color PRIMARY_COLOR = new Color(59, 130, 246);
    public static final Color ACCENT_COLOR = new Color(16, 185, 129);
    public static final Color TEXT_DARK = new Color(31, 41, 55);
    public static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);

    public static class ModernButton extends JButton {
        private Color normalColor;
        private Color hoverColor;
        private boolean hovering = false;

        public ModernButton(String text, Color color) {
            super(text);
            this.normalColor = color;
            this.hoverColor = color.brighter();

            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorder(new EmptyBorder(10, 20, 10, 20));
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setForeground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovering = true; repaint(); }
                public void mouseExited(MouseEvent e) { hovering = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(hovering ? hoverColor : normalColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            super.paintComponent(g2);
            g2.dispose();
        }
    }

    public static class RoundedPanel extends JPanel {
        private int radius = 25;

        public RoundedPanel() {
            super();
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            g2.setColor(new Color(0,0,0, 10));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
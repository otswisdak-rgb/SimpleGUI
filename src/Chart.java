

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Chart extends PageComponent {

    private String title;
    private String type;
    private String[] keys;
    private Number[] values;

    // A nice color palette for charts
    private static final Color[] COLORS = {
            new Color(110, 179, 213), new Color(237, 126, 124), new Color(157, 203, 131),
            new Color(248, 187, 110), new Color(178, 150, 203), new Color(255, 153, 204),
            new Color(153, 204, 204), new Color(221, 221, 153)
    };

    /**
     * Represents the type of chart.
     */
    public static enum ChartType {
        PIE, BAR
    }

    /**
     * @param title  The title of the chart
     * @param type   The type of chart ("PIE" or "BAR")
     * @param keys   An array of String labels
     * @param values An array of Numbers (Integers, Doubles, etc.)
     */
    public Chart(String title, String type, String[] keys, Number[] values) {
        this.title = title;
        this.type = type;
        this.keys = keys;
        this.values = values;
        // ERROR was here: setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    @Override
    public Component getAwtComponent() {
        // Return a custom JPanel that knows how to draw itself
        return new ChartPanel();
    }

    public void set(String[] keys, Number[] values) {
        this.keys = keys;
        this.values = values;
    }

    // Inner class that does the actual drawing
    private class ChartPanel extends JPanel {
        public ChartPanel() {
            setPreferredSize(new Dimension(300, 300));
            // FIX: The setBorder call belongs here, on the JPanel.
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Enable Anti-Aliasing for smooth shapes
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if ("PIE".equalsIgnoreCase(type)) {
                drawPieChart(g2d);
            } else if ("BAR".equalsIgnoreCase(type)) {
                drawBarChart(g2d);
            }
        }

        private void drawPieChart(Graphics2D g2d) {
            // Calculate total
            double total = 0;
            for (Number value : values) {
                total += value.doubleValue();
            }
            if (total == 0)
                return; // Avoid division by zero

            int width = getWidth() - 40;
            int height = getHeight() - 80; // Make more room for legend
            int diameter = Math.min(width, height);
            int x = (getWidth() - diameter) / 2;
            int y = 40; // Y offset for title

            // Draw title
            drawTitle(g2d);

            // Draw slices
            double currentAngle = 90.0; // Start at the top
            for (int i = 0; i < values.length; i++) {
                double arcAngle = (values[i].doubleValue() / total) * 360.0;
                if (arcAngle == 0)
                    continue;
                g2d.setColor(COLORS[i % COLORS.length]);
                Arc2D.Double arc = new Arc2D.Double(x, y, diameter, diameter, currentAngle, -arcAngle, Arc2D.PIE);
                g2d.fill(arc);
                currentAngle -= arcAngle;
            }

            // Draw legend
            // --- FIX ---
            // Was: drawLegend(g2d, (height + diameter) / 2 + 40);
            // Now: Use the same logic as the bar chart for consistent bottom padding.
            drawLegend(g2d, getHeight() - 20);
        }

        private void drawBarChart(Graphics2D g2d) {
            // Draw title
            drawTitle(g2d);

            int numBars = keys.length;
            if (numBars == 0)
                return;

            int titlePadding = 40;
            int bottomPadding = 40; // For labels
            int sidePadding = 40;
            int legendHeight = 40;

            int chartWidth = getWidth() - 2 * sidePadding;
            int chartHeight = getHeight() - titlePadding - bottomPadding - legendHeight;

            double maxValue = 0;
            for (Number value : values) {
                maxValue = Math.max(maxValue, value.doubleValue());
            }

            if (maxValue == 0)
                return; // Nothing to draw

            int barGap = 10;
            int barWidth = (chartWidth - (numBars - 1) * barGap) / numBars;
            int x = sidePadding;

            // Draw bars
            for (int i = 0; i < numBars; i++) {
                g2d.setColor(COLORS[i % COLORS.length]);
                int barHeight = (int) ((values[i].doubleValue() / maxValue) * chartHeight);
                int y = titlePadding + chartHeight - barHeight;
                g2d.fillRect(x, y, barWidth, barHeight);

                // Draw label
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
                FontMetrics fm = g2d.getFontMetrics();
                Rectangle2D labelBounds = fm.getStringBounds(keys[i], g2d);
                int labelX = x + (barWidth - (int) labelBounds.getWidth()) / 2;
                int labelY = titlePadding + chartHeight + 20;
                g2d.drawString(keys[i], labelX, labelY);

                x += (barWidth + barGap);
            }

            // Draw legend
            drawLegend(g2d, getHeight() - 20);
        }

        private void drawTitle(Graphics2D g2d) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("SansSerif", Font.BOLD, 18));
            FontMetrics fm = g2d.getFontMetrics();
            Rectangle2D titleBounds = fm.getStringBounds(title, g2d);
            int titleX = (getWidth() - (int) titleBounds.getWidth()) / 2;
            g2d.drawString(title, titleX, 30);
        }

        private void drawLegend(Graphics2D g2d, int y) {
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
            FontMetrics fm = g2d.getFontMetrics();
            int totalWidth = 0;
            for (String key : keys) {
                totalWidth += fm.stringWidth(key) + 40; // 15 (box) + 5 (pad) + text + 20 (gap)
            }

            int x = (getWidth() - totalWidth) / 2 + 10; // Start centered

            for (int i = 0; i < keys.length; i++) {
                g2d.setColor(COLORS[i % COLORS.length]);
                g2d.fillRect(x, y - 13, 15, 15);
                g2d.setColor(Color.BLACK);
                g2d.drawString(keys[i], x + 20, y);
                x += (fm.stringWidth(keys[i]) + 40);
            }
        }
    } // End of inner class ChartPanel
}


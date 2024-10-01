package GUI.Authentication;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class TimeSeriesChartPanel extends JPanel {

    private List<Map<String, Object>> stockHistory;
    private final int timeLabelOffset = 40;
    private final int pointDiameter = 6;
    private final int priceLabelYOffset = 16;

    public TimeSeriesChartPanel(List<Map<String, Object>> stockHistory) {
        this.stockHistory = stockHistory;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Anti-aliasing for smoother lines
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw axes with black color
        g2d.setColor(Color.BLACK);
        int axisPadding = 50;
        g2d.drawLine(axisPadding, getHeight() - axisPadding, getWidth() - axisPadding, getHeight() - axisPadding); // X-axis
        g2d.drawLine(axisPadding, getHeight() - axisPadding, axisPadding, axisPadding); // Y-axis

        // Check if we have enough data to draw the graph
        if (stockHistory.size() < 2) return;

        LocalDateTime latestTime = (LocalDateTime) stockHistory.get(stockHistory.size() - 1).get("last_update_time");

        // Green for price go up, red for price go down
        BigDecimal lastSale = (BigDecimal) stockHistory.get(stockHistory.size() - 1).get("last_sale");
        BigDecimal previousSale = (BigDecimal) stockHistory.get(stockHistory.size() - 2).get("last_sale");
        Color graphColor = lastSale.compareTo(previousSale) >= 0 ? new Color(0, 100, 0) : new Color(139,0,0); // Dark green or red

        g2d.setColor(graphColor);

        int labelInterval = Math.max(1, stockHistory.size() / 5);

        // Drawing lines and points
        for (int i = 0; i < stockHistory.size(); i++) {
            double sale = ((BigDecimal) stockHistory.get(i).get("last_sale")).doubleValue();

            // Calculate X and Y positions
            int x = axisPadding + i * (getWidth() - 2 * axisPadding) / (stockHistory.size() - 1);
            int y = getHeight() - axisPadding - (int) ((sale / getMaxSale()) * (getHeight() - 2 * axisPadding));

            // Draw lines to the next point, except for the last one
            if (i < stockHistory.size() - 1) {
                double nextSale = ((BigDecimal) stockHistory.get(i + 1).get("last_sale")).doubleValue();
                int nextX = axisPadding + (i + 1) * (getWidth() - 2 * axisPadding) / (stockHistory.size() - 1);
                int nextY = getHeight() - axisPadding - (int) ((nextSale / getMaxSale()) * (getHeight() - 2 * axisPadding));
                g2d.draw(new Line2D.Float(x, y, nextX, nextY));
            }

            // Print Price
            if (i % labelInterval == 0 || i == stockHistory.size() - 1) {
                g2d.setColor(Color.BLACK);
                g2d.drawString(String.format("%.2f", sale), x, y - priceLabelYOffset);
                g2d.setColor(graphColor);
            }

            // Draw a circle at each data point
            if (i % labelInterval == 0 || i == stockHistory.size() - 1) {
                g2d.fill(new Ellipse2D.Double(x - pointDiameter / 2.0, y - pointDiameter / 2.0, pointDiameter, pointDiameter));
            }
        }

        g2d.setColor(Color.BLACK);
        int tickLength = 5;

        // Drawing text labels
        for (int i = 0; i < stockHistory.size(); i++) {
            LocalDateTime time = (LocalDateTime) stockHistory.get(i).get("last_update_time");

            long hoursDiff = ChronoUnit.HOURS.between(time, latestTime);
            String timeLabel;

            double sale = ((BigDecimal) stockHistory.get(i).get("last_sale")).doubleValue();

            // Text position
            int x = axisPadding + i * (getWidth() - 2 * axisPadding) / (stockHistory.size() - 1);
            int y = getHeight() - axisPadding - (int) ((sale / getMaxSale()) * (getHeight() - 2 * axisPadding));

            if (i % labelInterval == 0 || i == stockHistory.size() - 1) {
                if (hoursDiff > 24) {
                    // If time is more than 24h
                    timeLabel = time.format(DateTimeFormatter.ofPattern("MM-dd"));
                } else {
                    // Show time under 24h
                    timeLabel = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                }

                int xPositionForLabel = x;
                int yPositionForAxis = getHeight() - axisPadding;
                g2d.drawLine(xPositionForLabel, yPositionForAxis, xPositionForLabel, yPositionForAxis + tickLength);

                AffineTransform orig = g2d.getTransform();
                g2d.translate(xPositionForLabel, yPositionForAxis + tickLength + timeLabelOffset);
                g2d.rotate(Math.toRadians(-45));
                g2d.drawString(timeLabel, 0, 0);
                g2d.setTransform(orig);
            }
        }

        // Axis labels
        g2d.drawString("Time", getWidth() - 40, getHeight() - 5);
        g2d.drawString("Price $", 5, 15);
    }


    private double getMaxSale() {
        return stockHistory.stream()
                .mapToDouble(map -> ((BigDecimal) map.get("last_sale")).doubleValue())
                .max()
                .orElse(1);
    }

}

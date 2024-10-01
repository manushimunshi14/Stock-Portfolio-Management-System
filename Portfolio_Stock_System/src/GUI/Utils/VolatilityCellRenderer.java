package GUI.Utils;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import javax.swing.JTable;
import java.awt.Color;
import java.awt.Font;

class VolatilityCellRenderer extends DefaultTableCellRenderer {
    private final Color darkGreen = new Color(0, 100, 0);
    private final Color darkRed = new Color(139, 0, 0);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value instanceof Number) {
            double numberValue = ((Number) value).doubleValue();
            if (numberValue < 0) {
                c.setForeground(darkRed); // negative is red
            } else {
                c.setForeground(darkGreen);  // positive is green
            }
            c.setFont(c.getFont().deriveFont(Font.BOLD));
        }
        return c;
    }
}

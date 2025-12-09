

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class Table extends PageComponent {
    private JTable table;
    private DefaultTableModel model;
    private String[] columnNames;
    private ArrayList originalObjects; // to store the original objects
    private JPanel mainPanel; // The panel that will be returned by getAwtComponent

    public Table(String[] columnNames, ArrayList objects) {
        super(); // Call PageComponent constructor
        this.columnNames = columnNames;
        this.originalObjects = objects;

        // We override isCellEditable to make the table read-only by default.
        this.model = new DefaultTableModel(this.columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // This makes all table cells non-editable
                return false;
            }
        };

        // 1. Initialize the mainPanel that will hold the table
        this.mainPanel = new JPanel(new BorderLayout());

        table = new JTable(model);

        // --- Aesthetic Improvements ---
        table.setRowHeight(24); // Make rows taller
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14)); // Make header bold
        // ---

        refresh();

        // Use a JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        // --- FIX ---
        // 2. Apply border and add the scrollPane to the mainPanel, not 'this'
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }


    /**
     * Adds a new Rowable object to both the internal list and the
     * visible table model.
     * @param rowObject The object (e.g., a Student) to add.
     */
    public void addRow(Rowable rowObject) {
        if (this.originalObjects != null) {
            this.originalObjects.add(rowObject); // Add to the data source
        }
        this.model.addRow(rowObject.getRowData()); // Add to the visible table
    }

    /**
     * Clears the visible table and repopulates it from the
     * originalObjects ArrayList. Call this if you modify the
     * ArrayList externally and need the table to update.
     */
    public void refresh() {
        // Clear the visible table
        model.setRowCount(0);

        // Repopulate from the data source
        if (this.originalObjects != null) {
            for (int i = 0; i < this.originalObjects.size(); i++) {
                Object obj = this.originalObjects.get(i);
                if (obj instanceof Rowable) {
                    Rowable rowObject = (Rowable) obj;
                    model.addRow(rowObject.getRowData());
                }
            }
        }
    }

//    public int getSelectedRowIndex() {
//        return table.getSelectedRow();
//    }
//
//    public void addRowSelectionListener(ListSelectionListener listener) {
//        table.getSelectionModel().addListSelectionListener(listener);
//    }
//
//    public int getRowCount() {
//        return model.getRowCount();
//    }
//
//    /**
//     * Returns the original Rowable objects that were added to the table.
//     * This is much more useful than getting raw data.
//     */
//    public Object[] getTableData() {
//        return originalObjects.toArray();
//    }

    @Override
    public Component getAwtComponent() {
        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
}


package View.Components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import View.Utils.*;

public class ReportsPanel extends JPanel {
    private JLabel summaryLabel;
    private JTable productsTable, inventoryTable, inventoryDetailsTable, salesTable;
    private DefaultTableModel productsModel, inventoryModel, inventoryDetailsModel, salesModel;

    public ReportsPanel() {
        setLayout(null);
        setBackground(new Color(245, 247, 250));
        JPanel summaryPanel = createSummaryPanel();
        JTabbedPane tabbedPane = createTabbedPane();
        summaryPanel.setBounds(10, 10, 1120, 60);
        tabbedPane.setBounds(10, 80, 1120, 350);
        BetterButtons export = new BetterButtons(10, 440, "export", "Export to CSV");
        export.addActionListener(e -> exportToCSV());
        add(export);
        add(summaryPanel);
        add(tabbedPane);
        
        // Auto-refresh on show
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshAllTablesAndSummary();
            }
        });
        refreshAllTablesAndSummary();
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(252, 253, 255));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        summaryLabel = new JLabel();
        summaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        summaryLabel.setForeground(new Color(40, 40, 40));
        panel.add(summaryLabel, BorderLayout.CENTER);
        return panel;
    }

    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(245, 247, 250));
        tabbedPane.addTab("Products", createTablePanel("products"));
        tabbedPane.addTab("Inventory", createTablePanel("inventory"));
        tabbedPane.addTab("Inventory Details", createTablePanel("inventoryDetails"));
        tabbedPane.addTab("Sales", createTablePanel("sales"));
        return tabbedPane;
    }

    private JPanel createTablePanel(String type) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));
        JTable table;
        DefaultTableModel model;
        switch (type) {
            case "products":
                productsModel = new DefaultTableModel(
                        new String[]{"Item ID", "Item Name", "Brand", "Price", "Markup", "In Stock"}, 0) {
                    public boolean isCellEditable(int row, int col) { return false; }
                };
                productsTable = new JTable(productsModel);
                styleTable(productsTable);
                table = productsTable;
                model = productsModel;
                break;
            case "inventory":
                inventoryModel = new DefaultTableModel(
                        new String[]{"OR Number", "Date", "User", "Supplier", "Total"}, 0) {
                    public boolean isCellEditable(int row, int col) { return false; }
                };
                inventoryTable = new JTable(inventoryModel);
                styleTable(inventoryTable);
                table = inventoryTable;
                model = inventoryModel;
                break;
            case "inventoryDetails":
                inventoryDetailsModel = new DefaultTableModel(
                        new String[]{"Inventory ID", "Item ID", "Quantity", "Unit Price", "Total Price"}, 0) {
                    public boolean isCellEditable(int row, int col) { return false; }
                };
                inventoryDetailsTable = new JTable(inventoryDetailsModel);
                styleTable(inventoryDetailsTable);
                table = inventoryDetailsTable;
                model = inventoryDetailsModel;
                break;
            case "sales":
                salesModel = new DefaultTableModel(
                        new String[]{"Sales ID", "Item ID", "Item Name", "Price", "Quantity", "Total", "User"}, 0) {
                    public boolean isCellEditable(int row, int col) { return false; }
                };
                salesTable = new JTable(salesModel);
                styleTable(salesTable);
                table = salesTable;
                model = salesModel;
                break;
            default:
                return panel;
        }
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(245, 247, 250));
        table.getTableHeader().setForeground(new Color(60, 60, 60));
        table.setGridColor(new Color(235, 235, 235));
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(230, 240, 255));
        table.setSelectionForeground(new Color(30, 30, 30));
        // Alternating row colors
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, s, f, r, c);
                if (r % 2 == 0) comp.setBackground(new Color(252, 253, 255));
                else comp.setBackground(new Color(240, 243, 247));
                return comp;
            }
        });
    }

    private void refreshAllTablesAndSummary() {
        // Load data logic would go here
        loadProducts();
        loadInventory();
        loadInventoryDetails();
        loadSales();
        updateSummary();
    }

    private void loadProducts() {
        productsModel.setRowCount(0);
        // Load products data logic would go here
        // Placeholder data for demonstration
        productsModel.addRow(new Object[]{"P001", "Sample Product", "Sample Brand", "100.00", "20%", "50"});
    }

    private void loadInventory() {
        inventoryModel.setRowCount(0);
        // Load inventory data logic would go here
        // Placeholder data for demonstration
        inventoryModel.addRow(new Object[]{"OR001", "2024-01-01", "User1", "Supplier A", "500.00"});
    }

    private void loadInventoryDetails() {
        inventoryDetailsModel.setRowCount(0);
        // Load inventory details data logic would go here
        // Placeholder data for demonstration
        inventoryDetailsModel.addRow(new Object[]{"INV001", "P001", "10", "50.00", "500.00"});
    }

    private void loadSales() {
        salesModel.setRowCount(0);
        // Load sales data logic would go here
        // Placeholder data for demonstration
        salesModel.addRow(new Object[]{"S001", "P001", "Sample Product", "100.00", "5", "500.00", "User1"});
    }

    private void updateSummary() {
        // Summary calculation logic would go here
        summaryLabel.setText("Total Products: 1 | Total Inventory Added: 1 | Total Sales: 1 | Total Sales Amount: 500.00");
    }

    private void exportToCSV() {
        // Export to CSV logic would go here
        JOptionPane.showMessageDialog(this, "Export functionality would be implemented here", "Export", JOptionPane.INFORMATION_MESSAGE);
    }
}
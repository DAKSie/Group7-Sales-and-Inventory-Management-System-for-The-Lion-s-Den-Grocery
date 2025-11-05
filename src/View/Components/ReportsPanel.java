package View.Components;

import Controller.ReportsController;
import View.Utils.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ReportsPanel extends JPanel {
    private JLabel summaryLabel;
    private JTable productsTable, inventoryTable, inventoryDetailsTable, salesTable;
    private DefaultTableModel productsModel, inventoryModel, inventoryDetailsModel, salesModel;
    private ReportsController reportsController;

    public ReportsPanel() {
        this.reportsController = new ReportsController();

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);

        JPanel summaryPanel = createSummaryPanel();
        topPanel.add(summaryPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonsPanel.setOpaque(false);
        BetterButtons export = new BetterButtons(0, 0, "export", "Export to CSV");
        export.addActionListener(e -> exportToCSV());
        BetterButtons refresh = new BetterButtons(0, 0, "refresh", "Refresh Data");
        refresh.addActionListener(e -> refreshAllTablesAndSummary());
        buttonsPanel.add(export);
        buttonsPanel.add(refresh);

        topPanel.add(buttonsPanel, BorderLayout.SOUTH);

        JTabbedPane tabbedPane = createTabbedPane();

        add(topPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        // Auto-refresh when shown
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshAllTablesAndSummary();
            }
        });

        refreshAllTablesAndSummary();
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(252, 253, 255));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        summaryLabel = new JLabel();
        summaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
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
        tabbedPane.addTab("Sales", createTablePanel("sales"));
        tabbedPane.addTab("Top Selling", createTablePanel("topSelling"));
        return tabbedPane;
    }

    private JPanel createTablePanel(String type) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));
        JTable table;
        DefaultTableModel model;

        switch (type) {
            case "products":
                productsModel = new DefaultTableModel(
                        new String[]{"ID", "Name", "Brand", "Price", "Markup", "Stock", "Selling Price"}, 0) {
                    public boolean isCellEditable(int row, int col) { return false; }
                };
                productsTable = new JTable(productsModel);
                styleTable(productsTable);
                table = productsTable;
                model = productsModel;
                break;
            case "inventory":
                inventoryModel = new DefaultTableModel(
                        new String[]{"ID", "OR number" , "Product", "Brand", "Quantity", "Unit Price", "Total Price", "Date"}, 0) {
                    public boolean isCellEditable(int row, int col) { return false; }
                };
                inventoryTable = new JTable(inventoryModel);
                styleTable(inventoryTable);
                table = inventoryTable;
                model = inventoryModel;
                break;
            case "sales":
                salesModel = new DefaultTableModel(
                        new String[]{"ID", "Product", "Brand", "Quantity", "Price", "Total", "User", "Date"}, 0) {
                    public boolean isCellEditable(int row, int col) { return false; }
                };
                salesTable = new JTable(salesModel);
                styleTable(salesTable);
                table = salesTable;
                model = salesModel;
                break;
            case "topSelling":
                inventoryDetailsModel = new DefaultTableModel(
                        new String[]{"Product", "Brand", "Quantity Sold", "Revenue", "Sales Count"}, 0) {
                    public boolean isCellEditable(int row, int col) { return false; }
                };
                inventoryDetailsTable = new JTable(inventoryDetailsModel);
                styleTable(inventoryDetailsTable);
                table = inventoryDetailsTable;
                model = inventoryDetailsModel;
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
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(245, 247, 250));
        table.getTableHeader().setForeground(new Color(60, 60, 60));
        table.setGridColor(new Color(235, 235, 235));
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(230, 240, 255));
        table.setSelectionForeground(new Color(30, 30, 30));
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
        loadProducts();
        loadInventory();
        loadSales();
        loadTopSelling();
        updateSummary();
    }

    private void loadProducts() {
        productsModel.setRowCount(0);
        List<Map<String, Object>> products = reportsController.getAllProductsReport();

        for (Map<String, Object> product : products) {
            Object[] rowData = {
                product.get("product_id"),
                product.get("product_name"),
                product.get("product_brand"),
                String.format("₱%.2f", product.get("product_price")),
                String.format("%.0f%%", product.get("product_markup")),
                product.get("product_stock"),
                String.format("₱%.2f", product.get("selling_price"))
            };
            productsModel.addRow(rowData);
        }
    }

    private void loadInventory() {
        inventoryModel.setRowCount(0);
        List<Map<String, Object>> inventory = reportsController.getAllInventoryReport();

        for (Map<String, Object> item : inventory) {
            Object[] rowData = {
                item.get("inventory_id"),
                item.get("or_number"),
                item.get("product_name"),
                item.get("product_brand"),
                item.get("inventory_quantity"),
                String.format("₱%.2f", item.get("unit_price")),
                String.format("₱%.2f", item.get("inventory_price")),
                item.get("created_at")
            };
            inventoryModel.addRow(rowData);
        }
    }

    private void loadSales() {
        salesModel.setRowCount(0);
        List<Map<String, Object>> sales = reportsController.getAllSalesReport();

        for (Map<String, Object> sale : sales) {
            Object[] rowData = {
                sale.get("sale_id"),
                sale.get("product_name"),
                sale.get("product_brand"),
                sale.get("sale_quantity"),
                String.format("₱%.2f", sale.get("sale_price")),
                String.format("₱%.2f", sale.get("sale_total")),
                sale.get("sale_user"),
                sale.get("sale_date")
            };
            salesModel.addRow(rowData);
        }
    }

    private void loadTopSelling() {
        inventoryDetailsModel.setRowCount(0);
        List<Map<String, Object>> topProducts = reportsController.getTopSellingProducts(10);

        for (Map<String, Object> product : topProducts) {
            Object[] rowData = {
                product.get("product_name"),
                product.get("product_brand"),
                product.get("total_quantity"),
                String.format("₱%.2f", product.get("total_revenue")),
                product.get("sale_count")
            };
            inventoryDetailsModel.addRow(rowData);
        }
    }

    private void updateSummary() {
        Map<String, Object> summary = reportsController.getDashboardSummary();
    
        String summaryText = String.format(
            "<html><div style='text-align: center; font-size: 13px; line-height: 1.4;'>"
            + "<b>Dashboard Summary</b><br>"
            + "Products: %s | Inventory Value: ₱%s | Sales Revenue: ₱%s | Total Sales: %s<br>"
            + "Low Stock: %s | Out of Stock: %s | Users: %s | Top Product: %s | Recent Sales (7 days): ₱%s"
            + "</div></html>",
            summary.get("totalProducts"),
            summary.get("totalInventoryValue"),
            summary.get("totalSalesRevenue"),
            summary.get("totalSalesCount"),
            summary.get("lowStockProducts"),
            summary.get("outOfStockProducts"),
            summary.get("totalUsers"),
            summary.get("topSellingProduct"),
            summary.get("recentSalesRevenue")
        );
    
        summaryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        summaryLabel.setText(summaryText);
    }

    private void exportToCSV() {
        JOptionPane.showMessageDialog(this,
            "Export feature coming soon!",
            "Export",
            JOptionPane.INFORMATION_MESSAGE);
    }
}

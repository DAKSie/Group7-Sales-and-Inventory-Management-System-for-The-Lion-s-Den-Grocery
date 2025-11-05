package View.Components;

import Controller.ReportsController;
import View.Utils.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ReportsPanel extends JPanel {
    private JLabel summaryLabel;
    private JTable productsTable, inventoryTable, inventoryDetailsTable, salesTable;
    private DefaultTableModel productsModel, inventoryModel, inventoryDetailsModel, salesModel;
    private ReportsController reportsController;
    private JTabbedPane tabbedPane;

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

        tabbedPane = createTabbedPane();

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
        int selectedTab = tabbedPane.getSelectedIndex();
        String tabTitle = tabbedPane.getTitleAt(selectedTab);
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export " + tabTitle + " Report to CSV");
        
        // Set default filename
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        String defaultFileName = "SIMS_" + tabTitle.replace(" ", "_") + "_" + timestamp + ".csv";
        fileChooser.setSelectedFile(new java.io.File(defaultFileName));
        
        // Add file filter for CSV
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files (*.csv)", "csv"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            
            // Ensure .csv extension
            if (!fileToSave.getName().toLowerCase().endsWith(".csv")) {
                fileToSave = new java.io.File(fileToSave.getAbsolutePath() + ".csv");
            }
            
            // Check if file exists and confirm overwrite
            if (fileToSave.exists()) {
                int overwrite = JOptionPane.showConfirmDialog(this,
                    "File already exists. Do you want to overwrite it?",
                    "Confirm Overwrite",
                    JOptionPane.YES_NO_OPTION);
                if (overwrite != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            
            try {
                switch (selectedTab) {
                    case 0: // Products
                        exportProductsToCSV(fileToSave);
                        break;
                    case 1: // Inventory
                        exportInventoryToCSV(fileToSave);
                        break;
                    case 2: // Sales
                        exportSalesToCSV(fileToSave);
                        break;
                    case 3: // Top Selling
                        exportTopSellingToCSV(fileToSave);
                        break;
                    default:
                        JOptionPane.showMessageDialog(this,
                            "Unsupported tab for export.",
                            "Export Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                }
                
                JOptionPane.showMessageDialog(this,
                    tabTitle + " report exported successfully to:\n" + fileToSave.getAbsolutePath(),
                    "Export Successful",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error exporting CSV file: " + ex.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void exportProductsToCSV(java.io.File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            // Write CSV header
            writer.write("Product ID,Product Name,Brand,Price,Markup %,Stock,Selling Price\n");
            
            // Write data
            List<Map<String, Object>> products = reportsController.getAllProductsReport();
            for (Map<String, Object> product : products) {
                writer.write(String.format("%d,\"%s\",\"%s\",%.2f,%.0f,%d,%.2f\n",
                    (Integer) product.get("product_id"),
                    escapeCsv((String) product.get("product_name")),
                    escapeCsv((String) product.get("product_brand")),
                    (Double) product.get("product_price"),
                    (Double) product.get("product_markup"),
                    (Integer) product.get("product_stock"),
                    (Double) product.get("selling_price")
                ));
            }
        }
    }

    private void exportInventoryToCSV(java.io.File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            // Write CSV header
            writer.write("Inventory ID,OR Number,Product ID,Product Name,Brand,Quantity,Unit Price,Total Price,Date Created\n");
            
            // Write data
            List<Map<String, Object>> inventory = reportsController.getAllInventoryReport();
            for (Map<String, Object> item : inventory) {
                writer.write(String.format("%d,%s,%d,\"%s\",\"%s\",%d,%.2f,%.2f,%s\n",
                    (Integer) item.get("inventory_id"),
                    escapeCsv((String) item.get("or_number")),
                    (Integer) item.get("product_id"),
                    escapeCsv((String) item.get("product_name")),
                    escapeCsv((String) item.get("product_brand")),
                    (Integer) item.get("inventory_quantity"),
                    (Double) item.get("unit_price"),
                    (Double) item.get("inventory_price"),
                    item.get("created_at").toString()
                ));
            }
        }
    }

    private void exportSalesToCSV(java.io.File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            // Write CSV header
            writer.write("Sale ID,Product ID,Product Name,Brand,Sale Item,Quantity,Unit Price,Total Price,User,Sale Date\n");
            
            // Write data
            List<Map<String, Object>> sales = reportsController.getAllSalesReport();
            for (Map<String, Object> sale : sales) {
                writer.write(String.format("%d,%d,\"%s\",\"%s\",\"%s\",%d,%.2f,%.2f,%s,%s\n",
                    (Integer) sale.get("sale_id"),
                    (Integer) sale.get("product_id"),
                    escapeCsv((String) sale.get("product_name")),
                    escapeCsv((String) sale.get("product_brand")),
                    escapeCsv((String) sale.get("sale_item")),
                    (Integer) sale.get("sale_quantity"),
                    (Double) sale.get("sale_price"),
                    (Double) sale.get("sale_total"),
                    escapeCsv((String) sale.get("sale_user")),
                    sale.get("sale_date").toString()
                ));
            }
        }
    }

    private void exportTopSellingToCSV(java.io.File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            // Write CSV header
            writer.write("Product Name,Brand,Total Quantity Sold,Total Revenue,Sales Count\n");
            
            // Write data
            List<Map<String, Object>> topProducts = reportsController.getTopSellingProducts(10);
            for (Map<String, Object> product : topProducts) {
                writer.write(String.format("\"%s\",\"%s\",%d,%.2f,%d\n",
                    escapeCsv((String) product.get("product_name")),
                    escapeCsv((String) product.get("product_brand")),
                    (Integer) product.get("total_quantity"),
                    (Double) product.get("total_revenue"),
                    (Integer) product.get("sale_count")
                ));
            }
        }
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        // Escape quotes by doubling them and wrap in quotes if contains comma, quote or newline
        if (value.contains("\"") || value.contains(",") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
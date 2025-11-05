// src/View/Components/SalesManagerPanel.java
package View.Components;

import Controller.ProductController;
import Controller.SalesController;
import Model.Product;
import Model.Sale;
import View.Utils.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SalesManagerPanel extends JPanel {
    private String _currentUser;
    private JComboBox<String> productCombo;
    private BetterInputs quantityInput, priceInput, totalInput, itemNameInput;
    private DefaultTableModel salesTableModel;
    private JTable salesTable;
    private SalesController salesController;
    private ProductController productController;

    public SalesManagerPanel(String currentUser) {
        this._currentUser = currentUser;
        this.salesController = new SalesController();
        this.productController = new ProductController();
        setLayout(null);
        buildPanel();
        loadSalesToTable();
        loadProductComboBox();
    }

    private void buildPanel() {
        // Table Panel
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(null);
        tablePanel.setBackground(new Color(245, 247, 250));
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        int tablePanelWidth = 825, tablePanelHeight = 475;
        tablePanel.setBounds(310, 10, tablePanelWidth, tablePanelHeight);

        String[] columnNames = {"Sale ID", "Product", "Brand", "Quantity", "Unit Price", "Total", "User"};
        salesTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        salesTable = new JTable(salesTableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    Color evenColor = new Color(250, 252, 255);
                    Color oddColor = new Color(240, 245, 250);
                    c.setBackground(row % 2 == 0 ? evenColor : oddColor);
                } else {
                    c.setBackground(new Color(230, 240, 255));
                }
                return c;
            }
        };
        salesTable.setShowHorizontalLines(false);
        salesTable.setShowVerticalLines(false);
        salesTable.setIntercellSpacing(new Dimension(0, 0));
        salesTable.setSelectionBackground(new Color(230, 240, 255));
        salesTable.setSelectionForeground(new Color(30, 30, 30));
        salesTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        salesTable.setRowHeight(26);
        salesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        salesTable.getTableHeader().setBackground(new Color(245, 247, 250));
        salesTable.getTableHeader().setForeground(new Color(60, 60, 60));
        salesTable.setGridColor(new Color(235, 235, 235));
        
        JScrollPane tableScrollPane = new JScrollPane(salesTable);
        tableScrollPane.setBounds(10, 10, tablePanelWidth - 20, tablePanelHeight - 20);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true));
        tablePanel.add(tableScrollPane);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(null);
        inputPanel.setBackground(new Color(252, 253, 255));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        int inputPanelWidth = 270, inputPanelHeight = 450;
        inputPanel.setBounds(10, 10, inputPanelWidth, inputPanelHeight);

        // Labels
        int labelY = 10, labelX = 20, labelOffset = 32;
        inputPanel.add(new BetterLabels(labelX, labelY, "Product: "));
        labelY += labelOffset;
        inputPanel.add(new BetterLabels(labelX, labelY, "Item Name: "));
        labelY += labelOffset;
        inputPanel.add(new BetterLabels(labelX, labelY, "Quantity: "));
        labelY += labelOffset;
        inputPanel.add(new BetterLabels(labelX, labelY, "Unit Price: "));
        labelY += labelOffset;
        inputPanel.add(new BetterLabels(labelX, labelY, "Total: "));

        // Inputs
        int textFieldY = 10, textFieldX = 120, textFieldOffset = 32;
        
        productCombo = new JComboBox<>();
        styleModernComboBox(productCombo);
        productCombo.setBounds(textFieldX, textFieldY, 120, 28);
        textFieldY += textFieldOffset;
        
        itemNameInput = new BetterInputs(textFieldX, textFieldY, "itemName", "");
        itemNameInput.setEditable(false);
        textFieldY += textFieldOffset;
        
        quantityInput = new BetterInputs(textFieldX, textFieldY, "quantity", "");
        textFieldY += textFieldOffset;
        
        priceInput = new BetterInputs(textFieldX, textFieldY, "price", "");
        priceInput.setEditable(false);
        textFieldY += textFieldOffset;
        
        totalInput = new BetterInputs(textFieldX, textFieldY, "total", "");
        totalInput.setEditable(false);

        inputPanel.add(productCombo);
        inputPanel.add(itemNameInput);
        inputPanel.add(quantityInput);
        inputPanel.add(priceInput);
        inputPanel.add(totalInput);

        // Buttons
        int buttonY = 250, buttonX = inputPanelWidth / 2 - 50, buttonOffset = 40;
        BetterButtons addButton = new BetterButtons(buttonX, buttonY, "addButton", "Add Sale");
        buttonY += buttonOffset;
        BetterButtons deleteButton = new BetterButtons(buttonX, buttonY, "deleteButton", "Delete Sale");
        buttonY += buttonOffset;
        BetterButtons clearButton = new BetterButtons(buttonX, buttonY, "clearButton", "Clear Fields");
        buttonY += buttonOffset;
        BetterButtons refreshButton = new BetterButtons(buttonX, buttonY, "refreshButton", "Refresh");
        
        addButton.setBackground(new Color(0, 122, 255));
        addButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        clearButton.setBackground(new Color(108, 117, 125));
        clearButton.setForeground(Color.WHITE);
        refreshButton.setBackground(new Color(255, 193, 7));
        refreshButton.setForeground(Color.BLACK);
        
        // Action Listeners
        addButton.addActionListener(e -> addSale());
        deleteButton.addActionListener(e -> deleteSale());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> {
            loadSalesToTable();
            loadProductComboBox();
        });
        
        // Event listeners for real-time calculation
        productCombo.addActionListener(e -> updateProductDetails());
        quantityInput.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { calculateTotal(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { calculateTotal(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { calculateTotal(); }
        });

        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        inputPanel.add(clearButton);
        inputPanel.add(refreshButton);

        add(tablePanel);
        add(inputPanel);
    }

    private void loadSalesToTable() {
        salesTableModel.setRowCount(0);
        List<Map<String, Object>> salesDetails = salesController.getSalesWithProductDetails();
        
        for (Map<String, Object> detail : salesDetails) {
            Object[] rowData = {
                detail.get("sale_id"),
                detail.get("product_name"),
                detail.get("product_brand"),
                detail.get("sale_quantity"),
                String.format("₱%.2f", detail.get("sale_price")),
                String.format("₱%.2f", detail.get("sale_total")),
                detail.get("sale_user")
            };
            salesTableModel.addRow(rowData);
        }
    }

    private void loadProductComboBox() {
        productCombo.removeAllItems();
        List<Product> products = productController.getAllProducts();
        
        for (Product product : products) {
            // Use the controller method to get selling price
            double sellingPrice = productController.getSellingPrice(product);
            productCombo.addItem(product.getProductId() + " - " + product.getProductName() + " (₱" + String.format("%.2f", sellingPrice) + ")");
        }
    }

    private void updateProductDetails() {
        String selected = (String) productCombo.getSelectedItem();
        if (selected != null && selected.contains(" - ")) {
            String[] parts = selected.split(" - ");
            String productName = parts[1].split(" \\(")[0]; // Extract product name before price
            itemNameInput.setText(productName);
            
            // Extract and set price
            String pricePart = selected.substring(selected.indexOf("₱") + 1, selected.indexOf(")"));
            try {
                double price = Double.parseDouble(pricePart);
                priceInput.setText(String.format("%.2f", price));
                calculateTotal();
            } catch (NumberFormatException e) {
                priceInput.setText("");
            }
        }
    }

    private void calculateTotal() {
        try {
            double price = Double.parseDouble(priceInput.getText());
            int quantity = quantityInput.getText().isEmpty() ? 0 : Integer.parseInt(quantityInput.getText());
            double total = price * quantity;
            totalInput.setText(String.format("%.2f", total));
        } catch (NumberFormatException e) {
            totalInput.setText("0.00");
        }
    }

    private void addSale() {
        try {
            String selectedProduct = (String) productCombo.getSelectedItem();
            if (selectedProduct == null) {
                JOptionPane.showMessageDialog(this, "Please select a product", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int productId = Integer.parseInt(selectedProduct.split(" - ")[0]);
            String itemName = itemNameInput.getText().trim();
            int quantity = Integer.parseInt(quantityInput.getText().trim());
            double price = Double.parseDouble(priceInput.getText());
            double total = Double.parseDouble(totalInput.getText());
            
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Sale sale = new Sale(0, productId, itemName, price, quantity, total, _currentUser);
            salesController.addSale(sale);
            
            JOptionPane.showMessageDialog(this, "Sale added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadSalesToTable();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for quantity", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding sale: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSale() {
        int selectedRow = salesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a sale to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int saleId = (int) salesTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this sale record?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            salesController.deleteSale(saleId);
            JOptionPane.showMessageDialog(this, "Sale deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadSalesToTable();
        }
    }

    private void clearFields() {
        quantityInput.setText("");
        totalInput.setText("0.00");
        itemNameInput.setText("");
        priceInput.setText("");
        if (productCombo.getItemCount() > 0) {
            productCombo.setSelectedIndex(0);
        }
    }

    private void styleModernComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(new Color(30, 30, 30));
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true));
        comboBox.setFocusable(false);
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                java.awt.Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                c.setBackground(isSelected ? new Color(230,240,255) : Color.WHITE);
                c.setForeground(new Color(30, 30, 30));
                return c;
            }
        });
    }
}
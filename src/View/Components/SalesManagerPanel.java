// src/View/Components/SalesManagerPanel.java
package View.Components;

import Controller.ProductController;
import Controller.SalesController;
import Model.Product;
import Model.Sale;
import View.Utils.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
    private List<Product> allProducts; // store all products for filtering
    private JLabel stockLabel; // Added stock display label

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
        int tablePanelWidth = 825, tablePanelHeight = 450;
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
        int inputPanelWidth = 270, inputPanelHeight = 480; // Increased height for stock label
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

        // Stock label
        stockLabel = new JLabel("Available Stock: 0");
        stockLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        stockLabel.setForeground(new Color(100, 100, 100));
        stockLabel.setBounds(labelX, labelY, 200, 20);
        inputPanel.add(stockLabel);

        // Inputs
        int textFieldY = 10, textFieldX = 120, textFieldOffset = 32;

        productCombo = new JComboBox<>();
        styleModernComboBox(productCombo);
        productCombo.setEditable(true); 
        productCombo.setBounds(textFieldX, textFieldY, 130, 28);
        textFieldY += textFieldOffset;

        // --- Search Feature ---
        JTextField editor = (JTextField) productCombo.getEditor().getEditorComponent();
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String input = editor.getText().trim().toLowerCase();
                filterComboBox(input);
            }
        });

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
        int buttonY = 280, buttonX = inputPanelWidth / 2 - 50, buttonOffset = 40;
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

        addButton.addActionListener(e -> addSale());
        deleteButton.addActionListener(e -> deleteSale());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> {
            loadSalesToTable();
            loadProductComboBox();
        });

        productCombo.addActionListener(e -> updateProductDetails());
        quantityInput.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { 
                calculateTotal(); 
                validateStock();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { 
                calculateTotal(); 
                validateStock();
            }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { 
                calculateTotal(); 
                validateStock();
            }
        });

        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        inputPanel.add(clearButton);
        inputPanel.add(refreshButton);

        add(tablePanel);
        add(inputPanel);
    }

    private void filterComboBox(String query) {
        if (allProducts == null) return;
        productCombo.removeAllItems();

        for (Product p : allProducts) {
            double sellingPrice = productController.getSellingPrice(p);
            String display = p.getProductId() + " - " + p.getProductName() + " (₱" + String.format("%.2f", sellingPrice) + ")";
            if (query.isEmpty() || p.getProductName().toLowerCase().contains(query)) {
                productCombo.addItem(display);
            }
        }

        JTextField editor = (JTextField) productCombo.getEditor().getEditorComponent();
        editor.setText(query);
        productCombo.showPopup();
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
        allProducts = productController.getAllProducts();

        for (Product product : allProducts) {
            double sellingPrice = productController.getSellingPrice(product);
            productCombo.addItem(product.getProductId() + " - " + product.getProductName() + " (₱" + String.format("%.2f", sellingPrice) + ")");
        }
    }

    private void updateProductDetails() {
        String selected = (String) productCombo.getSelectedItem();
        if (selected == null || !selected.contains(" - ")) return;

        String[] parts = selected.split(" - ");
        String productName = parts[1].split(" \\(")[0];
        itemNameInput.setText(productName);

        if (selected.contains("₱")) {
            String pricePart = selected.substring(selected.indexOf("₱") + 1, selected.indexOf(")"));
            try {
                double price = Double.parseDouble(pricePart);
                priceInput.setText(String.format("%.2f", price));
                calculateTotal();
            } catch (NumberFormatException e) {
                priceInput.setText("");
            }
        }

        // Update stock information
        updateStockDisplay();
    }

    private void updateStockDisplay() {
        String selected = (String) productCombo.getSelectedItem();

        stockLabel.setBounds(50, 180, 200, 25);
        stockLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        stockLabel.setOpaque(true);
        stockLabel.setHorizontalAlignment(SwingConstants.CENTER);
        stockLabel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        stockLabel.setBackground(new Color(245, 245, 245));

        if (selected == null || !selected.contains(" - ")) {
            stockLabel.setText("Available Stock: 0");
            return;
        }

        try {
            int productId = Integer.parseInt(selected.split(" - ")[0]);
            int availableStock = productController.getProductStock(productId);
            
            if (availableStock <= 0) {
                stockLabel.setText("Not enough: " + availableStock);
                stockLabel.setForeground(Color.RED);
            } else if (availableStock <= 10) {
                stockLabel.setText("Available Stock: " + availableStock + " (Low Stock)");
                stockLabel.setForeground(Color.ORANGE);
            } else {
                stockLabel.setText("Available Stock: " + availableStock);
                stockLabel.setForeground(new Color(0, 128, 0)); // Green
            }
        } catch (Exception e) {
            stockLabel.setText("Not enough: \" + availableStock");
            stockLabel.setForeground(Color.RED);
        }
    }

    private void validateStock() {
        String selected = (String) productCombo.getSelectedItem();
        if (selected == null || !selected.contains(" - ")) return;

        try {
            int productId = Integer.parseInt(selected.split(" - ")[0]);
            int availableStock = productController.getProductStock(productId);
            String quantityText = quantityInput.getText().trim();
            
            if (!quantityText.isEmpty()) {
                int enteredQuantity = Integer.parseInt(quantityText);
                
                if (enteredQuantity > availableStock) {
                    quantityInput.setBackground(new Color(255, 200, 200)); // Light red
                    stockLabel.setText("Available Stock: " + availableStock);
                    stockLabel.setForeground(Color.RED);
                } else if (enteredQuantity <= 0) {
                    quantityInput.setBackground(new Color(255, 255, 200)); // Light yellow
                    stockLabel.setText("Available Stock: " + availableStock);
                    stockLabel.setForeground(Color.ORANGE);
                } else {
                    quantityInput.setBackground(Color.WHITE);
                    updateStockDisplay(); // Reset to normal display
                }
            } else {
                quantityInput.setBackground(Color.WHITE);
                updateStockDisplay(); // Reset to normal display
            }
        } catch (NumberFormatException e) {
            // Ignore - user is still typing
            quantityInput.setBackground(Color.WHITE);
        } catch (Exception e) {
            quantityInput.setBackground(Color.WHITE);
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
            if (selectedProduct == null || selectedProduct.trim().isEmpty()) {
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

            // Check stock availability before proceeding
            int availableStock = productController.getProductStock(productId);
            if (quantity > availableStock) {
                JOptionPane.showMessageDialog(this, 
                    "Insufficient stock!\n" +
                    "Available stock: " + availableStock + "\n" +
                    "Requested quantity: " + quantity + "\n" +
                    "Please reduce the quantity or restock the product.", 
                    "Stock Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            Sale sale = new Sale(0, productId, itemName, price, quantity, total, _currentUser);
            
            if (salesController.addSale(sale)) {
                JOptionPane.showMessageDialog(this, "Sale added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadSalesToTable();
                loadProductComboBox(); // Refresh to get updated stock info
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add sale. Please check the inputs.", "Error", JOptionPane.ERROR_MESSAGE);
            }

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
            loadProductComboBox(); // Refresh to get updated stock info
        }
    }

    private void clearFields() {
        quantityInput.setText("");
        totalInput.setText("0.00");
        itemNameInput.setText("");
        priceInput.setText("");
        quantityInput.setBackground(Color.WHITE);
        stockLabel.setText("Available Stock: 0");
        stockLabel.setForeground(new Color(100, 100, 100));
        if (productCombo.getItemCount() > 0) {
            productCombo.setSelectedIndex(0);
        }
    }

    private void styleModernComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(new Color(30, 30, 30));
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true));
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                java.awt.Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                c.setBackground(isSelected ? new Color(230, 240, 255) : Color.WHITE);
                c.setForeground(new Color(30, 30, 30));
                return c;
            }
        });
    }
}
package View.Components;

import Controller.*;
import Model.*;
import View.Utils.*;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ProductManagerPanel extends JPanel {
    private String _currentUser;
    private DefaultTableModel inventoryTableModel;
    private JTable inventoryTable;
    private BetterInputs itemIdInput, itemNameInput, brandInput, priceInput, stockInput;
    private JComboBox<String> markupCombo;
    private ProductController productController;

    public ProductManagerPanel(String currentUser) {
        this._currentUser = currentUser;
        this.productController = new ProductController();
        setLayout(null);
        JPanel tablePanel = createTablePanel();
        JPanel inputPanel = createInputPanel();
        add(tablePanel);
        add(inputPanel);
        
        // Load products when panel is created
        loadProductsToTable();
        
        // Add table selection listener
        inventoryTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = inventoryTable.getSelectedRow();
                if (selectedRow != -1) {
                    fillInputFieldsFromTable(selectedRow);
                }
            }
        });
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(null);
        tablePanel.setBackground(new Color(245, 247, 250));
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        int tablePanelWidth = 825, tablePanelHeight = 450;
        tablePanel.setBounds(310, 10, tablePanelWidth, tablePanelHeight);

        String[] columnNames = {"Item ID", "Item Name", "Brand", "Price", "Price Markup", "In Stock"};
        inventoryTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        inventoryTable = new JTable(inventoryTableModel) {
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
        inventoryTable.setShowHorizontalLines(false);
        inventoryTable.setShowVerticalLines(false);
        inventoryTable.setIntercellSpacing(new Dimension(0, 0));
        inventoryTable.setSelectionBackground(new Color(230, 240, 255));
        inventoryTable.setSelectionForeground(new Color(30, 30, 30));
        inventoryTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inventoryTable.setRowHeight(26);
        inventoryTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        inventoryTable.getTableHeader().setBackground(new Color(245, 247, 250));
        inventoryTable.getTableHeader().setForeground(new Color(60, 60, 60));
        inventoryTable.setGridColor(new Color(235, 235, 235));
        
        JScrollPane tableScrollPane = new JScrollPane(inventoryTable);
        tableScrollPane.setBounds(10, 10, tablePanelWidth - 20, tablePanelHeight - 20);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true));
        
        tablePanel.add(tableScrollPane);
        return tablePanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(null);
        inputPanel.setBackground(new Color(252, 253, 255));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        int inputPanelWidth = 270, inputPanelHeight = 475;
        inputPanel.setBounds(10, 10, inputPanelWidth, inputPanelHeight);
        
        // Labels
        int labelY = 10, labelX = 20, labelOffset = 32;
        inputPanel.add(new BetterLabels(labelX, labelY, "Item ID: "));
        labelY += labelOffset;
        inputPanel.add(new BetterLabels(labelX, labelY, "Item Name: "));
        labelY += labelOffset;
        inputPanel.add(new BetterLabels(labelX, labelY, "Brand: "));
        labelY += labelOffset;
        inputPanel.add(new BetterLabels(labelX, labelY, "Price: "));
        labelY += labelOffset;
        inputPanel.add(new BetterLabels(labelX, labelY, "Price Markup: "));
        labelY += labelOffset;
        inputPanel.add(new BetterLabels(labelX, labelY, "Stock: "));
        
        // Inputs
        int textFieldY = 10, textFieldX = 120, textFieldOffset = 32;
        itemIdInput = new BetterInputs(textFieldX, textFieldY, "itemId", "");
        itemIdInput.setEditable(false);
        textFieldY += textFieldOffset;
        
        itemNameInput = new BetterInputs(textFieldX, textFieldY, "itemName", "");
        textFieldY += textFieldOffset;
        
        brandInput = new BetterInputs(textFieldX, textFieldY, "brand", "");
        textFieldY += textFieldOffset;
        
        priceInput = new BetterInputs(textFieldX, textFieldY, "price", "");
        textFieldY += textFieldOffset;
        
        markupCombo = new JComboBox<>(new String[]{"10%", "15%", "20%", "25%"});
        styleModernComboBox(markupCombo);
        markupCombo.setBounds(textFieldX, textFieldY, 120, 28);
        markupCombo.setSelectedIndex(0);
        textFieldY += textFieldOffset;
        
        stockInput = new BetterInputs(textFieldX, textFieldY, "stock", "");
        
        inputPanel.add(itemIdInput);
        inputPanel.add(itemNameInput);
        inputPanel.add(brandInput);
        inputPanel.add(priceInput);
        inputPanel.add(markupCombo);
        inputPanel.add(stockInput);
        
        // Buttons
        int buttonY = 240, buttonX = inputPanelWidth / 2 - 50, buttonOffset = 40;
        BetterButtons addButton = new BetterButtons(buttonX, buttonY, "addButton", "Add Item");
        buttonY += buttonOffset;
        BetterButtons updateButton = new BetterButtons(buttonX, buttonY, "updateButton", "Update Item");
        buttonY += buttonOffset;
        BetterButtons deleteButton = new BetterButtons(buttonX, buttonY, "deleteButton", "Delete Item");
        buttonY += buttonOffset;
        BetterButtons clearButton = new BetterButtons(buttonX, buttonY, "clearButton", "Clear Fields");
        buttonY += buttonOffset;
        BetterButtons refreshButton = new BetterButtons(buttonX, buttonY, "refreshButton", "Refresh");
        
        addButton.setBackground(new Color(0, 122, 255));
        addButton.setForeground(Color.WHITE);
        updateButton.setBackground(new Color(40, 167, 69));
        updateButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        clearButton.setBackground(new Color(108, 117, 125));
        clearButton.setForeground(Color.WHITE);
        refreshButton.setBackground(new Color(255, 193, 7));
        refreshButton.setForeground(Color.BLACK);
        
        // Action Listeners
        addButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> loadProductsToTable());
        
        inputPanel.add(addButton);
        inputPanel.add(updateButton);
        inputPanel.add(deleteButton);
        inputPanel.add(clearButton);
        inputPanel.add(refreshButton);
        
        return inputPanel;
    }

    private void loadProductsToTable() {
        inventoryTableModel.setRowCount(0); // Clear existing data
        List<Product> products = productController.getAllProducts();
        
        for (Product product : products) {
            Object[] rowData = {
                product.getProductId(),
                product.getProductName(),
                product.getProductBrand(),
                String.format("₱%.2f", product.getProductPrice()),
                String.format("%.0f%%", product.getProductMarkup()),
                product.getProductStock()
            };
            inventoryTableModel.addRow(rowData);
        }
    }

    private void addProduct() {
        try {
            String name = itemNameInput.getText().trim();
            String brand = brandInput.getText().trim();
            String priceText = priceInput.getText().replace("₱", "").trim();
            String markupText = ((String) markupCombo.getSelectedItem()).replace("%", "").trim();
            String stockText = stockInput.getText().trim();
            
            if (name.isEmpty() || brand.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double price = Double.parseDouble(priceText);
            double markup = Double.parseDouble(markupText);
            int stock = Integer.parseInt(stockText);
            
            // For new products, use -1 or 0 as temporary ID (the database will auto-generate the real ID)
            Product product = new Product(0, name, brand, price, markup, stock);
            productController.addProduct(product);
            
            JOptionPane.showMessageDialog(this, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadProductsToTable();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for price and stock", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding product: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProduct() {
        try {
            String idText = itemIdInput.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a product to update", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int id = Integer.parseInt(idText);
            String name = itemNameInput.getText().trim();
            String brand = brandInput.getText().trim();
            String priceText = priceInput.getText().replace("₱", "").trim();
            String markupText = ((String) markupCombo.getSelectedItem()).replace("%", "").trim();
            String stockText = stockInput.getText().trim();
            
            if (name.isEmpty() || brand.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double price = Double.parseDouble(priceText);
            double markup = Double.parseDouble(markupText);
            int stock = Integer.parseInt(stockText);
            
            Product product = new Product(id, name, brand, price, markup, stock);
            productController.updateProduct(product);
            
            JOptionPane.showMessageDialog(this, "Product updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadProductsToTable();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating product: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProduct() {
        try {
            String idText = itemIdInput.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a product to delete", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int id = Integer.parseInt(idText);
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this product?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                productController.deleteProduct(id);
                JOptionPane.showMessageDialog(this, "Product deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadProductsToTable();
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid product ID", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting product: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fillInputFieldsFromTable(int row) {
        itemIdInput.setText(inventoryTableModel.getValueAt(row, 0).toString());
        itemNameInput.setText(inventoryTableModel.getValueAt(row, 1).toString());
        brandInput.setText(inventoryTableModel.getValueAt(row, 2).toString());
        priceInput.setText(inventoryTableModel.getValueAt(row, 3).toString());
        
        String markup = inventoryTableModel.getValueAt(row, 4).toString().replace("%", "");
        for (int i = 0; i < markupCombo.getItemCount(); i++) {
            if (markupCombo.getItemAt(i).replace("%", "").equals(markup)) {
                markupCombo.setSelectedIndex(i);
                break;
            }
        }
        
        stockInput.setText(inventoryTableModel.getValueAt(row, 5).toString());
    }

    private void clearFields() {
        itemIdInput.setText("");
        itemNameInput.setText("");
        brandInput.setText("");
        priceInput.setText("");
        stockInput.setText("");
        markupCombo.setSelectedIndex(0);
        inventoryTable.clearSelection();
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
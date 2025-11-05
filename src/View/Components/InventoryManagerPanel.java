// src/View/Components/InventoryManagerPanel.java
package View.Components;

import Controller.InventoryController;
import Controller.ProductController;
import Model.Inventory;
import Model.Product;
import View.Utils.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class InventoryManagerPanel extends JPanel {
    private String _currentUser;
    private JComboBox<String> itemIdCombo;
    private JComboBox<String> supplierInput; // Changed from BetterInputs to JComboBox
    private BetterInputs quantityInput, priceInput, itemNameInput, orInput;
    private DefaultTableModel inventoryTableModel;
    private JTable inventoryTable;
    private ProductManagerPanel productManagerPanel;
    private InventoryController inventoryController;
    private ProductController productController;

    public InventoryManagerPanel(String currentUser) {
        this._currentUser = currentUser;
        this.inventoryController = new InventoryController();
        this.productController = new ProductController();
        setLayout(null);
        buildPanel();
        loadInventoryToTable();
        loadProductComboBox();
        loadSupplierComboBox();
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

        String[] columnNames = {"Inventory ID", "OR Number", "Product ID", "Supplier", "Product Name", "Brand", "Quantity", "Unit Price", "Total Price"};
        inventoryTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
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

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(null);
        inputPanel.setBackground(new Color(252, 253, 255));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        int inputPanelWidth = 270, inputPanelHeight = 510;
        inputPanel.setBounds(10, 10, inputPanelWidth, inputPanelHeight);

        // Labels
        int labelY = 10, labelX = 20, labelOffset = 32;
        inputPanel.add(new BetterLabels(labelX, labelY, "OR Number: "));
        labelY += labelOffset;
        inputPanel.add(new BetterLabels(labelX, labelY, "Product ID: "));
        labelY += labelOffset;
        inputPanel.add(new BetterLabels(labelX, labelY, "Product Name: "));
        labelY += labelOffset;
        inputPanel.add(new BetterLabels(labelX, labelY, "Supplier: "));
        labelY += labelOffset;
        inputPanel.add(new BetterLabels(labelX, labelY, "Quantity: "));
        labelY += labelOffset;
        inputPanel.add(new BetterLabels(labelX, labelY, "Unit Price: "));

        // Inputs
        int textFieldY = 10, textFieldX = 120, textFieldOffset = 32;
        orInput = new BetterInputs(textFieldX, textFieldY, "orNumber", "");
        textFieldY += textFieldOffset;
        
        itemIdCombo = new JComboBox<>();
        styleModernComboBox(itemIdCombo);
        itemIdCombo.setBounds(textFieldX, textFieldY, 120, 28);
        itemIdCombo.setEditable(true);
        ((JTextField) itemIdCombo.getEditor().getEditorComponent()).setBorder(null);
        textFieldY += textFieldOffset;
        
        itemNameInput = new BetterInputs(textFieldX, textFieldY, "itemName", "");
        itemNameInput.setEditable(false);
        textFieldY += textFieldOffset;
        
        // Supplier as JComboBox
        supplierInput = new JComboBox<>();
        styleModernComboBox(supplierInput);
        supplierInput.setBounds(textFieldX, textFieldY, 120, 28);
        supplierInput.setEditable(true);
        ((JTextField) supplierInput.getEditor().getEditorComponent()).setBorder(null);
        textFieldY += textFieldOffset;
        
        quantityInput = new BetterInputs(textFieldX, textFieldY, "quantity", "");
        textFieldY += textFieldOffset;
        
        priceInput = new BetterInputs(textFieldX, textFieldY, "price", "");

        inputPanel.add(orInput);
        inputPanel.add(itemIdCombo);
        inputPanel.add(itemNameInput);
        inputPanel.add(supplierInput);
        inputPanel.add(quantityInput);
        inputPanel.add(priceInput);

        // Buttons
        int buttonY = 280, buttonX = inputPanelWidth / 2 - 50, buttonOffset = 40;
        BetterButtons addButton = new BetterButtons(buttonX, buttonY, "addButton", "Add Stock");
        buttonY += buttonOffset;
        BetterButtons deleteButton = new BetterButtons(buttonX, buttonY, "deleteButton", "Delete Stock");
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
        addButton.addActionListener(e -> addInventory());
        deleteButton.addActionListener(e -> deleteInventory());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> {
            loadInventoryToTable();
            loadProductComboBox();
            loadSupplierComboBox();
        });
        
        // Product selection listener
        itemIdCombo.addActionListener(e -> updateProductName());

        // Search/filter feature for product combo
        JTextField productEditor = (JTextField) itemIdCombo.getEditor().getEditorComponent();
        productEditor.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String input = productEditor.getText().trim().toLowerCase();
                filterProductComboBox(input);
            }
        });

        // Auto-generate OR number button
        // BetterButtons generateOrButton = new BetterButtons(240, 10, "generateOr", "Generate");
        // generateOrButton.setBackground(new Color(40, 167, 69));
        // generateOrButton.setForeground(Color.WHITE);
        // generateOrButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        // generateOrButton.setBounds(240, 10, 70, 28);
        // generateOrButton.addActionListener(e -> generateOrNumber());
        // inputPanel.add(generateOrButton);

        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        inputPanel.add(clearButton);
        inputPanel.add(refreshButton);

        add(tablePanel);
        add(inputPanel);
    }

    private void loadInventoryToTable() {
        inventoryTableModel.setRowCount(0);
        List<Map<String, Object>> inventoryDetails = inventoryController.getInventoryWithProductDetails();
        
        for (Map<String, Object> detail : inventoryDetails) {
            Object[] rowData = {
                detail.get("inventory_id"),
                detail.get("or_number"),
                detail.get("product_id"),
                detail.get("product_supplier"),
                detail.get("product_name"),
                detail.get("product_brand"),
                detail.get("inventory_quantity"),
                String.format("₱%.2f", detail.get("unit_price")),
                String.format("₱%.2f", detail.get("inventory_price"))
            };
            inventoryTableModel.addRow(rowData);
        }
    }

    private void loadProductComboBox() {
        itemIdCombo.removeAllItems();
        List<Product> products = productController.getAllProducts();
        
        for (Product product : products) {
            itemIdCombo.addItem(product.getProductId() + " - " + product.getProductName());
        }
    }

    private void loadSupplierComboBox() {
        supplierInput.removeAllItems();
        List<String> suppliers = inventoryController.getAllSuppliers();
        
        for (String supplier : suppliers) {
            supplierInput.addItem(supplier);
        }
    }

    private void filterProductComboBox(String query) {
        itemIdCombo.removeAllItems();
        List<Product> products = productController.getAllProducts();

        for (Product product : products) {
            String display = product.getProductId() + " - " + product.getProductName();
            if (query.isEmpty() || product.getProductName().toLowerCase().contains(query)) {
                itemIdCombo.addItem(display);
            }
        }

        JTextField editor = (JTextField) itemIdCombo.getEditor().getEditorComponent();
        editor.setText(query);
        itemIdCombo.showPopup();
    }

    private void updateProductName() {
        String selected = (String) itemIdCombo.getSelectedItem();
        if (selected != null && selected.contains(" - ")) {
            String productName = selected.split(" - ")[1];
            itemNameInput.setText(productName);
        }
    }

    private void generateOrNumber() {
        String newOrNumber = inventoryController.generateNewOrNumber();
        orInput.setText(newOrNumber);
    }

    private void addInventory() {
        try {
            String selectedProduct = (String) itemIdCombo.getSelectedItem();
            if (selectedProduct == null || selectedProduct.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a product", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String orNumber = orInput.getText().trim();
            if (orNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter OR Number", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String supplier = (String) supplierInput.getSelectedItem();
            if (supplier == null || supplier.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a supplier", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int productId = Integer.parseInt(selectedProduct.split(" - ")[0]);
            int quantity = Integer.parseInt(quantityInput.getText().trim());
            double unitPrice = Double.parseDouble(priceInput.getText().replace("₱", "").trim());
            double totalPrice = quantity * unitPrice;
            
            // Create inventory with supplier
            Inventory inventory = new Inventory(0, orNumber, productId, supplier, quantity, unitPrice, totalPrice);
            
            if (inventoryController.addInventory(inventory)) {
                JOptionPane.showMessageDialog(this, "Inventory added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadInventoryToTable();
                loadSupplierComboBox(); // Refresh suppliers in case new one was added
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add inventory. Please check the inputs.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for quantity and price", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding inventory: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteInventory() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an inventory item to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int inventoryId = (int) inventoryTableModel.getValueAt(selectedRow, 0);
        String orNumber = (String) inventoryTableModel.getValueAt(selectedRow, 1);
        String productName = (String) inventoryTableModel.getValueAt(selectedRow, 4);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete inventory record?\n" +
            "OR Number: " + orNumber + "\n" +
            "Product: " + productName, 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (inventoryController.deleteInventory(inventoryId)) {
                JOptionPane.showMessageDialog(this, "Inventory deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadInventoryToTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete inventory", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearFields() {
        orInput.setText("");
        quantityInput.setText("");
        priceInput.setText("");
        supplierInput.setSelectedIndex(-1);
        ((JTextField) supplierInput.getEditor().getEditorComponent()).setText("");
        itemNameInput.setText("");
        if (itemIdCombo.getItemCount() > 0) {
            itemIdCombo.setSelectedIndex(0);
        }
    }

    private void styleModernComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(new Color(30, 30, 30));
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true));
        
        comboBox.setEditable(true);
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

    public void setProductManagerPanel(ProductManagerPanel panel) {
        this.productManagerPanel = panel;
    }
}
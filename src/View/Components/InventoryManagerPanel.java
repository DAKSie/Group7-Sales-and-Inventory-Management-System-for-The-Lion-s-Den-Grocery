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
    private BetterInputs quantityInput, priceInput, itemNameInput, supplierInput, orInput;
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

        String[] columnNames = {"Inventory ID", "Product ID", "OR number",  "Product Name", "Brand", "Quantity", "Unit Price", "Total Price"};
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
        int inputPanelWidth = 270, inputPanelHeight = 475;
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
        textFieldY += textFieldOffset;
        
        itemNameInput = new BetterInputs(textFieldX, textFieldY, "itemName", "");
        itemNameInput.setEditable(false);
        textFieldY += textFieldOffset;
        
        supplierInput = new BetterInputs(textFieldX, textFieldY, "supplier", "");
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
        int buttonY = 250, buttonX = inputPanelWidth / 2 - 50, buttonOffset = 40;
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
        });
        
        // Product selection listener
        itemIdCombo.addActionListener(e -> updateProductName());

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

    private void updateProductName() {
        String selected = (String) itemIdCombo.getSelectedItem();
        if (selected != null && selected.contains(" - ")) {
            String productName = selected.split(" - ")[1];
            itemNameInput.setText(productName);
        }
    }

    private void addInventory() {
        try {
            String selectedProduct = (String) itemIdCombo.getSelectedItem();
            if (selectedProduct == null) {
                JOptionPane.showMessageDialog(this, "Please select a product", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int productId = Integer.parseInt(selectedProduct.split(" - ")[0]);
            String orNumber = orInput.getText().trim();
            int quantity = Integer.parseInt(quantityInput.getText().trim());
            double unitPrice = Double.parseDouble(priceInput.getText().replace("₱", "").trim());
            double totalPrice = quantity * unitPrice;
            
            Inventory inventory = new Inventory(0, orNumber, productId,  quantity, unitPrice, totalPrice);
            inventoryController.addInventory(inventory);
            
            JOptionPane.showMessageDialog(this, "Inventory added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadInventoryToTable();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for quantity and price", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding inventory: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteInventory() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an inventory item to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int inventoryId = (int) inventoryTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this inventory record?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            inventoryController.deleteInventory(inventoryId);
            JOptionPane.showMessageDialog(this, "Inventory deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadInventoryToTable();
        }
    }

    private void clearFields() {
        orInput.setText("");
        quantityInput.setText("");
        priceInput.setText("");
        supplierInput.setText("");
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

    public void setProductManagerPanel(ProductManagerPanel panel) {
        this.productManagerPanel = panel;
    }
}
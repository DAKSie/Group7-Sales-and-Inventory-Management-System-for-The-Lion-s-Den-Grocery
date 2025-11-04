package View.Components;

import View.Utils.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class InventoryManagerPanel extends JPanel {
    private String _currentUser;
    private JComboBox<String> itemIdCombo;
    private ProductManagerPanel productManagerPanel; // Reference for real-time updates

    public InventoryManagerPanel(String currentUser) {
        this._currentUser = currentUser;
        setLayout(null);
        buildPanel();
    }

    private void buildPanel() {
        // Table Panel
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(null);
        tablePanel.setBackground(new Color(245, 247, 250)); // Lighter, modern background
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        int tablePanelWidth = 825, tablePanelHeight = 475;
        tablePanel.setBounds(310, 10, tablePanelWidth, tablePanelHeight);

        String[] columnNames = {"OR Number", "Item ID", "Item Name", "Supplier", "Quantity", "Price", "Date", "User"};
        DefaultTableModel inventoryTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        JTable inventoryTable = new JTable(inventoryTableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    Color evenColor = new Color(250, 252, 255); // very light blue tint
                    Color oddColor = new Color(240, 245, 250);  // slightly darker tint
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
        int labelY = 10;
        int labelX = 20;
        int labelOffset = 32;
        BetterLabels orLabel = new BetterLabels(labelX, labelY, "OR Number: ");
        labelY += labelOffset;
        BetterLabels itemIdLabel = new BetterLabels(labelX, labelY, "Item ID: ");
        labelY += labelOffset;
        BetterLabels itemNameLabel = new BetterLabels(labelX, labelY, "Item Name: ");
        labelY += labelOffset;
        BetterLabels supplierLabel = new BetterLabels(labelX, labelY, "Supplier: ");
        labelY += labelOffset;
        BetterLabels quantityLabel = new BetterLabels(labelX, labelY, "Quantity: ");
        labelY += labelOffset;
        BetterLabels priceLabel = new BetterLabels(labelX, labelY, "Price: ");
        inputPanel.add(orLabel);
        inputPanel.add(itemIdLabel);
        inputPanel.add(supplierLabel);
        inputPanel.add(quantityLabel);
        inputPanel.add(priceLabel);
        inputPanel.add(itemNameLabel);

        // Inputs
        int textFieldY = 10;
        int textFieldX = 120;
        int textFieldOffset = 32;
        BetterInputs orInput = new BetterInputs(textFieldX, textFieldY, "orNumber", "");
        textFieldY += textFieldOffset;
        
        itemIdCombo = new JComboBox<>(new String[]{"Item1", "Item2", "Item3"}); // Placeholder data
        styleModernComboBox(itemIdCombo);
        itemIdCombo.setBounds(textFieldX, textFieldY, 120, 28);
        textFieldY += textFieldOffset;
        
        BetterInputs itemNameInput = new BetterInputs(textFieldX, textFieldY, "itemName", "");
        itemNameInput.setEditable(false);
        inputPanel.add(itemNameInput);
        textFieldY += textFieldOffset;
        
        BetterInputs supplierInput = new BetterInputs(textFieldX, textFieldY, "supplier", "");
        textFieldY += textFieldOffset;
        
        BetterInputs quantityInput = new BetterInputs(textFieldX, textFieldY, "quantity", "");
        textFieldY += textFieldOffset;
        
        BetterInputs priceInput = new BetterInputs(textFieldX, textFieldY, "price", "");

        inputPanel.add(priceInput);
        inputPanel.add(orInput);
        inputPanel.add(itemIdCombo);
        inputPanel.add(supplierInput);
        inputPanel.add(quantityInput);
        inputPanel.add(itemNameInput);

        // Buttons
        int buttonY = 250;
        int buttonX = inputPanelWidth / 2 - 50;
        int buttonOffset = 40;
        BetterButtons addButton = new BetterButtons(buttonX, buttonY, "addButton", "Add Item");
        buttonY += buttonOffset;
        BetterButtons deleteButton = new BetterButtons(buttonX, buttonY, "deleteButton", "Delete Item");
        buttonY += buttonOffset;
        BetterButtons clearButton = new BetterButtons(buttonX, buttonY, "clearButton", "Clear Fields");
        buttonY += buttonOffset;
        BetterButtons confirmButton = new BetterButtons(buttonX, buttonY, "confirmButton", "Confirm");
        
        addButton.setBackground(new Color(0, 122, 255));
        addButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        clearButton.setBackground(new Color(108, 117, 125));
        clearButton.setForeground(Color.WHITE);
        confirmButton.setBackground(new Color(40, 167, 69));
        confirmButton.setForeground(Color.WHITE);
        
        inputPanel.add(confirmButton);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        inputPanel.add(clearButton);

        // Action Listeners (stubs only)
        addButton.addActionListener(e -> {
            System.out.println("add button");
        });
        
        clearButton.addActionListener(e -> {
            System.out.println("clear button");
        });
        
        deleteButton.addActionListener(e -> {
            System.out.println("delete button");
        });
        
        confirmButton.addActionListener(e -> {
            System.out.println("confirm button");
        });

        add(tablePanel);
        add(inputPanel);
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

    private void refreshItemIdCombo() {
        // Refresh combo box logic would go here
    }

    public void setProductManagerPanel(ProductManagerPanel panel) {
        this.productManagerPanel = panel;
    }
} // End of InventoryManagerPanel class
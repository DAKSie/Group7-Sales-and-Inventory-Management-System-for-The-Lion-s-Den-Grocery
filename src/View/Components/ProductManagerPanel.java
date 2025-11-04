package View.Components;

import View.Utils.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ProductManagerPanel extends JPanel {
    private String _currentUser;
    private DefaultTableModel inventoryTableModel;
    private JTable inventoryTable;
    private BetterInputs itemIdInput, itemNameInput, brandInput, priceInput;
    private JComboBox<String> markupCombo;

    public ProductManagerPanel(String currentUser) {
        this._currentUser = currentUser;
        setLayout(null);
        JPanel tablePanel = createTablePanel();
        JPanel inputPanel = createInputPanel();
        add(tablePanel);
        add(inputPanel);
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(null);
        tablePanel.setBackground(new Color(245, 247, 250)); // Modern light background
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        int tablePanelWidth = 825, tablePanelHeight = 475;
        tablePanel.setBounds(310, 10, tablePanelWidth, tablePanelHeight);

        String[] columnNames = {"Item ID", "Item Name", "Brand", "Price", "Price Markup", "In Stock"};
        inventoryTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        
        inventoryTable = new JTable(inventoryTableModel) {
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
        
        JComboBox<String> markupCombo = new JComboBox<>(new String[]{"0%", "10%", "15%", "20%", "25%"});
        styleModernComboBox(markupCombo);
        inventoryTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(markupCombo));
        
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
        
        // Inputs
        int textFieldY = 10, textFieldX = 120, textFieldOffset = 32;
        itemIdInput = new BetterInputs(textFieldX, textFieldY, "itemId", "");
        itemIdInput.setEditable(false);
        itemIdInput.setText(""); // Placeholder ID
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
        
        inputPanel.add(itemIdInput);
        inputPanel.add(itemNameInput);
        inputPanel.add(brandInput);
        inputPanel.add(priceInput);
        inputPanel.add(markupCombo);
        
        // Buttons
        int buttonY = 250, buttonX = inputPanelWidth / 2 - 50, buttonOffset = 40;
        BetterButtons addButton = new BetterButtons(buttonX, buttonY, "addButton", "Add Item");
        buttonY += buttonOffset;
        BetterButtons updateButton = new BetterButtons(buttonX, buttonY, "updateButton", "Update Item");
        buttonY += buttonOffset;
        BetterButtons deleteButton = new BetterButtons(buttonX, buttonY, "deleteButton", "Delete Item");
        buttonY += buttonOffset;
        BetterButtons clearButton = new BetterButtons(buttonX, buttonY, "clearButton", "Clear Fields");
        
        addButton.setBackground(new Color(0, 122, 255));
        addButton.setForeground(Color.WHITE);
        updateButton.setBackground(new Color(40, 167, 69));
        updateButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        clearButton.setBackground(new Color(108, 117, 125));
        clearButton.setForeground(Color.WHITE);
        
        // Action Listeners (stubs only)
        addButton.addActionListener(e -> {
            // Add item logic would go here
        });
        
        updateButton.addActionListener(e -> {
            // Update item logic would go here
        });
        
        deleteButton.addActionListener(e -> {
            // Delete item logic would go here
        });
        
        clearButton.addActionListener(e -> {
            // Clear fields logic would go here
        });
        
        inputPanel.add(addButton);
        inputPanel.add(updateButton);
        inputPanel.add(deleteButton);
        inputPanel.add(clearButton);
        
        return inputPanel;
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

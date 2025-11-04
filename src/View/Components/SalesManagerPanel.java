package View.Components;

import View.Utils.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SalesManagerPanel extends JPanel {

    private String _currentUser;
    private JComboBox<String> itemNameCombo;
    private BetterInputs itemIdInput, quantityInput;
    private DefaultTableModel salesTableModel;
    private JTable salesTable;

    public SalesManagerPanel(String currentUser) {
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
        tablePanel.setBackground(new Color(245, 247, 250));
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        int tablePanelWidth = 825, tablePanelHeight = 475;
        tablePanel.setBounds(310, 10, tablePanelWidth, tablePanelHeight);

        String[] columnNames = {"Sales ID", "Item ID", "Item Name", "Price", "Quantity", "Total", "User"};
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
        inputPanel.add(new BetterLabels(labelX, labelY, "Quantity: "));
        
        // Inputs
        int textFieldY = 10, textFieldX = 120, textFieldOffset = 32;
        itemIdInput = new BetterInputs(textFieldX, textFieldY, "itemId", "");
        itemIdInput.setEditable(true);
        inputPanel.add(itemIdInput);
        textFieldY += textFieldOffset;
        
        itemNameCombo = new JComboBox<>(); // Placeholder data
        styleModernComboBox(itemNameCombo);
        itemNameCombo.setBounds(textFieldX, textFieldY, 120, 28);
        inputPanel.add(itemNameCombo);
        
        textFieldY += textFieldOffset;
        quantityInput = new BetterInputs(textFieldX, textFieldY, "quantity", "");
        inputPanel.add(quantityInput);
        
        // Buttons
        int buttonY = 250, buttonX = inputPanelWidth / 2 - 50, buttonOffset = 40;
        BetterButtons addButton = new BetterButtons(buttonX, buttonY, "addButton", "Add");
        buttonY += buttonOffset;
        BetterButtons removeButton = new BetterButtons(buttonX, buttonY, "removeButton", "Remove Item");
        buttonY += buttonOffset;
        BetterButtons confirmButton = new BetterButtons(buttonX, buttonY, "confirmButton", "Confirm");
        
        addButton.setBackground(new Color(0, 122, 255));
        addButton.setForeground(Color.WHITE);
        removeButton.setBackground(new Color(220, 53, 69));
        removeButton.setForeground(Color.WHITE);
        confirmButton.setBackground(new Color(40, 167, 69));
        confirmButton.setForeground(Color.WHITE);
        
        inputPanel.add(addButton);
        inputPanel.add(removeButton);
        inputPanel.add(confirmButton);
        
        // Action Listeners (stubs only)
        addButton.addActionListener(e -> {
            // Add sale row logic would go here
        });
        
        removeButton.addActionListener(e -> {
            // Remove selected rows logic would go here
        });
        
        confirmButton.addActionListener(e -> {
            // Confirm sale logic would go here
        });
        
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

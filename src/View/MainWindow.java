package View;

import View.Components.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints; // Added missing import
import javax.swing.*;

public class MainWindow extends JFrame {
    private String _currentUser;

    public MainWindow(String currentUser) {
        this._currentUser = currentUser;
        setTitle("Sales and Inventory Management System - Welcome, " + currentUser);
        setSize(1200, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set application icon
        ImageIcon image = new ImageIcon("global/logo.png");
        setIconImage(image.getImage());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Initialize all panels
        ProductManagerPanel productManagerPanel = new ProductManagerPanel(currentUser);
        InventoryManagerPanel inventoryManagerPanel = new InventoryManagerPanel(currentUser);
        SalesManagerPanel salesManagerPanel = new SalesManagerPanel(currentUser);
        ReportsPanel reportsPanel = new ReportsPanel(); // Remove currentUser parameter

        // Remove the setProductManagerPanel call since the method doesn't exist
        // inventoryManagerPanel.setProductManagerPanel(productManagerPanel);
        
        // Add panels to tabs
        tabbedPane.addTab("Product Manager", productManagerPanel);
        tabbedPane.addTab("Inventory Manager", inventoryManagerPanel);
        tabbedPane.addTab("Sales Manager", salesManagerPanel);
        tabbedPane.addTab("Reports", reportsPanel);

        // Tab styling
        tabbedPane.setBackground(new Color(245, 245, 245));
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setForeground(new Color(30, 30, 30));
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        
        // Custom tab design
        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                tabAreaInsets.left = 12;
            }
            
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                paintCustomTabBackground(g, tabPlacement, tabIndex, x, y, w, h, isSelected);
            }
            
            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                paintCustomTabBorder(g, tabPlacement, tabIndex, x, y, w, h, isSelected);
            }
            
            @Override
            protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
                // No focus indicator
            }

            private void paintCustomTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isSelected) {
                    g2.setColor(new Color(230, 240, 255));
                    g2.fillRoundRect(x+2, y+2, w-4, h-4, 18, 18);
                } else {
                    g2.setColor(new Color(245, 245, 245));
                    g2.fillRoundRect(x+2, y+2, w-4, h-4, 18, 18);
                }
            }

            private void paintCustomTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isSelected) {
                    g2.setColor(new Color(0, 122, 255));
                    g2.drawRoundRect(x+1, y+1, w-3, h-3, 16, 16);
                } else {
                    g2.setColor(new Color(220, 220, 220));
                    g2.drawRoundRect(x+1, y+1, w-3, h-3, 16, 16);
                }
            }
            
            @Override
            protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
                return super.calculateTabWidth(tabPlacement, tabIndex, metrics) + 20;
            }
            
            @Override
            protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
                return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight) + 8;
            }
        });

        add(tabbedPane, BorderLayout.CENTER);

        // Add status bar
        add(createStatusBar(), BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel();
        statusBar.setLayout(new BorderLayout());
        statusBar.setBackground(new Color(240, 240, 240));
        statusBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JLabel statusLabel = new JLabel("Logged in as: " + _currentUser + " | Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(100, 100, 100));
        
        JLabel versionLabel = new JLabel("SIMS v1.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        versionLabel.setForeground(new Color(100, 100, 100));
        versionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(versionLabel, BorderLayout.EAST);
        
        return statusBar;
    }
    
    // Main method for testing
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new MainWindow("Test User");
        });
    }
}
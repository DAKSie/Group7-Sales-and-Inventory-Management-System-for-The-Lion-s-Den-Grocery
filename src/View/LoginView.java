package View;

import Model.User;
import java.awt.Color;
import java.sql.*;
import javax.swing.*;

public class LoginView extends JFrame {
    public static String _currentUser;

    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JButton loginLoginButton;
    private JButton loginRegisterButton;

    public LoginView() {
        setTitle("Login");
        setSize(370, 320);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Apple-style background (very light gray)
        getContentPane().setBackground(new Color(242, 242, 247));

        // Application Logo (centered, circular, subtle shadow)
        ImageIcon image = new ImageIcon("global/logo.png");
        setIconImage(image.getImage());
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(new ImageIcon(image.getImage().getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH)));
        logoLabel.setBounds(155, 22, 60, 60);
        logoLabel.setOpaque(true);
        logoLabel.setBackground(new Color(255, 255, 255, 220));
        logoLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 235), 1, true),
            BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(logoLabel);

        // Username Label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 100, 90, 22);
        usernameLabel.setFont(new java.awt.Font("San Francisco", java.awt.Font.PLAIN, 15));
        usernameLabel.setForeground(new Color(60, 60, 67, 200));
        add(usernameLabel);

        // Username Field (rounded, subtle shadow)
        loginUsernameField = new JTextField();
        loginUsernameField.setBounds(140, 97, 180, 30);
        loginUsernameField.setFont(new java.awt.Font("San Francisco", java.awt.Font.PLAIN, 16));
        loginUsernameField.setBackground(new Color(255, 255, 255));
        loginUsernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 230), 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        add(loginUsernameField);

        // Password Label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 145, 90, 22);
        passwordLabel.setFont(new java.awt.Font("San Francisco", java.awt.Font.PLAIN, 15));
        passwordLabel.setForeground(new Color(60, 60, 67, 200));
        add(passwordLabel);

        // Password Field (rounded, subtle shadow)
        loginPasswordField = new JPasswordField();
        loginPasswordField.setBounds(140, 142, 180, 30);
        loginPasswordField.setFont(new java.awt.Font("San Francisco", java.awt.Font.PLAIN, 16));
        loginPasswordField.setBackground(new Color(255, 255, 255));
        loginPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 230), 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        add(loginPasswordField);

        // Login Button (rounded, blue gradient, shadow, bold font)
        loginLoginButton = new JButton("Log in");
        loginLoginButton.setBounds(50, 195, 270, 38);
        loginLoginButton.setFont(new java.awt.Font("San Francisco", java.awt.Font.BOLD, 17));
        loginLoginButton.setBackground(new Color(0, 122, 255));
        loginLoginButton.setForeground(Color.WHITE);
        loginLoginButton.setFocusPainted(true);
        loginLoginButton.setFocusCycleRoot(rootPaneCheckingEnabled);
        loginLoginButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 122, 255), 1, true),
            BorderFactory.createEmptyBorder(8, 0, 8, 0)
        ));
        loginLoginButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginLoginButton.setOpaque(true);
        add(loginLoginButton);
        loginLoginButton.addActionListener(e -> LoginHandler());

        // Register Button (flat, blue text, no border, Apple style link)
        loginRegisterButton = new JButton("Register");
        loginRegisterButton.setContentAreaFilled(false);
        loginRegisterButton.setBorderPainted(false);
        loginRegisterButton.setFocusPainted(false);
        loginRegisterButton.setForeground(new Color(0, 122, 255));
        loginRegisterButton.setFont(new java.awt.Font("San Francisco", java.awt.Font.PLAIN, 14));
        loginRegisterButton.setBounds(120, 245, 130, 28);
        loginRegisterButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginRegisterButton.setHorizontalAlignment(SwingConstants.CENTER);
        add(loginRegisterButton);
        loginRegisterButton.addActionListener(e -> RegisterHandler());

        setVisible(true);
    }

    private void RegisterHandler() {
        new RegisterView();
        System.out.println("ASDASD");
        this.dispose();
    }

    private void LoginHandler() {
        String username = loginUsernameField.getText().trim();
        String password = new String(loginPasswordField.getPassword()).trim();
    
        // Validate that fields are not empty
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both username and password", 
                "Login Failed", 
                JOptionPane.ERROR_MESSAGE);
            return; // Stop execution if fields are empty
        }
    
        User authenticatedUser = authenticateUser(username, password);
        
        if (authenticatedUser != null) {
            _currentUser = authenticatedUser.getUserName();
            JOptionPane.showMessageDialog(this, 
                "Login successful! Welcome " + authenticatedUser.getUserName(), 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            new MainWindow(_currentUser);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid username or password", 
                "Login Failed", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private User authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE user_username = ? AND user_password = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            if (conn == null) {
                JOptionPane.showMessageDialog(this, 
                    "Database connection failed", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return null;
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password); // In production, use hashed passwords!
            rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("user_name"),
                    rs.getString("user_phone"),
                    rs.getString("user_username"),
                    rs.getString("user_password")
                );
            }

        } catch (SQLException e) {
            System.err.println("Database error during authentication: " + e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Database error: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        } finally {
            closeResources(stmt, conn, rs);
        }
        return null;
    }

    // Database connection method (similar to your DBContext)
    private Connection getConnection() {
        try {
            String URL = "jdbc:mysql://localhost:3306/sims?serverTimezone=UTC";
            String USER = "root";
            String PASSWORD = "";
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    // Resource cleanup method
    private void closeResources(PreparedStatement stmt, Connection conn, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}
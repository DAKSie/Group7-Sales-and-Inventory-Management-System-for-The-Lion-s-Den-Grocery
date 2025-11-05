package View;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class RegisterView extends JFrame {
    private JTextField nameField;
    private JTextField registerUsernameField;
    private JTextField contactField;
    private JPasswordField registerPasswordField;
    private JButton registerLoginButton;
    private JButton registerRegisterButton;

    public RegisterView() {
        setTitle("Register");
        setSize(350, 420); // Reduced height since we removed fields
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Application Logo (centered)
        ImageIcon originalIcon = new ImageIcon("global/logo.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        setIconImage(originalIcon.getImage());
        JLabel logoLabel = new JLabel(scaledIcon);
        logoLabel.setBounds(125, 10, 100, 100);
        add(logoLabel);

        // Apple-like colors and font
        Color bgColor = new Color(245, 245, 247);
        Color fieldColor = Color.WHITE;
        Color borderColor = new Color(200, 200, 200);
        Color buttonColor = new Color(0, 122, 255);
        Color buttonTextColor = Color.WHITE;
        Font labelFont = new Font("San Francisco", Font.PLAIN, 14);
        Font fieldFont = new Font("San Francisco", Font.PLAIN, 14);
        Font buttonFont = new Font("San Francisco", Font.BOLD, 15);

        getContentPane().setBackground(bgColor);

        int y = 120;
        int labelW = 100, labelH = 22, fieldW = 180, fieldH = 28, xLabel = 30, xField = 130;

        // Name
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setBounds(xLabel, y, labelW, labelH);
        nameLabel.setFont(labelFont);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(xField, y, fieldW, fieldH);
        nameField.setFont(fieldFont);
        nameField.setBackground(fieldColor);
        nameField.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
        add(nameField);

        // Contact Number
        y += 38;
        JLabel contactLabel = new JLabel("Contact No:");
        contactLabel.setBounds(xLabel, y, labelW, labelH);
        contactLabel.setFont(labelFont);
        add(contactLabel);

        contactField = new JTextField();
        contactField.setBounds(xField, y, fieldW, fieldH);
        contactField.setFont(fieldFont);
        contactField.setBackground(fieldColor);
        contactField.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
        add(contactField);

        // Username
        y += 38;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(xLabel, y, labelW, labelH);
        userLabel.setFont(labelFont);
        add(userLabel);

        registerUsernameField = new JTextField();
        registerUsernameField.setBounds(xField, y, fieldW, fieldH);
        registerUsernameField.setFont(fieldFont);
        registerUsernameField.setBackground(fieldColor);
        registerUsernameField.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
        add(registerUsernameField);

        // Password
        y += 38;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(xLabel, y, labelW, labelH);
        passwordLabel.setFont(labelFont);
        add(passwordLabel);

        registerPasswordField = new JPasswordField();
        registerPasswordField.setBounds(xField, y, fieldW, fieldH);
        registerPasswordField.setFont(fieldFont);
        registerPasswordField.setBackground(fieldColor);
        registerPasswordField.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
        add(registerPasswordField);

        // Register Button
        y += 48;
        registerRegisterButton = new JButton("Register");
        registerRegisterButton.setBounds(60, y, 220, 36);
        registerRegisterButton.setFont(buttonFont);
        registerRegisterButton.setBackground(buttonColor);
        registerRegisterButton.setForeground(buttonTextColor);
        registerRegisterButton.setFocusPainted(false);
        registerRegisterButton.setBorder(BorderFactory.createLineBorder(buttonColor, 1, true));
        registerRegisterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effects
        registerRegisterButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registerRegisterButton.setBackground(new Color(0, 100, 220));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registerRegisterButton.setBackground(buttonColor);
            }
        });
        
        add(registerRegisterButton);
        registerRegisterButton.addActionListener(e -> RegisterHandler());

        // Login Button (as a link)
        y += 44;
        registerLoginButton = new JButton("Already have an account? Login");
        registerLoginButton.setBounds(60, y, 220, 28);
        registerLoginButton.setFont(new Font("San Francisco", Font.PLAIN, 13));
        registerLoginButton.setContentAreaFilled(false);
        registerLoginButton.setBorderPainted(false);
        registerLoginButton.setForeground(new Color(0, 122, 255));
        registerLoginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        add(registerLoginButton);
        registerLoginButton.addActionListener(e -> LoginHandler());

        setVisible(true);
    }

    private void LoginHandler() {
        new LoginView();
        this.dispose();
    }

    private void RegisterHandler() {
        String name = nameField.getText().trim();
        String phone = contactField.getText().trim();
        String username = registerUsernameField.getText().trim();
        String password = new String(registerPasswordField.getPassword()).trim();

        // Validation
        if (name.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showError("Please fill in all required fields");
            return;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters long");
            return;
        }

        if (!isValidPhone(phone)) {
            showError("Please enter a valid phone number");
            return;
        }

        // Check if username already exists
        if (isUsernameTaken(username)) {
            showError("Username already exists. Please choose a different one.");
            return;
        }

        // Register the user
        if (registerUser(name, phone, username, password)) {
            JOptionPane.showMessageDialog(this, 
                "Registration successful! You can now login.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            new LoginView();
            this.dispose();
        } else {
            showError("Registration failed. Please try again.");
        }
    }

    private boolean isValidPhone(String phone) {
        // Basic phone validation - adjust as needed
        return phone.matches("^[0-9\\-\\+\\(\\)\\s]+$") && phone.length() >= 10;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Registration Failed", JOptionPane.ERROR_MESSAGE);
    }

    private boolean isUsernameTaken(String username) {
        String sql = "SELECT user_id FROM users WHERE user_username = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            if (conn == null) return true; // Assume taken if connection fails
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            
            return rs.next(); // If there's a result, username is taken
            
        } catch (SQLException e) {
            System.err.println("Error checking username: " + e.getMessage());
            return true; // Assume taken on error
        } finally {
            closeResources(stmt, conn, rs);
        }
    }

    private boolean registerUser(String name, String phone, String username, String password) {
        String sql = "INSERT INTO users (user_name, user_phone, user_username, user_password) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = getConnection();
            if (conn == null) {
                showError("Database connection failed");
                return false;
            }
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, username);
            stmt.setString(4, password); // In production, hash this password!
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            showError("Database error: " + e.getMessage());
            return false;
        } finally {
            closeResources(stmt, conn, null);
        }
    }

    // Database connection method
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
package View;

import java.awt.*;
import javax.swing.*;

public class RegisterView extends JFrame{
    private JTextField empIdField;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField registerUsernameField;
    private JTextField contactField;
    private JPasswordField registerPasswordField;
    private JButton registerLoginButton;
    private JButton registerRegisterButton;

    public RegisterView(){
        setTitle("Register");
        setSize(350, 520);
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

        // Employee ID
        JLabel empIdLabel = new JLabel("Employee ID:");
        empIdLabel.setBounds(xLabel, y, labelW, labelH);
        empIdLabel.setFont(labelFont);
        add(empIdLabel);

        empIdField = new JTextField();
        empIdField.setBounds(xField, y, fieldW, fieldH);
        empIdField.setFont(fieldFont);
        empIdField.setBackground(fieldColor);
        empIdField.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
        add(empIdField);

        // Name
        y += 38;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(xLabel, y, labelW, labelH);
        nameLabel.setFont(labelFont);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(xField, y, fieldW, fieldH);
        nameField.setFont(fieldFont);
        nameField.setBackground(fieldColor);
        nameField.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
        add(nameField);

        // Age
        y += 38;
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setBounds(xLabel, y, labelW, labelH);
        ageLabel.setFont(labelFont);
        add(ageLabel);

        ageField = new JTextField();
        ageField.setBounds(xField, y, fieldW, fieldH);
        ageField.setFont(fieldFont);
        ageField.setBackground(fieldColor);
        ageField.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
        add(ageField);

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

        // Gender
        y += 38;
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(xLabel, y, labelW, labelH);
        genderLabel.setFont(labelFont);
        add(genderLabel);

        String[] genders = {"Male", "Female", "Others", "Prefer not to say"};
        JComboBox<String> genderComboBox = new JComboBox<>(genders);
        genderComboBox.setBounds(xField, y, fieldW, fieldH);
        genderComboBox.setFont(fieldFont);
        genderComboBox.setBackground(fieldColor);
        genderComboBox.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
        add(genderComboBox);

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
        registerRegisterButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add(registerRegisterButton);
        registerRegisterButton.addActionListener(e -> System.out.println("Register"));

        // Login Button (as a link)
        y += 44;
        registerLoginButton = new JButton("Already have an account? Login");
        registerLoginButton.setBounds(60, y, 220, 28);
        registerLoginButton.setFont(new Font("San Francisco", Font.PLAIN, 13));
        registerLoginButton.setContentAreaFilled(false);
        registerLoginButton.setBorderPainted(false);
        registerLoginButton.setForeground(new Color(0, 122, 255));
        registerLoginButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add(registerLoginButton);
        registerLoginButton.addActionListener(e -> LoginHandler());

        setVisible(true);
    }

    private void LoginHandler() {
        new LoginViewDesigner();
        this.dispose();
    }
}

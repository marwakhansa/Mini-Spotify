package juke;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import java.awt.Color;

public class Register extends javax.swing.JFrame {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    static String dbURL = "jdbc:mysql://localhost/userregistration?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    public Register() {
        initComponents();
        getContentPane().setBackground(new Color(18, 18, 18)); // Dark theme
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        passwordField = new javax.swing.JPasswordField();
        registerButton = new javax.swing.JButton();
        genderLabel = new javax.swing.JLabel();
        genderComboBox = new javax.swing.JComboBox<>();
        userNameField = new javax.swing.JTextField();
        countryLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        eMailField = new javax.swing.JTextField();
        countryComboBox = new javax.swing.JComboBox<>();
        loginLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(18, 18, 18));

        passwordField.setText("Password");
        passwordField.setForeground(Color.WHITE);
        passwordField.setBackground(new Color(50, 50, 50));

        registerButton.setText("Register");
        registerButton.setBackground(new Color(29, 185, 84));
        registerButton.setForeground(Color.BLACK);
        registerButton.addActionListener(evt -> registerButtonActionPerformed());

        genderLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        genderLabel.setText("Gender");
        genderLabel.setForeground(Color.WHITE);

        genderComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Female", "Male", "Not Identify" }));
        genderComboBox.setBackground(new Color(50, 50, 50));
        genderComboBox.setForeground(Color.WHITE);

        userNameField.setText("Username");
        userNameField.setForeground(Color.WHITE);
        userNameField.setBackground(new Color(50, 50, 50));

        countryLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        countryLabel.setText("Country");
        countryLabel.setForeground(Color.WHITE);

        nameField.setText("Name");
        nameField.setForeground(Color.WHITE);
        nameField.setBackground(new Color(50, 50, 50));

        eMailField.setText("Email");
        eMailField.setForeground(Color.WHITE);
        eMailField.setBackground(new Color(50, 50, 50));

        countryComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Australia", "Belgium", "Canada", "China", "England", "France", "Germany", "Italy", "USA", "Spain", "Turkey", "Russia", "Japan", "Other" }));
        countryComboBox.setSelectedIndex(10);
        countryComboBox.setBackground(new Color(50, 50, 50));
        countryComboBox.setForeground(Color.WHITE);

        loginLabel.setFont(new java.awt.Font("SansSerif", 0, 12));
        loginLabel.setText("You are already a member: Login");
        loginLabel.setForeground(Color.WHITE);
        loginLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Login login = new Login();
                login.setVisible(true);
                dispose();
            }
        });

        // Layout (based on your .form, manually adjusted)
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eMailField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(genderLabel)
                    .addComponent(genderComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(countryLabel)
                    .addComponent(countryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loginLabel))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10)
                .addComponent(eMailField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10)
                .addComponent(userNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10)
                .addComponent(genderLabel)
                .addGap(5)
                .addComponent(genderComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10)
                .addComponent(countryLabel)
                .addGap(5)
                .addComponent(countryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20)
                .addComponent(registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10)
                .addComponent(loginLabel)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void registerButtonActionPerformed() {
        try {
            String query = "INSERT INTO user(name, email, username, password, country, gender) VALUES (?,?,?,?,?,?)";

            connection = DriverManager.getConnection(dbURL, "root", "12345");
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, nameField.getText());
            preparedStatement.setString(2, eMailField.getText());
            preparedStatement.setString(3, userNameField.getText());
            preparedStatement.setString(4, new String(passwordField.getPassword()));
            preparedStatement.setString(5, countryComboBox.getSelectedItem().toString());
            preparedStatement.setString(6, genderComboBox.getSelectedItem().toString());

            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully registered!");
            Login login = new Login();
            login.setVisible(true);
            this.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Registration failed: " + e.getMessage());
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new Register().setVisible(true));
    }

    private javax.swing.JComboBox<String> countryComboBox;
    private javax.swing.JLabel countryLabel;
    private javax.swing.JTextField eMailField;
    private javax.swing.JComboBox<String> genderComboBox;
    private javax.swing.JLabel genderLabel;
    private javax.swing.JLabel loginLabel;
    private javax.swing.JTextField nameField;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JButton registerButton;
    private javax.swing.JTextField userNameField;
}
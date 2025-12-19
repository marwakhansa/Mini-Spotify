package juke;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;

public class Login extends javax.swing.JFrame {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    static String dbURL = "jdbc:mysql://localhost/userregistration?useUnicode=true"
            + "&useJDBCCompliantTimezoneShift=true"
            + "&useLegacyDatetimeCode=false"
            + "&serverTimezone=UTC";

    public Login() {
        initComponents();
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(18, 18, 18)); // Dark theme
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        javax.swing.JLabel titleLabel = new javax.swing.JLabel();
        usernameTitle = new javax.swing.JLabel();
        passwordTitle = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();
        loginButton = new javax.swing.JButton();
        registerLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(18, 18, 18));

        titleLabel.setText("Welcome to Mehfil-e-Music");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        usernameTitle.setText("Username");
        usernameTitle.setForeground(Color.WHITE);
        passwordTitle.setText("Password");
        passwordTitle.setForeground(Color.WHITE);
        registerLabel.setText("Are You New Here: Register");
        registerLabel.setForeground(Color.WHITE);
        registerLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        loginButton.setText("Login");
        loginButton.setBackground(new Color(29, 185, 84));
        loginButton.setForeground(Color.BLACK);

        loginButton.addActionListener(e -> loginButtonActionPerformed());

        registerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Register register = new Register();
                register.setVisible(true);
                dispose();
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(20)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(usernameTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(passwordTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(10)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(usernameField)
                                                        .addComponent(passwordField)
                                                        .addComponent(loginButton, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                                                        .addComponent(registerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
                                                .addGap(20)))
                                .addContainerGap())
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20)
                                .addComponent(titleLabel)
                                .addGap(20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(usernameTitle)
                                        .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(passwordTitle)
                                        .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18)
                                .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10)
                                .addComponent(registerLabel)
                                .addContainerGap(80, Short.MAX_VALUE))
        );

        pack();
    }

    private void loginButtonActionPerformed() {
        try {
            String query = "SELECT * FROM user WHERE username = ? AND password = ?";

            connection = DriverManager.getConnection(dbURL, "root", "12345");
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, usernameField.getText());
            preparedStatement.setString(2, new String(passwordField.getPassword()));

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("id");
                JOptionPane.showMessageDialog(this, "Login successful!");
                Jukebox jukebox = new Jukebox(userId);
                jukebox.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Login failed: " + e.getMessage());
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new Login().setVisible(true));
    }

    private javax.swing.JButton loginButton;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordTitle;
    private javax.swing.JLabel registerLabel;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel usernameTitle;
}
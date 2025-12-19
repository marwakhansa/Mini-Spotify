package juke;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import net.proteanit.sql.DbUtils;
import java.awt.Color;

public class Playlist extends javax.swing.JFrame {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet;
    static String dbURL = "jdbc:mysql://localhost/userregistration?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    private final Jukebox parent;
    private final int userId;

    public Playlist(Jukebox parent, int userId) {
        this.parent = parent;
        this.userId = userId;
        initComponents();
        getContentPane().setBackground(new Color(18, 18, 18)); // Dark theme
        showTableData();
    }

    public void showTableData() {
        try {
            connection = DriverManager.getConnection(dbURL, "root", "12345");
            String sql = "SELECT s.* FROM playlist p JOIN song s ON p.song_id = s.id WHERE p.user_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            jTable1.setModel(DbUtils.resultSetToTableModel(resultSet));
            jTable1.setBackground(new Color(18, 18, 18));
            jTable1.setForeground(Color.WHITE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();
        deleteSong = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(18, 18, 18));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Song Name", "Artist", "Genre", "Duration"
            }
        ));
        jTable1.setBackground(new Color(18, 18, 18));
        jTable1.setForeground(Color.WHITE);
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 18));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("My Playlist");
        jLabel1.setForeground(Color.WHITE);

        backButton.setText("Back to Jukebox");
        backButton.setBackground(new Color(29, 185, 84));
        backButton.setForeground(Color.BLACK);
        backButton.addActionListener(evt -> {
            parent.setVisible(true);
            dispose();
        });

        deleteSong.setText("Delete Song");
        deleteSong.setBackground(new Color(29, 185, 84));
        deleteSong.setForeground(Color.BLACK);
        deleteSong.addActionListener(evt -> deleteSongActionPerformed());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(200, 200, 200)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(deleteSong, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100))
        );
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGap(10)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteSong, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        ;

        pack();
        setLocationRelativeTo(null);
    }

    private void deleteSongActionPerformed() {
        int row = jTable1.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a song to delete!");
            return;
        }

        Object idObj = jTable1.getValueAt(row, 0);
        if (idObj == null) {
            JOptionPane.showMessageDialog(this, "Cannot find ID of selected song!");
            return;
        }

        int songId;
        try {
            songId = Integer.parseInt(idObj.toString());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid song ID!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this song from playlist?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            connection = DriverManager.getConnection(dbURL, "root", "12345");
            String sql = "DELETE FROM playlist WHERE song_id = ? AND user_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, songId);
            preparedStatement.setInt(2, userId);
            int deleted = preparedStatement.executeUpdate();

            if (deleted > 0) {
                JOptionPane.showMessageDialog(this, "Song deleted successfully!");
                showTableData(); // Refresh table
            } else {
                JOptionPane.showMessageDialog(this, "Song not found in playlist!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to delete song: " + e.getMessage());
        }
    }

    private javax.swing.JButton backButton;
    private javax.swing.JButton deleteSong;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
}
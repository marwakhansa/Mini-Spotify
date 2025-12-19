package juke;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;

public class DeleteSong extends JFrame {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    static String dbURL = "jdbc:mysql://localhost/userregistration?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    private JTable table;
    private JButton deleteButton;
    private JButton backButton;
    private final Jukebox parent;

    public DeleteSong(Jukebox parent) {
        this.parent = parent;
        setTitle("Delete Song");
        setSize(700, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(18, 18, 18));

        JLabel titleLabel = new JLabel("Delete Songs");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        table = new JTable();
        table.setBackground(new Color(18, 18, 18));
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(29, 185, 84));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(18, 18, 18));
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(18, 18, 18));

        deleteButton = new JButton("Delete Selected Song");
        deleteButton.setBackground(new Color(29, 185, 84));
        deleteButton.setForeground(Color.BLACK);
        deleteButton.addActionListener(e -> deleteSelectedSong());
        bottomPanel.add(deleteButton);

        backButton = new JButton("Back to Jukebox");
        backButton.setBackground(new Color(29, 185, 84));
        backButton.setForeground(Color.BLACK);
        backButton.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
        });
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);

        showTableData();

        setLocationRelativeTo(null);
    }

    private void showTableData() {
        try {
            connection = DriverManager.getConnection(dbURL, "root", "12345");
            String sql = "SELECT id, SongName, Artist, Genre, Duration, file_path, image_path FROM song";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            table.setModel(DbUtils.resultSetToTableModel(resultSet));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load songs: " + e.getMessage());
        }
    }

    private void deleteSelectedSong() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a song to delete!");
            return;
        }

        int songId = Integer.parseInt(table.getValueAt(row, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this song?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            connection = DriverManager.getConnection(dbURL, "root", "12345");
            String sql = "DELETE FROM song WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, songId);
            int deleted = preparedStatement.executeUpdate();

            if (deleted > 0) {
                JOptionPane.showMessageDialog(this, "Song deleted successfully!");
                showTableData(); // refresh table
                parent.loadSongs(); // refresh parent
            } else {
                JOptionPane.showMessageDialog(this, "Song not found!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to delete song: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeleteSong(null).setVisible(true)); // For testing
    }
}
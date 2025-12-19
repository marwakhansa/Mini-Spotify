package juke;

import java.sql.*;
import javax.swing.*;
import net.proteanit.sql.DbUtils;
import java.awt.Color;

public class SearchSong extends JFrame {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet;
    static String dbURL = "jdbc:mysql://localhost/userregistration?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    private JTable table;
    private JTextField textField;
    private JComboBox<String> genreComboBox;
    private JButton songButton, singerButton, searchGenre, backButton;
    private JLabel jLabel1, jLabel2;
    private final Jukebox parent;

    public SearchSong(Jukebox parent) {
        this.parent = parent;
        setTitle("Search Song");
        setSize(700, 400);
        setLayout(null);
        getContentPane().setBackground(new Color(18, 18, 18));

        jLabel1 = new JLabel("Song or Singer Name:");
        jLabel1.setBounds(20, 10, 150, 25);
        jLabel1.setForeground(Color.WHITE);
        add(jLabel1);

        textField = new JTextField();
        textField.setBounds(180, 10, 200, 25);
        textField.setBackground(new Color(50, 50, 50));
        textField.setForeground(Color.WHITE);
        add(textField);

        songButton = new JButton("Search Song");
        songButton.setBounds(400, 10, 120, 25);
        songButton.setBackground(new Color(29, 185, 84));
        songButton.setForeground(Color.BLACK);
        add(songButton);

        singerButton = new JButton("Search Singer");
        singerButton.setBounds(530, 10, 130, 25);
        singerButton.setBackground(new Color(29, 185, 84));
        singerButton.setForeground(Color.BLACK);
        add(singerButton);

        jLabel2 = new JLabel("Search by Genre:");
        jLabel2.setBounds(20, 50, 150, 25);
        jLabel2.setForeground(Color.WHITE);
        add(jLabel2);

        genreComboBox = new JComboBox<>(new String[]{"Pop", "Blues", "Raggae", "R&B", "Jazz", "Metal", "Rock", "Country", "Rap", "Hip-Hop"});
        genreComboBox.setBounds(180, 50, 200, 25);
        genreComboBox.setBackground(new Color(50, 50, 50));
        genreComboBox.setForeground(Color.WHITE);
        add(genreComboBox);

        searchGenre = new JButton("Search Genre");
        searchGenre.setBounds(400, 50, 120, 25);
        searchGenre.setBackground(new Color(29, 185, 84));
        searchGenre.setForeground(Color.BLACK);
        add(searchGenre);

        table = new JTable();
        table.setBackground(new Color(18, 18, 18));
        table.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 90, 640, 250);
        add(scrollPane);

        backButton = new JButton("Back to Jukebox");
        backButton.setBounds(530, 350, 130, 25);
        backButton.setBackground(new Color(29, 185, 84));
        backButton.setForeground(Color.BLACK);
        backButton.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
        });
        add(backButton);

        // Button actions
        songButton.addActionListener(e -> searchBy("SongName"));
        singerButton.addActionListener(e -> searchBy("Artist"));
        searchGenre.addActionListener(e -> searchBy("Genre"));

        setLocationRelativeTo(null);
    }

    private void searchBy(String column) {
        try {
            String query = "SELECT * FROM song WHERE " + column + " LIKE ?";
            connection = DriverManager.getConnection(dbURL, "root", "12345");
            preparedStatement = connection.prepareStatement(query);

            if (column.equals("Genre")) {
                preparedStatement.setString(1, "%" + genreComboBox.getSelectedItem().toString() + "%");
            } else {
                preparedStatement.setString(1, "%" + textField.getText() + "%");
            }

            resultSet = preparedStatement.executeQuery();
            table.setModel(DbUtils.resultSetToTableModel(resultSet));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Search failed: " + e.getMessage());
        }
    }
}

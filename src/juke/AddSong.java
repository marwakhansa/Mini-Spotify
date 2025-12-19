package juke;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;

public class AddSong extends javax.swing.JFrame {

    Connection con = null;
    PreparedStatement pst = null;
    static String dbURL = "jdbc:mysql://localhost/userregistration?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    private final Jukebox parent;

    public AddSong(Jukebox parent) {
        this.parent = parent;
        initComponents();
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(18, 18, 18)); // Dark theme
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        javax.swing.JLabel titleLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        javax.swing.JLabel fileLabel = new javax.swing.JLabel();
        javax.swing.JLabel imageLabel = new javax.swing.JLabel();
        genreComboBox = new javax.swing.JComboBox<>();
        songNameField = new javax.swing.JTextField();
        artistNameField = new javax.swing.JTextField();
        durationField = new javax.swing.JTextField();
        filePathField = new javax.swing.JTextField();
        imagePathField = new javax.swing.JTextField();
        browseAudioButton = new javax.swing.JButton();
        browseImageButton = new javax.swing.JButton();
        addSongButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(18, 18, 18));

        titleLabel.setText("Add New Song");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Song Name");
        jLabel1.setForeground(Color.WHITE);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Artist Name");
        jLabel2.setForeground(Color.WHITE);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Genre");
        jLabel3.setForeground(Color.WHITE);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Duration");
        jLabel4.setForeground(Color.WHITE);

        fileLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fileLabel.setText("Audio File");
        fileLabel.setForeground(Color.WHITE);

        imageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageLabel.setText("Image File");
        imageLabel.setForeground(Color.WHITE);

        genreComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
                "Pop", "Blues", "Raggae", "R&B", "Jazz", "Metal (Heavy Metal)", "Rock", "Country", "Rap", "Hip-Hop"
        }));
        genreComboBox.setBackground(new Color(50, 50, 50));
        genreComboBox.setForeground(Color.WHITE);

        songNameField.setBackground(new Color(50, 50, 50));
        songNameField.setForeground(Color.WHITE);

        artistNameField.setBackground(new Color(50, 50, 50));
        artistNameField.setForeground(Color.WHITE);

        durationField.setBackground(new Color(50, 50, 50));
        durationField.setForeground(Color.WHITE);

        filePathField.setBackground(new Color(50, 50, 50));
        filePathField.setForeground(Color.WHITE);

        imagePathField.setBackground(new Color(50, 50, 50));
        imagePathField.setForeground(Color.WHITE);

        browseAudioButton.setText("Browse");
        browseAudioButton.setBackground(new Color(29, 185, 84));
        browseAudioButton.setForeground(Color.BLACK);
        browseAudioButton.addActionListener(evt -> browseFile(filePathField, "mp3"));

        browseImageButton.setText("Browse");
        browseImageButton.setBackground(new Color(29, 185, 84));
        browseImageButton.setForeground(Color.BLACK);
        browseImageButton.addActionListener(evt -> browseFile(imagePathField, "jpg,png"));

        addSongButton.setText("Add Song");
        addSongButton.setBackground(new Color(29, 185, 84));
        addSongButton.setForeground(Color.BLACK);
        addSongButton.addActionListener(evt -> addSongButtonActionPerformed());

        // Layout (adjusted from your .form and .java)
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10)
                        .addComponent(songNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10)
                        .addComponent(artistNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10)
                        .addComponent(genreComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10)
                        .addComponent(durationField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(fileLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10)
                        .addComponent(filePathField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10)
                        .addComponent(browseAudioButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(imageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10)
                        .addComponent(imagePathField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10)
                        .addComponent(browseImageButton))
                    .addComponent(addSongButton, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGap(20)
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(songNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(artistNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(genreComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(durationField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fileLabel)
                    .addComponent(filePathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseAudioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(imageLabel)
                    .addComponent(imagePathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseImageButton))
                .addGap(18, 18, 18)
                .addComponent(addSongButton)
                .addContainerGap(50, Short.MAX_VALUE))
        ;  

        pack();
    }

    private void browseFile(javax.swing.JTextField field, String extensions) {
        JFileChooser chooser = new JFileChooser();
        String desc = extensions.contains("mp3") ? "Audio Files" : "Image Files";
        FileNameExtensionFilter filter = new FileNameExtensionFilter(desc, extensions.split(","));
        chooser.setFileFilter(filter);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            field.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void addSongButtonActionPerformed() {
        try {
            String query = "INSERT INTO song (SongName, Artist, Genre, Duration, file_path, image_path) VALUES (?, ?, ?, ?, ?, ?)";
            con = DriverManager.getConnection(dbURL, "root", "12345");
            pst = con.prepareStatement(query);
            pst.setString(1, songNameField.getText());
            pst.setString(2, artistNameField.getText());
            pst.setString(3, genreComboBox.getSelectedItem().toString());
            pst.setString(4, durationField.getText());
            pst.setString(5, filePathField.getText());
            pst.setString(6, imagePathField.getText());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Song added!");
            parent.loadSongs(); // Refresh parent
            parent.setVisible(true);
            this.dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed: " + e.getMessage());
        }
    }

    private javax.swing.JButton addSongButton;
    private javax.swing.JTextField artistNameField;
    private javax.swing.JButton browseAudioButton;
    private javax.swing.JButton browseImageButton;
    private javax.swing.JTextField durationField;
    private javax.swing.JTextField filePathField;
    private javax.swing.JComboBox<String> genreComboBox;
    private javax.swing.JTextField imagePathField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField songNameField;
    private javax.swing.JLabel fileLabel;
    private javax.swing.JLabel imageLabel;
}
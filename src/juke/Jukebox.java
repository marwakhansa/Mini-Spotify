package juke;

import jaco.mp3.player.MP3Player;
import java.io.File;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Jukebox extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost/userregistration?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "12345";
    private static final Logger LOGGER = Logger.getLogger(Jukebox.class.getName());

    private JTable table;
    private JProgressBar progressBar;
    private JButton playButton, pauseButton, stopButton, nextButton, previousButton, addToPlaylistButton;
    private JLabel nowPlayingText;
    private MP3Player player;
    private List<SongInfo> currentPlaylist = new ArrayList<>();
    private int currentSongIndex = -1;
    private Timer timer;
    private JLabel backgroundLabel; // For song image
    private long currentPosition = 0; // To track position manually
    private final int userId;

    private static class SongInfo {
        int id;
        String name;
        String artist;
        String duration;
        long durationMs;
        String filePath;
        String imagePath;

        public SongInfo(int id, String name, String artist, String duration, String filePath, String imagePath) {
            this.id = id;
            this.name = name;
            this.artist = artist;
            this.duration = duration;
            String[] parts = duration.split(":");
            this.durationMs = Long.parseLong(parts[0]) * 60000 + Long.parseLong(parts[1]) * 1000;
            this.filePath = filePath;
            this.imagePath = imagePath;
        }
    }

    public Jukebox(int userId) {
        this.userId = userId;
        setTitle("Spotify-Music of your choice:) ");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        // Spotify-like dark theme
        getContentPane().setBackground(new Color(18, 18, 18));
        UIManager.put("ProgressBar.foreground", new Color(29, 185, 84)); // Green
        UIManager.put("ProgressBar.background", new Color(50, 50, 50));

        // Background label for song image
        backgroundLabel = new JLabel();
        backgroundLabel.setLayout(new BorderLayout());
        backgroundLabel.setIcon(new ImageIcon("background.jpg")); // Default background (add your image)
        setContentPane(backgroundLabel);

        // Top Panel: Now Playing
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(18, 18, 18));
        nowPlayingText = new JLabel("Pick a song and groove", SwingConstants.CENTER);
        nowPlayingText.setForeground(Color.WHITE);
        nowPlayingText.setFont(new Font("SansSerif", Font.ITALIC, 14));
        topPanel.add(nowPlayingText, BorderLayout.CENTER);
        backgroundLabel.add(topPanel, BorderLayout.NORTH);

        // Center: Song Table
        table = new JTable();
        table.setBackground(new Color(18, 18, 18));
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(29, 185, 84));
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                playSelectedSong();
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(18, 18, 18));
        backgroundLabel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel: Controls
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(new Color(18, 18, 18));

        previousButton = new JButton("Previous");
        previousButton.setBackground(new Color(29, 185, 84));
        previousButton.setForeground(Color.BLACK);
        previousButton.addActionListener(e -> playPreviousSong());
        bottomPanel.add(previousButton);

        playButton = new JButton("Play");
        playButton.setBackground(new Color(29, 185, 84));
        playButton.setForeground(Color.BLACK);
        playButton.addActionListener(e -> playSelectedSong());
        bottomPanel.add(playButton);

        pauseButton = new JButton("Pause");
        pauseButton.setBackground(new Color(29, 185, 84));
        pauseButton.setForeground(Color.BLACK);
        pauseButton.addActionListener(e -> pauseSong());
        bottomPanel.add(pauseButton);

        stopButton = new JButton("Stop");
        stopButton.setBackground(new Color(29, 185, 84));
        stopButton.setForeground(Color.BLACK);
        stopButton.addActionListener(e -> stopSong());
        bottomPanel.add(stopButton);

        nextButton = new JButton("Next");
        nextButton.setBackground(new Color(29, 185, 84));
        nextButton.setForeground(Color.BLACK);
        nextButton.addActionListener(e -> playNextSong());
        bottomPanel.add(nextButton);

        addToPlaylistButton = new JButton("Add to Playlist");
        addToPlaylistButton.setBackground(new Color(29, 185, 84));
        addToPlaylistButton.setForeground(Color.BLACK);
        addToPlaylistButton.addActionListener(e -> addToPlaylist());
        bottomPanel.add(addToPlaylistButton);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(300, 20));
        bottomPanel.add(progressBar);

        backgroundLabel.add(bottomPanel, BorderLayout.SOUTH);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu userMenu = new JMenu("User");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> logout());
        JMenuItem playlistItem = new JMenuItem("Playlist");
        playlistItem.addActionListener(e -> openPlaylist());
        userMenu.add(playlistItem);
        userMenu.add(logoutItem);

        JMenu searchMenu = new JMenu("Search");
        JMenuItem searchSongItem = new JMenuItem("Search Song");
        searchSongItem.addActionListener(e -> openSearchSong());
        searchMenu.add(searchSongItem);

        JMenu songMenu = new JMenu("Song");
        JMenuItem addSongItem = new JMenuItem("Add Song");
        addSongItem.addActionListener(e -> openAddSong());
        JMenuItem deleteSongItem = new JMenuItem("Delete Song");
        deleteSongItem.addActionListener(e -> openDeleteSong());
        songMenu.add(addSongItem);
        songMenu.add(deleteSongItem);

        menuBar.add(userMenu);
        menuBar.add(searchMenu);
        menuBar.add(songMenu);
        setJMenuBar(menuBar);

        loadSongs();
    }

    public void loadSongs() {
        currentPlaylist.clear();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String query = "SELECT id, SongName, Artist, Genre, Duration, file_path, image_path FROM song";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = new DefaultTableModel(new String[]{"Song Name", "Artist", "Genre", "Duration"}, 0);
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("SongName"), rs.getString("Artist"), rs.getString("Genre"), rs.getString("Duration")});
                currentPlaylist.add(new SongInfo(rs.getInt("id"), rs.getString("SongName"), rs.getString("Artist"), rs.getString("Duration"), rs.getString("file_path"), rs.getString("image_path")));
            }
            table.setModel(model);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to load songs", e);
            JOptionPane.showMessageDialog(this, "Failed to load songs: " + e.getMessage());
        }
    }

    private void startPlayback(int index) {
        stopSong(); // Stop any current playback
        currentSongIndex = index;
        SongInfo song = currentPlaylist.get(index);
        nowPlayingText.setText("Now Playing: " + song.name + " by " + song.artist);
        updateBackground(song.imagePath);

        try {
            player = new MP3Player(new File(song.filePath));
            player.play();
            long durationMs = song.durationMs;

            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (player == null || player.isStopped() || player.isPaused()) return;
                    currentPosition += 1000;
                    int progress = (int) ((currentPosition * 100) / durationMs);
                    progressBar.setValue(progress);
                    progressBar.setString(progress + "%");
                    if (currentPosition >= durationMs) {
                        timer.cancel();
                        playNextSong();
                    }
                }
            }, 0, 1000);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Playback error", e);
            JOptionPane.showMessageDialog(this, "Playback failed: " + e.getMessage());
        }
    }

    private void updateBackground(String imagePath) {
        try {
            if (imagePath != null && !imagePath.isEmpty()) {
                ImageIcon icon = new ImageIcon(imagePath);
                Image scaled = icon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                backgroundLabel.setIcon(new ImageIcon(scaled));
            } else {
                backgroundLabel.setIcon(new ImageIcon("background.jpg")); // Default
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to load background image", e);
            backgroundLabel.setIcon(new ImageIcon("background.jpg")); // Fallback
        }
    }

    private void playSelectedSong() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a song first!");
            return;
        }
        startPlayback(row);
    }

    private void playNextSong() {
        if (currentPlaylist.isEmpty()) return;
        int nextIndex = (currentSongIndex + 1) % currentPlaylist.size();
        table.setRowSelectionInterval(nextIndex, nextIndex);
        startPlayback(nextIndex);
    }

    private void playPreviousSong() {
        if (currentPlaylist.isEmpty()) return;
        int prevIndex = currentSongIndex - 1;
        if (prevIndex < 0) prevIndex = currentPlaylist.size() - 1;
        table.setRowSelectionInterval(prevIndex, prevIndex);
        startPlayback(prevIndex);
    }

    private void pauseSong() {
        if (player != null && !player.isPaused()) {
            player.pause();
            pauseButton.setText("Resume");
        } else if (player != null) {
            player.play();
            pauseButton.setText("Pause");
        }
    }

    private void stopSong() {
        if (player != null) {
            player.stop();
            if (timer != null) timer.cancel();
            currentSongIndex = -1;
            nowPlayingText.setText("Stopped");
            progressBar.setValue(0);
            progressBar.setString("0%");
            updateBackground(null); // Reset to default
            currentPosition = 0;
        }
    }

    private void addToPlaylist() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a song first!");
            return;
        }
        int songId = currentPlaylist.get(row).id;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String query = "INSERT INTO playlist (song_id, user_id) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, songId);
            ps.setInt(2, userId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Added to playlist!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to add to playlist", e);
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void logout() {
        this.setVisible(false);
        new Login().setVisible(true);
    }

    private void openPlaylist() {
        this.setVisible(false);
        new Playlist(this, userId).setVisible(true);
    }

    private void openSearchSong() {
        this.setVisible(false);
        new SearchSong(this).setVisible(true);
    }

    private void openAddSong() {
        this.setVisible(false);
        new AddSong(this).setVisible(true);
    }

    private void openDeleteSong() {
        this.setVisible(false);
        new DeleteSong(this).setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to set look and feel", e);
        }
        // Start from Login
        new Login().setVisible(true);
    }
}
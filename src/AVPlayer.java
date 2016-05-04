import Configurations.Settings;
import MediaLoader.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.TimerTask;
import java.util.Timer;

/**
 * Created by omi on 4/24/16.
 */
@SuppressWarnings("serial")
public class AVPlayer extends javax.swing.JFrame {

    private String FRAMES_FILE;
    private String AUDIO_FILE;
    private String SEARCH_FILE;
    private int SEARCH_IMAGE_WIDTH;
    private int SEARCH_IMAGE_HEIGHT;
    private boolean isPlaying;
    private long seekPositionVideo = 0;
    private long seekPositionAudio = 0;

    private AudioPlayer audioPlayer;
    private ImageLoader imageLoader;

    private Timer timer;

    /**
     * Creates new form PlayerFrame
     */
    public AVPlayer(String RGB_FILE, String AUDIO_FILE, String SEARCH_FILE, int searchImageWidth, int searchImageHeight) {
        initComponents();
        this.SEARCH_FILE = SEARCH_FILE;
        this.SEARCH_IMAGE_WIDTH = searchImageWidth;
        this.SEARCH_IMAGE_HEIGHT = searchImageHeight;
        this.FRAMES_FILE = RGB_FILE;
        this.AUDIO_FILE = AUDIO_FILE;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.playButton.setFocusPainted(false);
            this.stopButton.setFocusPainted(false);
            loadResources(RGB_FILE, AUDIO_FILE);
            System.out.println();
            seekBar.setMaximum((int) (audioPlayer.getLength() / 1000000));
            seekBar.setMinimum(0);
            seekBar.addMouseListener(seekBarAdapter());
            setLocationRelativeTo(null);
            setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /*
    Utility seekbar change
     */
    private MouseAdapter seekBarAdapter() {
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {


            }
        };
        return ma;
    }


    /*
       Utility functions to control the video Component
     */
    private void loadResources(String RGB_FILE, String AUDIO_FILE) throws Exception {

        audioPlayer = new AudioPlayer(new FileInputStream(AUDIO_FILE));
        imageLoader = new ImageLoader(RGB_FILE);

    }

    private BufferedImage getFrameImage(byte[] bytes) {
        BufferedImage img = new BufferedImage(Settings.WIDTH, Settings.HEIGHT, BufferedImage.TYPE_INT_RGB);

        //Generate Buffered Image
        int ind = 0;
        for (int y = 0; y < Settings.HEIGHT; y++) {

            for (int x = 0; x < Settings.WIDTH; x++) {

                byte a = 0;
                byte r = bytes[ind];
                byte g = bytes[ind + Settings.PIXELS_PER_FRAME];
                byte b = bytes[ind + Settings.PIXELS_PER_FRAME * 2];

                int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                img.setRGB(x, y, pix);
                ind++;
            }
        }
        return img;
    }

    /*AV controls  to Play, Pause, and stop the video */
    private void play() throws Exception {
        audioPlayer.play();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {

                    byte[] image = imageLoader.getNext();
                    frameContainer.setIcon(new ImageIcon(getFrameImage(image)));
                    seekPositionVideo += 1;

                    //update progress bar
                    seekBar.setValue((int) (seekPositionAudio / 1000));
                    seekPositionAudio = audioPlayer.getPosition() / 1000;
                    long predicted_current_frame = (int) (seekPositionAudio / 66.666);

                    if (seekPositionVideo < predicted_current_frame) {
                        imageLoader.skip((predicted_current_frame - seekPositionVideo) * Settings.BYTES_PER_FRAME);
                        seekPositionVideo += (predicted_current_frame - seekPositionVideo);
                    }

                    if (seekPositionVideo > predicted_current_frame) {
                        Thread.sleep((seekPositionVideo - predicted_current_frame) * Settings.TIMER_INTERVAL);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        this.timer = new Timer();
        this.timer.schedule(timerTask, 0, Settings.TIMER_INTERVAL);
    }

    private void pause() {
        if (this.timer != null) {
            audioPlayer.pause();
            this.timer.cancel();
        }
    }

    private void stop() {

        audioPlayer.stop();
        this.timer.cancel();

        this.frameContainer.setIcon(new ImageIcon());
        this.playButton.setText("PLAY");
        this.isPlaying = false;
        this.imageLoader = new ImageLoader(this.FRAMES_FILE);

        this.seekBar.setValue(0);
        seekPositionVideo = 0;
        seekPositionAudio = 0;
    }

    //USELESS
    public void setFrame(BufferedImage img) {
        this.frameContainer.setIcon(new ImageIcon(img));
    }

    public void setColor(Color color) {
        this.controlContainer.setBackground(color);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        videoContainer = new javax.swing.JPanel();
        frameContainer = new javax.swing.JLabel();
        controlContainer = new javax.swing.JPanel();
        playButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        optionContainer = new javax.swing.JPanel();
        loadOriginalButton = new javax.swing.JButton();
        loadSearchedButton = new javax.swing.JButton();
        loadSummarizedButton = new javax.swing.JButton();
        seekBar = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Player");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        videoContainer.setBackground(new java.awt.Color(255, 255, 255));
        videoContainer.setPreferredSize(new java.awt.Dimension(480, 270));

        frameContainer.setBackground(new java.awt.Color(255, 255, 255));
        frameContainer.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        frameContainer.setBorder(null);
        frameContainer.setFocusable(false);
        frameContainer.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        frameContainer.setIconTextGap(0);

        javax.swing.GroupLayout videoContainerLayout = new javax.swing.GroupLayout(videoContainer);
        videoContainer.setLayout(videoContainerLayout);
        videoContainerLayout.setHorizontalGroup(
                videoContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(frameContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
        );
        videoContainerLayout.setVerticalGroup(
                videoContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(frameContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
        );

        controlContainer.setBackground(new java.awt.Color(50, 50, 50));
        controlContainer.setMaximumSize(new java.awt.Dimension(480, 50));
        controlContainer.setMinimumSize(new java.awt.Dimension(480, 50));

        playButton.setText("PLAY");
        playButton.setPreferredSize(new java.awt.Dimension(80, 30));
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });

        stopButton.setText("STOP");
        stopButton.setPreferredSize(new java.awt.Dimension(80, 30));
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        optionContainer.setBackground(new java.awt.Color(50, 50, 50));
        optionContainer.setMaximumSize(new java.awt.Dimension(480, 50));
        optionContainer.setMinimumSize(new java.awt.Dimension(480, 50));

        loadOriginalButton.setText("ORIGINAL");
        loadOriginalButton.setPreferredSize(new java.awt.Dimension(80, 30));
        loadOriginalButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadOriginalButtonActionPerformed(evt);
            }
        });

        loadSearchedButton.setText("SEARCHED");
        loadSearchedButton.setPreferredSize(new java.awt.Dimension(80, 30));
        loadSearchedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadSearchedButtonActionPerformed(evt);
            }
        });

        loadSummarizedButton.setText("SUMMARIZED");
        loadSummarizedButton.setPreferredSize(new java.awt.Dimension(80, 30));
        loadSummarizedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadSummarizedButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout optionContainerLayout = new javax.swing.GroupLayout(optionContainer);
        optionContainer.setLayout(optionContainerLayout);
        optionContainerLayout.setHorizontalGroup(
                optionContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(optionContainerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(loadOriginalButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(loadSummarizedButton, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(loadSearchedButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        optionContainerLayout.setVerticalGroup(
                optionContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, optionContainerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(optionContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(loadOriginalButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(loadSearchedButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(loadSummarizedButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        javax.swing.GroupLayout controlContainerLayout = new javax.swing.GroupLayout(controlContainer);
        controlContainer.setLayout(controlContainerLayout);
        controlContainerLayout.setHorizontalGroup(
                controlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(controlContainerLayout.createSequentialGroup()
                                .addGap(151, 151, 151)
                                .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, controlContainerLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(optionContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        controlContainerLayout.setVerticalGroup(
                controlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, controlContainerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(controlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addComponent(optionContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0))
        );

        seekBar.setBackground(new java.awt.Color(255, 255, 255));
        seekBar.setBorder(null);
        seekBar.setPreferredSize(new java.awt.Dimension(480, 5));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(controlContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(seekBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(videoContainer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(videoContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(seekBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(controlContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        try {
            if (isPlaying) {
                isPlaying = false;
                playButton.setText("PLAY");
                pause();
            } else {
                isPlaying = true;
                playButton.setText("PAUSE");
                play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        stop();
    }

    private void loadOriginalButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        stop();
        try {
            loadResources(this.FRAMES_FILE, this.AUDIO_FILE);
            seekBar.setMaximum((int) (audioPlayer.getLength() / 1000000));
            play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSearchedButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        stop();
        //call searcher
        Searcher searcher = new Searcher(this.FRAMES_FILE);
        try {
            int frameNumber = searcher.search(this.SEARCH_FILE, this.SEARCH_IMAGE_WIDTH, this.SEARCH_IMAGE_HEIGHT);
            if(frameNumber >= 0){
                System.out.println("Frame found: " + frameNumber);
                searcher.dumpSnippet(frameNumber, this.FRAMES_FILE, this.AUDIO_FILE, "snippet");

                loadResources("snippet.rgb", "snippet.wav");
                seekBar.setMaximum((int) (audioPlayer.getLength() / 1000000));
                play();
            }else {
                System.out.println("Image not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSummarizedButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        stop();

        Summarizer summarizer = new Summarizer(this.FRAMES_FILE, this.AUDIO_FILE);
        summarizer.summarize("summarized");
        try {
            loadResources("summarized.rgb", "summarized.wav");
            seekBar.setMaximum((int) (audioPlayer.getLength() / 1000000));
            play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Variables declaration - do not modify
    private javax.swing.JPanel controlContainer;
    private javax.swing.JLabel frameContainer;
    private javax.swing.JButton loadOriginalButton;
    private javax.swing.JButton loadSearchedButton;
    private javax.swing.JButton loadSummarizedButton;
    private javax.swing.JPanel optionContainer;
    private javax.swing.JButton playButton;
    private javax.swing.JProgressBar seekBar;
    private javax.swing.JButton stopButton;
    private javax.swing.JPanel videoContainer;
    // End of variables declaration

}


package player;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;

public class Player {

	String audFile;
	String vidFile;
	JFrame frame;
	InputStream is;
//BufferedImage img;
	static int width = 480;
	static int height = 270;
	Video videoPlayer;
	Audio audioPlayer;

	JLabel lbIm1;
	public Player(String video, String audio){
		audFile = audio;
		vidFile = video;
	}

	public void loadAudio(){
		audioPlayer = new Audio(audFile);
		audioPlayer.name = "sailesh";

	}

	public void loadVideo(){
		videoPlayer = new Video(vidFile,audioPlayer);
	}

	public void initialize(){

		frame = new JFrame();

		//frame.getContentPane().setLayout(gLayout);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(400, height));
		frame.getContentPane().add(buttonPanel, BorderLayout.EAST);

		JButton playButton = new JButton("PLAY");
		playButton.addActionListener(new CustomActionListener());
		buttonPanel.add(playButton, BorderLayout.NORTH);

		JButton pauseButton = new JButton("PAUSE");
		pauseButton.addActionListener(new CustomActionListener());
		buttonPanel.add(pauseButton, BorderLayout.NORTH);

		JButton stopButton = new JButton("STOP");
		stopButton.addActionListener(new CustomActionListener());
		buttonPanel.add(stopButton, BorderLayout.NORTH);


		JLabel lbText1 = new JLabel("Video: " + vidFile);
		lbText1.setHorizontalAlignment(SwingConstants.LEFT);
		JLabel lbText2 = new JLabel("Audio: " + audFile);
		lbText2.setHorizontalAlignment(SwingConstants.LEFT);
		lbIm1 = new JLabel(new ImageIcon( videoPlayer.img));

		frame.getContentPane().add(lbIm1);

		frame.pack();
		frame.setVisible(true);
	}

	public void play(){
		lbIm1.setIcon(new ImageIcon(videoPlayer.img));
		audioPlayer.play();
		videoPlayer.play(frame);
	}

	public void pause(){
		audioPlayer.pause();
		videoPlayer.pause();
	}

	public void stop(){
		audioPlayer.stop();
		videoPlayer.stop();
		lbIm1.setIcon(null);
	}




	class CustomActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String buttonText = ((JButton) e.getSource()).getText();
			if(buttonText.equals("PLAY")) {
				play();
			}
			else if(buttonText.equals("PAUSE")) {
				pause();
			}
			else if(buttonText.equals("STOP")) {
				stop();
			}
		}
	}

}

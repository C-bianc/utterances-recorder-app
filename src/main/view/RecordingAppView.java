package view;

import javax.swing.*;
import java.awt.*;

public class RecordingAppView {
	private JFrame frame;
	private JPanel leftPanelContent;
	private JPanel mainPanel;
	private JPanel centerPanel;
	private JPanel buttonPanel;
	private JLabel recordingStatusLabel;
	private JButton recordButton;
	private JButton playButton;
	private JButton nextButton;
	private JButton previousButton;
	private JPanel topPanel;
	private JButton loadCsvButton;
	private JButton deleteButton;

	public RecordingAppView() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame("Audio Recording App");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 800);

		frame.setLayout(new BorderLayout());
		frame.add(createLeftPanel(), BorderLayout.WEST);
		frame.add(createMainPanel(), BorderLayout.CENTER);

		frame.setVisible(true);

		recordingStatusLabel = new JLabel();
		recordingStatusLabel.setText(null);
		recordingStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		recordingStatusLabel.setVerticalAlignment(SwingConstants.BOTTOM);
	}

	private JPanel createLeftPanel() {
		JPanel leftPanelContainer = new JPanel();
		leftPanelContainer.setLayout(new BorderLayout());
		leftPanelContainer.setBackground(new Color(240, 240, 240));

		JButton toggleButton = createButton("Toggle Panel", Color.gray, new Dimension(150, 50));
		toggleButton.addActionListener(e -> {
			leftPanelContent.setVisible(!leftPanelContent.isVisible());
		});

		leftPanelContainer.add(toggleButton, BorderLayout.NORTH);

		leftPanelContent = new JPanel();
		leftPanelContent.setLayout(new BoxLayout(leftPanelContent, BoxLayout.Y_AXIS));
		leftPanelContent.setBackground(new Color(240, 240, 240));

		JList<String> utterancesList = new JList<>();
		utterancesList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		utterancesList.setBackground(Color.WHITE);
		utterancesList.setForeground(Color.DARK_GRAY);
		leftPanelContent.add(new JScrollPane(utterancesList));

		leftPanelContainer.add(leftPanelContent, BorderLayout.CENTER);

		return leftPanelContainer;
	}

	private JPanel createMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(new Color(245, 245, 245));

		mainPanel.add(createCenterPanel(), BorderLayout.CENTER);

		buttonPanel = createButtonPanel();
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		topPanel = createTopPanel();
		mainPanel.add(topPanel, BorderLayout.NORTH);

		return mainPanel;
	}

	private JPanel createCenterPanel() {
		centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		centerPanel.setBackground(new Color(245, 245, 245));

		JLabel messageLabel = new JLabel("Please load data", SwingConstants.CENTER);
		messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
		messageLabel.setForeground(Color.DARK_GRAY);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		centerPanel.add(messageLabel, gbc);

		loadCsvButton = createButton("Load CSV", new Color(100, 150, 255), new Dimension(150, 50));
		gbc.gridy = 1;
		centerPanel.add(loadCsvButton, gbc);

		return centerPanel;
	}

	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setBackground(new Color(245, 245, 245));

		recordButton = createButton("Record", new Color(70, 200, 120), new Dimension(150, 50));

		playButton = createButton("Play", new Color(70, 200, 120), new Dimension(150, 50));
		playButton.setVisible(false);

		deleteButton = createButton("Delete", new Color(255, 80, 80), new Dimension(150, 50));
		deleteButton.setVisible(false);

		buttonPanel.add(playButton);
		buttonPanel.add(recordButton);
		buttonPanel.add(deleteButton);

		buttonPanel.setVisible(false);

		return buttonPanel;
	}

	private JPanel createTopPanel() {
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());

		nextButton = createButton("Next", new Color(100, 150, 255), new Dimension(150, 50));
		previousButton = createButton("Previous", new Color(100, 150, 255), new Dimension(150, 50));

		topPanel.add(previousButton, BorderLayout.WEST);
		topPanel.add(nextButton, BorderLayout.EAST);
		topPanel.add(new JPanel(), BorderLayout.CENTER);
		topPanel.setVisible(false);

		return topPanel;
	}

	public JButton createButton(String text, Color bgColor, Dimension size) {
		JButton button = new JButton(text);
		button.setFont(new Font("Segoe UI", Font.BOLD, 18));
		button.setBackground(bgColor);
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setPreferredSize(size);

		button.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(bgColor, 2),
			BorderFactory.createEmptyBorder(10, 20, 10, 20)
		));

		button.setContentAreaFilled(false);
		button.setOpaque(true);
		button.setBorder(BorderFactory.createLineBorder(bgColor, 2));

		button.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(bgColor, 2),
			BorderFactory.createEmptyBorder(10, 20, 10, 20)
		));
		return button;
	}

	public void addMessage(String text, Color color){
		recordingStatusLabel.setText(text);
		recordingStatusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 18));
		recordingStatusLabel.setForeground(color);
		mainPanel.add(recordingStatusLabel, BorderLayout.CENTER);
	}

	public JFrame getFrame() {
		return frame;
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}

	public JButton getRecordButton() {
		return recordButton;
	}

	public JButton getPlayButton() {
		return playButton;
	}

	public JButton getNextButton(){
		return nextButton;
	}

	public JButton getPreviousButton(){
		return previousButton;
	}

	public JButton getDeleteButton(){
		return deleteButton;
	}

	public JButton getLoadCsvButton(){
		return loadCsvButton;
	}

	public JLabel getRecordingStatusLabel() {
		return recordingStatusLabel;
	}

	public JPanel getButtonPanel() {
		return buttonPanel;
	}

	public JPanel getTopPanel() {
		return topPanel;
	}
	public JPanel getCenterPanel() {
		return centerPanel;
	}
	public JPanel getLeftPanelContent(){
		return leftPanelContent;
	}
}

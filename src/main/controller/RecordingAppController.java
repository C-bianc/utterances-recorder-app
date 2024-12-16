package controller;

import model.Utterance;
import view.RecordingAppView;
import utils.CsvManager;
import utils.AudioRecorder;
import utils.AudioFileManager;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RecordingAppController {
	private final RecordingAppView view;
	private final CsvManager csvManager;
	private final AudioRecorder audioRecorder;
	private final AudioFileManager audioFileManager;
	private List<Utterance> utterances;
	private int currentUtteranceIndex;
	private Utterance currentUtterance;
	private String currentFile;
	private final File recordingsDir;
	private AtomicBoolean isRecording = new AtomicBoolean(false);

	public RecordingAppController(RecordingAppView view) {
		this.view = view;
		this.csvManager = new CsvManager();
		this.audioRecorder = new AudioRecorder();
		this.audioFileManager = new AudioFileManager(csvManager);
		this.recordingsDir = new File("recordings");

		if (!recordingsDir.exists()) {
			recordingsDir.mkdirs();
		}

		initialize();
	}

	private void initialize() {
		view.getRecordButton().addActionListener(e -> {
			if (isRecording.get()) {
				stopRecording();
			}
			else {
				startRecording();
			}
		});

		view.getPlayButton().addActionListener(e -> {
			try {
				audioFileManager.playAudio(currentFile);
			} catch (LineUnavailableException | IOException ex) {
				throw new RuntimeException(ex);
			}
		});

		view.getNextButton().addActionListener(e -> loadNextUtterance());
		view.getPreviousButton().addActionListener(e -> loadPreviousUtterance());

		view.getLoadCsvButton().addActionListener(e -> loadCsvFileUsingFileChooser());

		view.getDeleteButton().addActionListener(e -> {
			audioFileManager.deleteAudio(currentFile, currentUtterance);
			view.addMessage("Audio for this utterance deleted.", new Color(60, 190, 150));
			view.getDeleteButton().setVisible(false);
			view.getPlayButton().setVisible(false);
		});
	}

	private void startRecording() {
		isRecording.set(true);
		try {
			File audioFile = new File(currentFile);

			if (audioFile.exists()) {
				view.addMessage("Audio file already exists. Delete the current one first.", new Color(160, 10, 10));
				view.getFrame().revalidate();
				view.getFrame().repaint();
				return;
			}

			new Thread(() -> {
				try {
					audioRecorder.recordAudio(currentFile, isRecording);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}).start();


			view.getRecordButton().setText("Stop");
			view.addMessage("Recording...", Color.gray);
			view.getFrame().revalidate();
			view.getFrame().repaint();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void stopRecording() {
		try {
			isRecording.set(false);

			view.getRecordButton().setText("Record");
			view.addMessage("Recording stopped. You can play the audio.", new Color(60, 190, 150));
			view.getPlayButton().setVisible(true);
			view.getDeleteButton().setVisible(true);

			// mark as done
			csvManager.updateUtteranceStatus(currentUtterance.getText(), "done");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void loadCsvFileUsingFileChooser() {
		utterances = csvManager.getUnrecordedUtterances();
		currentUtteranceIndex = 0;
		displayCurrentUtterance();
	}

	private void displayCurrentUtterance() {
		view.getMainPanel().removeAll();

		if (utterances.isEmpty()) {
			view.getCenterPanel().setVisible(true);
		}
		else {
			currentUtterance = utterances.get(currentUtteranceIndex);
			currentFile = recordingsDir + File.separator + CsvManager.LANGUAGE + "_" + currentUtterance.getId() + "_" + CsvManager.SPEAKER_ID + ".wav";
			File file = new File(currentFile);

			JLabel utteranceLabel = new JLabel(currentUtterance.getText(), SwingConstants.CENTER);
			utteranceLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
			utteranceLabel.setForeground(Color.DARK_GRAY);
			view.getMainPanel().add(utteranceLabel, BorderLayout.CENTER);
			view.getMainPanel().add(view.getButtonPanel(), BorderLayout.SOUTH);
			view.getMainPanel().add(view.getTopPanel(), BorderLayout.NORTH);

			view.getButtonPanel().setVisible(true);
			view.getTopPanel().setVisible(true);

			if (!isRecording.get()) {
				if (file.exists()) {
					view.getPlayButton().setVisible(true);
					view.getDeleteButton().setVisible(true);
				}
				else {
					view.getPlayButton().setVisible(false);
					view.getDeleteButton().setVisible(false);
				}
			}

			if (isRecording.get()) {
				if (view.getRecordingStatusLabel() == null) {
					view.addMessage("Recording started...", new Color(70, 160, 100));
				}
			}

		}

		view.getFrame().revalidate();
		view.getFrame().repaint();
	}

	private void loadNextUtterance() {
		currentUtteranceIndex++;
		if (currentUtteranceIndex < utterances.size()) {
			displayCurrentUtterance();
		}
		else {
			JOptionPane.showMessageDialog(view.getFrame(), "Cannot go further.");
			currentUtteranceIndex = utterances.size() - 1;
		}
	}

	private void loadPreviousUtterance() {
		currentUtteranceIndex--;
		if (currentUtteranceIndex > -1) {
			displayCurrentUtterance();
		}
		else {
			JOptionPane.showMessageDialog(view.getFrame(), "Cannot go further.");
			currentUtteranceIndex = 0;
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			RecordingAppView view = new RecordingAppView();
			new RecordingAppController(view);
		});
	}
}

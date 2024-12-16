package utils;

import model.Utterance;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioFileManager {
	private final CsvManager csvManager;

	public AudioFileManager(CsvManager csvManager) {
		this.csvManager = csvManager;
	}

	public void playAudio(String filePath) throws IOException, LineUnavailableException {
		try {
			System.out.println("Playing the recorded audio...");
			AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filePath));
			AudioFormat format = stream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip clip = (Clip) AudioSystem.getLine(info);

			clip.open(stream);
			clip.addLineListener(event -> {
				if (event.getType() == LineEvent.Type.STOP) {
					clip.close();
					try {
						stream.close();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			});

			clip.start();

			// wait
			while (clip.isRunning()) {
				Thread.sleep(100);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteAudio(String filePath, Utterance currentUtterance) {
		File audioFile = new File(filePath);
		if (audioFile.exists()) {
			audioFile.delete();

			csvManager.updateUtteranceStatus(currentUtterance.getText(), "not_done");
			System.out.println("Audio file deleted and status updated to not_done.");
		}
	}
}

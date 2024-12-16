package utils;

import javax.sound.sampled.*;
import java.io.*;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.math3.transform.*;
import org.apache.commons.math3.complex.Complex;

public class AudioRecorder {

	private static final int SAMPLE_RATE = 16000;
	private static final int BUFFER_SIZE = 4096;
	private static final double SILENCE_DURATION_MS = 0.5; // 0.5 seconds
	private static final int FREQUENCY_THRESHOLD = 80; // frequency threshold in Hz (below average human voice range)

	public void recordAudio(String filePath, AtomicBoolean isRecording) throws LineUnavailableException, IOException, InterruptedException {
		AudioFormat format = createAudioFormat();
		TargetDataLine line = openAudioLine(format);

		byte[] buffer = new byte[BUFFER_SIZE];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		CountDownLatch latch = new CountDownLatch(1);

		try {
			captureAudio(line, buffer, out, isRecording, latch);
		} finally {
			System.out.println("Stopped recording.");
			closeAudioLine(line, latch);
		}


		byte[] audioData = filterFrequencies(out.toByteArray(), format);
		writeToWavFile(audioData, format, filePath);
	}

	private AudioFormat createAudioFormat() {
		return new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
	}

	private TargetDataLine openAudioLine(AudioFormat format) throws LineUnavailableException {
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
		line.open(format);
		line.start();
		return line;
	}

	private void captureAudio(TargetDataLine line, byte[] buffer, ByteArrayOutputStream out, AtomicBoolean isRecording, CountDownLatch latch) {
		try {
			int bytesRead;
			while (isRecording.get() && (bytesRead = line.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			System.out.println("Finished capturing audio.");
		} catch (Exception e) {
			System.err.println("Error capturing audio: " + e.getMessage());
			throw new RuntimeException("Error capturing audio", e);
		} finally {
			latch.countDown();
		}
	}

	private void closeAudioLine(TargetDataLine line, CountDownLatch latch) {
		line.stop();
		line.close();
		System.out.println("Audio line closed.");
	}

	private byte[] filterFrequencies(byte[] audioData, AudioFormat format) {
		int frameSize = format.getFrameSize();
		int numSamples = audioData.length / frameSize;
		double[] audioSamples = new double[numSamples];

		// convert byte array to double array
		for (int i = 0; i < numSamples; i++) {
			int byteIndex = i * frameSize;
			short sample = (short) ((audioData[byteIndex + 1] << 8) | (audioData[byteIndex] & 0xff));
			audioSamples[i] = sample;
		}

		// pad the audio data to the next power of 2
		int paddedLength = getNextPowerOfTwo(numSamples);
		double[] paddedAudioSamples = new double[paddedLength];
		System.arraycopy(audioSamples, 0, paddedAudioSamples, 0, numSamples);

		// perform FFT
		FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
		Complex[] complexSamples = fft.transform(paddedAudioSamples, TransformType.FORWARD);

		// filter out frequencies below the threshold

		for (int i = 0; i < complexSamples.length; i++) {
			double frequency = (double) (i * SAMPLE_RATE) / complexSamples.length;
			if (frequency < FREQUENCY_THRESHOLD) {
				complexSamples[i] = Complex.ZERO;
			}
		}

		// Inverse FFT
		Complex[] filteredSamples = fft.transform(complexSamples, TransformType.INVERSE);
		Complex[] trimmedFilteredSamples = Arrays.copyOfRange(filteredSamples, 0, audioSamples.length);

		ByteArrayOutputStream finalOut = getByteArrayOutputStream(format, trimmedFilteredSamples);

		return finalOut.toByteArray();
	}

	private static ByteArrayOutputStream getByteArrayOutputStream(AudioFormat format, Complex[] filteredSamples) {
		ByteArrayOutputStream filteredOut = new ByteArrayOutputStream();
		for (Complex sample : filteredSamples) {
			short shortSample = (short) sample.getReal();
			filteredOut.write((byte) (shortSample & 0xff));
			filteredOut.write((byte) ((shortSample >> 8) & 0xff));
		}

		byte[] filteredAudioData = filteredOut.toByteArray();

		ByteArrayOutputStream finalOut = new ByteArrayOutputStream();
		finalOut.write(filteredAudioData, 0, filteredAudioData.length);
		return finalOut;
	}

	private int getNextPowerOfTwo(int number) {
		int power = 1;
		while (power < number) {
			power <<= 1;
		}
		return power;
	}

	private void writeToWavFile(byte[] audioData, AudioFormat format, String filePath) throws IOException {
		try (InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
			 AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream, format, audioData.length / format.getFrameSize())) {
			AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(filePath));
			System.out.println("Audio file saved to: " + filePath);

		} catch (IOException e) {
			System.err.println("Error writing audio file: " + e.getMessage());
			throw e;
		}
	}
}

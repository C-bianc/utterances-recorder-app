package utils;

import model.Utterance;
import org.apache.commons.csv.*;
import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvManager {

	public static final String SPEAKER_ID = "speaker_3";
	public static final String LANGUAGE = "fr-BE";
	private static final String UTTERANCE_COLUMN = "translated";
	private static final String DATA_DIR = "data" + File.separator;
	private static final String FILENAME = "speaker_3_data";
	private static final String FILEPATH = DATA_DIR + FILENAME + ".csv";

	public List<Utterance> getUnrecordedUtterances() {
		List<Utterance> utterances = new ArrayList<>();
		try (Reader reader = new FileReader(FILEPATH);
			 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

			for (CSVRecord record : csvParser) {
				int id = Integer.parseInt(record.get("id"));
				String text = record.get(UTTERANCE_COLUMN);
				try {
					String status = record.get("Status");
					if ("not_done".equals(status) || status.isEmpty()) {
						utterances.add(new Utterance(id, text, status));
					}
				} catch (Exception ignored) {
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return utterances;
	}

	public void updateUtteranceStatus(String utteranceText, String newStatus) {
		List<CSVRecord> records = new ArrayList<>();
		try (CSVParser parser = new CSVParser(new FileReader(FILEPATH), CSVFormat.DEFAULT.withHeader())) {
			records.addAll(parser.getRecords());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		if (records.isEmpty()) {
			System.out.println("The CSV file is empty or does not contain any records.");
			return;
		}

		// add col "Status" if it does not exist
		List<String> headers = new ArrayList<>(records.get(0).toMap().keySet());
		if (!headers.contains("Status")) {
			headers.add("Status");
		}

		Path tempFilePath = Paths.get(DATA_DIR, FILENAME + ".tmp");

		try (FileWriter out = new FileWriter(tempFilePath.toFile());
			 CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers.toArray(new String[0])))) {
			for (CSVRecord record : records) {
				List<String> updatedRecord = new ArrayList<>();

				updateStatusColumn(utteranceText, newStatus, record, headers, updatedRecord);

				printer.printRecord(updatedRecord);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		// replace original file with temp file
		try {

			Files.move(tempFilePath, Paths.get(FILEPATH), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void updateStatusColumn(String utteranceText, String newStatus, CSVRecord record, List<String> headers, List<String> updatedRecord) {
		for (String header : headers) {
			if (header.equals("Status")) {
				if (record.get(UTTERANCE_COLUMN).equals(utteranceText)) {
					updatedRecord.add(newStatus);
				}
			}
			else {
				updatedRecord.add(record.get(header));
			}
		}
	}

}

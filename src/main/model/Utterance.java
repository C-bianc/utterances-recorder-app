package model;

public class Utterance {

	private int id;
	private String text;
	private String status;

	public Utterance(int id, String text, String status) {
		this.id = id;
		this.text = text;
		this.status = status;
	}

	public String getText() {
		return text;
	}

	public String getStatus() {
		return status;
	}

	public int getId() {
		return id;
	}
}

package kemukupu;

// Results data type for the ScoreboardController class
public class Results {

	private int score;
	private String topic;
	private String date;

	public Results(int score, String topic, String date) {
		this.score = score;
		this.topic = topic;
		this.date = date;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
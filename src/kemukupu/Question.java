package kemukupu;

// Question data type for the TableView in the ResultsController class
public class Question {

	private String word;
	private String answer;
	private boolean isCorrect;

	public Question(String word, String answer, boolean isCorrect) {
		this.word = word;
		this.answer = answer;
		this.isCorrect = isCorrect;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

}

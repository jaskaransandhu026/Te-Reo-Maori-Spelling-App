package kemukupu;

import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.event.EventHandler;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

//this class contains the logic for the games and practise module
public class QuizController {
	@FXML
	private Label speedLabel;
	@FXML
	private Slider speedSlider;
	@FXML
	private TextField responseTextField;
	@FXML
	private Label feedbackLabel;
	@FXML
	private Label hintLabel;
	@FXML
	private Button repeatButton;
	@FXML
	private Button dontKnowButton;
	@FXML
	private Label scoreLabel;
	@FXML
	private Label timerLabel;
	@FXML
	private Button enterButton;
	@FXML
	private Button skipButton;
	@FXML
	private ImageView topicImageView;
	@FXML
	private Rectangle rectangle1, rectangle2, rectangle3, rectangle4, rectangle5;
	@FXML
	private Label timesUpLabel;
	@FXML
	private Label scoreIncrementLabel;
	@FXML
	private Button aButton;
	@FXML
	private Button eButton;
	@FXML
	private Button iButton;
	@FXML
	private Button oButton;
	@FXML
	private Button uButton;

	private String topic;
	private ArrayList<String> words;
	private int score = 0;
	private int currentWordNumber = -1;
	private boolean isFirstAttempt;
	private double speed = 1;
	private ArrayList<Rectangle> rectangles;
	private String red = "#FF513F";
	private String green = "#69D642";
	private int speedSetting;
	private boolean isGame;
	private Image topicImage;
	private int caretPos = 0;
	ObservableList<Question> data = FXCollections.observableArrayList(new Question("", "", false),
			new Question("", "", false), new Question("", "", false), new Question("", "", false),
			new Question("", " ", false));
	private boolean isRunning;
	private Timeline timeline = new Timeline();
	private int counterTime;
	private int maxTime;
	private long startTime;
	private long stopTime;
	private long answerTime;

	// initialise quiz scene for a given topic
	public void start(String topic, Image topicImage) {
		this.topic = topic;
		this.topicImage = topicImage;
		topicImageView.setImage(topicImage);
		setWords();
		testNextWord();

		if (!isGame) {
			timerLabel.setVisible(false);
			scoreLabel.setVisible(false);
			skipButton.setVisible(true);
		}
	}

	public void setGame(boolean isGame) { this.isGame = isGame; }

	// setup scene elements
	@FXML
	public void initialize() {
		Platform.runLater(() -> responseTextField.requestFocus());
		
		// set up progress bar
		rectangles = new ArrayList<Rectangle>();
		rectangles.add(rectangle1);
		rectangles.add(rectangle2);
		rectangles.add(rectangle3);
		rectangles.add(rectangle4);
		rectangles.add(rectangle5);

		speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				
				// Set slider position to a discrete position
				String sliderSpeedName;				
				speedSetting = (int) Math.round(speedSlider.getValue());
				speedSlider.valueProperty().set(speedSetting);

				switch (speedSetting) {
				case 0:
					speed = 2;
					sliderSpeedName = "Very Slow";
					break;
				case 1:
					speed = 1.5;
					sliderSpeedName = "Slow";
					break;
				case 3:
					speed = 0.67;
					sliderSpeedName = "Fast";
					break;
				case 4:
					speed = 0.5;
					sliderSpeedName = "Very Fast";
					break;
				default:
					speed = 1;
					sliderSpeedName = "Normal";
				}

				// Update speed label
				speedLabel.setText("Speed: " + sliderSpeedName);
				responseTextField.requestFocus();
				responseTextField.positionCaret(caretPos);
			}
		});
		// get cursor position for macron buttons
		responseTextField.caretPositionProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				if (responseTextField.isFocused()) {
					caretPos = responseTextField.getCaretPosition();
				}
			}
		});
	}

	// pick 5 random words from the appropriate topic list
	private void setWords() {
		try {
			String file = "topics/" + topic;

			// pick up to 5 random words
			String cmd = "cat " + file + " | wc -l";
			int listLength = Integer.parseInt(Commands.bash(cmd).get(0));
			listLength++; // to make up for leading new line removed
			cmd = "shuf -i 1-" + listLength + " -n 5";
			ArrayList<String> wordNumbers = Commands.bash(cmd);

			words = new ArrayList<String>();

			// retrieve and save words
			for (String n : wordNumbers) {
				cmd = "sed '" + n + "q;d' " + file;
				String word = Commands.bash(cmd).get(0);
				words.add(word);
			}
		} catch (Exception e) { }
	}

	// move onto the next word (if there is a next word)
	public void testNextWord() {
		if (isGame) {
			timesUpLabel.setVisible(false);
			scoreIncrementLabel.setVisible(false);
		}
		responseTextField.clear();
		caretPos = 0;

		// no more words to test
		if (currentWordNumber >= words.size() - 1) {
			viewResults();
		} else {
			// move onto next word
			enableScene(true);
			currentWordNumber++;
			isFirstAttempt = true;
			setUnderscores("underscores");
			if (isGame) startTimer();
			Commands.speak(words.get(currentWordNumber), speed);
		}
	}

	// submit and handle user's response
	@FXML
	public void actionEnter() {
		calcAnswerTime();

		// collect response and set up scene for feedback
		responseTextField.requestFocus();
		String response = responseTextField.getText();
		responseTextField.clear();
		enableScene(false);

		// check and save response
		boolean isCorrect = response.toLowerCase().trim().equals(words.get(currentWordNumber).toLowerCase().trim());
		data.set(currentWordNumber, new Question(words.get(currentWordNumber), response, isCorrect));

		// any attempt and right
		if (isCorrect) {
			if (isGame) updateScore();
			showFeedback(true);
			updateProgress(true);
			setUnderscores("answer");
			pauseAndTestNextWord();

			// move onto second word
		} else if (!isFirstAttempt || isGame) {
			updateProgress(false);
			setUnderscores("answer");
			showFeedback(false);

			if (isGame) setScoreIncrementLabel(0, red);
			pauseAndTestNextWord();

			// second attempt
		} else {
			enableScene(true);
			setUnderscores("hint");
			showFeedback(false);
			isFirstAttempt = false;
			Commands.speak(words.get(currentWordNumber), speed);
		}
	}

	// update score for a correct answer based on user response time
	public void updateScore() {
		int increment = Math.round((maxTime * 1000 - answerTime) / words.get(currentWordNumber).length() / 8);
		
		// set up a minimum increment
		if (increment <= 5) increment = 5;
		
		score += increment;
		setScoreIncrementLabel(increment, green);
	}

	// show how much the score has been incremented by
	public void setScoreIncrementLabel(int increment, String colour) {
		scoreIncrementLabel.setText("+" + increment);
		scoreIncrementLabel.setStyle("-fx-text-fill:" + colour + ";");
		scoreIncrementLabel.setVisible(true);
	}

	// leave answer up before a bit before moving onto next word
	public void pauseAndTestNextWord() {
		PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
		pause.setOnFinished(e -> {
			testNextWord();
		});
		pause.play();
	}

	// colour the next progress bar rectangle appropriately and update score
	public void updateProgress(boolean isCorrect) {
		String colour = red;
		if (isCorrect) colour = green;
		rectangles.get(currentWordNumber).setFill(Color.web(colour));
		scoreLabel.setText("Score: " + score);
	}

	// tell the user whether their response was correct/incorrect and update progress bar
	public void showFeedback(boolean isCorrect) {

		// determine colours and text to be displayed
		String colour = red;
		String feedbackText = "Incorrect";

		if (isCorrect) {
			colour = green;
			feedbackText = "Correct";
		}

		// show feedback
		feedbackLabel.setText(feedbackText);
		responseTextField.setStyle("-fx-focus-color: " + colour + ";");
		responseTextField.setStyle("-fx-text-box-border: " + colour + ";");
		feedbackLabel.setTextFill(Color.web(colour));

		// leave feedback on for 1s
		PauseTransition pause = new PauseTransition(Duration.seconds(1));
		pause.setOnFinished(e -> {

			// reset to default
			responseTextField.setStyle("-fx-focus-color: transparent;");
			responseTextField.setStyle("-fx-text-box-border: transparent;");
			feedbackLabel.setText("");
		});
		pause.play();
	}

	// speak word again
	@FXML
	public void actionRepeat(ActionEvent event) {
		Commands.speak(words.get(currentWordNumber), speed);
		responseTextField.requestFocus();
		responseTextField.positionCaret(caretPos);
	}

	// set label to just underscores, hint or the answer
	public void setUnderscores(String instruction) {
		String labelText = "";
		String word = words.get(currentWordNumber);
		String answer = word.replace("", " ").trim();

		// replace all the letters and macrons with underscores separated by spaces
		if (instruction == "underscores") {
			labelText = answer.replaceAll("[A-Za-z0-9]", "_").replaceAll("[āēīōūĀĒĪŌŪ]", "_");

		} else if (instruction == "hint") {
			labelText = answer.replaceAll("[A-Za-z0-9]", "_").replaceAll("[āēīōūĀĒĪŌŪ]", "_");

			// replace every other underscore with the correct letter
			StringBuilder labelSB = new StringBuilder(labelText);
			for (int i = 2; i < labelText.length(); i += 4) {
				labelSB.setCharAt(i, answer.charAt(i));
			}
			labelText = labelSB.toString();

		} else if (instruction == "answer") labelText = answer;
		hintLabel.setText(labelText);
	}

	// enable/disable all interactables on scene
	public void enableScene(boolean enable) {
		aButton.setDisable(!enable);
		eButton.setDisable(!enable);
		iButton.setDisable(!enable);
		oButton.setDisable(!enable);
		uButton.setDisable(!enable);
		repeatButton.setDisable(!enable);
		enterButton.setDisable(!enable);
		responseTextField.setDisable(!enable);
		if (!isGame) skipButton.setDisable(!enable);
		if (enable) responseTextField.requestFocus();
	}

	// setup and start new timer
	public void startTimer() {
		// when timer is first set up
		if (isRunning == false) {
			KeyFrame keyframe = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// decrement timer label every 1s
					counterTime--;

					// handle times up
					if (counterTime <= 0) {
						timesUpLabel.setVisible(true);
						timeline.stop();
						actionEnter();
					}
					timerLabel.setText(Integer.toString(counterTime));
				}
			});
			// start timer
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.getKeyFrames().add(keyframe);
			timeline.playFromStart();
			isRunning = true;
		}
		resetTimer();
		timeline.play();
		startTime = System.currentTimeMillis();
	}

	// stop and set up timer with new max time
	public void resetTimer() {
		timeline.stop();
		maxTime = (int) Math.round(words.get(currentWordNumber).length() * 1.5);
		maxTime += 6; // allow extra time for speech

		counterTime = maxTime;
		timerLabel.setText(Integer.toString(counterTime));
	}

	// calculate answer time
	public void calcAnswerTime() {
		if (isGame) {
			timeline.pause();
			stopTime = System.currentTimeMillis();
			answerTime = stopTime - startTime;
		}
	}

	// move from quiz to results scene
	public void viewResults() {
		try {
			timeline.stop();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/results.fxml"));
			Parent root = loader.load();

			// pass score into results controller
			ResultsController contr = loader.getController();

			// setup fields and results table
			contr.start(score, topic.replaceAll("_", " ").substring(3), topicImage, data);
			contr.setGame(isGame);
			contr.setUpResults();
			contr.setUpScoreboardData();
			Stage stage = (Stage) ((Node) responseTextField).getScene().getWindow();
			Scene scene = new Scene(root);

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) { }
	}

	// return to home from quiz
	public void actionHomeQuiz(ActionEvent event) {
		timeline.stop();
		ToolBarController contr = new ToolBarController();
		contr.actionHome(event);
	}

	// quit application from quiz
	public void actionQuitQuiz(ActionEvent event) {
		ToolBarController contr = new ToolBarController();
		contr.actionQuit(event);
		responseTextField.requestFocus();
		responseTextField.positionCaret(caretPos);
	}

	// Allows user to skip word in the practice module
	public void actionSkip(ActionEvent event) {
		enableScene(false);
		isFirstAttempt = false;
		actionEnter();
	}

	// quit application from quiz
	public void actionHelpQuiz(ActionEvent event) {
		ToolBarController contr = new ToolBarController();
		if (isGame) {
			contr.setSceneName("quiz_scene_game");
		} else {
			contr.setSceneName("quiz_scene_practise");
		}
		contr.actionHelp(event);
		responseTextField.requestFocus();
		responseTextField.positionCaret(caretPos);
	}

	// Macron action events
	public void actionA(ActionEvent event) { appendMacron("ā"); }
	public void actionE(ActionEvent event) { appendMacron("ē"); }
	public void actionI(ActionEvent event) { appendMacron("ī"); }
	public void actionO(ActionEvent event) { appendMacron("ō"); }
	public void actionU(ActionEvent event) { appendMacron("ū"); }	
	
	// Insert macron at cursor position
	public void appendMacron(String letter) {
		String response = responseTextField.getText();
		response = response.substring(0, caretPos) + letter + response.substring(caretPos);
		responseTextField.setText(response);
		responseTextField.requestFocus();
		responseTextField.positionCaret(caretPos + 1);
	}
}
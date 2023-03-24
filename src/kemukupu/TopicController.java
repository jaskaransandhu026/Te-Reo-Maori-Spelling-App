package kemukupu;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

// controller class for the topic selection screen
public class TopicController {

	@FXML
	private ComboBox<String> topicsComboBox;
	@FXML
	private Label selectTopicLabel;
	@FXML
	private ImageView topicImageView;
	@FXML
	private ToolBarController toolBarController;

	private boolean isGame;

	private Image topicImage;

	@FXML
	public void initialize() {
		toolBarController.setSceneName("topic_scene");
		setOptions();
	}

	public void setGame(boolean isGame) {
		this.isGame = isGame;
	}

	// change preview image
	public void actionPickTopic(ActionEvent event) {
		try {
			String currentTopic = topicsComboBox.getSelectionModel().getSelectedItem().replaceAll(" ", "_")
					.toLowerCase();
			topicImage = new Image(getClass().getResource("/images/" + currentTopic + ".png").toURI().toString());
			topicImageView.setImage(topicImage);
		} catch (Exception e) {
		}
	}

	// find and set topics options from file system
	public void setOptions() {
		topicsComboBox.getItems().clear();
		String cmd = "ls topics";
		List<String> topics = Commands.bash(cmd);

		// replace underscores in topic names with spaces for readability
		for (int i = 0; i < topics.size(); i++) {
			topics.set(i, topics.get(i).substring(3).replaceAll("_", " "));
		}

		ObservableList<String> topicsOL = FXCollections.observableList(topics);
		topicsComboBox.getItems().clear();
		topicsComboBox.getItems().addAll(topicsOL);
	}
	
	// set selected topic from previous attempt
	public void setTopicSelected(String topic){		
		try {
			topicsComboBox.setValue(topic);
			topicImage = new Image(getClass().getResource("/images/" + topic.toLowerCase().replaceAll(" ", "_") + ".png").toURI().toString());
			topicImageView.setImage(topicImage);
		} catch (Exception e) {
		}
	}

	// move from topics selection to quiz
	public void actionStart(ActionEvent event) {
		if (!topicsComboBox.getSelectionModel().isEmpty()) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/quiz.fxml"));
				Parent root = loader.load();

				// first, setup the quiz as a practice quiz or game
				// then, pass in the topic and topic image into the controller fields
				QuizController contr = loader.getController();
				contr.setGame(isGame);
				String topic = topicsComboBox.getSelectionModel().getSelectedItem().replaceAll(" ", "_");
				String cmd = "ls ./topics | grep ." + topic;
				topic = Commands.bash(cmd).get(0);
				contr.start(topic, topicImage);

				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				stage.show();
			} catch (Exception e) {
			}
		} else {
			// show error message
			selectTopicLabel.setVisible(true);
		}
	}

}

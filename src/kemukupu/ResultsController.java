package kemukupu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.Arrays;

//controller class for the results screen, including the summary table
public class ResultsController {

	@FXML
	private Label scoreLabel;
	@FXML
	private Label scoreNumberLabel;
	@FXML
	private VBox scoreboardVBox;
	@FXML
	private ImageView midImage;
	@FXML
	private ImageView rightImage;
	@FXML
	TableView<Question> wordsTableView;
	@FXML
	private ToolBarController toolBarController;

	private int score;
	private String topic;
	private boolean isGame;
	private Image topicImage;
	private ObservableList<Question> tableData;

	// Setup class fields and scene elements
	public void start(int num, String topic, Image topicImage, ObservableList<Question> data) {
		this.score = num;
		this.topic = topic;
		this.topicImage = topicImage;
		this.tableData = data;
		scoreNumberLabel.setText(String.valueOf(score));
		rightImage.setImage(this.topicImage);
	}

	// create a file and append results to file
	public void setUpScoreboardData() {
		if (isGame) {
			try {
				// create file if doesn't exist
				File resultsFile = new File(".results.txt");
				if (!resultsFile.exists()) {
					resultsFile.createNewFile();
				}

				// append data to file
				Writer output;
				output = new BufferedWriter(new FileWriter(resultsFile, true));
				output.append(String.valueOf(this.score));
				output.append("\n");
				output.append(this.topic);
				output.append("\n");
				String date = new SimpleDateFormat("dd MMMM yyyy").format(new java.util.Date());
				output.append(date);
				output.append("\n");
				output.close();
			} catch (Exception e) {
			}

		}
	}

	public void setUpResults() {
		// set up table
		wordsTableView.setEditable(false);
		wordsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// create columns
		TableColumn<Question, String> word = new TableColumn<>("Word");
		word.setCellValueFactory(new PropertyValueFactory<>("word"));
		TableColumn<Question, String> answer = new TableColumn<>("You Spelled");
		answer.setCellValueFactory(new PropertyValueFactory<>("answer"));

		word.setSortable(false);
		answer.setSortable(false);
		word.setReorderable(false);
		answer.setReorderable(false);

		// add data to table
		wordsTableView.setItems(tableData);
		wordsTableView.getColumns().addAll(Arrays.asList(word, answer));

		// color based on isCorrect
		wordsTableView.setRowFactory(tv -> new TableRow<Question>() {
			@Override
			protected void updateItem(Question item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null) {
					setStyle("");
				} else if (item.isCorrect())
					this.setId("greenCell");
				else
					this.setId("redCell");
			}
		});

		// set first column back to black
		word.setStyle("-fx-text-fill: black");
	}

	// Update scene based on whether results scene relates to a practice quiz/game
	public void setGame(boolean isGame) {
		this.isGame = isGame;

		// make not needed elements invisble and setup the 3 images
		if (!isGame) {
			scoreboardVBox.setVisible(false);
			scoreboardVBox.setManaged(false);
			scoreLabel.setVisible(false);
			scoreNumberLabel.setVisible(false);
			midImage.setVisible(true);
			midImage.setImage(topicImage);
			try {
				rightImage.setImage(new Image(getClass().getResource("/images/trophy.png").toURI().toString()));
			} catch (Exception e) {
			}
			toolBarController.setSceneName("results_scene_practise");
		} else {
			toolBarController.setSceneName("results_scene_game");
		}
	}

	// move from results to topic scene
	@FXML
	public void actionAgain(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/topic.fxml"));
			Parent root = loader.load();
			TopicController contr = loader.getController();
			contr.setGame(isGame);
			contr.setTopicSelected(topic);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
		}
	}

	// move from results to scoreboard scene
	@FXML
	public void actionScoreboard(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/scoreboard.fxml"));
			Parent root = loader.load();

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
		}
	}

}
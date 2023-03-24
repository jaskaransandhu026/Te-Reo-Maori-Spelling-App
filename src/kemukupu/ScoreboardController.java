package kemukupu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ScoreboardController {

	@FXML
	private TableView<Results> resultsTableView;
	@FXML
	private ToolBarController toolBarController;

	private ObservableList<Results> tableData = FXCollections.observableArrayList();

	// tableview with data
	@FXML
	public void initialize() {
		toolBarController.setSceneName("scoreboard_scene");

		// text displayed if the table view is empty
		resultsTableView.setPlaceholder(new Label("No scores to show!"));

		// set up table
		resultsTableView.setEditable(false);
		resultsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// create columns
		TableColumn<Results, Integer> score = new TableColumn<>("Score");
		score.setCellValueFactory(new PropertyValueFactory<>("Score"));
		TableColumn<Results, String> topic = new TableColumn<>("Topic");
		topic.setCellValueFactory(new PropertyValueFactory<>("Topic"));
		TableColumn<Results, String> date = new TableColumn<>("Date");
		date.setCellValueFactory(new PropertyValueFactory<>("Date"));

		score.setSortable(true);
		topic.setSortable(false);
		date.setSortable(false);
		score.setReorderable(true);
		topic.setReorderable(true);
		date.setReorderable(true);

		// get tableData from text file
		try {
			File resultsFile = new File(".results.txt");
			if (!resultsFile.exists()) {
				resultsFile.createNewFile();
			}

			BufferedReader br = new BufferedReader(new FileReader(resultsFile));
			String line;
			while ((line = br.readLine()) != null) {
				tableData.add(new Results(Integer.valueOf(line), br.readLine(), br.readLine()));
			}
			br.close();

		} catch (Exception e) {
		}

		// add data to table
		resultsTableView.setItems(tableData);
		resultsTableView.getColumns().addAll(Arrays.asList(score, topic, date));

		// sort the table entries by score
		score.setSortType(TableColumn.SortType.DESCENDING);
		resultsTableView.getSortOrder().add(score);
		resultsTableView.sort();
	}
	
	// move from scoreboard to menu scene
	@FXML
	public void actionBack(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/menu.fxml"));
			Parent root = loader.load();
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
		}
	}
	
	// delete results file after confirmation
	@FXML
	public void actionClear(ActionEvent event) {
		ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
		ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
		Alert alert = new Alert(AlertType.CONFIRMATION, "", yesButton, noButton);
		alert.setHeaderText("Are you sure you want to clear the scoreboard? ");

		if (alert.showAndWait().get() == yesButton) {
			// delete results file
			File resultsFile = new File(".results.txt");
			if (resultsFile.exists()) {
				resultsFile.delete();
			}
			tableData.clear();
		}
	}
}
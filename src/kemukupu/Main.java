package kemukupu;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;

public class Main extends Application {

	// launch first scene (menu)
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/scenes/menu.fxml"));
			Scene scene = new Scene(root);

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			scene.getStylesheets().add("http://fonts.googleapis.com/css?family=Luckiest%20Guy");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();

			primaryStage.setOnCloseRequest(event -> {
				event.consume();
				exitAlert(primaryStage);
			});

		} catch (Exception e) {
		}
	}

	// confirmation pop-up dialogue
	public void exitAlert(Stage stage) {

		ButtonType yesButton = new ButtonType("Leave", ButtonBar.ButtonData.OK_DONE);
		ButtonType noButton = new ButtonType("Stay", ButtonBar.ButtonData.CANCEL_CLOSE);
		Alert alert = new Alert(AlertType.CONFIRMATION, "", yesButton, noButton);
		alert.setHeaderText("Are you sure you want to leave the game?");

		if (alert.showAndWait().get() == yesButton) {
			// delete festival file from system
			File file = new File(".festivalSceme.scm");
			file.delete();
			stage.close();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}

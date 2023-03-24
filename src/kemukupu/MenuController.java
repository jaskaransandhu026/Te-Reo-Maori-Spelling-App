package kemukupu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

//controller class for the opening (menu) scene
public class MenuController {

	@FXML
	private ImageView titleImageView;
	@FXML
	private ToolBarController toolBarController;

	@FXML
	public void initialize() {
		toolBarController.setSceneName("menu_scene");
	}

	// move from menu scene to topic scene (play)
	public void actionPlay(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/topic.fxml"));
			Parent root = loader.load();
			TopicController contr = loader.getController();
			contr.setGame(true);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();

		} catch (Exception e) {
		}
	}

	// move from menu scene to topic scene (practise)
	public void actionPractise(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/topic.fxml"));
			Parent root = loader.load();
			TopicController contr = loader.getController();
			contr.setGame(false);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();

		} catch (Exception e) {
		}
	}

	// move from menu scene to scoreboard scene
	@FXML
	public void actionScoreBoard(ActionEvent event) {
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
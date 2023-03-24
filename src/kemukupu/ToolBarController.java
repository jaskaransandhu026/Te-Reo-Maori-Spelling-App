package kemukupu;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

// controller class for the three buttons actions on the toolbar
public class ToolBarController {

	private String sceneName;

	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}

	// return to home (menu)
	public void actionHome(ActionEvent event) {
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

	// quit application
	public void actionQuit(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Main main = new Main();
		main.exitAlert(stage);
	}

	// bring up help window
	public void actionHelp(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/help.fxml"));
			Parent root = (Parent) loader.load();

			HelpController contr = loader.getController();
			contr.setImage(sceneName);

			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage stage = new Stage();
			stage.setResizable(false);
			stage.setScene(scene);

			// disable underlying window
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();

		} catch (Exception e) {
		}
	}

}

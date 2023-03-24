package kemukupu;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

// controller class for the help pop up window
public class HelpController {
	@FXML
	private ImageView helpImageView;

	// set an image relevant to the current scene
	public void setImage(String sceneName) {
		try {
			helpImageView
					.setImage(new Image(getClass().getResource("/images/" + sceneName + ".png").toURI().toString()));
		} catch (Exception e) {
		}
	}

	// close help window
	public void actionClose(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
	}
}

package _3024c.heroregistry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
//import org.springframework.boot.autoconfigure.SpringBootApplication;

/* Aram Valcourt
   CEN 3024C
   10/28/2025
   SOFTWARE DEVELOPMENT I
   Main Class running the HRS; The HRS purpose is to enable management and registry of the Hero HQ's heroes through a
   JavaFx driven app to allow C.R.U.D functionality and one custom methods
*/


//Icons thanks to Icons8
//<a target="_blank" href="https://icons8.com/icon/c3Z8IwwzvmWR/upload">Upload</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>

import java.util.Objects;

//@SpringBootApplication
public class Main extends Application {
	double x = 0;
	double y = 0;

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/_3024c/heroregistry/Dashboard.fxml")));
		primaryStage.initStyle(StageStyle.UNDECORATED);



		root.setOnMousePressed(event -> {
			x = event.getSceneX();
			y = event.getSceneY();
		});

		root.setOnMouseDragged(event -> {
			primaryStage.setX(event.getScreenX() - x);
			primaryStage.setY(event.getScreenY() - y);
		});

		primaryStage.setScene(new Scene(root, 1500, 900));
        primaryStage.setTitle("Hero Registry System");
		primaryStage.show();
	}

    /*  Main method
        contains the logic to trigger the launch of the HRS
        the parameter args takes in array of strings
        the return type is void.
    */
	public static void main(String[] args) {
        launch(args);
	}

}

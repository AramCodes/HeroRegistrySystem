package _3024c.heroregistry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
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

	public static void main(String[] args) {
        launch(args);
	}

}

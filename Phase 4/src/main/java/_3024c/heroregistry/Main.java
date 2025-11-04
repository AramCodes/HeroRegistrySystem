package _3024c.heroregistry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(proxyBeanMethods = false)
public class Main extends Application {

    private ConfigurableApplicationContext springContext;
    Parent rootNode;
	double x = 0;
	double y = 0;

    /*  Main method
        contains the logic to trigger the launch of the HRS
        the parameter args takes in array of strings
        the return type is void.
    */
    public static void main(String[] args) {
        launch(args);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.initStyle(StageStyle.UNDECORATED);



		rootNode.setOnMousePressed(event -> {
			x = event.getSceneX();
			y = event.getSceneY();
		});

		rootNode.setOnMouseDragged(event -> {
			primaryStage.setX(event.getScreenX() - x);
			primaryStage.setY(event.getScreenY() - y);
		});

		primaryStage.setScene(new Scene(rootNode, 1500, 900));
        primaryStage.setTitle("Hero Registry System");
		primaryStage.show();
	}

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(Main.class);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/_3024c/heroregistry/Dashboard.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        rootNode =  fxmlLoader.load();
    }

    @Override
    public void stop() throws Exception {
        springContext.close();
    }

}

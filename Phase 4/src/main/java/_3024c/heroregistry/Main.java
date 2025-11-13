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

//Help from YouTube MVP Java "Integrating Spring Boot with JavaFX"
@SpringBootApplication(proxyBeanMethods = false)

/** This is the main class for launching the application containing most of the logic of launching
 * the java Fx and the integration and dual launch of JavaFX and SpringBoot
 */
public class Main extends Application {

    private ConfigurableApplicationContext springContext;
    Parent rootNode;
	double x = 0;
	double y = 0;

    /**  Main method
    *    contains the logic to set the stage and trigger the launch of the HRS
    *    the parameter args takes in array of strings
    *    the return type is void.
    */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method is necessary for JavasFX
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
	public void start(Stage primaryStage) {
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


    /**
     * The init jerry rigs JavaFX and SpringBoot unto working together at the same time
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(Main.class);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/_3024c/heroregistry/Dashboard.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        rootNode =  fxmlLoader.load();
    }

    /**
     * Runs the context concurrently while avoiding collision
     */
    @Override
    public void stop() {
        springContext.close();
    }

}

package _3024c.heroregistry.controllers;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static _3024c.heroregistry.controllers.Controller.goToDb;
/*
    This contains all the logic for the database page its startup and button clicks on it page
 */
@Component
public class DatabaseController implements Initializable {
    static String url = "";
    static String user = "";
    static String pass = "";
    static String statement = "";

    @FXML
    private Button btnDash;

    @FXML
    private Button btnData;

    @FXML
    private Button connectBtn;

    @FXML
    private  TextField dbName;

    @FXML
    private  TextField dbPass;

    @FXML
    private TextField dbTable;

    @FXML
    private TextField dbUser;

    @FXML
    private ImageView exit;

    @FXML
    private Label menuClose;

    @FXML
    private AnchorPane slider;

    @FXML
    private Text statusLabel;

    @FXML
    void handleClicks(ActionEvent event) throws Exception {
        Object src = event.getSource();
        if (!(src instanceof Button)) return;

        Button btn = (Button) src;


        String key = (btn.getId() != null && !btn.getId().isBlank())
                ? btn.getId()
                : "btn" + (btn.getText() == null ? "" : btn.getText().trim());

        if(key.equals("btnDash")){
            goToDash(event);
        } else if (key.equals("btnData")) {
            goToDb(event);
        } else if (key.equals("connectBtn")) {
            getConnection();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        slider.setTranslateX(0);
        menuClose.setVisible(true);

        exit.setOnMouseClicked(e -> {
            System.exit(0);
        });

        EventHandler<MouseEvent> toggleMenu = e -> {
            boolean isClosed = slider.getTranslateX() <= -199; // closed at -200
            TranslateTransition slide = new TranslateTransition(Duration.seconds(0.4), slider);
            slide.setToX(isClosed ? 0 : -200);
            slide.play();
        };

        menuClose.setOnMouseClicked(toggleMenu);
    }

    /*
    goToDash()
    this method goes to the main dashboard
    the parameter is the action event object
    the return type is void
*/
    public static void goToDash(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(DatabaseController.class.getResource("/_3024c/heroregistry/Dashboard.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /*
    showDbStatus
    this helper method simply hides and shows messages(errors or completions) on the announcement text field and auto-hides message after 15 sec.
    the parameters are a message to be displayed and the success status which determines the color the text is outputted
    the return type is void
*/
    private void showDbStatus(String msg, boolean success) {
        statusLabel.setText(msg);
        statusLabel.setStyle(
                success
                        ? "-fx-text-fill: #1f8f36; -fx-background-color: rgba(31,143,54,0.10); -fx-padding: 8; -fx-background-radius: 6;"
                        : "-fx-text-fill: #c62828; -fx-background-color: rgba(198,40,40,0.10); -fx-padding: 8; -fx-background-radius: 6;"
        );
        statusLabel.setVisible(true);

        // auto-hides announcements after 15s
        PauseTransition hide = new PauseTransition(Duration.seconds(15));
        hide.setOnFinished(e -> statusLabel.setVisible(false));
        hide.playFromStart();
    }

    /*
        getConnection()
        this method connect the DB to the app
        there are no parameters
        the return type is void
    */
    @FXML
    public Connection getConnection() {
        url = "jdbc:mysql://localhost:3306/" + dbName.getText().trim();
        user = dbUser.getText();
        pass = dbPass.getText();
        statement = "select * from " + dbTable.getText().trim();

        try {

            Connection connection = DriverManager.getConnection(url, user, pass);
            showDbStatus("Connected successfully!", true);
            return connection;

        } catch (SQLException e) {
            showDbStatus("Database connection failed: " + e.getMessage(), false);
        }
        return null;
    }
}

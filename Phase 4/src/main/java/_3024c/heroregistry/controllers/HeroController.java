package _3024c.heroregistry.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;


@Component
public class HeroController implements Initializable {
    static String url = "jdbc:mysql://localhost:3306/";
    static String user = "";
    static String pass = "";
    static String statement = "select * from ";

    @FXML
    private static Text statusLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

//    /*
//        showDbStatus
//        this helper method simply hides and shows messages(errors or completions) on the announcement text field and auto-hides message after 7 sec.
//        the parameters are a message to be displayed and the success status which determines the color the text is outputted
//        the return type is void
//    */
//    private static void showDbStatus(String msg, boolean success) {
//        statusLabel.setText(msg);
//        statusLabel.setStyle(
//                success
//                        ? "-fx-text-fill: #1f8f36; -fx-background-color: rgba(31,143,54,0.10); -fx-padding: 8; -fx-background-radius: 6;"
//                        : "-fx-text-fill: #c62828; -fx-background-color: rgba(198,40,40,0.10); -fx-padding: 8; -fx-background-radius: 6;"
//        );
//        statusLabel.setVisible(true);
//
//        // auto-hides announcements after 5s
//        PauseTransition hide = new PauseTransition(Duration.seconds(7));
//        hide.setOnFinished(e -> statusLabel.setVisible(false));
//        hide.playFromStart();
//    }
//
//    /*
//        getConnection()
//        this method connect the DB to the app
//        there are no parameters
//        the return type is void
//    */
//    public static Connection getConnection() {
//        try {
//
//            Connection connection = DriverManager.getConnection(url, user, pass);
//            showDbStatus("Connected successfully!", true);
//            return connection;
//
//        } catch (SQLException e) {
//            showDbStatus("Database connection failed: " + e.getMessage(), false);
//        }
//        return null;
//    }
}

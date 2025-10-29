package _3024c.heroregistry.controllers;

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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

import static _3024c.heroregistry.controllers.Controller.goToDb;
/*
    This contains all the logic for the database page its startup and button clicks on it page
 */
public class DatabaseController implements Initializable {

    @FXML
    private Button btnDash;

    @FXML
    private Button btnData;

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

    public static void goToDash(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(DatabaseController.class.getResource("/_3024c/heroregistry/Dashboard.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}

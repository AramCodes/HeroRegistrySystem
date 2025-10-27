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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button btnDash;

    @FXML
    private Button btnData;

    @FXML
    private Button btnAverage;

    @FXML
    private Button btnCreate;

    @FXML
    private Button btnDisplay;

    @FXML
    private Button btnRead;

    @FXML
    private Button btnRemove;

    @FXML
    private Button btnUpdate;

    @FXML
    private ImageView exit;

    @FXML
    private Label menuClose;

    @FXML
    private GridPane pnDisplay;

    @FXML
    private Pane pnRead;

    @FXML
    private Pane pnCreate;

    @FXML
    private Pane pnRemove;

    @FXML
    private Pane pnUpdate;

    @FXML
    private Pane pnAverage;

    @FXML
    private AnchorPane slider;

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

    @FXML
    private void handleClicks(ActionEvent event) throws Exception {
        Object src = event.getSource();
        if (!(src instanceof Button)) return;

        Button btn = (Button) src;


        String key = (btn.getId() != null && !btn.getId().isBlank())
                ? btn.getId()
                : "btn" + (btn.getText() == null ? "" : btn.getText().trim());

        switch (key) {
            case "btnRead":
                pnRead.toFront();
                break;

            case "btnDisplay", "btnDash":
                pnDisplay.toFront();
                break;

            case "btnCreate":
                pnCreate.toFront();
                break;

            case "btnRemove":
                pnRemove.toFront();
                break;

            case "btnUpdate":
                pnUpdate.toFront();
                break;

            case "btnAverage":
                pnAverage.toFront();
                break;
            case "btnData":
                goToDb(event);
                break;
            default:
                // TODO: unknown button
                break;
        }
    }

    public void goToDb(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/_3024c/heroregistry/Database.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}

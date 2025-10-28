package _3024c.heroregistry.controllers;

import _3024c.heroregistry.models.Hero;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private final List<Hero> heroes = new ArrayList<>();
    private long curId;

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
    private Button actionAverage;

    @FXML
    private Button actionCreate;

    @FXML
    private Button actionDelete;

    @FXML
    private Button actionDisplay;

    @FXML
    private Button actionImport;

    @FXML
    private Button actionUpdate;

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
    private Pane avgResult;

    @FXML private TableView<Hero> heroTable;

    @FXML
    private TableColumn<Hero, Long> idCol;

    @FXML
    private TableColumn<Hero, String>  heroNameCol;

    @FXML
    private TableColumn<Hero, String>  realNameCol;

    @FXML
    private TableColumn<Hero, String>  headshotCol;

    @FXML
    private TableColumn<Hero, Integer> ageCol;

    @FXML
    private TableColumn<Hero, Double>  ratingCol;

    @FXML
    private TableColumn<Hero, Boolean> activeCol;

    @FXML
    private TableColumn<Hero, String>  descriptionCol;

    @FXML
    private TableColumn<Hero, String>  strengthBaseCol;

    @FXML
    private TextField searchBar;

    @FXML
    private TextField announcement;

    @FXML
    private AnchorPane slider;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pnDisplay.toFront(); //make sure this is the first page to see
        copyHeroesFromOriginal(); //original population of list

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        heroNameCol.setCellValueFactory(new PropertyValueFactory<>("heroName"));
        realNameCol.setCellValueFactory(new PropertyValueFactory<>("realName"));
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        activeCol.setCellValueFactory(new PropertyValueFactory<>("active"));
        strengthBaseCol.setCellValueFactory(new PropertyValueFactory<>("strengthBase"));

        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionCol.setCellFactory(col -> {
            TableCell<Hero, String> cell = new TableCell<>() {
                private final Text text = new Text();
                { setGraphic(text); setContentDisplay(ContentDisplay.GRAPHIC_ONLY); text.wrappingWidthProperty().bind(col.widthProperty().subtract(16)); }
                @Override protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    text.setText(empty ? "" : item);
                }
            };
            return cell;
        });

        headshotCol.setCellValueFactory(new PropertyValueFactory<>("heroHeadshot"));
        headshotCol.setCellFactory(col -> new TableCell<>() {
            private final ImageView iv = new ImageView();
            { iv.setFitWidth(48); iv.setFitHeight(48); iv.setPreserveRatio(true); }
            @Override protected void updateItem(String url, boolean empty) {
                super.updateItem(url, empty);
                if (empty || url == null || url.isBlank()) {
                    setGraphic(null);
                } else {
                    try {
                        iv.setImage(new Image(url, 48, 48, true, true, true));
                        setGraphic(iv);
                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
            }
        });

        slider.setTranslateX(0);
        menuClose.setVisible(true);

        exit.setOnMouseClicked(e -> System.exit(0));

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

    @FXML
    private void handleActions(ActionEvent event){
        Object src = event.getSource();
        if (!(src instanceof Button)) return;

        Button btn = (Button) src;

        switch (btn.getId()) {
            case "btnQ":
                //TODO
                break;

            case "actionDisplay":
                displayAllHeroes();
                break;

            case "btnA":
                //TODO
                break;

            case "actionDelete":
                try{
                    curId = Long.parseLong(searchBar.getText());
                    heroDelete(curId);
                } catch (NumberFormatException e) {
                    showAnnouncement("Enter a numeric 7-digit ID.", false);
                }

                break;

            case "btnU":
                //TODO
                break;

            case "actionAverage":
                double avg = getAverageRating();

                Label avgLabel = new Label(Double.isFinite(avg) ? String.format("%.2f", avg) : "N/A");

                avgResult.getChildren().setAll(avgLabel);

                avgLabel.setFont(javafx.scene.text.Font.font(16));

                avgLabel.layoutXProperty().bind(
                        avgResult.widthProperty().subtract(avgLabel.widthProperty()).divide(2)
                );
                avgLabel.layoutYProperty().bind(
                        avgResult.heightProperty().subtract(avgLabel.heightProperty()).divide(2)
                );

                break;
            default:
                // TODO: unknown button
                break;
        }
    }

    public void showHeroes(List<Hero> heroes) {
        heroTable.setItems(FXCollections.observableArrayList(heroes));
    }

    public static void goToDb(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(Controller.class.getResource("/_3024c/heroregistry/Database.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /*
        getAverageRating()
        this method sums all the rating data and divides it by the total number of heroes
        there is not a parameter on this method
        the return type is double which returns is the average waiting
    */
    public double getAverageRating() {
        double total = 00.00;

        if(heroes.isEmpty()){
            return total;
        }

        for (Hero hero : heroes) {
            total += hero.getRating();
        }

        return total / heroes.size();
    }

    /*
        displayAllHeroes
        (in the heroes list)
        this method prints all heroes to the screen.
        there are no parameters needed for this method
        the return type is int, a 1 if it is successful
    */

    public int displayAllHeroes(){
        copyHeroesFromOriginal();

        if (heroes == null || heroes.isEmpty()) {
            heroTable.getItems().clear();
            return 0;
        }

        heroTable.setItems(FXCollections.observableArrayList(heroes));

        return 1;
    }

        /*
       copyHeroesFromOriginal
       (file to list)
       this method makes sure that the data in the original file is inside heroes List. Used before mutations to maintain the order of data
       and update chosen source of truth(temporary heroes' list until Db).
       there are no parameters needed for this method
       the return type is int, will return int of total records
   */

    public int copyHeroesFromOriginal() {
        if (!heroes.isEmpty()) {
            heroes.clear();
        }


        String filePath = "data.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("-");

                if (parts.length != 9) { //pseudo-defense to only accept line with 9 parts for the 9 attributes of a hero
                    System.err.println("Invalid line (skipped): " + line);
                }

                try {
                    Hero hero = Hero.builder()
                            .id(Long.parseLong(parts[0].trim()))
                            .heroName(parts[1].trim())
                            .realName(parts[2].trim())
                            .heroHeadshot(parts[3].trim())
                            .age(Integer.parseInt(parts[4].trim()))
                            .rating(Double.parseDouble(parts[5].trim()))
                            .isActive(Boolean.parseBoolean(parts[6].trim()))
                            .description(parts[7].trim())
                            .strengthBase(parts[8].trim())
                            .build();

                    heroes.add(hero);
                } catch (NumberFormatException e) {
                    System.err.println("Failed to parse number in line: " + line);
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }

        System.out.println();
        System.out.println("List Loaded!!!");
        System.out.println();

        if (!heroes.isEmpty()){
            return heroes.size();
        }

        return 0;
    }

        /*
        heroDelete
        this method searches Heroes for matching id  and removes if found then uses fileWrite to reflect
        deletion in text file.
        there is a parameter of id this method
        the return type is void
    */

    public boolean heroDelete(long id) {
        if (!isSevenDigitAndLong(id)) {
            showAnnouncement("Invalid ID: must be exactly 7 digits.", false);
            return false;
        }

        int idx = -1;
        for (int i = 0; i < heroes.size(); i++) {
            if (heroes.get(i).getId() == id) { idx = i; break; }
        }
        if (idx == -1) {
            showAnnouncement("Hero with ID " + id + " not found.", false);
            return false;
        }

        Hero removed = heroes.remove(idx);
        try {
            overwriteOriginalFile(); // updates file after hero removal from array
        } catch (Exception ex) {
            // rollback array if save to file fails
            heroes.add(idx, removed);
            showAnnouncement("Delete failed while saving: " + ex.getMessage(), false);
            return false;
        }

        //refreshes table based on array
        showHeroes(heroes);

        searchBar.clear();
        showAnnouncement(id + " was successfully deleted from the Hero Registry System.", true);
        return true;
    }

        /*
        isSevenDigitAndLong
        this method simply verifies that the number in it is a seven digit long(id format)
        the parameter is a long called id
        the return type is boolean, will return true if it is valid false if not
    */

    public boolean isSevenDigitAndLong(long id) {
        return id >= 1_000_000L && id <= 9_999_999L;
    }

    /*
        overwriteOriginalFile()
        this method writes and saves the items from heroes list into data file.
        there are no parameters needed for this method
        the return type is String
    */
    public String overwriteOriginalFile() {
        String filePath = "data.txt";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            for (Hero hero : heroes) {
                String heroLine = formatHeroRecord(hero);

                writer.write(heroLine);
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {
            System.out.println("Error writing to file. Info: "  + e.getMessage());
        }

        return "done";
    }

        /*
        formatHeroRecord
        this method simply converts the Hero Objects into the string format used in files.
        the parameter is an object containing a heroes data
        the return type is a String, will return converted data
    */

    public String formatHeroRecord(Hero hero) {
        if (hero == null) {
            return "";
        }

        return String.format(
                "%d-%s-%s-%s-%d-%.1f-%b-%s-%s",
                hero.getId(),
                hero.getHeroName(),
                hero.getRealName(),
                hero.getHeroHeadshot(),
                hero.getAge(),
                hero.getRating(),
                hero.isActive(),
                hero.getDescription(),
                hero.getStrengthBase()
        );
    }

    /*
        showAnnouncement
        this helper method simply hides and shows messages(errors or completions) on the announcement text field and auto-hides message after 5 sec.
        the parameters are a message to be displayed and the success status which determines the color the text is outputted
        the return type is void
    */
    private void showAnnouncement(String msg, boolean success) {
        announcement.setText(msg);
        announcement.setStyle(
                success
                        ? "-fx-text-fill: #1b7e22; -fx-font-weight: 600;"
                        : "-fx-text-fill: #b00020; -fx-font-weight: 600;"
        );
        announcement.setVisible(true);

        // auto-hides announcements after 5s
        PauseTransition hide = new PauseTransition(Duration.seconds(5));
        hide.setOnFinished(e -> announcement.setVisible(false));
        hide.playFromStart();
    }
}

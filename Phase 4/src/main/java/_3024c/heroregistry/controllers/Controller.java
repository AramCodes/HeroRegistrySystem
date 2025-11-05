package _3024c.heroregistry.controllers;

import _3024c.heroregistry.models.Hero;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/*
    This class contains all the logic for the dash page its startup and button clicks on it. This serves as the main class containing all the logic behind
    the functionality
 */
@Component
public class Controller implements Initializable {

    private final List<Hero> heroes = new ArrayList<>();
    private final ObservableList<Hero> heroesObs = FXCollections.observableArrayList();
    private Hero loadedHero = null; // reference to the found hero
    private int loadedIndex = -1;
    private int index;

    @FXML
    private Button actionCreate;

    @FXML
    private Button actionImport;

    @FXML
    private Button actionUpdate;

    @FXML private TextField  announcement4;

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
    private TextField announcement2;

    @FXML
    private TextField announcement3;

    @FXML
    private AnchorPane slider;

    @FXML
    private TableColumn<Hero, Boolean> colActive;

    @FXML
    private TableColumn<Hero, Integer> colAge;

    @FXML
    private TableColumn<Hero, String> colDescription;

    @FXML
    private TableColumn<Hero, String> colHeroName;

    @FXML
    private TableColumn<Hero, Long> colId;

    @FXML
    private TableColumn<Hero, Double> colRating;

    @FXML
    private TableColumn<Hero, String> colRealName;

    @FXML
    private TableColumn<Hero, String> colHeadshot;

    @FXML
    private TableColumn<Hero, String> colStrengthBase;

    @FXML
    private TableView<Hero> createTable;

    @FXML
    private TextField ratingField;

    @FXML
    private TextField activeField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField heroNameField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField realNameField;

    @FXML
    private TextField strengthBaseField;

    @FXML private TextField fieldId;
    @FXML private TextField fieldHeroesName;
    @FXML private TextField fieldRealName;
    @FXML private TextField fieldHeadshot;
    @FXML private TextField fieldAge;
    @FXML private TextField fieldRating;
    @FXML private TextField fieldDescription;
    @FXML private TextField fieldStrengthBase;
    @FXML private TextField  fieldActive;

    @FXML private TableView<Hero> thirdTable;

    @FXML
    private TableColumn<Hero, Boolean> active;

    @FXML
    private TableColumn<Hero, Integer> age;

    @FXML
    private TableColumn<Hero, String> description;

    @FXML
    private TableColumn<Hero, String> heroName;

    @FXML
    private TableColumn<Hero, Long> id;

    @FXML
    private TableColumn<Hero, Double> rating;

    @FXML
    private TableColumn<Hero, String> realName;

    @FXML
    private TableColumn<Hero, String> headshot;

    @FXML
    private TableColumn<Hero, String> strengthBase;

    @FXML
    private TableView<Hero> deleteTable;

    @FXML
    private TableColumn<Hero, Long> id1;

    @FXML
    private TableColumn<Hero, String> heroName1;

    @FXML
    private TableColumn<Hero, String> realName1;

    @FXML
    private TableColumn<Hero, String> headshot1;

    @FXML TableColumn<Hero, Integer> age1;

    @FXML TableColumn<Hero, Double>  rating1;

    @FXML TableColumn<Hero, Boolean> active1;

    @FXML TableColumn<Hero, String>  description1;

    @FXML TableColumn<Hero, String>  strengthBase1;

    /*
        initialize()
        this overridden and necessary method helps to set the stage to wanted values and set important rules
        the parameter is the url to the fxml page and the resourceBundle are key value pair loaded from .yaml
        or .properties page
        the return type is void
    */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pnDisplay.toFront(); //make sure this is the first page to see
        copyHeroesFromOriginal(); //original population of list

        //first table display
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        heroNameCol.setCellValueFactory(new PropertyValueFactory<>("heroName"));
        realNameCol.setCellValueFactory(new PropertyValueFactory<>("realName"));
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        activeCol.setCellValueFactory(new PropertyValueFactory<>("isActive"));
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


        createTable.setItems(FXCollections.observableArrayList(heroes));
        //second table create
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colHeroName.setCellValueFactory(new PropertyValueFactory<>("heroName"));
        colRealName.setCellValueFactory(new PropertyValueFactory<>("realName"));
        colHeadshot.setCellValueFactory(new PropertyValueFactory<>("heroHeadshot"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        colActive.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colStrengthBase.setCellValueFactory(new PropertyValueFactory<>("strengthBase"));

        heroesObs.setAll(heroes);
        createTable.setItems(heroesObs);

        thirdTable.setItems(FXCollections.observableArrayList(heroes));
        announcement4.setVisible(false);

        //third table create
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        heroName.setCellValueFactory(new PropertyValueFactory<>("heroName"));
        realName.setCellValueFactory(new PropertyValueFactory<>("realName"));
        headshot.setCellValueFactory(new PropertyValueFactory<>("heroHeadshot"));
        age.setCellValueFactory(new PropertyValueFactory<>("age"));
        rating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        active.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        strengthBase.setCellValueFactory(new PropertyValueFactory<>("strengthBase"));

;

        deleteTable.setItems(FXCollections.observableArrayList(heroes));
        id1.setCellValueFactory(new PropertyValueFactory<>("id"));
        heroName1.setCellValueFactory(new PropertyValueFactory<>("heroName"));
        realName1.setCellValueFactory(new PropertyValueFactory<>("realName"));
        headshot1.setCellValueFactory(new PropertyValueFactory<>("heroHeadshot"));
        age1.setCellValueFactory(new PropertyValueFactory<>("age"));
        rating1.setCellValueFactory(new PropertyValueFactory<>("rating"));
        active1.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        description1.setCellValueFactory(new PropertyValueFactory<>("description"));
        strengthBase1.setCellValueFactory(new PropertyValueFactory<>("strengthBase"));


        setEditableFieldsEnabled(false);

        fieldId.textProperty().addListener((obs, oldV, newV) -> onIdTyped(newV));

        Platform.runLater(() -> fieldId.requestFocus());

        slider.setTranslateX(0);
        menuClose.setVisible(true);

        exit.setOnMouseClicked(e -> System.exit(0));

        EventHandler<MouseEvent> toggleMenu = e -> {
            boolean isClosed = slider.getTranslateX() <= -199; // closed at -200
            TranslateTransition slide = new TranslateTransition(Duration.seconds(0.4), slider);
            slide.setToX(isClosed ? 0 : -200);
            slide.play();
        };

        actionCreate.setDefaultButton(true);

        menuClose.setOnMouseClicked(toggleMenu);
    }

    /*
        onIdTyped()
        this method validates Id as it is being typed
        the parameter is string being entered
        the return type is void
    */
    private void onIdTyped(String txt) {

        if (txt == null || !txt.matches("\\d{7}")) {
            loadedHero = null;
            loadedIndex = -1;
            setEditableFieldsEnabled(false);
            clearNonIdFields();
            showAnnouncementUpdate("Enter a 7-digit ID to search.", false);
            return;
        }

        long id = Long.parseLong(txt);
        int idx = -1;
        for (int i = 0; i < heroes.size(); i++) {
            if (Objects.equals(heroes.get(i).getId(), id)) {
                idx = i;
                break;
            }
        }

        if (idx < 0) {
            loadedHero = null;
            loadedIndex = -1;
            setEditableFieldsEnabled(false);
            clearNonIdFields();
            showAnnouncementUpdate("No hero found with ID " + id + ".", false);
        } else {
            loadedHero = heroes.get(idx);
            loadedIndex = idx;
            setEditableFieldsEnabled(true);
            populateFieldsFromHero(loadedHero);
            showAnnouncementUpdate("Loaded hero with ID " + id + ". You can edit the fields now.", true);

            fieldId.setDisable(true);
        }
    }

    /*
        setEditableFieldsEnabled()
        this helper method helps increase the UX by decreasing confusion when of when data is needed vs being processed
        the parameter is enabled a boolean that confirms switch
        the return type is void
    */
    private void setEditableFieldsEnabled(boolean enabled) {
        fieldHeroesName.setDisable(!enabled);
        fieldRealName.setDisable(!enabled);
        fieldHeadshot.setDisable(!enabled);
        fieldAge.setDisable(!enabled);
        fieldRating.setDisable(!enabled);
        fieldDescription.setDisable(!enabled);
        fieldStrengthBase.setDisable(!enabled);
        fieldActive.setDisable(!enabled);
        actionUpdate.setDisable(!enabled);
    }

    /*
        populateFieldsFromHero()
        this helper method helps increase the UX by populating existing hero data into update field so
        that user doesn't have to retype everything and can simply edit the parts that need to be changed
        the parameter is the found hero obj
        the return type is void
    */
    private void populateFieldsFromHero(Hero h) {
        fieldHeroesName.setText(safe(h.getHeroName()));
        fieldRealName.setText(safe(h.getRealName()));
        fieldHeadshot.setText(safe(h.getHeroHeadshot()));
        fieldAge.setText(String.valueOf(h.getAge()));
        fieldRating.setText(String.valueOf(h.getRating()));
        fieldDescription.setText(safe(h.getDescription()));
        fieldStrengthBase.setText(safe(h.getStrengthBase()));
        fieldActive.setText(String.valueOf(h.getIsActive()));
    }

    /*
        clearNonIdFields()
        this helper method helps ensure operations cleaned after
        there are no parameters
        the return type is void
    */
    private void clearNonIdFields() {
        fieldHeroesName.clear();
        fieldRealName.clear();
        fieldHeadshot.clear();
        fieldAge.clear();
        fieldRating.clear();
        fieldDescription.clear();
        fieldStrengthBase.clear();
        fieldActive.clear();
    }
    private String safe(String s) { return s == null ? "" : s; }

    /*
        parseId()
        this helper method helps parse inputs to ensure operations are completed without error
        the parameters are txt from input and a list of errors
        the return type is a long
    */
    private Long parseId(String txt, List<String> errors) {
        if (txt == null || !txt.matches("\\d{7}")) {
            errors.add("Hero ID must be exactly 7 digits (e.g., 1234567).");
            return null;
        }
        try {
            return Long.parseLong(txt);
        } catch (NumberFormatException ex) {
            errors.add("Hero ID is not a valid number.");
            return null;
        }
    }

    /*
        nonEmpty()
        this helper method helps sanitize inputs to ensure operations are completed without error
        the parameters are txt from input, a label and a list of errors
        the return type is the same string trimmed of external whitespace if it passes the test
    */
    private String nonEmpty(String txt, String label, List<String> errors) {
        if (txt == null || txt.trim().isEmpty()) {
            errors.add(label + " cannot be empty.");
            return null;
        }
        return txt.trim();
    }

    /*
    validUrl()
    this helper method helps sanitize inputs to ensure operations are completed without error
    the parameters are txt from input, a label and a list of errors
    the return type is the same string if it passes all test
    */
    private String validUrl(String txt, String label, List<String> errors) {
        String v = nonEmpty(txt, label, errors);
        if (v == null) return null;
        // Light URL check
        if (!v.matches("^(https?|ftp)://\\S+$")) {
            errors.add(label + " must start with http:// or https://");
            return null;
        }
        return v;
    }

    /*
        parseAge()
        this helper method helps parse inputs to ensure operations are completed without error
        the parameters are txt from input and a list of errors
        the return type is the corrected integer
    */
    private Integer parseAge(String txt, List<String> errors) {
        String v = nonEmpty(txt, "Age", errors);
        if (v == null) return null;
        try {
            int age = Integer.parseInt(v);
            if (age <= 0 || age >= 200) {
                errors.add("Age must be between 1 and 199.");
                return null;
            }
            return age;
        } catch (NumberFormatException e) {
            errors.add("Age must be a whole number.");
            return null;
        }
    }

    /*
        parseRating()
        this helper method helps parse inputs to ensure operations are completed without error
        the parameters are txt from input and a list of errors
        the return type is the corrected double
    */
    private Double parseRating(String txt, List<String> errors) {
        String v = nonEmpty(txt, "Rating", errors);
        if (v == null) return null;
        try {
            double r = Double.parseDouble(v);
            if (r < 0.0 || r > 10.0) {
                errors.add("Rating must be between 0.0 and 10.0.");
                return null;
            }
            return r;
        } catch (NumberFormatException e) {
            errors.add("Rating must be a decimal number (e.g., 7.8).");
            return null;
        }
    }

    /*
        clearInputs()
        this helper method clears text fields after operations are complete
        there are no parameter
        the return type is void
    */
    private void clearInputs() {
        heroNameField.clear();
        realNameField.clear();
        ageField.clear();
        ratingField.clear();
        descriptionField.clear();
        strengthBaseField.clear();
        activeField.clear();
    }

    /*
        handleClicks()
        this method dispatches the correct panel switch  according to button via event object and a switch case
        the parameter is the action event
        the return type is void
    */
    @FXML
    private void handleClicks(ActionEvent event) throws Exception {
        Object src = event.getSource();
        if (!(src instanceof Button btn)) return;


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

    /*
        handleActions()
        this method dispatches the correct actions via event object and a switch case
        the parameter is the action event
        the return type is void
    */
    @FXML
    private void handleActions(ActionEvent event){
        Object src = event.getSource();
        if (!(src instanceof Button btn)) return;

        switch (btn.getId()) {
            case "actionImport":
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Select file to import");
                chooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Text/CSV Files", "*.txt", "*.csv"),
                        new FileChooser.ExtensionFilter("All Files", "*.*")
                );

                File selected = chooser.showOpenDialog(actionImport.getScene().getWindow());
                if (selected == null) {
                    showAnnouncementImport("No file selected.", false);
                    return;
                }

                Path importPath = selected.toPath();
                ImportResult result = batchUpload(importPath);

                if (!result.ok()) {
                    String msg = "Error reading import file: " + result.error().getMessage();
                    showAnnouncementImport(msg, false);
                    return;
                }

                showAnnouncementImport(
                        "Imported " + result.imported() + " records. Skipped " + result.skippedMalformed() + " malformed line(s).",
                        true
                );
                break;

            case "actionDisplay":
                displayAllHeroes();
                break;

            case "actionCreate":
                createHero(event);
                break;

            case "actionDelete":
                try{
                    long curId = Long.parseLong(searchBar.getText());
                    heroDelete(curId);
                } catch (NumberFormatException e) {
                    showAnnouncement("Enter a numeric 7-digit ID.", false);
                }

                break;

            case "actionUpdate":
                if (loadedHero == null || loadedIndex < 0) {
                    showAnnouncementUpdate("No hero is loaded. Enter a valid 7-digit ID first.", false);
                    return;
                }

                List<String> errors = new ArrayList<>();

                String heroName     = nonEmpty(fieldHeroesName.getText(), "Hero name", errors);
                String realName     = nonEmpty(fieldRealName.getText(), "Real name", errors);
                String headshot     = validUrl(fieldHeadshot.getText(), "Headshot URL", errors);
                Integer age         = parseAge(fieldAge.getText(), errors);
                Double rating       = parseRating(fieldRating.getText(), errors);
                boolean isActive    = Boolean.parseBoolean(fieldActive.getText());
                String description  = nonEmpty(fieldDescription.getText(), "Description", errors);
                String strengthBase = nonEmpty(fieldStrengthBase.getText(), "Strength base", errors);

                if (!errors.isEmpty()) {
                    showAnnouncementUpdate(String.join("\n", errors), false);
                    return;
                }

                long id = loadedHero.getId();

                Hero updated = Hero.builder()
                        .id(id)
                        .heroName(heroName)
                        .realName(realName)
                        .heroHeadshot(headshot)
                        .age(age)
                        .rating(rating)
                        .isActive(isActive)
                        .description(description)
                        .strengthBase(strengthBase)
                        .build();

                heroes.set(loadedIndex, updated);
                HeroController.updateHero(updated);

                heroesObs.set(loadedIndex, updated);
                showHeroes(heroes);

                showAnnouncementUpdate("Updated hero ID " + id + " successfully.", true);

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

    /*
    getAttributes() //for update
    this method allow the selection of an object's attributes through mose click on the selected table view
    the parameter is the mouse event
    the return type is void
    */
    @FXML
    void getAttributes(MouseEvent event) {
        index = thirdTable.getSelectionModel().getSelectedIndex();

        if(index <= -1){
            return;
        }

        fieldId.setText(id.getCellData(index).toString());
    }

    /*
        getAttribute() //for delete
        this method allow the selection of an object's attributes through mose click on the selected table view
        the parameter is the mouse event
        the return type is void
    */
    @FXML
    void getAttribute(MouseEvent event) {
        index = deleteTable.getSelectionModel().getSelectedIndex();

        if(index <= -1){
            return;
        }

        searchBar.setText(id1.getCellData(index).toString());
    }

    /*
        createHero()
        this method creates a heroe object and places it in heroes table then change the db
        the parameter is the action event
        the return type is void
    */
    public void createHero(ActionEvent event) {
        List<String> errors = new ArrayList<>();

        String heroName = nonEmpty(heroNameField.getText(), "Hero name", errors);
        String realName = nonEmpty(realNameField.getText(), "Real name", errors);
        Integer age = parseAge(ageField.getText(), errors);
        Double rating = parseRating(ratingField.getText(), errors);
        boolean isActive = Boolean.parseBoolean(activeField.getText());
        String description = nonEmpty(descriptionField.getText(), "Description", errors);
        String strengthBase = nonEmpty(strengthBaseField.getText(), "Strength base", errors);

        if (id != null && heroes.stream().anyMatch(h -> Objects.equals(h.getId(), id))) {
            errors.add("A hero with ID " + id + " already exists.");
        }

        if (!errors.isEmpty()) {
            showAnnouncementCreate(String.join("\n", errors), false);
            return;
        }

        Hero newHero = Hero.builder()
                .heroName(heroName)
                .realName(realName)
                .heroHeadshot("https://unsplash.com/" + heroName)
                .age(age)
                .rating(rating)
                .isActive(isActive)
                .description(description)
                .strengthBase(strengthBase)
                .build();

        HeroController.saveHero(newHero);
        heroes.add(newHero);


        heroesObs.add(newHero);
        showHeroes(heroes);

        showAnnouncementCreate("Created hero " + newHero.getHeroName() + " successfully.", true);
        clearInputs();
    }


    /*
        showHeroes()
        this method takes in a list of Hero types and make them visible on fx tables
        the parameter is a list of Heroes
        the return type is void
    */
    public void showHeroes(List<Hero> heroes) {
        heroTable.setItems(FXCollections.observableArrayList(heroes));
        deleteTable.setItems(FXCollections.observableArrayList(heroes));
        createTable.setItems(FXCollections.observableArrayList(heroes));
        thirdTable.setItems(FXCollections.observableArrayList(heroes));
    }

    /*
        goToDb()
        this method goes to the database login page
        the parameter is the action event object
        the return type is void
    */
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

        heroes.addAll(HeroController.getHeroes());

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
            HeroController.deleteHero(id);
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
                hero.getIsActive(),
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

    /*
        batchUpload()
        this method read and writes new records from input file to original db.
        there are no parameters needed for this method
        the return type is int, the amount of items added
    */


    public ImportResult batchUpload(Path importPath){
        Set<Hero> merged = new LinkedHashSet<>(heroes);
        List<Hero> newlyParsed = new ArrayList<>();
        int importedCount = 0;
        int skippedMalformed = 0;

        try {
            Path path = Paths.get("");
            if (!importPath.isAbsolute()) {
                importPath = path.toAbsolutePath().resolve(importPath);
            }

            if (!Files.isRegularFile(importPath) || !Files.isReadable(importPath)) {
                return new ImportResult(importedCount, skippedMalformed,
                        new IOException("Cannot read: " + importPath + " (cwd: " + path.toAbsolutePath() + ")"));
            }

            try (Stream<String> lines = Files.lines(importPath, StandardCharsets.UTF_8)) {
                for (String line : (Iterable<String>) lines::iterator) {
                    String trimmed = (line == null) ? "" : line.trim();
                    if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;

                    Optional<Hero> parsed = parseHeroFromLine(trimmed);
                    if (parsed.isPresent()) {
                        Hero h = parsed.get();
                        if (merged.add(h)) {
                            newlyParsed.add(h);
                            importedCount++;
                        }
                    } else {
                        skippedMalformed++;
                    }
                }
            }

            heroes.clear();
            heroes.addAll(merged);

            int dbSaved = 0, dbFailed = 0;
            for (Hero h : newlyParsed) {
                try {
                    HeroController.updateHero(h);
                    dbSaved++;
                } catch (Exception ex) {
                    dbFailed++;
                }
            }



            return new ImportResult(importedCount, skippedMalformed, null);

        } catch (Exception e) {
            return new ImportResult(0, 0, e);
        }
    }

    /*
        parseHeroFromLine
        this method simply reads Hero data  and converts it into a hero object
        the parameter is a single line
        the return type is an Optional<Hero>, will return converted data
    */

    public Optional<Hero> parseHeroFromLine(String line){
        if (line == null || line.isEmpty()) {
            throw new IllegalArgumentException("Line cannot be null or empty");
        }

        String[] parts = line.split("-", 9);

        if (parts.length != 9) {
            throw new IllegalArgumentException("Invalid hero data format: " + line);
        }

        try {

            Hero ParsedHero = Hero.builder()
                    .id(Long.parseLong(parts[0]))
                    .heroName(parts[1])
                    .realName(parts[2])
                    .heroHeadshot(parts[3])
                    .age(Integer.parseInt(parts[4]))
                    .rating(Double.parseDouble(parts[5]))
                    .isActive(Boolean.parseBoolean(parts[6]))
                    .description(parts[7])
                    .strengthBase(parts[8])
                    .build();

            return Optional.of(ParsedHero);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error parsing fields in hero data: " + line, e);
        }
    }

    /*
        showAnnouncementImport
        this helper method simply hides and shows messages(errors or completions) on the announcement text field and auto-hides message after 5 sec.
        the parameters are a message to be displayed and the success status which determines the color the text is outputted
        the return type is void
    */
    private void showAnnouncementImport(String msg, boolean success) {
        announcement2.setText(msg);
        announcement2.setStyle(
                success
                        ? "-fx-text-fill: #1f8f36; -fx-background-color: rgba(31,143,54,0.10); -fx-padding: 8; -fx-background-radius: 6;"
                        : "-fx-text-fill: #c62828; -fx-background-color: rgba(198,40,40,0.10); -fx-padding: 8; -fx-background-radius: 6;"
        );
        announcement2.setVisible(true);

        // auto-hides announcements after 5s
        PauseTransition hide = new PauseTransition(Duration.seconds(5));
        hide.setOnFinished(e -> announcement2.setVisible(false));
        hide.playFromStart();
    }

    /*
        ImportResult
        this helper method simply tallies the result fed to it
        the parameters are number of successfully imported lines, failed lines, and the exception object thrown
        the return type is containing ok if truth and eror object if contains false.
    */
    public record ImportResult(int imported, int skippedMalformed, Exception error) {
        boolean ok() { return error == null; }
    }

    /*
    showAnnouncementCreate
    this helper method simply hides and shows messages(errors or completions) on the announcement text field and auto-hides message after 5 sec.
    the parameters are a message to be displayed and the success status which determines the color the text is outputted
    the return type is void
    */
    private void showAnnouncementCreate(String msg, boolean success) {
        announcement3.setText(msg);
        announcement3.setStyle(
                success
                        ? "-fx-text-fill: #1f8f36; -fx-background-color: rgba(31,143,54,0.10); -fx-padding: 8; -fx-background-radius: 6;"
                        : "-fx-text-fill: #c62828; -fx-background-color: rgba(198,40,40,0.10); -fx-padding: 8; -fx-background-radius: 6;"
        );
        announcement3.setVisible(true);

        // auto-hides announcements after 5s
        PauseTransition hide = new PauseTransition(Duration.seconds(5));
        hide.setOnFinished(e -> announcement3.setVisible(false));
        hide.playFromStart();
    }

    /*
        addHero
        this helper method simply takes in a built hero and places it in heroes array and writes array to file
        the parameters are a message to be displayed and the success status which determines the color the text is outputted
        the return type is void
    */
    public String addHero(Hero hero) {
        heroes.add(hero);
        HeroController.saveHero(hero);

        return hero.getHeroName() + " has been added to the Hero Registry System";
    }

    /*
        makeHero
        this helper method simply takes in all data points to build a hero
        the parameters are all the 9 fields of a hero object
        the return type is the built hero
    */
    public Hero makeHero(long id, String heroName, String realName, String heroHeadshot, int age, double rating, boolean isActive, String description, String strengthBase) {

        return Hero.builder()
                .id(id)
                .heroName(heroName)
                .realName(realName)
                .heroHeadshot(heroHeadshot)
                .age(age)
                .rating(rating)
                .isActive(isActive)
                .description(description)
                .strengthBase(strengthBase)
                .build();
    }

    /*
        showAnnouncementUpdate
        this helper method simply hides and shows messages(errors or completions) on the announcement text field and auto-hides message after 5 sec.
        the parameters are a message to be displayed and the success status which determines the color the text is outputted
        the return type is void
    */
    private void showAnnouncementUpdate(String message, boolean success) {
        announcement4.setText(message);
        announcement4.setStyle(success
                ? "-fx-text-fill: #1f8f36; -fx-background-color: rgba(31,143,54,0.10); -fx-padding: 8; -fx-background-radius: 6;"
                : "-fx-text-fill: #c62828; -fx-background-color: rgba(198,40,40,0.10); -fx-padding: 8; -fx-background-radius: 6;");
        announcement4.setVisible(true);

        // auto-hides announcements after 5s
        PauseTransition hide = new PauseTransition(Duration.seconds(5));
        hide.setOnFinished(e -> announcement2.setVisible(false));
        hide.playFromStart();
    }
}

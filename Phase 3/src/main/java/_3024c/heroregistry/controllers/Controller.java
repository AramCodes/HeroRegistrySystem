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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Controller implements Initializable {

    private final List<Hero> heroes = new ArrayList<>();
    private final ObservableList<Hero> heroesObs = FXCollections.observableArrayList();
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
    private TextField announcement2;

    @FXML
    private TextField announcement3;

    @FXML
    private AnchorPane slider;

    @FXML
    private TableColumn<?, ?> colActive;

    @FXML
    private TableColumn<?, ?> colAge;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colHeroName;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colRating;

    @FXML
    private TableColumn<?, ?> colRealName;

    @FXML
    private TableColumn<?, ?> colStrengthBase;

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
    private TextField idField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField realNameField;

    @FXML
    private TextField strengthBaseField;


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

        //second table create
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colHeroName.setCellValueFactory(new PropertyValueFactory<>("heroName"));
        colRealName.setCellValueFactory(new PropertyValueFactory<>("realName"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        colActive.setCellValueFactory(new PropertyValueFactory<>("active"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colStrengthBase.setCellValueFactory(new PropertyValueFactory<>("strengthBase"));

        heroesObs.setAll(heroes);
        createTable.setItems(heroesObs);

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

    private String nonEmpty(String txt, String label, List<String> errors) {
        if (txt == null || txt.trim().isEmpty()) {
            errors.add(label + " cannot be empty.");
            return null;
        }
        return txt.trim();
    }

    private String validUrl(String txt, String label, List<String> errors) {
        String v = nonEmpty(txt, label, errors);
        if (v == null) return null;
        // Light URL check
        if (!v.matches("^(https?|ftp)://[^\\s]+$")) {
            errors.add(label + " must start with http:// or https://");
            return null;
        }
        return v;
    }

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

    private void clearInputs() {
        idField.clear();
        heroNameField.clear();
        realNameField.clear();
        ageField.clear();
        ratingField.clear();
        descriptionField.clear();
        strengthBaseField.clear();
        activeField.clear();
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

    public void createHero(ActionEvent event) {
        List<String> errors = new ArrayList<>();

        // 1) Validate + parse fields
        Long id = parseId(idField.getText(), errors);
        String heroName = nonEmpty(heroNameField.getText(), "Hero name", errors);
        String realName = nonEmpty(realNameField.getText(), "Real name", errors);
        Integer age = parseAge(ageField.getText(), errors);
        Double rating = parseRating(ratingField.getText(), errors);
        boolean isActive = Boolean.parseBoolean(activeField.getText());
        String description = nonEmpty(descriptionField.getText(), "Description", errors);
        String strengthBase = nonEmpty(strengthBaseField.getText(), "Strength base", errors);

        // Unique ID check
        if (id != null && heroes.stream().anyMatch(h -> Objects.equals(h.getId(), id))) {
            errors.add("A hero with ID " + id + " already exists.");
        }

        if (!errors.isEmpty()) {
            showAnnouncementCreate(String.join("\n", errors), false);
            return;
        }

        Hero newHero = Hero.builder()
                .id(id)
                .heroName(heroName)
                .realName(realName)
                .heroHeadshot("https://unsplash.com/" + heroName)
                .age(age)
                .rating(rating)
                .isActive(isActive)
                .description(description)
                .strengthBase(strengthBase)
                .build();

        heroes.add(newHero);
        overwriteOriginalFile();

        heroesObs.add(newHero);
        createTable.refresh();

        showAnnouncementCreate("Created hero “" + heroName + "” (ID " + id + ") successfully.", true);
        clearInputs();
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

    /*
        batchUpload()
        this method read and writes new records from input file to original db.
        there are no parameters needed for this method
        the return type is int, the amount of items added
    */


    public ImportResult batchUpload(Path importPath){
        Set<Hero> merged = new LinkedHashSet<>(heroes);
        int importedCount = 0;
        int skippedMalformed = 0;

        try {
            if (!importPath.isAbsolute()) {
                importPath = Paths.get("").toAbsolutePath().resolve(importPath);
            }

            if (!Files.isRegularFile(importPath) || !Files.isReadable(importPath)) {
                return new ImportResult(importedCount, skippedMalformed,
                        new IOException("Cannot read: " + importPath + " (cwd: " + Paths.get("").toAbsolutePath() + ")"));
            }

            try (Stream<String> lines = Files.lines(importPath, StandardCharsets.UTF_8)) {
                for (String line : (Iterable<String>) lines::iterator) {
                    String trimmed = (line == null) ? "" : line.trim();
                    if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;

                    Optional<Hero> parsed = parseHeroFromLine(trimmed);
                    if (parsed.isPresent()) {
                        if (merged.add(parsed.get())) {
                            importedCount++;
                        }
                    } else {
                        skippedMalformed++;
                    }
                }
            }

            heroes.clear();
            heroes.addAll(merged);
            overwriteOriginalFile();

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
                        ? "-fx-text-fill: #1b7e22; -fx-font-weight: 600;"
                        : "-fx-text-fill: #b00020; -fx-font-weight: 600;"
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
                        ? "-fx-text-fill: #1b7e22; -fx-font-weight: 600;"
                        : "-fx-text-fill: #b00020; -fx-font-weight: 600;"
        );
        announcement3.setVisible(true);

        // auto-hides announcements after 5s
        PauseTransition hide = new PauseTransition(Duration.seconds(5));
        hide.setOnFinished(e -> announcement3.setVisible(false));
        hide.playFromStart();
    }
}

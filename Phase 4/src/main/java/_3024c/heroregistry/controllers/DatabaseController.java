package _3024c.heroregistry.controllers;

import _3024c.heroregistry.models.Hero;
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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static _3024c.heroregistry.controllers.Controller.goToDb;
/*
    This contains all the logic for the database page its startup and button clicks on it page
 */

public class DatabaseController implements Initializable {
    public static DatabaseController instance;

    static String url = "";
    static String user = "";
    static String pass = "";

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

    // --- Database Connection ---
    /*
      This field will hold the active database connection
      for the entire controller to use.
    */
    private static Connection dbConnection;

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
            connectionToDatabase();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        DatabaseController.instance = this;

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
      Static utility method to safely update the UI status from any static context.
      DatabaseController.updateStatus(...).
     */
    public static void updateStatus(String message, boolean success) {
        if (instance != null) {
            instance.showDbStatus(message, success);
        } else {
            System.err.println("DB Status Update attempted before controller initialized: " + message);
        }
    }

    /*
        connectionToDatabase()
        This method attempts to establish a connection to the database
        and stores it in the `dbConnection` instance field.
    */
    @FXML
    public void connectionToDatabase() {
        url = "jdbc:mysql://localhost:3306/" + dbName.getText().trim();
        user = dbUser.getText().trim();
        pass = dbPass.getText().trim();

        try {
            // Check if a connection is already open and close it if so
            if (dbConnection != null && !dbConnection.isClosed()) {
                dbConnection.close();
                System.out.println("Closed previous connection.");
            }

            dbConnection = DriverManager.getConnection(url, user, pass);
            showDbStatus("Connected successfully!", true);

        } catch (SQLException e) {
            dbConnection = null;
            showDbStatus("Database connection failed: " + e.getMessage(), false);
        }
    }

    /*
        Closes the database connection if it is open.
        This is crucial for preventing memory leaks.
    */
    public void closeConnection() {
        try {
            if (dbConnection != null && !dbConnection.isClosed()) {
                dbConnection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
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
        performRead()
        Read operation for database.
        This method uses the stored `dbConnection` to run a query.
        The return type is a list of Heroes
    */
    @FXML
    public static List<Hero> performRead() {
        List<Hero> heroes = new ArrayList<>();

        try {
            if (dbConnection == null || dbConnection.isClosed()) {
                DatabaseController.updateStatus("Not connected to database.", false);
                return heroes;
            }
        } catch (SQLException e) {
            DatabaseController.updateStatus("Error checking connection: " + e.getMessage(), false);
            return heroes;
        }

        String table = DatabaseController.instance.dbTable.getText().trim().toLowerCase();
        if (table.isEmpty()) {
            DatabaseController.updateStatus("Table name cannot be empty.", false);
            return heroes;
        }

        String sql = "SELECT * FROM " + table;


        try (Statement stmt = dbConnection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Long id = rs.getLong("id");
                String heroName = rs.getString("hero_name");
                String realName = rs.getString("real_name");
                String heroHeadshot = rs.getString("hero_headshot");
                int age = rs.getInt("age");
                double rating = rs.getDouble("rating");
                boolean isActive = rs.getBoolean("is_active");
                String description = rs.getString("description");
                String strengthBase = rs.getString("strength_base");

                Hero hero = new Hero(
                        id, heroName, realName, heroHeadshot, age, rating,
                        isActive, description, strengthBase
                );

                heroes.add(hero);
            }

            DatabaseController.updateStatus("Successfully read data from " + table, true);

        } catch (SQLException e) {
            DatabaseController.updateStatus("Error reading data: " + e.getMessage(), false);
        }

        return heroes;
    }

    /*
        performCreate()
        Create operation for database.
        This method uses the stored `dbConnection` to run a query.This uses PreparedStatement to safely insert data.
        The parameter is hero. This hero object avoids using Id to not confuse the mySQL/Jakarta Blend into thinking this is an update
    */
    public static void performCreate(Hero hero) {

        try {
            if (dbConnection == null || dbConnection.isClosed()) {
                DatabaseController.updateStatus("Not connected to database.", false);
                return;
            }
        } catch (SQLException e) {
            DatabaseController.updateStatus("Error checking connection: " + e.getMessage(), false);
            return;
        }

        String table = DatabaseController.instance.dbTable.getText().trim().toLowerCase();
        if (table.isEmpty()) {
            DatabaseController.updateStatus("Table name cannot be empty.", false);
            return;
        }

        String sql = "INSERT INTO " + table + " (hero_name, real_name, hero_headshot, age, rating, is_active, description, strength_base) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {

            pstmt.setString(1, hero.getHeroName());
            pstmt.setString(2, hero.getRealName());
            pstmt.setString(3, hero.getHeroHeadshot());
            pstmt.setInt(4, hero.getAge());
            pstmt.setDouble(5, hero.getRating());
            pstmt.setBoolean(6, hero.getIsActive());
            pstmt.setString(7, hero.getDescription());
            pstmt.setString(8, hero.getStrengthBase());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                DatabaseController.updateStatus("Successfully created new hero!", true);
            }

        } catch (SQLException e) {
            DatabaseController.updateStatus("Error creating record: " + e.getMessage(), false);
        }
    }

    /*
      performUpdate()
      Update operation for database.
      This method uses the stored `dbConnection` to run a query. This uses PreparedStatement to safely update data.
      the parameter is id, The ID of the record to update
     */
    public static void performUpdate(Hero hero) {

        try {
            if (dbConnection == null || dbConnection.isClosed()) {
                DatabaseController.updateStatus("Not connected to database.", false);
                return;
            }
        } catch (SQLException e) {
            DatabaseController.updateStatus("Error checking connection: " + e.getMessage(), false);
            return;
        }

        String table = DatabaseController.instance.dbTable.getText().trim().toLowerCase();
        if (table.isEmpty()) {
            DatabaseController.updateStatus("Table name cannot be empty.", false);
            return;
        }

        String sql = "UPDATE " + table + " SET "
                + "hero_name = ?, "
                + "real_name = ?, "
                + "hero_headshot = ?, "
                + "age = ?, "
                + "rating = ?, "
                + "is_active = ?, "
                + "description = ?, "
                + "strength_base = ? "
                + "WHERE id = ?";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {

            pstmt.setString(1, hero.getHeroName());
            pstmt.setString(2, hero.getRealName());
            pstmt.setString(3, hero.getHeroHeadshot());
            pstmt.setInt(4, hero.getAge());
            pstmt.setDouble(5, hero.getRating());
            pstmt.setBoolean(6, hero.getIsActive());
            pstmt.setString(7, hero.getDescription());
            pstmt.setString(8, hero.getStrengthBase());;
            pstmt.setLong(9, hero.getId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                DatabaseController.updateStatus("Successfully updated record " + hero.getId(), true);

            } else {
                DatabaseController.updateStatus("No record found with ID " + hero.getId(), false);
            }

        } catch (SQLException e) {
            DatabaseController.updateStatus("Error updating record: " + e.getMessage(), false);
        }
    }

    /*
        performDelete()
        An example CRUD (Delete) operation.
        This uses PreparedStatement to safely delete data.
        The parameter of id is the ID of the record to delete
        The method returns void upon completion
     */
    public static void performDelete(Long id) {

        try {
            if (dbConnection == null || dbConnection.isClosed()) {
                DatabaseController.updateStatus("Not connected to database.", false);
                return;
            }
        } catch (SQLException e) {
            DatabaseController.updateStatus("Error checking connection: " + e.getMessage(), false);
            return;
        }

        String table = DatabaseController.instance.dbTable.getText().trim().toLowerCase();
        if (table.isEmpty()) {
            DatabaseController.updateStatus("Table name cannot be empty.", false);
            return;
        }


        String sql = "DELETE FROM " + table + " WHERE id = ?";


        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                DatabaseController.updateStatus("Successfully deleted record " + id, true);
                System.out.println("Deleted record " + id);
            } else {
                DatabaseController.updateStatus("No record found with ID " + id, false);
            }

        } catch (SQLException e) {
            DatabaseController.updateStatus("Error deleting record: " + e.getMessage(), false);
        }
    }
}

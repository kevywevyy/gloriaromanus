package unsw.gloriaromanus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.plaf.synth.SynthOptionPaneUI;

import org.json.JSONObject;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;


public class GloriaRomanusApplication extends Application {

  private static GloriaRomanusController controller;

  @FXML
  private GridPane gridPane;

  @FXML
  private Button newGameButton;

  @FXML
  private Button loadGameButton;

  @FXML
  private Button exitGameButton;

  @FXML
  private Label playerNo;

  @FXML
  private Button romansButton;

  @FXML
  private Button gaulsButton;

  @FXML
  private Button backButton;

  @FXML
  private Pane pane;

  // @Override
  // public void start(Stage stage) throws IOException {
  //   // set up the scene
  //   FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
  //   Parent root = loader.load();
  //   controller = loader.getController();
  //   Scene scene = new Scene(root);

  //   // set up the stage
  //   stage.setTitle("Gloria Romanus");
  //   stage.setWidth(800);
  //   stage.setHeight(700);
  //   stage.setScene(scene);
  //   stage.show();

  // }
  @Override
  public void start(Stage stage) throws IOException {
    // set up the scene
    FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
    loader.setController(this);
    Parent root = loader.load();
    Scene scene = new Scene(root);

    // set up the stage
    stage.setTitle("Gloria Romanus");
    stage.setWidth(1000);
    stage.setHeight(800);
    stage.setScene(scene);
    stage.show();
    playMedia("minecraft.mp3");
  }
  private MediaPlayer mediaPlayer;
  public void playMedia(String path) {
        Media media = new Media(getClass().getResource(path).toString());
        mediaPlayer = new MediaPlayer(media);
        System.out.println("Playing music");
        mediaPlayer.setVolume(1);
        mediaPlayer.play(); {
            mediaPlayer.setOnEndOfMedia(new Runnable() {
                public void run() {
                    mediaPlayer.seek(Duration.ZERO);
                }
            });
            mediaPlayer.play();
        }
  }

  @FXML
  public void handleBackButton (ActionEvent event) throws IOException {
      // set up the scene
      FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
      loader.setController(this);
      Parent root = loader.load();
      Scene scene = new Scene(root);

      // set up the stage
      Stage newGameWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
      newGameWindow.setTitle("Gloria Romanus");
      newGameWindow.setWidth(1000);
      newGameWindow.setHeight(800);
      newGameWindow.setScene(scene);
      newGameWindow.show();
  }

  @FXML
  public void handleNewGameButton (ActionEvent event) throws IOException {
      // FXMLLoader newGame = FXMLLoader.load(getClass().getResource("selectFaction.fxml"));
      // Scene newGameScene = new Scene(newGame);
      
      // Stage newGameWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
      // newGameWindow.setScene(newGameScene);
      // newGameWindow.show();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("selectFaction.fxml"));
      loader.setController(this);
      Parent root = loader.load();
      Scene scene = new Scene(root);
  
      // set up the stage
      Stage newGameWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
      newGameWindow.setTitle("Gloria Romanus");
      newGameWindow.setWidth(1000);
      newGameWindow.setHeight(800);
      newGameWindow.setScene(scene);
      newGameWindow.show();
  }

  @FXML 
  public void handleLoadGameButton (ActionEvent event) throws IOException {
      String file = Files.readString(Paths.get("src/unsw/gloriaromanus/SaveFile.json"));
      JSONObject fileContents = new JSONObject(file);
      String romansPlayer = fileContents.getString("romansPlayer");
      // String gaulsPlayer = fileContents.getString("gaulsPlayer");
      boolean player0Turn = fileContents.getBoolean("player0Turn");

      // grab data from json save file,change following based off it!
      FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
      Parent root = loader.load();
      controller = loader.getController();
      controller.setSaveFile(fileContents);
      controller.loadSave();
      if (romansPlayer.equals("player0")) {
          // player 0 picked romans at the start
          controller.setPlayer0Faction("Romans");
          controller.setPlayer1Faction("Gauls");
          controller.getGame().getFactionByName("Romans").setPlayer("player0");
          controller.getGame().getFactionByName("Gauls").setPlayer("player1");
      } else {
          // player 0 picked gauls
          controller.setPlayer0Faction("Gauls");
          controller.setPlayer1Faction("Romans");
          controller.getGame().getFactionByName("Gauls").setPlayer("player0");
          controller.getGame().getFactionByName("Romans").setPlayer("player1");
      }

      if (player0Turn) {
          // If it was player 0's turn when they saved
          controller.setHumanFaction(controller.getPlayer0Faction());
      } else {
          // setting humanFaction to player0Faction
          controller.setHumanFaction(controller.getPlayer1Faction());
      }
      
      // populating initial invasionMenu info
      controller.updateFactionInfo();
      // load goals
      controller.loadGoals();
      // loader.setController(controller);
      // System.out.println("Player0's faction is " + controller.getPlayer0Faction());
      // System.out.println("Player1's faction is " + controller.getPlayer1Faction());
      Scene scene = new Scene(root);
      Stage newGameWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
      // set up the stage
      newGameWindow.setTitle("Gloria Romanus");
      newGameWindow.setWidth(1000);
      newGameWindow.setHeight(800);
      newGameWindow.setScene(scene);
      newGameWindow.show();
  }

  @FXML
  public void handleRomansButton(ActionEvent event) throws IOException {
      // if player0 picks the roman option
      // set the player0 to have control over Romans
      // automatically set player1 to have control over Gauls
      // enter main.fxml
      System.out.println("romansbutton pressed");
      FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
      Parent root = loader.load();
      controller = loader.getController();
      controller.setPlayer0Faction("Romans");
      controller.setPlayer1Faction("Gauls");
      // setting humanFaction to player0Faction
      controller.setHumanFaction(controller.getPlayer0Faction());
      // populating initial invasionMenu info
      controller.updateFactionInfo();
      // load goals
      controller.loadGoals();
      // Set player references in each Faction object
      controller.getGame().getFactionByName("Romans").setPlayer("player0");
      controller.getGame().getFactionByName("Gauls").setPlayer("player1");

      // loader.setController(controller);
      // System.out.println("Player0's faction is " + controller.getPlayer0Faction());
      // System.out.println("Player1's faction is " + controller.getPlayer1Faction());
      Scene scene = new Scene(root);
      Stage newGameWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
      // set up the stage
      newGameWindow.setTitle("Gloria Romanus");
      newGameWindow.setWidth(1000);
      newGameWindow.setHeight(800);
      newGameWindow.setScene(scene);
      newGameWindow.show();
  }

  @FXML
  public void handleGaulsButton(ActionEvent event) throws IOException {
      System.out.println("gauls button pressed");
      FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
      Parent root = loader.load();
      controller = loader.getController();
      controller.setPlayer0Faction("Gauls");
      controller.setPlayer1Faction("Romans");

      // setting humanFaction to player0Faction
      controller.setHumanFaction(controller.getPlayer0Faction());
      // populating initial invasionMenu info
      controller.updateFactionInfo();
      // load goals
      controller.loadGoals();
      // Set player references in each Faction object
      controller.getGame().getFactionByName("Gauls").setPlayer("player0");
      controller.getGame().getFactionByName("Romans").setPlayer("player1");

      // loader.setController(controller);
      // System.out.println("Player0's faction is " + controller.getPlayer0Faction());
      // System.out.println("Player1's faction is " + controller.getPlayer1Faction());

      Scene scene = new Scene(root);
      Stage newGameWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
      // set up the stage
      newGameWindow.setTitle("Gloria Romanus");
      newGameWindow.setWidth(1000);
      newGameWindow.setHeight(800);
      newGameWindow.setScene(scene);
      newGameWindow.show();
  }

  @FXML
  public void handleExitGameButton(ActionEvent event) {
    Stage stage = (Stage) exitGameButton.getScene().getWindow();
    // do what you have to do
    stage.close();
  }

  // @Override
  // public void start(Stage stage) throws IOException {
  //   // set up the scene
  //   FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
  //   loader.setController(new MenuController());

  //   Parent root = loader.load();
  //   // controller = loader.getController();
  //   Scene scene = new Scene(root);

  //   // set up the stage
  //   stage.setTitle("Gloria Romanus");
  //   stage.setWidth(800);
  //   stage.setHeight(700);
  //   stage.setScene(scene);
  //   stage.show();

  // }
  // /**
  //  * Stops and releases all resources used in application.
  //  */
  // @Override
  // public void stop() {
  //   controller.terminate();
  // }

  /**
   * Opens and runs application.
   *
   * @param args arguments passed to this application
   */
  public static void main(String[] args) {

    Application.launch(args);
  }
}

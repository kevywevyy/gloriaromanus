package unsw.gloriaromanus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.GeoPackage;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol.HorizontalAlignment;
import com.esri.arcgisruntime.symbology.TextSymbol.VerticalAlignment;
import com.esri.arcgisruntime.data.Feature;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.util.Pair;

public class GloriaRomanusController{
  private GloriaRomanusApplication parent;
  @FXML
  private MapView mapView;

  @FXML
  private StackPane stackPaneMain;

  // could use ControllerFactory?
  private ArrayList<Pair<MenuController, VBox>> controllerParentPairs;

  private ArcGISMap map;

  private Map<String, String> provinceToOwningFactionMap;

  private Map<String, Integer> provinceToNumberTroopsMap;

  private String humanFaction;

  private Feature currentlySelectedHumanProvince; // Human province is the source province
  private Feature currentlySelectedEnemyProvince; // Enemy province is the target province (either ally or enemy)

  private FeatureLayer featureLayer_provinces;
  private JSONObject saveFile;
  private MediaPlayer mediaPlayer;
  // the instance of the game
  private GameState game;
  private String player0Faction; // faction of player0
  private String player1Faction;
  private int menu = 0; // either menu index 0 or menu index 1 (start off with invade menu)
  public GloriaRomanusController() {
    // making sure controller is created ONCE only
    System.out.println("haha yes");
  }
  @FXML
  private void initialize() throws JsonParseException, JsonMappingException, IOException, InterruptedException {
    // We start a fresh game
    game = new GameState(getSaveFile());

    // TODO = you should rely on an object oriented design to determine ownership
    provinceToOwningFactionMap = getProvinceToOwningFactionMap();

    provinceToNumberTroopsMap = new HashMap<String, Integer>();
    // Displaying the correct number of troops we initialise the game with in each province
    updateProvNumTroopsMap();  
    // TODO = load this from a configuration file you create (user should be able to
    // select in loading screen)

    currentlySelectedHumanProvince = null;
    currentlySelectedEnemyProvince = null;

    String []menus = {"invasion_menu.fxml", "basic_menu.fxml", "trainingMenu.fxml", "allTroops.fxml", "romansPrompt.fxml", "gaulsPrompt.fxml"};
    controllerParentPairs = new ArrayList<Pair<MenuController, VBox>>();
    for (String fxmlName: menus){
      System.out.println(fxmlName);
      FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
      VBox root = (VBox)loader.load();
      MenuController menuController = (MenuController)loader.getController();
      menuController.setParent(this);
      controllerParentPairs.add(new Pair<MenuController, VBox>(menuController, root));
    }

    stackPaneMain.getChildren().add(controllerParentPairs.get(getMenu()).getValue());

    initializeProvinceLayers();

  }

  public void loadSave() throws IOException {
    game = new GameState(getSaveFile());
    provinceToOwningFactionMap = getProvinceToOwningFactionMap();
    // addAllPointGraphics();
    updateProvNumTroopsMap();
    // updateFactionInfo();
    // TODO: fixing bug
    // addAllPointGraphics();
    initializeProvinceLayers();
  }

  public void clickedSaveGame(ActionEvent e) throws IOException {
    printMessageToTerminal("Saving game...");
    getGame().saveGame();
  }

  @FXML
  private Label winnerFaction;
  @FXML
  private Label loserFaction;

  public void clickedEndTurn(ActionEvent e) throws IOException {
    printMessageToTerminal("Ending "+ getHumanFaction()+" turn...");
    // changes human faction to the next faction!
    String prevFaction = getHumanFaction();
    if (getHumanFaction().equals("Romans")) {
      setHumanFaction("Gauls");
    } else {
      setHumanFaction("Romans");
    }
    // ends the turn - call gamestate end turn function...
    getGame().getFactionByName(prevFaction).endTurn();
    // resets the goal pane
    ((InvasionMenuController)controllerParentPairs.get(0).getKey()).clearGoalContainer();
    // check if goals have been completed
    System.out.println("Human faction NOW is "+getHumanFaction());
    ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setTabs("");
    loadGoals();
    // If goals completed, go ahead and declare victory!
    Faction currFac = getGame().getFactionByName(getHumanFaction());
    if (winnerFaction == null) {
      winnerFaction = new Label();
    }
    if (loserFaction == null) {
      loserFaction = new Label();
    }
    if(currFac.winConditions.completed(currFac, getGame()) && getGame().isNoWinnerYet() == true) {
      printMessageToTerminal(getHumanFaction()+" have won!");
      // when currFaction wins, open prompt
      if (getHumanFaction().equals("Romans")) {
        stackPaneMain.getChildren().add(controllerParentPairs.get(4).getValue());
      } else {
        stackPaneMain.getChildren().add(controllerParentPairs.get(5).getValue());
      }      
      music("winGame.mp3");
      // save game 
      getGame().setNoWinnerYet(false);
      printMessageToTerminal("Saving game...");
      getGame().saveGame();
    }

    // Update wealth/all the other stuff in the menu.
    updateFactionInfo();
    updateProvNumTroopsMap();
    resetSelections();
    controllerParentPairs.get(2).getKey().clearProvTrainingData();
    
  }

  /**
   * Called at the start of each new turn, this updates the labels in invasionMenu with relevant faction info.
   */
  public void updateFactionInfo() {
    int year = getGame().getYear();
    String faction = getHumanFaction();
    int treasury = getGame().getFactionByName(faction).getTreasury();
    int wealth = getGame().getFactionByName(faction).getTotalWealth();

    ((InvasionMenuController)controllerParentPairs.get(0).getKey()).updateFactionInfo(year, faction, treasury, wealth);
  }

  /**
   * Loads the goals into the visual invasion menu
   */
  public void loadGoals() {
    Faction currFaction = getGame().getFactionByName(getHumanFaction());
    ((InvasionMenuController)controllerParentPairs.get(0).getKey()).loadGoals(currFaction.getWinConditions());
  }

  /**
   * updates the troop details on the map 
   */
  public void updateProvNumTroopsMap() throws JsonParseException, JsonMappingException, IOException {
    for (String pName : provinceToOwningFactionMap.keySet()) {
      Province p = game.findProvinceByName(pName);
      // take info about province's troops from backend and display it on the frontend
      int count = 0;
      if (p.getUnits().size() > 0) {
          for (Unit u : p.getUnits()) {
              count += u.getNumTroops();
          }
      }
      provinceToNumberTroopsMap.put(pName, count);
    }

    addAllPointGraphics();
  }

  /**
   * Implementation of Battle Resolver inside the invasion interaction
   */
  public void clickedInvadeButton(ActionEvent e) throws IOException {
    // Determine which faction's turn it is rn and call its relevant invade() method
    MenuController m = controllerParentPairs.get(1).getKey();
    if (m == null) {
      System.out.println("MENUCONTROLLER IS NULL IN CLICK INVADE BUTTON");
      // return;
    }

    // Get the current faction object
    Faction curr = getGame().getFactionByName(humanFaction);
    if (currentlySelectedHumanProvince != null && currentlySelectedEnemyProvince != null){
        String humanProvince = (String)currentlySelectedHumanProvince.getAttributes().get("name");
        String enemyProvince = (String)currentlySelectedEnemyProvince.getAttributes().get("name");
        if (confirmIfProvincesConnected(humanProvince, enemyProvince)){
          ArrayList<Integer> invadingArmy = new ArrayList<Integer>();
          if (m.getUnitGroupIDs().size() > 0) {
            invadingArmy = m.getUnitGroupIDs();
          
            int result = curr.invade(humanProvince, enemyProvince, invadingArmy);
            if (result == -1) {
                printMessageToTerminal("There was an issue with your invading army, cannot invade! Check if they're all eligible to move");
            } else if (result == 0) {
                System.out.println("fail invasion sound effect");
                music("damage.mp3");
                printMessageToTerminal("Your army lost the battle! If there are any remaining units, they've returned back to your province.");
            } else if (result == 1) {
                System.out.println("win invasion sound effect");
                music("winInvade.mp3");
                printMessageToTerminal("Your army won the battle! Province conquered!");
                // Updates the ownership of the province depending on the result
                // and reflects it on the backend
                // TODO: investigating map not displaying ownership properly upon loading game
                provinceToOwningFactionMap.put(enemyProvince, humanFaction);
            }  
            
            updateProvNumTroopsMap();  
          } else {
            printMessageToTerminal("Seems like you haven't made a unit group for your invasion yet!");
          }
          resetSelections();  // reset selections in UI
        } else {
          printMessageToTerminal("Provinces not adjacent, cannot invade!");
        }

    }
  }
  
  public void music(String path) {
        Media media = new Media(getClass().getResource(path).toString());
        mediaPlayer = new MediaPlayer(media);
        System.out.println("Playing music");
        mediaPlayer.setVolume(0.4);
        mediaPlayer.play();
  }
  /**
   * run this initially to update province owner, change feature in each
   * FeatureLayer to be visible/invisible depending on owner. Can also update
   * graphics initially
   */
  private void initializeProvinceLayers() throws JsonParseException, JsonMappingException, IOException {

    Basemap myBasemap = Basemap.createImagery();
    // myBasemap.getReferenceLayers().remove(0);
    map = new ArcGISMap(myBasemap);
    mapView.setMap(map);

    // note - tried having different FeatureLayers for AI and human provinces to
    // allow different selection colors, but deprecated setSelectionColor method
    // does nothing
    // so forced to only have 1 selection color (unless construct graphics overlays
    // to give color highlighting)
    GeoPackage gpkg_provinces = new GeoPackage("src/unsw/gloriaromanus/provinces_right_hand_fixed.gpkg");
    gpkg_provinces.loadAsync();
    gpkg_provinces.addDoneLoadingListener(() -> {
      if (gpkg_provinces.getLoadStatus() == LoadStatus.LOADED) {
        // create province border feature
        featureLayer_provinces = createFeatureLayer(gpkg_provinces);
        map.getOperationalLayers().add(featureLayer_provinces);

      } else {
        System.out.println("load failure");
      }
    });

    addAllPointGraphics();
  }

  private void addAllPointGraphics() throws JsonParseException, JsonMappingException, IOException {
    mapView.getGraphicsOverlays().clear();

    InputStream inputStream = new FileInputStream(new File("src/unsw/gloriaromanus/provinces_label.geojson"));
    FeatureCollection fc = new ObjectMapper().readValue(inputStream, FeatureCollection.class);

    GraphicsOverlay graphicsOverlay = new GraphicsOverlay();

    for (org.geojson.Feature f : fc.getFeatures()) {
      if (f.getGeometry() instanceof org.geojson.Point) {
        org.geojson.Point p = (org.geojson.Point) f.getGeometry();
        LngLatAlt coor = p.getCoordinates();
        Point curPoint = new Point(coor.getLongitude(), coor.getLatitude(), SpatialReferences.getWgs84());
        PictureMarkerSymbol s = null;
        String province = (String) f.getProperty("name");
        String faction = provinceToOwningFactionMap.get(province);

        TextSymbol t = new TextSymbol(10,
            faction + "\n" + province + "\n" + provinceToNumberTroopsMap.get(province), 0xFFFF0000,
            HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);

        switch (faction) {
          case "Gauls":
            // note can instantiate a PictureMarkerSymbol using the JavaFX Image class - so could
            // construct it with custom-produced BufferedImages stored in Ram
            // http://jens-na.github.io/2013/11/06/java-how-to-concat-buffered-images/
            // then you could convert it to JavaFX image https://stackoverflow.com/a/30970114

            // you can pass in a filename to create a PictureMarkerSymbol...
            s = new PictureMarkerSymbol(new Image((new File("images/Celtic_Druid.png")).toURI().toString()));
            break;
          case "Romans":
            // you can also pass in a javafx Image to create a PictureMarkerSymbol (different to BufferedImage)
            s = new PictureMarkerSymbol("images/legionary.png");
            break;
          // TODO = handle all faction names, and find a better structure...
        }
        t.setHaloColor(0xFFFFFFFF);
        t.setHaloWidth(2);
        Graphic gPic = new Graphic(curPoint, s);
        Graphic gText = new Graphic(curPoint, t);
        graphicsOverlay.getGraphics().add(gPic);
        graphicsOverlay.getGraphics().add(gText);
      } else {
        System.out.println("Non-point geo json object in file");
      }

    }

    inputStream.close();
    mapView.getGraphicsOverlays().add(graphicsOverlay);
  }

  /**
   * Added implementation where we can select and deselect province methodology
   * Reselecting a selected source province will reset all selections
   * Also when deselected source province, the menus such as training and province menu
   * details will be set to null it was deselected and reinitialised when reselected
   */
  private FeatureLayer createFeatureLayer(GeoPackage gpkg_provinces) {
    FeatureTable geoPackageTable_provinces = gpkg_provinces.getGeoPackageFeatureTables().get(0);

    // Make sure a feature table was found in the package
    if (geoPackageTable_provinces == null) {
      System.out.println("no geoPackageTable found");
      return null;
    }

    // Create a layer to show the feature table
    FeatureLayer flp = new FeatureLayer(geoPackageTable_provinces);
    // We add the updateProvinceMenu() upon each click 
    // https://developers.arcgis.com/java/latest/guide/identify-features.htm
    // listen to the mouse clicked event on the map view
    mapView.setOnMouseClicked(e -> {
      // was the main button pressed?
      if (e.getButton() == MouseButton.PRIMARY) {
        // get the screen point where the user clicked or tapped
        Point2D screenPoint = new Point2D(e.getX(), e.getY());

        // specifying the layer to identify, where to identify, tolerance around point,
        // to return pop-ups only, and
        // maximum results
        // note - if select right on border, even with 0 tolerance, can select multiple
        // features - so have to check length of result when handling it
        final ListenableFuture<IdentifyLayerResult> identifyFuture = mapView.identifyLayerAsync(flp,
            screenPoint, 0, false, 25);

        // add a listener to the future
        // identifyFuture determines which province i clicked
        identifyFuture.addDoneListener(() -> {
          try {
            // get the identify results from the future - returns when the operation is
            // complete
            IdentifyLayerResult identifyLayerResult = identifyFuture.get();
            // a reference to the feature layer can be used, for example, to select
            // identified features
            if (identifyLayerResult.getLayerContent() instanceof FeatureLayer) {
              FeatureLayer featureLayer = (FeatureLayer) identifyLayerResult.getLayerContent();
              // select all features that were identified
              List<Feature> features = identifyLayerResult.getElements().stream().map(f -> (Feature) f).collect(Collectors.toList());

              if (features.size() > 1) {
                printMessageToTerminal("Have more than 1 element - you might have clicked on boundary!");
              }
              else if (features.size() == 1){
                // note maybe best to track whether selected...
                Feature f = features.get(0);
                String province = (String)f.getAttributes().get("name");
                boolean deselected = false;

                // Allows you to deselect some provinces
                if (currentlySelectedEnemyProvince != null && province.equals((String)currentlySelectedEnemyProvince.getAttributes().get("name"))) {
                    // If the clicked on province is the currently selected enemy province, deselect it.
                    featureLayer.unselectFeature(currentlySelectedEnemyProvince); 
                    currentlySelectedEnemyProvince = null;
                    if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){ 
                      ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setOpponentProvince("");
                    }
                    deselected = true;
                } else if (currentlySelectedHumanProvince != null && currentlySelectedEnemyProvince != null && province.equals((String)currentlySelectedHumanProvince.getAttributes().get("name"))) {
                    // If you clicked on the "human Province", i.e. the province you're managing right now
                    // Resets all selections, both "human" and "enemy" province
                    featureLayer.unselectFeature(currentlySelectedHumanProvince); 
                    currentlySelectedHumanProvince = null;
                    if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){ 
                      ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setInvadingProvince("");
                    }
                    featureLayer.unselectFeature(currentlySelectedEnemyProvince); 
                    currentlySelectedEnemyProvince = null;
                    if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
                      ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setOpponentProvince("");
                    }
                    deselected = true;
                } else if (currentlySelectedHumanProvince != null && province.equals((String)currentlySelectedHumanProvince.getAttributes().get("name"))) {
                  // Human province is only thing selected, so just deselect that
                  featureLayer.unselectFeature(currentlySelectedHumanProvince); 
                  currentlySelectedHumanProvince = null;
                  if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){ 
                    ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setInvadingProvince("");
                  }
                  deselected = true;
                }

                if (!deselected) {
                  // If the selection is of a province you own, two things could happen:
                  // If you already selected a friendly province beforehand, this will make the newly selected province
                  // an "enemy Province", so we can select two friendly provinces at once for movement purposes
                  // If not, friendly province is set to whatever new selection is.
                  if (checkOwnedProvince(province)){
                    // newly selected province owned by human
                    // we allow a max of two selections 
                    if (currentlySelectedHumanProvince == null) {
                        // Then nothing has been selected on the map and we set the human province to whatever we select
                        currentlySelectedHumanProvince = f;
                        // TODO: do we even need the if statement check anymore?
                        if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
                          ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setInvadingProvince(province);
                        }
                        controllerParentPairs.get(1).getKey().updateSelectedSourceProv(province);
                        // after clicking identifyFuture will update the units ListView
                        ArrayList<Unit> unitsList = getGame().findProvinceByName(province).getUnits();
                        controllerParentPairs.get(1).getKey().updateUnitsListView(unitsList);
                        controllerParentPairs.get(1).getKey().populateTaxMenu();
                        controllerParentPairs.get(2).getKey().populateTrainingMenu();
                        controllerParentPairs.get(3).getKey().populateTroopDetails();
                        controllerParentPairs.get(2).getKey().updateTrainingTroops();
                    } else if (currentlySelectedEnemyProvince != null) {
                        // We want to select another human province in addition to the currently selected one, but enemy province was previously selected
                        featureLayer.unselectFeature(currentlySelectedEnemyProvince); 
                        currentlySelectedEnemyProvince = f;
                        if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
                          ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setOpponentProvince(province);
                        }
                        controllerParentPairs.get(1).getKey().updateSelectedTargetProv(province);                
                    } else {
                        // Enemy province selection is null rn
                        currentlySelectedEnemyProvince = f; 
                        if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
                          ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setOpponentProvince(province);
                        }
                        controllerParentPairs.get(1).getKey().updateSelectedTargetProv(province);                   
                    }

                  } else {
                    // We've selected an enemy province
                    if (currentlySelectedEnemyProvince != null){
                      featureLayer.unselectFeature(currentlySelectedEnemyProvince);
                    }
                    currentlySelectedEnemyProvince = f;
                    if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
                      ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setOpponentProvince(province);
                    }
                    controllerParentPairs.get(1).getKey().updateSelectedTargetProv(province);                
                  }
                  featureLayer.selectFeature(f);
                } else {
                  // clear the ListView of units, and clear the province reference in MenuControl
                  controllerParentPairs.get(1).getKey().clearSelectedProvData();
                  controllerParentPairs.get(2).getKey().clearProvTrainingData();           
                }
              }

              
            }
            // after clicking identifyFuture will update the currentlySelectedHumanProvince
            // controllerParentPairs.get(1).get() will get the specific controller
            controllerParentPairs.get(1).getKey().updateProvinceMenu();
            
          } catch (InterruptedException | ExecutionException ex) {
            // ... must deal with checked exceptions thrown from the async identify
            // operation
            System.out.println("InterruptedException occurred");
          }
        });
      }
    });
    return flp;
  }

  /**
   * Takes in the province's name and checks whether the "humanFaction" owns it
   * @param province
   * @return True if the province is owned by the current turn's humanFaction
   */
  public boolean checkOwnedProvince(String province) {
      return (provinceToOwningFactionMap.get(province).equals(humanFaction));
  }

  private Map<String, String> getProvinceToOwningFactionMap() throws IOException {
    Map<String, String> m = new HashMap<String, String>();
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    if (getSaveFile() == null) {
      for (String key : ownership.keySet()) {
        // key will be the faction name
        JSONArray ja = ownership.getJSONArray(key);
        // value is province name
        for (int i = 0; i < ja.length(); i++) {
          String value = ja.getString(i);
          m.put(value, key);
        }
      }
    } else {
      // load province ownership from savefile
      JSONArray provincesJSON = getSaveFile().getJSONArray("provinces");

      for (int i = 0; i < provincesJSON.length(); i++) {
        JSONObject data = provincesJSON.getJSONObject(i);
        String name = data.getString("name");
        String owner = data.getString("owner");
        m.put(name, owner);
      }
    }
    
    return m;
  }

  private ArrayList<String> getHumanProvincesList() throws IOException {
    // https://developers.arcgis.com/labs/java/query-a-feature-layer/

    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    return ArrayUtil.convert(ownership.getJSONArray(humanFaction));
  }

  /**
   * returns query for arcgis to get features representing human provinces can
   * apply this to FeatureTable.queryFeaturesAsync() pass string to
   * QueryParameters.setWhereClause() as the query string
   */
  private String getHumanProvincesQuery() throws IOException {
    LinkedList<String> l = new LinkedList<String>();
    for (String hp : getHumanProvincesList()) {
      l.add("name='" + hp + "'");
    }
    return "(" + String.join(" OR ", l) + ")";
  }

  private boolean confirmIfProvincesConnected(String province1, String province2) throws IOException {
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
    JSONObject provinceAdjacencyMatrix = new JSONObject(content);
    return provinceAdjacencyMatrix.getJSONObject(province1).getBoolean(province2);
  }

  private void resetSelections(){
    if (currentlySelectedEnemyProvince != null && currentlySelectedHumanProvince != null) {
      featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedEnemyProvince, currentlySelectedHumanProvince));
      currentlySelectedEnemyProvince = null;
      currentlySelectedHumanProvince = null;
      if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
        ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setInvadingProvince("");
        ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setOpponentProvince("");
      }
    } else if (currentlySelectedHumanProvince != null) {
      // just reset currently selected human province
      featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedHumanProvince));
      currentlySelectedHumanProvince = null;
      if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
        ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setInvadingProvince("");
      }
    } else if (currentlySelectedEnemyProvince != null) {
      // just reset currently selected enemy province
      featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedEnemyProvince));
      currentlySelectedEnemyProvince = null;
      if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
        ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setOpponentProvince("");
      }
    }
  }

  public void printMessageToTerminal(String message){
    if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
      ((InvasionMenuController)controllerParentPairs.get(0).getKey()).appendToTerminal(message);
    }
  }

  /**
   * Stops and releases all resources used in application.
   */
  void terminate() {

    if (mapView != null) {
      mapView.dispose();
    }
  }
  /**
   * Upon clicking the province menu button from invade, 
   * the invade menu will appear on the right hand side of the screen
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  public void goToProvinceMenu() throws JsonParseException, JsonMappingException, IOException {
    System.out.println("trying to go to province menu from invade");
    if (menu == 0) {
      this.menu = 1;
      stackPaneMain.getChildren().add(controllerParentPairs.get(getMenu()).getValue());
    }
  }

  /**
   * Going back to province menu from training menu
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  public void goToProvinceMenuFromTraining() throws JsonParseException, JsonMappingException, IOException {
    System.out.println("trying to go back to province menu from training menu");
    stackPaneMain.getChildren().remove(controllerParentPairs.get(getMenu()).getValue());
    this.menu = 1;
    stackPaneMain.getChildren().add(controllerParentPairs.get(getMenu()).getValue());
  }

  /**
   * When clicked the training button in the province menu, we will 
   * be redirected to the training menu
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  public void goToTrainingMenu() throws JsonParseException, JsonMappingException, IOException {
    System.out.println("trying to switch to training menu from province");
    stackPaneMain.getChildren().remove(controllerParentPairs.get(getMenu()).getValue());
    this.menu = 2;
    stackPaneMain.getChildren().add(controllerParentPairs.get(getMenu()).getValue());
  }

  /**
   * Close Province Menu button will close the province menu on the right hand 
   * leaving with only the invade menu on the left at all times
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  public void switchBackToInvasionMenu() throws JsonParseException, JsonMappingException, IOException {
    System.out.println("trying to go back to invade menu from province menu");
    stackPaneMain.getChildren().remove(controllerParentPairs.get(getMenu()).getValue());
    this.menu = 0;
    // stackPaneMain.getChildren().add(controllerParentPairs.get(getMenu()).getValue());
  }

  /**
   * Will be directed to a menu which can allow users to select to view
   * all details of specific troops in the game
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  public void goToAllTroopMenu() throws JsonParseException, JsonMappingException, IOException {
    System.out.println("trying to switch to all troop details menu");
    stackPaneMain.getChildren().remove(controllerParentPairs.get(getMenu()).getValue());
    this.menu = 3;
    stackPaneMain.getChildren().add(controllerParentPairs.get(getMenu()).getValue());
  }
  /**
   * Prompt shows for when specific faction finishes all goals
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  public void closeRomansPrompt() throws JsonParseException, JsonMappingException, IOException {
    System.out.println("trying winner prompt");
    this.menu = 4;
    stackPaneMain.getChildren().remove(controllerParentPairs.get(getMenu()).getValue());
  }
  public void closeGaulsPrompt() throws JsonParseException, JsonMappingException, IOException {
    System.out.println("trying winner prompt");
    this.menu = 5;
    stackPaneMain.getChildren().remove(controllerParentPairs.get(getMenu()).getValue());
  }

  public GameState getGame() {
      return game;
  }

  public void setGame(GameState game) {
      this.game = game;
  }

  public String getPlayer0Faction() {
    return player0Faction;
  }

  public void setPlayer0Faction(String player0Faction) {
    this.player0Faction = player0Faction;
  }

  public String getPlayer1Faction() {
    return player1Faction;
  }

  public void setPlayer1Faction(String player1Faction) {
    this.player1Faction = player1Faction;
  }

  public Feature getCurrentlySelectedHumanProvince() {
    return currentlySelectedHumanProvince;
  }

  public void setCurrentlySelectedHumanProvince(Feature currentlySelectedHumanProvince) {
    this.currentlySelectedHumanProvince = currentlySelectedHumanProvince;
  }

  public int getMenu() {
    return menu;
  }

  public void setMenu(int menu) {
    this.menu = menu;
  }

  public String getHumanFaction() {
    return humanFaction;
  }

  public void setHumanFaction(String humanFaction) {
    this.humanFaction = humanFaction;
  }

  public JSONObject getSaveFile() {
    return saveFile;
  }

  public void setSaveFile(JSONObject saveFile) {
    this.saveFile = saveFile;
  }
}

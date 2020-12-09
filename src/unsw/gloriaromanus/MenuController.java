package unsw.gloriaromanus;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.esri.arcgisruntime.data.Feature;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

// THIS CONTROLLER SHOULD CONTROL THE INGAME UI
public class MenuController {
    private GloriaRomanusController parent;
    private Province selectedSourceProv;
    private Province selectedTargetProv;
    private ArrayList<Integer> unitGroupIDs = new ArrayList<Integer>();
    private UnitFactory unit = null;
    private List<String> troopNames;
    private String selectedUnit;
    @FXML
    private URL location; // has to be called location
    @FXML
    private Label provinceLabel;
    @FXML
    private Label wealthLabel;
    @FXML
    private ChoiceBox<String> taxOptionDropdown;
    @FXML
    private ChoiceBox<String> selectedTroopChoiceBox;
    @FXML
    private ChoiceBox<String> troopToTrainChoiceBox;
    @FXML
    private ListView<String> availableUnitsList;
    @FXML
    private Label troopSizeNum;
    @FXML
    private Label attackNum;
    @FXML
    private Label speedNum;
    @FXML
    private Label armourNum;
    @FXML
    private Label moraleNum;
    @FXML
    private Label chargeStatNum;
    @FXML
    private Label defSkillNum;
    @FXML
    private Label shieldDefNum;
    @FXML
    private Label troopGoldNum;
    @FXML 
    private Label mpNum;
    @FXML 
    private Label trainTimeNum;

    
    public void setParent(GloriaRomanusController parent) {
        if (parent == null){
            System.out.println("GOT NULL");
        }
        this.parent = parent;
    }

    public GloriaRomanusController getParent(){
        return parent;
    }

    public void clearTaxMenu() {
        taxOptionDropdown.getItems().clear();
        taxOptionDropdown.setValue("");
    }

    /**
     * populateTaxMenu contains the choiceBox of all Tax types
     * and will save the specific selected tax rate for each specific province
     */
    public void populateTaxMenu() {
        // get selected province's tax
        // System.out.println("CURRENT TaxRate is "+ getSelectedSourceProv().getTaxRateName());
        String taxNames[] = {"LowTax", "NormalTax", "HighTax", "VeryHighTax"};
        taxOptionDropdown.setItems(FXCollections.observableArrayList(taxNames)); 
        taxOptionDropdown.setValue(getSelectedSourceProv().getTaxRateName());
        
        taxOptionDropdown.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() { 
  
            // if the item of the list is changed 
            public void changed(ObservableValue<? extends Number> ov, Number value, Number newValue) { 
                // set the text for the label to the selected item 
                if (newValue.intValue() == -1) {
                    // do nothing
                    return;
                } 

                if (!getSelectedSourceProv().getTaxRateName().equals(taxNames[newValue.intValue()])) {
                    getParent().printMessageToTerminal(getSelectedSourceProv().getName()+ " now has "+ taxNames[newValue.intValue()]);
                }

                String playerFaction = parent.getHumanFaction();
                Faction playerFacObject = parent.getGame().getFactionByName(playerFaction);
                playerFacObject.setTax(getSelectedSourceProv().getName(), taxNames[newValue.intValue()]);
                taxOptionDropdown.setValue(getSelectedSourceProv().getTaxRateName());
            } 
        }); 
    }

    /**
     * populateTaxMenu contains the choiceBox of all units to be trained
     */
    public void populateTrainingMenu() {
        selectedUnit = "";
        if (unit == null) {
            try {
                unit = new UnitFactory();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (troopNames == null) {
            troopNames = unit.getAllTroopName();
        }
        // loop through the json file and add the names into the troopNames[]
        troopNames = unit.getAllTroopName();

        troopToTrainChoiceBox.setItems(FXCollections.observableArrayList(troopNames)); 
        troopToTrainChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number value, Number newValue) { 
                if (newValue.intValue() == -1) {
                    // do nothing
                    return;
                } 

                troopToTrainChoiceBox.setValue(getTroopNamesFromList(newValue.intValue()));
                selectedUnit = getTroopNamesFromList(newValue.intValue());
            } 
        });

    }

    public void populateTroopDetails() {

        if (unit == null) {
            try {
                unit = new UnitFactory();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (troopNames == null) {
            troopNames = unit.getAllTroopName();
        }
        // loop through the json file and add the names into the troopNames[]
        troopNames = unit.getAllTroopName();

        selectedTroopChoiceBox.setItems(FXCollections.observableArrayList(troopNames)); 
        selectedTroopChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number value, Number newValue) { 
                if (newValue.intValue() == -1) {
                    // do nothing
                    return;
                } 
                selectedTroopChoiceBox.setValue(getTroopNamesFromList(newValue.intValue()));
                // go to the unit factory to retrieve the stats
                String name = getTroopNamesFromList(newValue.intValue());
                viewStats(name);
            } 
        });
    }

    public void viewStats(String name) {
        Unit troopUnit = unit.createUnit(name);
        int size = troopUnit.getNumTroops();
        int attack = troopUnit.getAttack();
        int speed = troopUnit.getSpeed();
        int armour = troopUnit.getArmour();
        int morale = troopUnit.getMorale();
        int chargeStat = troopUnit.getChargeStatistic();
        int defSkill = troopUnit.getDefenseSkill();
        int shieldDef = troopUnit.getShieldDefense();
        int troopCost = troopUnit.getTroopGoldCost();
        int mp = troopUnit.getMovementPoints();
        int trainTime = troopUnit.getTrainingTime();
        changeLabelText(size, attack, speed, armour, morale, chargeStat, defSkill, 
                    shieldDef, troopCost, mp, trainTime);
    }

    public void changeLabelText(int size, int attack, int speed, int armour, 
        int morale, int chargeStat, int defSkill, int shieldDef, int troopCost, 
        int mp, int trainTime) {
        troopSizeNum.setText(Integer.toString(size));
        attackNum.setText(Integer.toString(attack));
        speedNum.setText(Integer.toString(speed));
        armourNum.setText(Integer.toString(armour));
        moraleNum.setText(Integer.toString(morale));
        chargeStatNum.setText(Integer.toString(chargeStat));
        defSkillNum.setText(Integer.toString(defSkill));
        shieldDefNum.setText(Integer.toString(shieldDef));
        troopGoldNum.setText(Integer.toString(troopCost));
        mpNum.setText(Integer.toString(mp));
        trainTimeNum.setText(Integer.toString(trainTime));
    }
    @FXML
    private Label unitInTraining1;
    @FXML
    private Label unitInTraining2; 
    @FXML
    private Label numTurnsLeft1; 
    @FXML
    private Label numTurnsLeft2; 

    /**
     * Troops are trained and sent the trainingUnit List first for training.
     * Per turn, their trainingTime will decrement until 0 and then will be added
     * to their respective armies.
     * SPECIAL FACTION RESTRICITON:
     * Romans can train everything but Berserke
     * Gauls can train everything LegionaryUnit
     * @param e
     * @throws Exception
     */
    @FXML 
    public void clickedTrainTroopButton (ActionEvent e) throws Exception {
        // when we click the train troop button
        // we grab the selected troop in the choicebox and add to the trainingUnit list
        // At the end of this reset the selected unit variable to ""
        Feature f = parent.getCurrentlySelectedHumanProvince();
        String pName = (String)f.getAttributes().get("name");
        Province currSelectedProv = parent.getGame().findProvinceByName(pName);
        System.out.println("Selected Source Province :"+currSelectedProv);
        Faction currFaction = parent.getGame().getFactionByName(currSelectedProv.getOwner());
        // If faction is Romans, can't train beserkers
        // If faction is Gauls, can't train LegionaryUnits
        if (currSelectedProv.getOwner().equals("Romans") && selectedUnit.equals("Berserker")) {
            getParent().printMessageToTerminal("Romans cannot train Beserker units");
            return;
        } else if (currSelectedProv.getOwner().equals("Gauls") && selectedUnit.equals("LegionaryUnit")) {
            getParent().printMessageToTerminal("Gauls cannot train Legionary units");
            return;
        }   
        int result = currFaction.trainTroops(selectedUnit, pName);
        if (result == 1) {
            Unit referenceUnit = unit.createUnit(selectedUnit);
            getParent().printMessageToTerminal("New "+selectedUnit+" is now training for "+referenceUnit.getTrainingTime()+" turns!");
        } else {
            getParent().printMessageToTerminal("Not enough gold to train a new "+selectedUnit);
        }
        updateTrainingList(currSelectedProv);
    }

    /**
     * Populates the list of units in training
     * @param p
     */
    public void updateTrainingList(Province p) {
        ArrayList<Unit> trainingUnitList = p.getTrainingUnits();
        if (trainingUnitList.size() > 0) {            
            unitInTraining1.setText(trainingUnitList.get(0).getName());
            numTurnsLeft1.setText(Integer.toString(trainingUnitList.get(0).getTrainingTime()));
            if (trainingUnitList.size() == 2) {
                unitInTraining2.setText(trainingUnitList.get(1).getName());
                numTurnsLeft2.setText(Integer.toString(trainingUnitList.get(1).getTrainingTime()));
            }
        }
        parent.updateFactionInfo();
    }

    @FXML
    public void clickedSwitchMenu(ActionEvent e) throws IOException {
        parent.goToProvinceMenu();
    }

    @FXML
    public void goBackToProvinceMenu(ActionEvent e) throws IOException {
        parent.goToProvinceMenuFromTraining();
    }

    @FXML 
    public void clickedTrainingButton(ActionEvent e) throws IOException {
        Feature f = parent.getCurrentlySelectedHumanProvince();
        // If no province is selected and we press train troops, don't allow it to enter the menu
        if (f != null) {
            parent.goToTrainingMenu();    
        }
    }

    @FXML
    public void clickToCloseWinPromptRomans(ActionEvent e) throws IOException {
        parent.closeRomansPrompt();
    } 
    @FXML
    public void clickToCloseWinPromptGauls(ActionEvent e) throws IOException {
        parent.closeGaulsPrompt();
    } 
    /**
     * upon the moving of troops between trainingUnit List and the actual 
     * army of their respective provinces, we will also link to the frontend 
     * and update them when they leave (i.e. set back to null)
     */
    public void updateTrainingTroops() {
        Feature f = parent.getCurrentlySelectedHumanProvince();

        String pName = (String)f.getAttributes().get("name");
        Province currSelectedProv = parent.getGame().findProvinceByName(pName);
        updateTrainingList(currSelectedProv);
    }

    @FXML
    public void clickedInvadeButton(ActionEvent e) throws IOException {
        parent.clickedInvadeButton(e);
    }

    @FXML
    public void goBackToInvadeMenu(ActionEvent e) throws IOException {
        parent.switchBackToInvasionMenu();
        // parent.goBackToInvadeFromProvince();
    }

    @FXML
    public void goToAllTroopDetailMenu(ActionEvent e) throws IOException {
        parent.goToAllTroopMenu();
    }
    /**
     * go to the province and its trainingUnit list and look for the first troop
     * if exists remove from list and refund half the balance back to the province treasury
     * otherwise do nothing
     * @param e
     * @throws IOException
     */
    @FXML
    public void clickedRefundTroop1(ActionEvent e) throws IOException {
        initialiseLabelRefund();
        parent.printMessageToTerminal("No troop in training list");
        Feature f = parent.getCurrentlySelectedHumanProvince();
        String pName = (String)f.getAttributes().get("name");
        Province currSelectedProv = parent.getGame().findProvinceByName(pName);
        if (currSelectedProv.getTrainingUnits().size() == 0) {
            unitInTraining1.setText("null");
            numTurnsLeft1.setText("?");
            return;
        } else if (currSelectedProv.getTrainingUnits().size() < 0) {
            // do nothing
            return;
        }
        // System.out.println("Selected Source Province :"+currSelectedProv);
        int refundCost = currSelectedProv.getTrainingUnits().get(0).getTroopGoldCost()/2;
        parent.printMessageToTerminal(currSelectedProv.getTrainingUnits().get(0).getName() + " is refunded to treasury for " + refundCost + " gold");
        // System.out.println("TrainingUnitList: " + currSelectedProv.getTrainingUnits());
        currSelectedProv.getTrainingUnits().remove(currSelectedProv.getTrainingUnits().get(0));
        Faction ownerFac = parent.getGame().getFactionByName(currSelectedProv.getOwner());
        // System.out.println("Old Faction Treasury: " + ownerFac.getTreasury());
        ownerFac.setTreasury(ownerFac.getTreasury() + refundCost);
        // System.out.println("New TrainingUnitList: " + currSelectedProv.getTrainingUnits());
        // System.out.println("New Faction Treasury: " + ownerFac.getTreasury());
        // in the end update the training unit list details  when refunded
        if (currSelectedProv.getTrainingUnits().size() == 0) {
            unitInTraining1.setText("null");
            numTurnsLeft1.setText("?");
            unitInTraining2.setText("null");
            numTurnsLeft2.setText("?");
        } else if (currSelectedProv.getTrainingUnits().size() == 1) {
            unitInTraining2.setText("null");
            numTurnsLeft2.setText("?");
        }
        updateTrainingList(currSelectedProv);
    }

    @FXML
    public void clickedRefundTroop2(ActionEvent e) throws IOException {
        initialiseLabelRefund();
        Feature f = parent.getCurrentlySelectedHumanProvince();
        String pName = (String)f.getAttributes().get("name");
        Province currSelectedProv = parent.getGame().findProvinceByName(pName);
        if (currSelectedProv.getTrainingUnits().size() == 0 || currSelectedProv.getTrainingUnits().size() == 1) {
            unitInTraining2.setText("null");
            numTurnsLeft2.setText("?");
            parent.printMessageToTerminal("No troop in position 2 of the training list");
            return;
        } else if (currSelectedProv.getTrainingUnits().size() < 0) {
            // do nothing
            return;
        // } 
        // else if (currSelectedProv.getTrainingUnits().size() == 1) {
        //     clickedRefundTroop1(e);
        } else if (currSelectedProv.getTrainingUnits().size() == 2) {
            // System.out.println("Selected Source Province :"+currSelectedProv);
            int refundCost = currSelectedProv.getTrainingUnits().get(1).getTroopGoldCost()/2;
            parent.printMessageToTerminal(currSelectedProv.getTrainingUnits().get(1).getName() + " is refunded to treasury for " + refundCost + " gold");
            // System.out.println("Before refund TrainingUnitList: " + currSelectedProv.getTrainingUnits());
            currSelectedProv.getTrainingUnits().remove(currSelectedProv.getTrainingUnits().get(1));
            //System.out.println("After refund TrainingUnitList: " + currSelectedProv.getTrainingUnits());
            Faction ownerFac = parent.getGame().getFactionByName(currSelectedProv.getOwner());
            // System.out.println("Old Faction Treasury: " + ownerFac.getTreasury());
            ownerFac.setTreasury(ownerFac.getTreasury() + refundCost);
            // System.out.println("New Faction Treasury: " + ownerFac.getTreasury());
            if (currSelectedProv.getTrainingUnits().size() == 1) {
                unitInTraining2.setText("null");
                numTurnsLeft2.setText("?");
            }
        }
        // System.out.println("New TrainingUnitList: " + currSelectedProv.getTrainingUnits());
        // System.out.println("New Faction Treasury: " + ownerFac.getTreasury());
        // in the end update the training unit list details  when refunded
        updateTrainingList(currSelectedProv);
    }

    public void initialiseLabelRefund() {
        if (unitInTraining1 == null) {
            unitInTraining1 = new Label();
        }
        if (numTurnsLeft1 == null) {
            numTurnsLeft1 = new Label();
        }
        if (unitInTraining2 == null) {
            unitInTraining2 = new Label();
        }
        if (numTurnsLeft2 == null) {
            numTurnsLeft2 = new Label();
        }
    }
    /**
     * upon the selected and deselection of province, we will make sure the information
     * of each province is stored and instantly viewed upon selection
     */
    public void updateProvinceMenu() {
        Feature f = parent.getCurrentlySelectedHumanProvince();
        if (provinceLabel == null) {
            System.out.println("province label is null");
            return;
        }
        if (f == null) {
            provinceLabel.setText("null");
            wealthLabel.setText("null");
            return;
        } 
        String name = (String)f.getAttributes().get("name");
        provinceLabel.setText(name);
        GameState g = parent.getGame();
        int wealthVal = g.findProvinceByName(name).getWealth();
        wealthLabel.setText(Integer.toString(wealthVal));        
    }
    /**
     * Function will update the ListView which contains the available units of each
     * province that have finished training and in the actual province army.
     * @param unitsInProvince
     */
    public void updateUnitsListView(ArrayList<Unit> unitsInProvince) {
        // making the ListView able to accept multiple selections
        availableUnitsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Clearing the listView to make sure only units from selected province is displayed
        availableUnitsList.getItems().clear();
        availableUnitsList.getSelectionModel().clearSelection();
        
        // create an observable list for the ListView, populating it with the units in province
        ObservableList<Unit> unitsList = FXCollections.<Unit>observableArrayList();
        unitsList.addAll(unitsInProvince);
        // adding to the actual ListView javafx element
        for (Unit u : unitsList) {
            availableUnitsList.getItems().add(u.toString());
        }
        //availableUnitsList.getItems().addAll(unitsList);
        
    }
    @FXML
    private Button quitGameButton;

    @FXML 
    public void handleQuitGame(ActionEvent event) {
        Stage stage = (Stage) quitGameButton.getScene().getWindow();
        stage.close();
    }
    /**
     * Clears the listView of units, clears the province reference and clears the unit group list of IDs (for creating the unit group). To be called when the province gets deselected
     */
    public void clearSelectedProvData() {
        // Clearing the listView 
        availableUnitsList.getItems().clear();
        availableUnitsList.getSelectionModel().clearSelection();
        // Clearing the currently selected province reference
        setSelectedSourceProv(null);
        setSelectedTargetProv(null);
        // Clearing the list of unitIDs (for unit groups)
        getUnitGroupIDs().clear();
        clearTaxMenu();
    }

    public void clearProvTrainingData() {
        unitInTraining1.setText("null");
        unitInTraining2.setText("null");
        numTurnsLeft1.setText("?");
        numTurnsLeft2.setText("?");
        // reset selection of dropdown
        if (selectedTroopChoiceBox == null) {
            selectedTroopChoiceBox = new ChoiceBox<String>();
            selectedTroopChoiceBox.getItems().clear();
            selectedTroopChoiceBox.setValue("");    
        }
    }

    /**
     * clickedMakeGroup() will allow to create a group given a list of uhit ids
     * which are used by the player for travelling and invading purposes. 
     * This gives the user the freedom to move as many units as they want to specific 
     * provinces as well as the freedom to add desired number of units to enter an 
     * invade of enemy provinces
     * @param e
     * @throws IOException
     */
    @FXML
    public void clickedMakeGroup(ActionEvent e) throws IOException {
        // clearing any existing unit groups.
        getUnitGroupIDs().clear();

        if (availableUnitsList.getItems().size() > 0 && selectedSourceProv != null) {
            ObservableList<String> selectedUnits = availableUnitsList.getSelectionModel().getSelectedItems();
            // hacky method since the listView stores the toString() version of each unit
            // loop through, if the toString of this equals toString of a unit in the units list, add to arrayList of uIDs
            for (String selectedUnit : selectedUnits) {
                for (Unit u : getSelectedSourceProv().getUnits()) {
                    if (u.toString().equals(selectedUnit)) {
                        getUnitGroupIDs().add(u.getID());
                        System.out.println("Added "+u.getName()+" with ID "+u.getID()+" to the unit group!");
                    }
                }
            }
            int groupSize = selectedUnits.size();
            if (groupSize == 1) {
                getParent().printMessageToTerminal("Created group with 1 unit!");
            } else {
                getParent().printMessageToTerminal("Created group with "+selectedUnits.size()+" units!");
            }
        } else {
            getParent().printMessageToTerminal("Either you have no available units or the source province is not specified");
        }
    }

    public void updateSelectedSourceProv(String name) {
        setSelectedSourceProv(parent.getGame().findProvinceByName(name));
    }

    public void updateSelectedTargetProv(String name) {
        setSelectedTargetProv(parent.getGame().findProvinceByName(name));
    }

    /**
     * The moving of units to ally provinces via a UnitGroups
     */
    @FXML
    public void clickedMoveGroup(ActionEvent e) throws IOException {
        // We check if selected target province is an enemy province - if so, reject the move (do this either here, or in the backend)
        if (getUnitGroupIDs().size() > 0) {
            String playerFaction = parent.getHumanFaction();
            // get target province too
            if (getSelectedTargetProv() == null) {
                getParent().printMessageToTerminal("You haven't selected a target province");
                return;
            } else if (getSelectedSourceProv() == null) {
                getParent().printMessageToTerminal("You haven't selected a source province");
                return;
            }
            if (!parent.getGame().getFactionByName(playerFaction).makeAndMoveUnitGroup(getSelectedSourceProv(), getSelectedTargetProv(), getUnitGroupIDs())) {
                getParent().printMessageToTerminal("Movement failed.");
            } else {
                getParent().printMessageToTerminal("Successfully moved group to "+getSelectedTargetProv().getName()+"!");
                // update the map to reflect the move!
                parent.updateProvNumTroopsMap();
                // update the ListView to reflect the move!
                // By getting new list of units in province
                updateUnitsListView(selectedSourceProv.getUnits());
            }

        } else {
            getParent().printMessageToTerminal("Seems like you haven't made a unit group yet!");
        }
    }

    public Province getSelectedSourceProv() {
        return selectedSourceProv;
    }

    public void setSelectedSourceProv(Province selectedSourceProv) {
        this.selectedSourceProv = selectedSourceProv;
    }

    public ArrayList<Integer> getUnitGroupIDs() {
        return unitGroupIDs;
    }

    public void setUnitGroupIDs(ArrayList<Integer> unitGroupIDs) {
        this.unitGroupIDs = unitGroupIDs;
    }

    public Province getSelectedTargetProv() {
        return selectedTargetProv;
    }

    public void setSelectedTargetProv(Province selectedTargetProv) {
        this.selectedTargetProv = selectedTargetProv;
    }

    public String getTroopNamesFromList(int index) {
        return troopNames.get(index);
    }

}

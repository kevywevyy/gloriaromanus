package unsw.gloriaromanus;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class InvasionMenuController extends MenuController{
    @FXML
    private TextField invading_province;
    @FXML
    private TextField opponent_province;
    @FXML
    private TextArea output_terminal;

    private String tabs = ""; // for the goals

    // @FXML
    // public void initialize() {
    //     loadGoals(parent.getGame().getFactions().get(0).getWinConditions());
    // }

    // https://stackoverflow.com/a/30171444
    @FXML
    private URL location; // has to be called location

    public void setInvadingProvince(String p) {
        invading_province.setText(p);
    }

    public void setOpponentProvince(String p) {
        opponent_province.setText(p);
    }

    public void appendToTerminal(String message) {
        output_terminal.appendText(message + "\n");
    }

    @FXML
    public void clickedInvadeButton(ActionEvent e) throws IOException {
        getParent().clickedInvadeButton(e);
    }

    @FXML
    public void clickedSaveGame(ActionEvent e) throws IOException {
        getParent().clickedSaveGame(e);
    }

    @FXML
    public void clickedEndTurn(ActionEvent e) throws IOException {
        getParent().clickedEndTurn(e);
    }

    @FXML
    private Label gameYear;

    @FXML
    private Label factionBeingControlled;

    @FXML
    private Label treasuryBal;

    @FXML 
    private Label factionWealth;

    public void updateFactionInfo(int year, String faction, int treasury, int wealth) {
        gameYear.setText(Integer.toString(year));
        factionBeingControlled.setText(faction);
        treasuryBal.setText(Integer.toString(treasury));
        factionWealth.setText(Integer.toString(wealth));
    }

    @FXML
    private VBox goalContainer;

    public void clearGoalContainer() {
        goalContainer.getChildren().clear();
    }
    /**
     * Recursively loads Faction goals
     * // TODO: call this at the end of each turn too
     * 
     * @param goal
     */
    public void loadGoals(Goal goal) {
        Label label = new Label();
        label.setText(tabs + goal.getGoalName());
        label.setStyle("-fx-text-fill: white; -fx-font-family: sans-serif");
        label.setTextAlignment(TextAlignment.LEFT);

        // Sets the text to green if the goal is completed 
        goal.getCompletedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                if (arg2) {
                    label.setStyle("-fx-text-fill: #00ff00; -fx-font-family: sans-serif");
                } else {
                    label.setStyle("-fx-text-fill: white; -fx-font-family: sans-serif");
                }
            }
        });

        if (goal.getCompletedProperty().get()) {
            System.out.println(getParent().getHumanFaction());
            label.setStyle("-fx-text-fill: #00ff00; -fx-font-family: sans-serif");
        } else {
            label.setStyle("-fx-text-fill: white; -fx-font-family: sans-serif");
        }

        goalContainer.getChildren().add(label);

        if (goal instanceof GoalConjunction) {
            tabs += "\t"; // Increment the layer by a tab
            GoalConjunction goalAnd = (GoalConjunction) goal;
            List<Goal> subgoals = goalAnd.getGoals();

            // Adds each of the subgoals
            for (Goal g : subgoals) {
                goalContainer.getChildren().add(createFormat());
                loadGoals(g);
            }  
        } else if (goal instanceof GoalDisjunction) {
            tabs += "\t"; // Increment the layer by a tab
            GoalDisjunction goalOr = (GoalDisjunction) goal;
            List<Goal> subgoals = goalOr.getGoals();

            // Adds each of the subgoals 
            for (Goal g : subgoals) {
                goalContainer.getChildren().add(createFormat());
                loadGoals(g);
            }
        }
    }

    /**
     * Adds "|" to indicate which goals are part of the same list
     * 
     * @return Label containing formatting
     */
    private Label createFormat() {
        Label formatting = new Label();
        formatting.setText(tabs + "|");
        formatting.setStyle("-fx-text-fill: white");
        return formatting;
    }

    public String getTabs() {
        return tabs;
    }

    public void setTabs(String tabs) {
        this.tabs = tabs;
    }
}

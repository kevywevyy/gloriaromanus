package unsw.gloriaromanus;

import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
public class GoalConquest implements Goal {
    private BooleanProperty completedProperty;
    public GoalConquest() {
        completedProperty = new SimpleBooleanProperty(false);
    }

    public boolean completed(Faction f, GameState g) {
        ArrayList<Province> factionOwned = g.getOwnedProvinces(f); 
        ArrayList<Province> allFactions = g.getProvinceList();
        if (factionOwned.size() == allFactions.size()) {
            completedProperty.set(true);
        } else {
            completedProperty.set(false);
        }

        return completedProperty.get();
    }

    // public String getName() {
    //     return "Conquest";
    // }

    public String getGoalName() {
        return "Conquer all provinces";
    }

    public BooleanProperty getCompletedProperty() {
        return completedProperty;
    }
}

package unsw.gloriaromanus;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class GoalTreasury implements Goal {
    private int needed;
    private BooleanProperty completedProperty;

    public GoalTreasury() {
        // needed = 100000;
        needed = 500;

        completedProperty = new SimpleBooleanProperty(false);
    }

    public boolean completed(Faction f, GameState g) {
        // if (f.getTreasury() >= needed) return true;
        completedProperty.set(f.getTreasury() >= needed);
        return completedProperty.get();
    }

    // public String getName() {
    //     return "Treasury";
    // }

    public String getGoalName() {
        return "Treasury: 100000 gold";
    }

    public BooleanProperty getCompletedProperty() {
        return completedProperty;
    }
}

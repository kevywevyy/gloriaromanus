package unsw.gloriaromanus;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
public class GoalWealth implements Goal {
    private int needed;
    private BooleanProperty completedProperty;

    public GoalWealth() {
        // needed = 400000;
        needed = 500;
        completedProperty = new SimpleBooleanProperty(false);
    }

    public boolean completed(Faction f, GameState g) {
        //if (f.getTotalWealth() >= needed) return true;
        completedProperty.set(f.getTotalWealth() >= needed);
        return completedProperty.get();
    }

    // public String getName() {
    //     return "Wealth";
    // }

    public String getGoalName() {
        return "Faction wealth: 400000";
    }

    public BooleanProperty getCompletedProperty() {
        return completedProperty;
    }

}

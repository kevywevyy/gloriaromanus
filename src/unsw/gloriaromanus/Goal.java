package unsw.gloriaromanus;

import javafx.beans.property.BooleanProperty;

public interface Goal {
    public boolean completed(Faction f, GameState g);
    // public String getName();
    public BooleanProperty getCompletedProperty();
    public String getGoalName();
}

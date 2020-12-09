package unsw.gloriaromanus;

import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class GoalDisjunction implements Goal {
    private ArrayList<Goal> goals;
    private BooleanProperty completedProperty;

    public GoalDisjunction() {
        goals = new ArrayList<Goal>();
        completedProperty = new SimpleBooleanProperty(false);
    }
    public boolean completed(Faction f, GameState game) {
        boolean result = false;
        for (Goal g : goals) {
            result = (result || g.completed(f, game));
        }

        completedProperty.set(result);
        return completedProperty.get();
    }

    // public String getName() {
    //     return "OR";
    // }

    public void addGoal(Goal goal) {
        getGoals().add(goal);
        setGoals(getGoals()); 
        for (Goal i : goals) {
            System.out.println(i.toString());
        }
    }

    public ArrayList<Goal> getGoals() {
        return goals;
    }

    public void setGoals(ArrayList<Goal> goals) {
        this.goals = goals;
    }

    public String getGoalName() {
        return "Complete ANY of the following:";
    }

    public BooleanProperty getCompletedProperty() {
        return completedProperty;
    }
}

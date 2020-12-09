package unsw.gloriaromanus;

import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class GoalConjunction implements Goal {
    private ArrayList<Goal> goals;
    private BooleanProperty completedProperty;
    public GoalConjunction() {
        goals = new ArrayList<Goal>();
        completedProperty = new SimpleBooleanProperty(false);
    }

    @Override
    public boolean completed(Faction f, GameState game) {
        int flag = 1;
        for (Goal g : goals) {
            if (!g.completed(f, game)) {
                completedProperty.set(false);
                flag = 0;
            }
        }

        if (flag == 1) { // All the goals have been completed
			completedProperty.set(true);
		} 

		return completedProperty.get(); 
    }

    // public String getName() {
    //     return "AND";
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
        return "Complete ALL of the following:";
    }

    public BooleanProperty getCompletedProperty() {
        return completedProperty;
    }
}

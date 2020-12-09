package unsw.gloriaromanus;

import java.io.IOException;
import java.util.ArrayList;

public class Faction { 
    private int totalWealth;
    private String name;
    Goal winConditions;
    private int treasury;
    private UnitFactory unitFactory; // the factory from which units are all created
    private GameState gameState; // The game state that this instance of Faction belongs to
    private String player; // Either player0 or player1

    public Faction(GameState gameState, Goal winConditions, String name, String player) throws IOException {
        this.name = name;
        treasury = 0;
        // total wealth = num of owned provinces * initial value of province wealth (10)
        totalWealth = 0;
        // totalWealth = 0;
        // give them some random provinces
        unitFactory = new UnitFactory();
        this.gameState = gameState;

        // Generates a random set of goals for the faction
        this.winConditions = winConditions;
        this.player = player;
    }

    public void saveGame() throws IOException {
        gameState.saveGame();
    }

    public void endTurn() throws IOException {
        // pass execution onto other player
        gameState.changeTurn(getName());
    }

    public void newTurn() throws IOException {
        // System.out.println("Testing win condiiton");
        if (gameState.returnNumOwnedProvinces(this) == 0) {
            System.out.println("Faction " + getName() + ", You've lost all of your provinces, Game Lost.");
            return;
        }
        // Move any fully trained units into province units list
        for (Province p : getGameState().getOwnedProvinces(this)) {
            // System.out.println(p.getName());
            ArrayList<Unit> trainingUnits = p.getTrainingUnits();
            ArrayList<Unit> tmp = new ArrayList<Unit>();
            for (Unit u : trainingUnits) {
                // System.out.println("Checking unit " + u.getName());
                // decrement troop training time for everyone in trainingUnits
                // If training time = 0, add to units array
                u.setTrainingTime(u.getTrainingTime() - 1);
                System.out.println(u.getName());
                if (u.getTrainingTime() == 0) {
                    // System.out.println("Test: " + trainingUnits.toString());
                    p.addUnit(u);
                    // System.out.println(u.getName() + " got added to units");
                    tmp.add(u);
                }
            }
            for (Unit u : tmp) {
                if (trainingUnits.contains(u)) {
                    trainingUnits.remove(u);
                }
            }
            // p.setTrainingUnits(trainingUnits.removeAll(tmp));
            // trainingUnits.removeAll(tmp);
            tmp.clear();
            //p.setTrainingUnits(tmp);
        }

        // reset the MP of all troops across all provinces
        // reset isConqueredInCurrTurn for all provinces
        DefaultUnitMP resetter = new DefaultUnitMP();
        for (Province p : getGameState().getOwnedProvinces(this)) {
            for (Unit u : p.getUnits()) {
                resetter.resetMP(u);
            }
            p.setConqueredInCurrTurn(false);
        }

        collectTax();
        updateTotalWealth();
        // updates win conditions
        //if (gameState.isNoWinnerYet()) {
            winConditions.completed(this, gameState);
        // }
        // Checks if the faction has won, but will shut up if someone has already won the game to leave the players able to play in peace without further victory prompts :)
        // Functionality moved to GloriaRomanusController.
        // if (winConditions.completed(this, gameState) && gameState.isNoWinnerYet()) {
        //     System.out.println("User won the game hip hip hooray!");
        //     gameState.setNoWinnerYet(false);
        //     saveGame();
        // }    
    }

    /**
     * Called by a faction object to invade another province.
     * fromName and toName are string names of the province the faction is attacking from
     * @param fromName 
     * @param toName
     * @param uIDs
     * @throws IOException
     * @return <ul>
                    <li>-1 if the invasion cannot happen</li>
                    <li>0 if the invasion fails and defenders win</li>
                    <li>1 if the invasion succeeds and this faction conquers the province</li>
                </ul>
     */
    public int invade(String fromName, String toName, ArrayList<Integer> uIDs) throws IOException {
        Province from = gameState.findProvinceByName(fromName);
        Province to = gameState.findProvinceByName(toName);
        if (from.isConqueredInCurrTurn()) {
            System.out.println("Factions cannot 'attack through' a settlement they conquered in the current turn");
            return -1;
        }
        // create tmp rollback array to put units in unitgroup back into source province if invasion is stopped by an error.
        ArrayList<Unit> rollbackTmp = new ArrayList<Unit>();
        rollbackTmp.addAll(from.getUnits());
        // make unit group with uIDs
        ArrayList<Unit> toMove = makeGroup(from, uIDs);
        
        if (toMove == null) {
            return -1;
        } else {
            if (!moveGroup(from, to, toMove, true)) {
                System.out.println("The created army could not move to the specified province to invade.");
                // reset units List in Province from
                from.setUnits(rollbackTmp);
                return -1;
            }
        }

        // Army is now inside the target province. Begin battle resolver
        BattleResolver battleResolver = new BattleResolver(toMove, to.getUnits(), from, to);
        // When the attacker wins the fight, change ownership of the province 
        // and clear trainingTroop list.
        if (battleResolver.battle() == 1) {
            System.out.println("Province " + to.getName() + " has been conquered!");
            to.setConqueredInCurrTurn(true);
            to.getUnits().clear();
            // add attacking army into conquered province
            // any routed attackers also join them.
            // Troops used to invade a province or move into a province invaded in the current turn cannot be moved for the rest of the turn
            for (Unit u : battleResolver.getAttackingArmy()) {
                to.addUnit(u);
            }
            for (Unit u : battleResolver.getAttackingRouteList()) {
                to.addUnit(u);
            }
            for (Unit u : to.getUnits()) {
                u.setCanMove(false);
            }
            to.setOwner(getName());
            System.out.println("Conquered province " + to.getName() + "'s owner has been changed!");

            if (to.getTrainingUnits().size() > 0) {
                to.getTrainingUnits().clear();
                System.out.println("Enemy units currently training in province have been wiped out.");
            }
            //addProvinceToFaction(toName);
            // remove province from the faction that was defending
            // return 1 as attackers have won
            return 1;
        } else {
            System.out.println("Province " + to.getName() + " has been defended. Invasion failed.");
            // Case: if it's a draw, return attacking army back to their original province
            // If attacking army is wiped out, this should do nothing.
            if (battleResolver.getAttackingArmy().size() != 0) {
                System.out.println("Result: Draw -> Attacking army goes home");
                for (Unit u : battleResolver.getAttackingArmy()) {
                    from.addUnit(u);
                }    
            }
            // return routed attackers to their original province
            if (battleResolver.getAttackingRouteList().size() != 0) {
                System.out.println("Routed attackers go to their original province");
                for (Unit u : battleResolver.getAttackingRouteList()) {
                    from.addUnit(u);
                }    
            }
            // return routed defenders to their original province
            if (battleResolver.getDefendingRouteList().size() != 0) {
                System.out.println("Routed defenders go back to their original province");
                for (Unit u : battleResolver.getDefendingRouteList()) {
                    to.addUnit(u);
                }    
            }
            // return 0 as defenders have won and attaack failed
            return 0;
        }
    }

    /**
     * Given a list of unit IDs, finds them in the specified province and 
     * returns them in a list format
     * @param from
     * @param uIDs
     * @return An ArrayList of the specified units.
     */
    public ArrayList<Unit> makeGroup(Province from, ArrayList<Integer> uIDs) {
        // take unit(s) from Province from with ID(s) specified.
        // If a unit with that id doesn't exist, return false
        ArrayList<Unit> unitsAvailable = from.getUnits();
        ArrayList<Unit> toMove = new ArrayList<Unit>();
        ArrayList<Unit> tmp = new ArrayList<Unit>();

        // This nested loop adds all specified units into a unitGroups object
        for (int i : uIDs) {
            boolean found = false;
            for (Unit u : unitsAvailable) {
                if (u.getID() == i) {
                    toMove.add(u);
                    tmp.add(u);
                    found = true;
                }
            }

            // Some logic to return false if we don't find the unit specified by id "i"
            if (!found) {
                System.out.println("unit with id: " + i + " not found.");
                from.setUnits(unitsAvailable);
                return null;
            }
        }
        //unitsAvailable.removeAll(tmp);
        for (Unit u : tmp) {
            if (unitsAvailable.contains(u)) {
                unitsAvailable.remove(u);
            }
        }

        for (Unit u : toMove) {
            if (!u.isCanMove()) {
                System.out.println("Troops used to invade a province or move into a province invaded in the current turn cannot be moved for the rest of the turn");
                return null;
            }
        }

        return toMove;
    }

    /**
     * The movement function - we place one or more Units into a "basket" and move them.
     * @param from
     * @param to
     * @param uIDs
     * @return True if the operation was successful, false otherwise.
     * @throws IOException
     */
    public boolean makeAndMoveUnitGroup(Province from, Province to, ArrayList<Integer> uIDs) throws IOException {
        // take unit(s) from Province from with ID(s) specified.
        // If a unit with that id doesn't exist, return false
        // then call moveGroup
        // if return true, move() in UnitGroups should add them into Province "to"
        // We need to remove them from Province "from" in here.
        ArrayList<Unit> rollbackTmp = new ArrayList<Unit>();
        rollbackTmp.addAll(from.getUnits());
        ArrayList<Unit> toMove = makeGroup(from, uIDs);
        
        if (toMove == null) {
            System.out.println("Couldn't make the group. Are all of the uIDs valid?");
            return false;
        }

        if (moveGroup(from, to, toMove, false)) {
            return true;
        } else {
            // reset units List in Province from
            from.setUnits(rollbackTmp);
            return false;
        }
    }

    /**
     * Uses BFS checking *shortest path* to the province.
     * Sources java implementation of Dijkstra's from online
     */
    public boolean moveGroup(Province from, Province to, ArrayList<Unit> unitsToMove, boolean isInvasion) throws IOException {
        // create a UnitGroups object and call its move() method
        UnitGroups toMove = new UnitGroups(unitsToMove);
        // then call the move() function.
        if (isInvasion) {
            return toMove.moveInvade(from, to, this);
        } else {
            // if you're trying to move into an enemy province, this should be rejected.
            if (!from.getOwner().equals(to.getOwner())) {
                System.out.println("To move into an enemy province, you must invade it!");
                return false;
            }
            return toMove.move(from, to, this);
        }
    }
    
    /**
     * Given the cost of a unit, checks if you can actually buy it
     * @param cost
     * @return
     */
    public boolean checkBalForTraining(int cost) {
        return getTreasury() >= cost;
    }

    /**
     * Given a unit name and a province to train them in, creates the group
     * @param unit
     * @param pName
     * @return 1 if success, 0 if failure
     */
    public int trainTroops(String unit, String pName) {
        Province p = getGameState().findOwnedProvinceByName(pName, this);
        
        // check treasury to see if valid transaction if allowed, remove from treasury
        // Add to trainingTroops first, until troops have finished training then move to p.units
        int unitID;
        Unit toTrain = unitFactory.createUnit(unit);
        unitID = toTrain.getID();
        // System.out.println("UnitID is: " + toTrain.getID());
        if (checkBalForTraining(toTrain.getTroopGoldCost())) {
            if (p.addTraineeUnit(toTrain)) {
                System.out.println("New unit has ID: "+unitID);
                Unit.setNumProduced(unitID);
                // deduct the gold cost from treasury
                setTreasury(getTreasury() - toTrain.getTroopGoldCost());
                return 1;
            }    
        } else {
            System.out.println("You don't have enough gold to train Unit " + unit + ". Your balance is " + getTreasury() + " and the cost of the troop was " + toTrain.getTroopGoldCost());
            return 0;
        }
        // Don't think this ever gets called
        return 0;
    }

    public void setTax(String pName, String taxRate) {
        Province p = getGameState().findOwnedProvinceByName(pName, this);
        p.setTaxRate(taxRate);
    }

    // asks each province to call its collectTax method, which uses strategy pattern
    public void collectTax() {
        int taxRevenue = 0;
        for (Province p : getGameState().getOwnedProvinces(this)) {
            taxRevenue += p.collectTax();
        }

        setTreasury(getTreasury() + taxRevenue);
    }

    /**
     * Called at the start of every turn, 
     * updateTotalWealth() will update the totalWealth value of the faction
     */
    public void updateTotalWealth() {
        // go into all the provinces and add all the wealth values together
        int sum = 0;
        ArrayList<Province> provinces = getGameState().getOwnedProvinces(this);
        for (Province p : provinces) {
            p.updateProvinceWealth();
            sum += p.getWealth();
        }
        setTotalWealth(sum);

    }
    
    public int getTreasury() {
        return treasury;
    }

    public void setTreasury(int treasury) {
        this.treasury = treasury;
    }

    public int getTotalWealth() {
        return totalWealth;
    }

    public void setTotalWealth(int totalWealth) {
        this.totalWealth = totalWealth;
    }

    public UnitFactory getUnitFactory() {
        return unitFactory;
    }

    public void setUnitFactory(UnitFactory unitFactory) {
        this.unitFactory = unitFactory;
    }

	public Goal getWinConditions() {
		return winConditions;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }
}

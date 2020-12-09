package unsw.gloriaromanus;

/**
 * Represents a basic unit of soldiers
 * 
 * incomplete - should have heavy infantry, skirmishers, spearmen, lancers,
 * heavy cavalry, elephants, chariots, archers, slingers, horse-archers,
 * onagers, ballista, etc... higher classes include ranged infantry, cavalry,
 * infantry, artillery
 * 
 * current version represents a heavy infantry unit (almost no range, decent
 * armour and morale)
 */
public class Unit { // abstract class so we can never directly create just a unit
    private String name; // may need this in the future to reference the unit type?????????????????????
    private static int numProduced = 0; // static int that tracks num of troops trained throughout entire game
    private int id; // so that our id of every created unit is unique (depending on the order it was
                    // created in)
    private int numTroops; // the number of troops in this unit (should reduce based on depletion)
    private String type; // whether the unit is ranged or melee
    private int armour; // armour defense
    private int morale; // resistance to fleeing
    private int speed; // ability to disengage from disadvantageous battle
    private int attack; // can be either missile or melee attack to simplify. Could improve
                        // implementation by differentiating!
    private int defenseSkill; // skill to defend in battle. Does not protect from arrows!
    private int shieldDefense; // a shield
    private int troopGoldCost; // gold cost of the unit to be trained
    private int movementPoints;
    private int chargeStatistic;
    private boolean breaking; // used in battle resolver to check whether a unit is broken or not
    private int trainingTime; // turns it takes to train the troop
    private boolean canMove; // Ensuring troops used to invade a province or move into a province invaded in
                             // the current turn cannot be moved for the rest of the turn

    public Unit(String name) {
        this.name = name;
        this.id = getNumProduced() + 1;
        this.breaking = false;
        this.canMove = true;
    }

    public int getNumTroops() {
        return numTroops;
    }

    public int getTroopGoldCost() {
        return troopGoldCost;
    }

    public void setTroopGoldCost(int troopGoldCost) {
        this.troopGoldCost = troopGoldCost;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public static int getNumProduced() {
        return numProduced;
    }

    public static void setNumProduced(int numProduced) {
        Unit.numProduced = numProduced;
    }

    public String getType() {
        return type;
    }

    public int getMovementPoints() {
        return movementPoints;
    }

    public void setMovementPoints(int movementPoints) {
        this.movementPoints = movementPoints;
    }

    public void setNumTroops(int numTroops) {
        this.numTroops = numTroops;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getArmour() {
        return armour;
    }

    public void setArmour(int armour) {
        this.armour = armour;
    }

    public int getMorale() {
        return morale;
    }

    public void setMorale(int morale) {
        this.morale = morale;
        if (morale < 0)
            this.morale = 0;

    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefenseSkill() {
        return defenseSkill;
    }

    public void setDefenseSkill(int defenseSkill) {
        this.defenseSkill = defenseSkill;
    }

    public int getShieldDefense() {
        return shieldDefense;
    }

    public void setShieldDefense(int shieldDefense) {
        this.shieldDefense = shieldDefense;
    }

    public void removeNumTroops(int casualties) {
        this.numTroops = getNumTroops() - casualties;
    }

    public int getChargeStatistic() {
        return chargeStatistic;
    }

    public void setChargeStatistic(int chargeStatistic) {
        this.chargeStatistic = chargeStatistic;
    }

    public boolean isBreaking() {
        return breaking;
    }

    public void setBreaking(boolean breaking) {
        this.breaking = breaking;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTrainingTime() {
        return trainingTime;
    }

    public void setTrainingTime(int trainingTime) {
        this.trainingTime = trainingTime;
    }

    public boolean isCanMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    @Override
    public String toString() {
        return getName()+" | "+getNumTroops()+" troops | "+getMovementPoints()+" MP | canMove: "+isCanMove()+" | ID: "+getID();
    }

    
}

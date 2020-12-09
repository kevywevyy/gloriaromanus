package unsw.gloriaromanus;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
// json imports
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Stores all the information about the current instance of the game. Could be considerd the "save"
 */
public class GameState {
    private ArrayList<Province> provinceList;
    private ArrayList<Faction> factions;
    private int year;                           // The game starts in 100AD for historical reasons lol
    private boolean player0Turn;                // Determines whether it's player 0 or player 1's turn
    private boolean noWinnerYet;                // Exists so that the game will stop checking victory conditions once someone wins.
    private int winCondCase;
    private int winCondCase2;
    // TODO: Remove all seeds after milestone 2 
    private Random r;
    public GameState(JSONObject save) throws IOException {
        this.provinceList = new ArrayList<Province>();
        this.factions = new ArrayList<Faction>();

        if (save != null) {
            System.out.println("save not null");
            loadGame(save);
        } else {
            System.out.println("SAVE IS NULL");
            this.r = new Random(LocalTime.now().getNano());
            // Generates the same goals for both factions
            Goal winConditions1 = generateGoals("Romans");
            Goal winConditions2 = generateGoals("Gauls");
            if (winConditions1 == null) {
                System.out.println("Why didn't any goals generate wat");
            }
            // initialises all of the factions
            factions = new ArrayList<Faction>();
            Faction romans = new Faction(this, winConditions1, "Romans", "");
            Faction gauls = new Faction(this, winConditions2, "Gauls", "");
            factions.add(romans);
            factions.add(gauls);
            // initialises all of the provinces
            provinceList = new ArrayList<Province>();
            String file = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
            JSONObject obj1 = new JSONObject(file);
            JSONArray romanProvinces = obj1.getJSONArray("Romans");
            // Goes through the java object for Roman, creating a new Province object
            // and assigning it to the roman faction object.
            // Also adds it to provinceList Array
            for (int i = 0; i < romanProvinces.length(); i++) {
                String provName = romanProvinces.getString(i);
                Province p = new Province("Romans", provName);
                // give every province a starting amount of troops
                Unit startingUnit = romans.getUnitFactory().createUnit("MeleeCavalry");
                // manually incrementing Unit.numProduced since this isn't naturally being trained
                Unit.setNumProduced(startingUnit.getID());
                p.addUnit(startingUnit);
                // giving every province an additional unit of missile infantry to start
                Unit startingUnit2 = romans.getUnitFactory().createUnit("MissileInfantry");
                Unit.setNumProduced(startingUnit2.getID());
                p.addUnit(startingUnit2);
                
                provinceList.add(p);
            }

            JSONObject obj2 = new JSONObject(file);
            JSONArray gaulProvinces = obj2.getJSONArray("Gauls");
            // JSONArray gaulProvinces = new JSONObject(file).getJSONArray("");
            for (int i = 0; i < gaulProvinces.length(); i++) {
                String provName = gaulProvinces.getString(i);
                Province p = new Province("Gauls", provName);
                // give every province a starting amount of troops
                Unit startingUnit = romans.getUnitFactory().createUnit("MeleeCavalry");
                // manually incrementing Unit.numProduced since this isn't naturally being trained
                Unit.setNumProduced(startingUnit.getID());
                p.addUnit(startingUnit);

                // giving every province an additional unit of missile infantry to start
                Unit startingUnit2 = gauls.getUnitFactory().createUnit("MissileInfantry");
                Unit.setNumProduced(startingUnit2.getID());
                p.addUnit(startingUnit2);
                
                provinceList.add(p);
            }
            // // Set initial wealth values
            romans.updateTotalWealth();
            gauls.updateTotalWealth();
            // 200BC????
            year = 200;
            player0Turn = true;
            noWinnerYet = true;
        }
        
    }

    public GameState(long seed) throws IOException {
        this.r = new Random(seed);
        // Generates the same goals for both factions
        Goal winConditions = generateGoals("Romans");
        if (winConditions == null) {
            System.out.println("Why didn't any goals generate wat");
        }
        // initialises all of the factions
        factions = new ArrayList<Faction>();
        Faction romans = new Faction(this, winConditions, "Romans", "");
        Faction gauls = new Faction(this, winConditions, "Gauls", "");
        factions.add(romans);
        factions.add(gauls);
        // initialises all of the provinces
        provinceList = new ArrayList<Province>();
        String file = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
        JSONObject obj1 = new JSONObject(file);
        JSONArray romanProvinces = obj1.getJSONArray("Romans");
        // Goes through the java object for Roman, creating a new Province object
        // and assigning it to the roman faction object.
        // Also adds it to provinceList Array
        for (int i = 0; i < romanProvinces.length(); i++) {
            String provName = romanProvinces.getString(i);
            Province p = new Province("Romans", provName);
            // give every province a starting amount of troops
            Unit startingUnit = romans.getUnitFactory().createUnit("MeleeCavalry");
            p.addUnit(startingUnit);
            provinceList.add(p);
            // increment numProduced which is their unique id for starting troops
            Unit.setNumProduced(Unit.getNumProduced() + 1);
        }

        JSONObject obj2 = new JSONObject(file);
        JSONArray gaulProvinces = obj2.getJSONArray("Gauls");
        // JSONArray gaulProvinces = new JSONObject(file).getJSONArray("Gaul");
        for (int i = 0; i < gaulProvinces.length(); i++) {
            String provName = gaulProvinces.getString(i);
            Province p = new Province("Gauls", provName);
            // give every province a starting amount of troops
            Unit startingUnit = romans.getUnitFactory().createUnit("MeleeCavalry");
            p.addUnit(startingUnit);
            provinceList.add(p);
            // increment numProduced which is their unique id for starting troops
            Unit.setNumProduced(Unit.getNumProduced() + 1);

        }
        // // Set initial wealth values
        romans.newTurn();
        gauls.newTurn();
        // 100AD????
        year = 200;
        player0Turn = true;
        noWinnerYet = true;
    }

    /**
     * Uses filewriter to save the return result of saveGameAsJSON to a json file
     */
    public void saveGame() {
        try {
            FileWriter file = new FileWriter("src/unsw/gloriaromanus/SaveFile.json");
            file.write(saveGameAsJSON().toString(2));
            file.close();
            System.out.println("Successfully created a save file.");
        } catch (IOException e) {
            System.out.println("An error occurred while trying to save.");
            e.printStackTrace();
        }
    }

    /**
     * Saves the vital details of the game as a JSONObject.
     *
     * @return
     * @throws IOException
     */
    public JSONObject saveGameAsJSON() throws IOException {
        JSONObject saveFile = new JSONObject();
        saveFile.put("numProduced", Unit.getNumProduced());
        // save all attributes
        JSONArray factions = new JSONArray();
        for (Faction f : getFactions()) {
            JSONObject factionInfo = new JSONObject();
            factionInfo.put("name", f.getName());
            //factionInfo.put("totalWealth", f.getTotalWealth());
            if (f.getName().equals("Romans")) {
                factionInfo.put("winConditions", getWinCondCase());
            } else {
                factionInfo.put("winConditions", getWinCondCase2());
            }
            factionInfo.put("treasury", f.getTreasury());
            // we don't need the list of provinces from within faction
            // since it's already saved in the for loop below
            factions.put(factionInfo);
        }
        saveFile.put("factions", factions);

        JSONArray provincesList = new JSONArray();
        for (Province p : getProvinceList()) {
            JSONObject provinceInfo = new JSONObject();
            provinceInfo.put("owner", p.getOwner());
            provinceInfo.put("name", p.getName());
            provinceInfo.put("wealth", p.getWealth());
            JSONArray trainingUnits = new JSONArray();
            for (Unit u : p.getTrainingUnits()) {
                JSONObject unit = new JSONObject();
                unit.put("name", u.getName());
                trainingUnits.put(unit);
            }
            provinceInfo.put("trainingUnits", trainingUnits);
            
            JSONArray units = new JSONArray();
            for (Unit u : p.getUnits()) {
                JSONObject unit = new JSONObject();
                unit.put("name", u.getName());
                unit.put("numTroops", u.getNumTroops());
                unit.put("canMove", u.isCanMove());
                unit.put("id", u.getID());
                units.put(unit);
            }
            provinceInfo.put("units", units);
            provinceInfo.put("taxRate", p.getTaxRateName());
            provinceInfo.put("conqueredInCurrTurn", p.isConqueredInCurrTurn());
            provincesList.put(provinceInfo);
        }
        saveFile.put("provinces", provincesList);

        saveFile.put("year", year);
        saveFile.put("player0Turn", isPlayer0Turn());
        saveFile.put("noWinnerYet", isNoWinnerYet());
        saveFile.put("romansPlayer", getFactionByName("Romans").getPlayer());
        saveFile.put("gaulsPlayer", getFactionByName("Gauls").getPlayer());
        // Print it out to the console
        // System.out.println(saveFile.toString(2));
        return saveFile;
    }

    /**
     * Given a JSONObject save file, it reads and restores the game state.
     * @param in
     * @throws IOException
     */
    public void loadGame(JSONObject in) throws IOException {
        JSONArray factions = in.getJSONArray("factions");
        JSONArray provincesJSON = in.getJSONArray("provinces");
        int year = in.getInt("year");
        Boolean player0Turn = in.getBoolean("player0Turn");
        Boolean noWinnerYet = in.getBoolean("noWinnerYet");
        int numProduced = in.getInt("numProduced");
        Unit.setNumProduced(numProduced);
        
        setYear(year);
        setPlayer0Turn(player0Turn);
        setNoWinnerYet(noWinnerYet);

        for (int i = 0; i < factions.length(); i++) {
            JSONObject data = factions.getJSONObject(i);
            String name = data.getString("name");
            int treasury = data.getInt("treasury");

            Goal goalObj = null;
            GoalConjunction and = new GoalConjunction();
            GoalDisjunction or = new GoalDisjunction();
            GoalConquest conq = new GoalConquest();
            GoalTreasury treasuryG = new GoalTreasury();
            GoalWealth wealth = new GoalWealth();
            int goal = data.getInt("winConditions");
            
            switch(goal) {
                case 0:
                    and.addGoal(treasuryG);
                    and.addGoal(or);
                    or.addGoal(conq);
                    or.addGoal(wealth);
                    goalObj = and;
                    break;
                case 1:
                    and.addGoal(conq);
                    and.addGoal(or);
                    or.addGoal(treasuryG);
                    or.addGoal(wealth);
                    goalObj = and;
                    break;
                case 2:
                    and.addGoal(wealth);
                    and.addGoal(or);
                    or.addGoal(conq);
                    or.addGoal(treasuryG);
                    goalObj = and;
                    break;
            }
            //setWinCondCase(goal);
            if (i == 0) {
                setWinCondCase(goal);
            } else if (i == 1) {
                setWinCondCase2(goal);
            }
            Faction f = new Faction(this, goalObj, name, "");
            f.setTreasury(treasury);
            System.out.println(f.getName());
            this.factions.add(f);
        }
        // establish unit factory to repopulate provinces
        UnitFactory factory = new UnitFactory();

        for (int i = 0; i < provincesJSON.length(); i++) {
            JSONObject data = provincesJSON.getJSONObject(i);

            String owner = data.getString("owner");
            String name = data.getString("name");
            int wealth = data.getInt("wealth");
            String taxRate = data.getString("taxRate");
            Boolean conqueredInCurrTurn = data.getBoolean("conqueredInCurrTurn");

            Province p = new Province(owner, name);
            p.setConqueredInCurrTurn(conqueredInCurrTurn);
            p.setWealth(wealth);
            p.setTaxRate(taxRate);
            p.setOwner(owner);

            // for each, create a new unit with that type 
            JSONArray trainingUnits = data.getJSONArray("trainingUnits");
            for (int j = 0; j < trainingUnits.length(); j++) {
                JSONObject trainObj = trainingUnits.getJSONObject(j);
                String trainingName = trainObj.getString("name");
                Unit unit = factory.createUnit(trainingName);
                p.addTraineeUnit(unit);
            }
            JSONArray units = data.getJSONArray("units");
            for (int k = 0; k < units.length(); k++) {
                JSONObject unitObj = units.getJSONObject(k);
                String unitName = unitObj.getString("name");
                Unit unit = factory.createUnit(unitName);
                Boolean canMove = unitObj.getBoolean("canMove");
                int numTroops = unitObj.getInt("numTroops");
                int id = unitObj.getInt("id");
                unit.setCanMove(canMove);
                unit.setNumTroops(numTroops);
                unit.setID(id);
                p.addUnit(unit);
                
            }
            this.provinceList.add(p);
        }
    }

    /**
     * Controls which faction's turn it is and calls the faction who has a turn now to update their provinces/state
     * @param facThatEndedID
     * @throws IOException
     */
    public void changeTurn(String facThatEnded) throws IOException {
        
        // if player0Turn = false, it's the end of the second player's turn -> increment game year and update all factions for new turn.
        if (!isPlayer0Turn()) {
            setPlayer0Turn(true);
            setYear(getYear() + 1);
            for (Faction f : getFactions()) {
                f.newTurn();
            }
        } else {
            // player 0 ended their turn
            // It's player 1's turn now
            setPlayer0Turn(false);
        }

        
    }
    
    /**
     * Generates a conjunction/disjunction of the three goals for every faction at the start of the game.
     * @param faction
     * @return The Goal object to be added in each Faction's constructor
     */
    public Goal generateGoals(String faction) {
        Goal goalObj = null;
        GoalConjunction and = new GoalConjunction();
        GoalDisjunction or = new GoalDisjunction();
        GoalConquest conq = new GoalConquest();
        GoalWealth wealth = new GoalWealth();
        GoalTreasury treasury = new GoalTreasury();

        // Random r = new Random();
        //int goal = r.nextInt(6);
        int goal = r.nextInt(3);
        System.out.println("we're picking goal " + goal);

        switch(goal) {
            case 0:
                and.addGoal(treasury);
                and.addGoal(or);
                or.addGoal(conq);
                or.addGoal(wealth);
                goalObj = and;
                break;
            case 1:
                and.addGoal(conq);
                and.addGoal(or);
                or.addGoal(treasury);
                or.addGoal(wealth);
                goalObj = and;
                break;
            case 2:
                and.addGoal(wealth);
                and.addGoal(or);
                or.addGoal(conq);
                or.addGoal(treasury);
                goalObj = and;
                break;
            /*    
            case 3:
                or.addGoal(treasury);
                or.addGoal(and);
                and.addGoal(conq);
                and.addGoal(wealth);
                goalObj = or;
                break;
            case 4:
                or.addGoal(conq);
                or.addGoal(and);
                and.addGoal(treasury);
                and.addGoal(wealth);
                goalObj = or;
                break;
            case 5:
                or.addGoal(wealth);
                or.addGoal(and);
                and.addGoal(conq);
                and.addGoal(treasury);
                goalObj = or;
                break; */
        }
        if (faction.equals("Romans")) {
            setWinCondCase(goal);
        } else {
            setWinCondCase2(goal);
        }
        return goalObj;
    }

    public Province findProvinceByName(String name) {
        for (Province p : getProvinceList()) {
            if (p.getName().equals(name)) {
                return p;
            }
        }

        return null;
    }

    public int returnNumOwnedProvinces(Faction f) {
        int sum = 0;
        for (Province p : getProvinceList()) {
            if (p.getOwner().equals(f.getName())) {
                sum++;
            }
        }
        return sum;
    }

    public Province findOwnedProvinceByName(String name, Faction f) {
        String facName = f.getName();
        for (Province p : getProvinceList()) {
            if (p.getName().equals(name) && p.getOwner().equals(facName)) {
                return p;
            }
        }

        return null;
    }

    public ArrayList<Province> getOwnedProvinces(Faction f) {
        ArrayList<Province> result = new ArrayList<Province>();
        String facName = f.getName();
        for (Province p : getProvinceList()) {
            if (p.getOwner().equals(facName)) {
                result.add(p);
            }
        }

        return result;
    }

    public Faction getFactionByName(String name) {
        for (Faction f: getFactions()) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }

    public ArrayList<Province> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(ArrayList<Province> provinceList) {
        this.provinceList = provinceList;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isPlayer0Turn() {
        return player0Turn;
    }

    public void setPlayer0Turn(boolean player0Turn) {
        this.player0Turn = player0Turn;
    }

    public boolean isNoWinnerYet() {
        return noWinnerYet;
    }

    public void setNoWinnerYet(boolean noWinnerYet) {
        this.noWinnerYet = noWinnerYet;
    }

    public ArrayList<Faction> getFactions() {
        return factions;
    }

    public void setFactions(ArrayList<Faction> factions) {
        this.factions = factions;
    }

    public int getWinCondCase() {
        return winCondCase;
    }

    public void setWinCondCase(int winCondCase) {
        this.winCondCase = winCondCase;
    }

    public int getWinCondCase2() {
        return winCondCase2;
    }

    public void setWinCondCase2(int winCondCase2) {
        this.winCondCase2 = winCondCase2;
    }
}

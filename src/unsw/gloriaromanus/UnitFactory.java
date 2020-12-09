package unsw.gloriaromanus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

// UnitFactory is created so that we don't have to keep reloading allUnits.json
// everytime we want to create a new unit - this factory should be accessible
// from each Faction's Faction object.

public class UnitFactory {
    String file;
    JSONObject fileContents;

    public UnitFactory() throws IOException {
        file = Files.readString(Paths.get("src/unsw/gloriaromanus/allUnits.json"));
        fileContents = new JSONObject(file);
    }

    public Unit createUnit(String name) {
        JSONObject obj = fileContents.getJSONObject(name);

        Unit created = new Unit(name);

        // Specific unit attributes from allUnits.json
        created.setType(obj.getString("type"));
        created.setNumTroops(obj.getInt("numTroops"));
        created.setArmour(obj.getInt("armour"));
        created.setMorale(obj.getInt("morale"));
        created.setSpeed(obj.getInt("speed"));
        created.setAttack(obj.getInt("attack"));
        created.setDefenseSkill(obj.getInt("defenseSkill")); 
        created.setShieldDefense(obj.getInt("shieldDefense"));
        created.setTroopGoldCost(obj.getInt("troopGoldCost"));
        created.setMovementPoints(obj.getInt("movementPoints"));
        created.setChargeStatistic(obj.getInt("chargeStatistic"));
        created.setTrainingTime(obj.getInt("trainingTime"));

        return created;
    }

    public List<String> getAllTroopName() {
        Iterator<String> keys = fileContents.keys();
        List<String> list = new ArrayList<String>();
        while(keys.hasNext()) {
            String key = keys.next();
            list.add(key);
        }
        return list;
    }
}

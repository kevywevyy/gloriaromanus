package unsw.gloriaromanus;

import java.io.IOException;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

public class DefaultUnitStat {

    String file;
    JSONObject fileContents;

    public DefaultUnitStat() throws IOException {
        file = Files.readString(Paths.get("src/unsw/gloriaromanus/allUnits.json"));
        fileContents = new JSONObject(file);
    }

    /**
     * Called to reset units to their java config file states, clearing them of any battle buffs/debuffs. This is called at the start of every skirmish, then buffs/debuffs are applied.
     * @param unit
     */
    public void setDefault(Unit unit) {
        JSONObject obj = fileContents.getJSONObject(unit.getName());

        // Specific unit attributes from allUnits.json
        unit.setArmour(obj.getInt("armour"));
        unit.setMorale(obj.getInt("morale"));
        unit.setSpeed(obj.getInt("speed"));
        unit.setAttack(obj.getInt("attack"));
        unit.setDefenseSkill(obj.getInt("defenseSkill")); 
        unit.setShieldDefense(obj.getInt("shieldDefense"));
        unit.setChargeStatistic(obj.getInt("chargeStatistic"));
    }

    public void resetArmyStat (List<Unit> army) {
        for (Unit u : army) {
            setDefault(u);
        }
    }

}

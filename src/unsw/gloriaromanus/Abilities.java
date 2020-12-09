package unsw.gloriaromanus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

/**
 * Abilities
 */
public class Abilities {
    String file;
    JSONObject fileContents;
    private String Ability_name;
    private String Ability_target;
    private int Ability_attack;
    private int Ability_defense;
    private int Ability_morale;
    private int Ability_armour;
    private int Ability_speed;
    private String Ability_type;
    private String troopName;

    public Abilities (Unit unitName) throws IOException {
        troopName = unitName.getName();
        file = Files.readString(Paths.get("src/unsw/gloriaromanus/SpecialAbilities.json"));
        fileContents = new JSONObject(file);
        JSONObject ability = fileContents.getJSONObject("Abilities").getJSONObject(troopName);
        Ability_name = ability.getString("name");
        Ability_target = ability.getString("target");
        Ability_attack = ability.getInt("attack");
        Ability_defense = ability.getInt("defense");
        Ability_morale = ability.getInt("morale");
        Ability_armour = ability.getInt("armour");
        Ability_speed = ability.getInt("speed");
        Ability_type = ability.getString("type");

        if (Ability_target.equals("Myself")) {
            if (Ability_type.equals("multiplicative")) {
                unitName.setAttack(unitName.getAttack() * Ability_attack);
                unitName.setDefenseSkill(unitName.getDefenseSkill() * Ability_defense);
                unitName.setMorale(unitName.getMorale() * Ability_morale);
                unitName.setArmour(unitName.getArmour() * Ability_armour);
                unitName.setSpeed(unitName.getSpeed() * Ability_speed);
            }
        }
    }

    // apply the debuff to all enemy (HorseArchers)
    // missile units, javelin skirmishers and missile infantry, and they are getting
    // debuffed due to horse archer 
    /**
     * Applies the debuff to all enemy missile units (javelin skirmishers and missile infantry)
     * @param allyUnit
     * @param enemyUnit
     */
    public void horseArcherDebuff(Unit allyUnit, Unit enemyUnit) {
        if (allyUnit.getName().equals("HorseArchers")) {
            // determine who the buff/debuff is targetted towards
            if (Ability_target.equals("Enemy") && enemyUnit.getType().equals("ranged") && Ability_type.equals("multiplicative")) {
                enemyUnit.setAttack(Ability_attack * enemyUnit.getAttack());
                enemyUnit.setDefenseSkill(enemyUnit.getDefenseSkill() * Ability_defense);
                enemyUnit.setMorale(enemyUnit.getMorale() * Ability_morale);
                enemyUnit.setArmour(enemyUnit.getArmour() * Ability_armour);
                enemyUnit.setSpeed(enemyUnit.getSpeed() * Ability_speed);
                System.out.println("Attacker HorseArchers used Cantabrian circle");
            }
        } 
    }

    public String getAbility_name() {
        return Ability_name;
    }

    public void setAbility_name(String ability_name) {
        Ability_name = ability_name;
    }
}
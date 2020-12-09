package unsw.gloriaromanus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

public class DefaultUnitMP {

    String file;
    JSONObject fileContents;

    public DefaultUnitMP() throws IOException {
        file = Files.readString(Paths.get("src/unsw/gloriaromanus/allUnits.json"));
        fileContents = new JSONObject(file);
    }

    /**
     * Resets the MP of a specified unit based off the json config file.
     * @param unit
     */
    public void resetMP(Unit unit) {
        JSONObject obj = fileContents.getJSONObject(unit.getName());

        // Specific unit attributes from allUnits.json
        unit.setMovementPoints(obj.getInt("movementPoints"));
        unit.setCanMove(true);
    }
}

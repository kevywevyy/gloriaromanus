package unsw.gloriaromanus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;
/**
 * UnitGroups:
 * group of one or more units that we use to move between provinces
 */
public class UnitGroups {
    // lowest movement point of unit in the list
    private int lowestMP; 
    private List<Unit> unitGroupList;
    // preloaded adjacency matrix for moving people around 
    JSONObject adjMatrixJSON;
    
    public UnitGroups(List<Unit> unitGroupList) throws IOException {
        this.unitGroupList = unitGroupList;
        lowestMP = getMinMP(unitGroupList);
        // TODO: discuss whether lowestMP will ever change after creation
        String file = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
        adjMatrixJSON = new JSONObject(file);
    }

    /**
     * Ensures that "when multiple units are moved at the same time, the maximum distance they can be moved during the turn together should be limited by the number of movement points of the unit with the fewest movement points".
     * @param unitGroupList
     * @return The MP of the unit in the group that has the LOWEST MP
     */
    public int getMinMP(List<Unit> unitGroupList) {
        int min = 16;
        // if the list is empty, return -1
        if (unitGroupList.size() == 0) {
            return -1;
        }
        for (Unit u : unitGroupList) {
            if (u.getMovementPoints() < min) {
                min = u.getMovementPoints();
            }
        }
        return min;
    }

    /**
     * Moves the Unit Group from one province to another
     * @param curr
     * @param target
     * @param owner
     * @return true if move was successful, false otherwise
     */
    public boolean move(Province curr, Province target, Faction owner) {
        int distance = calcDistance(curr, target, owner);
        if (distance == Integer.MAX_VALUE) {
            // No path to that province
            System.out.println("No path found to province " + target.getName());
            return false;
        }
        // update lowest MP (for cases where you move a unit twice in a turn)
        setLowestMP(getMinMP(getUnitGroupList()));
        // check if distance is greater than one or more units can travel
        if (distance > getLowestMP()) {
            System.out.println("One or more of the units in this group don't have enough MP to make the journey.");
            return false;
        } 

        // move the person to the new province
        // by looping through all units in the group and dumping them in the province
        for (Unit unit : getUnitGroupList()) {
            target.addUnit(unit);
            unit.setMovementPoints(unit.getMovementPoints() - distance);
        }
        
        return true;
        // If troops have been used to invade a province, or are moved into a province invaded in the current turn, they cannot be moved for the rest of the turn.
        // The above is addressed in Faction -> makeGroup
    }

    /**
     * Calculates the shortest distance between one province and the next.
     * @param curr
     * @param target
     * @param currController
     * @return the minimum MP cost to get from curr province to the target province.
     */
    public int calcDistance(Province curr, Province target, Faction currController) {
        // Finds shortest distance between curr and target using the given map, only passing through friendly territories
        // times that by 4 and return it (every adjacent province costs 4 MP to move to)
        // Units CANNOT move through enemy provinces!
        JSONObject matrix = getAdjMatrixJSON();
        // initialise Graph object
        Graph provGraph = new Graph();
        
        Set<Node> tempHolder = new HashSet<>();
        for (String outerKey : matrix.keySet()) {
            if (provinceOwned(outerKey, currController)) {
                // create all the nodes first, then we're able to pass in a reference
                Node ownedProvince = new Node(outerKey);
                tempHolder.add(ownedProvince);
            }
        }

        // Giving each province node in tempHolder edges to connected provinces
        for (Node outerKey : tempHolder) {
            String outerName = outerKey.getName();
            JSONObject outerProvince = matrix.getJSONObject(outerName);
            for (String provinces : outerProvince.keySet()) {
                if (provinceOwned(provinces, currController) && outerProvince.getBoolean(provinces)) {
                    // create an edge for that Node.
                    Node adjacentAndOwnedProvince = getNodeByName(provinces, tempHolder);
                    outerKey.addDestination(adjacentAndOwnedProvince, 4);
                }
            }

            provGraph.addNode(outerKey);
        }

        Node startingProvince = getNodeByName(curr.getName(), provGraph.getNodes());
        Graph sps = Graph.calculateShortestPathFromSource(provGraph, startingProvince);
        Node targetProvince = getNodeByName(target.getName(), sps.getNodes());
        return targetProvince.getDistance();
    }

    public boolean moveInvade(Province curr, Province target, Faction owner) throws IOException {
        int distance = 4;

        if (!confirmIfProvincesConnected(curr.getName(), target.getName())) {
            System.out.println("province you want to invade is not adjacent");
            return false;
        }

        if (target.getOwner().equals(owner.getName())) {
            System.out.println("You can't invade your own province!");
            return false;
        }

        // update lowest MP (for cases where you move a unit twice in a turn)
        setLowestMP(getMinMP(getUnitGroupList()));
        // check if distance is greater than one or more units can travel
        if (distance > getLowestMP()) {
            System.out.println("One or more of the units in this group don't have enough MP to make the journey.");
            return false;
        } 

        // Subtract MP for trying to attack
        // by looping through all units in the group and dumping them in the province
        for (Unit unit : getUnitGroupList()) {
            unit.setMovementPoints(unit.getMovementPoints() - distance);
        }

        // Bring them out into a battle, don't move them into the target province 
        
        return true;
        // If troops have been used to invade a province, or are moved into a province invaded in the current turn, they cannot be moved for the rest of the turn.
        // The above is addressed in Faction -> makeGroup
    }

    private boolean confirmIfProvincesConnected(String province1, String province2) throws IOException {
        String content = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
        JSONObject provinceAdjacencyMatrix = new JSONObject(content);
        return provinceAdjacencyMatrix.getJSONObject(province1).getBoolean(province2);
    }

    /**
     * Given a set
     * @param name
     * @param holder
     * @return A Node object corresponding to the name parameter
     */
    private Node getNodeByName(String name, Set<Node> holder) {
        for (Node n : holder) {
            if (n.getName().equals(name)) {
                return n;
            }
        }

        return null;
    }
    
    /**
     * 
     * @param province
     * @param f
     * @return True if province specified is owned by the faction
     */
    private boolean provinceOwned(String province, Faction f) {
        Province p = f.getGameState().findProvinceByName(province);
        return (p.getOwner().equals(f.getName()));
    }
    

    public int getLowestMP() {
        return lowestMP;
    }

    public void setLowestMP(int lowestMP) {
        this.lowestMP = lowestMP;
    }

    public List<Unit> getUnitGroupList() {
        return unitGroupList;
    }

    public void setUnitGroupList(List<Unit> unitGroupList) {
        this.unitGroupList = unitGroupList;
    }

    public JSONObject getAdjMatrixJSON() {
        return adjMatrixJSON;
    }
}

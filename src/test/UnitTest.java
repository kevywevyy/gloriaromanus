package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import unsw.gloriaromanus.*;

public class UnitTest{
    @Test
    public void CreateArtilleryUnit() throws IOException {
        String name = "Artillery";
        UnitFactory u = new UnitFactory();
        Unit artilleryUnit = u.createUnit(name);
        assertEquals(artilleryUnit.getName(), name);
        assertEquals(artilleryUnit.getType(), "ranged");
        assertEquals(artilleryUnit.getNumTroops(), 10);
        assertEquals(artilleryUnit.getArmour(), 5);
        assertEquals(artilleryUnit.getMorale(), 10);
        assertEquals(artilleryUnit.getSpeed(), 5);
        assertEquals(artilleryUnit.getAttack(), 6);
        assertEquals(artilleryUnit.getChargeStatistic(), 0);
        assertEquals(artilleryUnit.getDefenseSkill(), 5);
        assertEquals(artilleryUnit.getShieldDefense(), 2);
        assertEquals(artilleryUnit.getTroopGoldCost(), 3);
        assertEquals(artilleryUnit.getMovementPoints(), 4);
        assertEquals(artilleryUnit.getTrainingTime(), 1);
    }

    @Test
    public void CreateDruidUnit() throws IOException {
        String name = "Druids";
        UnitFactory u = new UnitFactory();
        Unit druidUnit = u.createUnit(name);
        assertEquals(druidUnit.getName(), name);
        assertEquals(druidUnit.getType(), "ranged");
        assertEquals(druidUnit.getNumTroops(), 5);
        assertEquals(druidUnit.getArmour(), 3);
        assertEquals(druidUnit.getMorale(), 10);
        assertEquals(druidUnit.getSpeed(), 5);
        assertEquals(druidUnit.getAttack(), 8);
        assertEquals(druidUnit.getChargeStatistic(), 0);
        assertEquals(druidUnit.getDefenseSkill(), 5);
        assertEquals(druidUnit.getShieldDefense(), 4);
        assertEquals(druidUnit.getTroopGoldCost(), 4);
        assertEquals(druidUnit.getMovementPoints(), 10);
        assertEquals(druidUnit.getTrainingTime(), 1);
    }

   @Test
    public void CreateElephantUnit() throws IOException {
        String name = "Elephants";
        UnitFactory u = new UnitFactory();
        Unit elephantUnit = u.createUnit(name);
        assertEquals(elephantUnit.getName(), name);
        assertEquals(elephantUnit.getType(), "melee");
        assertEquals(elephantUnit.getNumTroops(), 3);
        assertEquals(elephantUnit.getArmour(), 5);
        assertEquals(elephantUnit.getMorale(), 8);
        assertEquals(elephantUnit.getSpeed(), 2);
        assertEquals(elephantUnit.getAttack(), 8);
        assertEquals(elephantUnit.getChargeStatistic(), 5);
        assertEquals(elephantUnit.getDefenseSkill(), 5);
        assertEquals(elephantUnit.getShieldDefense(), 5);
        assertEquals(elephantUnit.getTroopGoldCost(), 3);
        assertEquals(elephantUnit.getMovementPoints(), 15);
        assertEquals(elephantUnit.getTrainingTime(), 2);
    }

    @Test
    public void CreateHorseArchersUnits() throws IOException {
        String name = "HorseArchers";
        UnitFactory u = new UnitFactory();
        Unit HorseArchersUnit = u.createUnit(name);
        assertEquals(HorseArchersUnit.getName(), name);
        assertEquals(HorseArchersUnit.getType(), "ranged");
        assertEquals(HorseArchersUnit.getNumTroops(), 15);
        assertEquals(HorseArchersUnit.getArmour(), 2);
        assertEquals(HorseArchersUnit.getMorale(), 10);
        assertEquals(HorseArchersUnit.getSpeed(), 5);
        assertEquals(HorseArchersUnit.getAttack(), 4);
        assertEquals(HorseArchersUnit.getChargeStatistic(), 0);
        assertEquals(HorseArchersUnit.getDefenseSkill(), 4);
        assertEquals(HorseArchersUnit.getShieldDefense(), 4);
        assertEquals(HorseArchersUnit.getTroopGoldCost(), 4);
        assertEquals(HorseArchersUnit.getMovementPoints(), 15);
        assertEquals(HorseArchersUnit.getTrainingTime(), 1);
    }

    @Test
    public void CreateJavelinSkirmishers() throws IOException {
        String name = "JavelinSkirmishers";
        UnitFactory u = new UnitFactory();
        Unit JavelinSkirmishersUnit = u.createUnit(name);
        assertEquals(JavelinSkirmishersUnit.getName(), name);
        assertEquals(JavelinSkirmishersUnit.getType(), "ranged");
        assertEquals(JavelinSkirmishersUnit.getNumTroops(), 10);
        assertEquals(JavelinSkirmishersUnit.getArmour(), 7);
        assertEquals(JavelinSkirmishersUnit.getMorale(), 7);
        assertEquals(JavelinSkirmishersUnit.getSpeed(), 4);
        assertEquals(JavelinSkirmishersUnit.getAttack(), 7);
        assertEquals(JavelinSkirmishersUnit.getChargeStatistic(), 0);
        assertEquals(JavelinSkirmishersUnit.getDefenseSkill(), 2);
        assertEquals(JavelinSkirmishersUnit.getShieldDefense(), 2);
        assertEquals(JavelinSkirmishersUnit.getTroopGoldCost(), 5);
        assertEquals(JavelinSkirmishersUnit.getMovementPoints(), 10);
        assertEquals(JavelinSkirmishersUnit.getTrainingTime(), 1);
    }


    @Test
    public void CreateMeleeCavalry() throws IOException {
        String name = "MeleeCavalry";
        UnitFactory u = new UnitFactory();
        Unit MeleeCavalryUnit = u.createUnit(name);
        assertEquals(MeleeCavalryUnit.getName(), name);
        assertEquals(MeleeCavalryUnit.getType(), "melee");
        assertEquals(MeleeCavalryUnit.getNumTroops(), 8);
        assertEquals(MeleeCavalryUnit.getArmour(), 6);
        assertEquals(MeleeCavalryUnit.getMorale(), 6);
        assertEquals(MeleeCavalryUnit.getSpeed(), 5);
        assertEquals(MeleeCavalryUnit.getAttack(), 6);
        assertEquals(MeleeCavalryUnit.getChargeStatistic(), 5);
        assertEquals(MeleeCavalryUnit.getDefenseSkill(), 4);
        assertEquals(MeleeCavalryUnit.getShieldDefense(), 4);
        assertEquals(MeleeCavalryUnit.getTroopGoldCost(), 3);
        assertEquals(MeleeCavalryUnit.getMovementPoints(), 15);
        assertEquals(MeleeCavalryUnit.getTrainingTime(), 1);
    }

    @Test
    public void CreateMeleeInfantry() throws IOException {
        String name = "MeleeInfantry";
        UnitFactory u = new UnitFactory();
        Unit MeleeInfantryUnit = u.createUnit(name);
        assertEquals(MeleeInfantryUnit.getName(), name);
        assertEquals(MeleeInfantryUnit.getType(), "melee");
        assertEquals(MeleeInfantryUnit.getNumTroops(), 25);
        assertEquals(MeleeInfantryUnit.getArmour(), 5);
        assertEquals(MeleeInfantryUnit.getMorale(), 7);
        assertEquals(MeleeInfantryUnit.getSpeed(), 4);
        assertEquals(MeleeInfantryUnit.getAttack(), 8);
        assertEquals(MeleeInfantryUnit.getChargeStatistic(), 0);
        assertEquals(MeleeInfantryUnit.getDefenseSkill(), 5);
        assertEquals(MeleeInfantryUnit.getShieldDefense(), 5);
        assertEquals(MeleeInfantryUnit.getTroopGoldCost(), 4);
        assertEquals(MeleeInfantryUnit.getMovementPoints(), 10);
        assertEquals(MeleeInfantryUnit.getTrainingTime(), 1);
    }

    @Test
    public void CreateMissileInfantry() throws IOException {
        String name = "MissileInfantry";
        UnitFactory u = new UnitFactory();
        Unit MissileInfantryUnit = u.createUnit(name);
        assertEquals(MissileInfantryUnit.getName(), name);
        assertEquals(MissileInfantryUnit.getType(), "ranged");
        assertEquals(MissileInfantryUnit.getNumTroops(), 15);
        assertEquals(MissileInfantryUnit.getArmour(), 2);
        assertEquals(MissileInfantryUnit.getMorale(), 5);
        assertEquals(MissileInfantryUnit.getSpeed(), 3);
        assertEquals(MissileInfantryUnit.getAttack(), 7);
        assertEquals(MissileInfantryUnit.getChargeStatistic(), 0);
        assertEquals(MissileInfantryUnit.getDefenseSkill(), 2);
        assertEquals(MissileInfantryUnit.getShieldDefense(), 2);
        assertEquals(MissileInfantryUnit.getTroopGoldCost(), 5);
        assertEquals(MissileInfantryUnit.getMovementPoints(), 10);
        assertEquals(MissileInfantryUnit.getTrainingTime(), 1);
    }

    @Test
    public void CreatePikemenHoplites() throws IOException {
        String name = "PikemenHoplites";
        UnitFactory u = new UnitFactory();
        Unit PikemenHoplitesUnit = u.createUnit(name);
        assertEquals(PikemenHoplitesUnit.getName(), name);
        assertEquals(PikemenHoplitesUnit.getType(), "melee");
        assertEquals(PikemenHoplitesUnit.getNumTroops(), 3);
        assertEquals(PikemenHoplitesUnit.getArmour(), 4);
        assertEquals(PikemenHoplitesUnit.getMorale(), 1);
        assertEquals(PikemenHoplitesUnit.getSpeed(), 5);
        assertEquals(PikemenHoplitesUnit.getAttack(), 2);
        assertEquals(PikemenHoplitesUnit.getChargeStatistic(), 0);
        assertEquals(PikemenHoplitesUnit.getDefenseSkill(), 3);
        assertEquals(PikemenHoplitesUnit.getShieldDefense(), 3);
        assertEquals(PikemenHoplitesUnit.getTroopGoldCost(), 4);
        assertEquals(PikemenHoplitesUnit.getMovementPoints(), 10);
        assertEquals(PikemenHoplitesUnit.getTrainingTime(), 1);
    }

    @Test
    public void CreateLegionaryUnit() throws IOException {
        String name = "LegionaryUnit";
        UnitFactory u = new UnitFactory();
        Unit LegionaryUnit = u.createUnit(name);
        assertEquals(LegionaryUnit.getName(), name);
        assertEquals(LegionaryUnit.getType(), "melee");
        assertEquals(LegionaryUnit.getNumTroops(), 1);
        assertEquals(LegionaryUnit.getArmour(), 5);
        assertEquals(LegionaryUnit.getMorale(), 9);
        assertEquals(LegionaryUnit.getSpeed(), 5);
        assertEquals(LegionaryUnit.getAttack(), 10);
        assertEquals(LegionaryUnit.getChargeStatistic(), 0);
        assertEquals(LegionaryUnit.getDefenseSkill(), 5);
        assertEquals(LegionaryUnit.getShieldDefense(), 5);
        assertEquals(LegionaryUnit.getTroopGoldCost(), 8);
        assertEquals(LegionaryUnit.getMovementPoints(), 15);
        assertEquals(LegionaryUnit.getTrainingTime(), 2);
    }

    @Test
    public void CreateBerserkerUnit() throws IOException {
        String name = "Berserker";
        UnitFactory u = new UnitFactory();
        Unit BerserkerUnit = u.createUnit(name);
        assertEquals(BerserkerUnit.getName(), name);
        assertEquals(BerserkerUnit.getType(), "melee");
        assertEquals(BerserkerUnit.getNumTroops(), 2);
        assertEquals(BerserkerUnit.getArmour(), 0);
        assertEquals(BerserkerUnit.getMorale(), 500);
        assertEquals(BerserkerUnit.getSpeed(), 3);
        assertEquals(BerserkerUnit.getAttack(), 12);
        assertEquals(BerserkerUnit.getChargeStatistic(), 0);
        assertEquals(BerserkerUnit.getDefenseSkill(), 3);
        assertEquals(BerserkerUnit.getShieldDefense(), 0);
        assertEquals(BerserkerUnit.getTroopGoldCost(), 6);
        assertEquals(BerserkerUnit.getMovementPoints(), 10);
        assertEquals(BerserkerUnit.getTrainingTime(), 2);
    }

    @Test
    public void TestRangedEngagement() throws IOException {
        GameState newgame = new GameState(1);
        UnitFactory u = new UnitFactory();
        // creating the horsearcher and artillery unit
        String name1 = "HorseArchers";
        String name2 = "Artillery";
        Unit attackUnit = u.createUnit(name1);
        Unit defendUnit = u.createUnit(name2);  

        List<Unit> attackingArmy = new ArrayList<>();
        List<Unit> defendingArmy = new ArrayList<>();
        attackingArmy.add(attackUnit);
        defendingArmy.add(defendUnit);
        Province invader = newgame.findProvinceByName("Britannia");
        Province defender = newgame.findProvinceByName("Lugdunensis");
        BattleResolver test = new BattleResolver(1, attackingArmy, defendingArmy, 
        invader, defender);
        assertEquals(test.engagements(attackUnit, defendUnit), "ranged");
    }

    @Test
    public void TestMelee1Engagement() throws IOException {
        GameState newgame = new GameState(1);
        UnitFactory u = new UnitFactory();
        // creating the horsearcher and artillery unit
        String name1 = "Elephants";
        String name2 = "MeleeCavalry";
        Unit attackUnit = u.createUnit(name1);
        Unit defendUnit = u.createUnit(name2);  

        List<Unit> attackingArmy = new ArrayList<>();
        List<Unit> defendingArmy = new ArrayList<>();
        attackingArmy.add(attackUnit);
        defendingArmy.add(defendUnit);
        Province invader = newgame.findProvinceByName("Britannia");
        Province defender = newgame.findProvinceByName("Lugdunensis");
        BattleResolver test = new BattleResolver(1, attackingArmy, defendingArmy, 
        invader, defender);
        assertEquals(test.engagements(attackUnit, defendUnit), "melee");
    }    

    /**
     * Test for consistency: when testing again the same test, it should
     * return the same value
     * @throws IOException
     */
    @Test
    public void TestMelee2Engagement() throws IOException {
        GameState newgame = new GameState(1);
        UnitFactory u = new UnitFactory();
        // creating the horsearcher and artillery unit
        String name1 = "Elephants";
        String name2 = "MeleeCavalry";
        Unit attackUnit = u.createUnit(name1);
        Unit defendUnit = u.createUnit(name2);  

        List<Unit> attackingArmy = new ArrayList<>();
        List<Unit> defendingArmy = new ArrayList<>();
        attackingArmy.add(attackUnit);
        defendingArmy.add(defendUnit);
        Province invader = newgame.findProvinceByName("Britannia");
        Province defender = newgame.findProvinceByName("Lugdunensis");
        BattleResolver test = new BattleResolver(1, attackingArmy, defendingArmy, 
        invader, defender);
        assertEquals(test.engagements(attackUnit, defendUnit), "melee");
    }    


    @Test
    public void TestRangeMeleeEngagement() throws IOException {
        GameState newgame = new GameState(1);
        UnitFactory u = new UnitFactory();
        // creating the horsearcher and artillery unit
        String name1 = "Elephants";
        String name2 = "Artillery";
        Unit attackUnit = u.createUnit(name1);
        Unit defendUnit = u.createUnit(name2);  
        // instantly finish training
        List<Unit> attackingArmy = new ArrayList<>();
        List<Unit> defendingArmy = new ArrayList<>();
        attackingArmy.add(attackUnit);
        defendingArmy.add(defendUnit);
        Province invader = newgame.findProvinceByName("Britannia");
        Province defender = newgame.findProvinceByName("Lugdunensis");
        BattleResolver test = new BattleResolver(1, attackingArmy, defendingArmy, 
        invader, defender);
        assertEquals(test.engagements(attackUnit, defendUnit), "ranged");
    }

    @Test
    public void TestStartGameOwnedProvinceSize() throws IOException {
        GameState newgame = new GameState(1);
        String name = "Romans";
        Faction faction1 = newgame.getFactionByName(name);
        assertEquals(newgame.getOwnedProvinces(faction1).size(), 25);
    }    

    @Test
    public void TestAllProvinceTaxRateDefaultRoman() throws IOException {
        GameState newgame = new GameState(1);
        String name = "Romans";
        Faction faction1 = newgame.getFactionByName(name);
        for (Province p : newgame.getOwnedProvinces(faction1)) {
            assertEquals(p.getTaxRateName(), "NormalTax");
        }
    }
    @Test
    public void TestAllProvinceTaxRateDefaultGaul() throws IOException {
        GameState newgame = new GameState(1);
        String name = "Gauls";
        Faction faction1 = newgame.getFactionByName(name);
        for (Province p : newgame.getOwnedProvinces(faction1)) {
            assertEquals(p.getTaxRateName(), "NormalTax");
        }
    }    

    @Test
    public void TestSetProvinceLowTax() throws IOException {
        GameState newgame = new GameState(1);
        String name = "Romans";
        Faction faction1 = newgame.getFactionByName(name);
        Province p = newgame.getOwnedProvinces(faction1).get(0);
        faction1.setTax(p.getName(), "LowTax");
        assertEquals(p.getTaxRateName(), "LowTax");
    }

    @Test
    public void TestSetProvinceHighTax() throws IOException {
        GameState newgame = new GameState(1);
        String name = "Romans";
        Faction faction1 = newgame.getFactionByName(name);
        Province p = newgame.getOwnedProvinces(faction1).get(0);
        // int num = p.getWealth();
        faction1.setTax(p.getName(), "HighTax");
        assertEquals(p.getTaxRateName(), "HighTax");
        // assertEquals(p.getWealth(), num-10);
    }   
    @Test
    public void TestBeginningTotalWealth() throws IOException {
        GameState newgame = new GameState(1);
        
        String name1 = "Romans";
        Faction faction1 = newgame.getFactionByName(name1);
        Province p1 = newgame.getOwnedProvinces(faction1).get(0);
        
        String name2 = "Gauls";
        Faction faction2 = newgame.getFactionByName(name2);
        Province p2 = newgame.getOwnedProvinces(faction2).get(0);
        // Checking the intiial wealth of both provinces = 0
        // checking the total wealth of faction1 
        // = 25(initial num of provinces per faction) * 10(initial wealth of every province)
        assertEquals(p1.getWealth(), p2.getWealth());
        assertEquals(p1.getWealth(), 10);
        // faction 1 should have a size of 25 initially 
        assertEquals(newgame.getOwnedProvinces(faction1).size(), 25);
        assertEquals(faction1.getTotalWealth(), 10*25);
    }

    @Test
    public void TestDifferentTotalWealthAndTreasury() throws IOException {
        GameState newgame = new GameState(1);
        
        String name1 = "Romans";
        Faction faction1 = newgame.getFactionByName(name1);
        Province p1 = newgame.getOwnedProvinces(faction1).get(0);
        
        String name2 = "Gauls";
        Faction faction2 = newgame.getFactionByName(name2);
        Province p2 = newgame.getOwnedProvinces(faction2).get(0);
        // faction1 should have a start treasury of 25
        // no. of provinces * (starting wealth * normalTax)
        // 25 * (10 * 0.1) = 25
        assertEquals(faction1.getTreasury(), 25);
        // faction1 should have a start treasury of 28
        // no. of provinces * (starting wealth * normalTax)
        // 28 * (10 * 0.1) = 28
        assertEquals(faction2.getTreasury(), 28);
        // set p1 to have high tax
        faction1.setTax(p1.getName(), "HighTax");
        faction1.endTurn();
        // set p2 to low tax
        faction2.setTax(p2.getName(), "LowTax");
        faction2.endTurn();
        // end turn and next turn we collect tax and then reveal total wealth 
        // faction1 calculation: 10%normal + 20% hightax 
        // 24provinces * (0.1*10) + 1province * (10*0.2) = +26 treasury gold 
        assertEquals(faction1.getTreasury(), 51);
        // High Tax reduces province's wealth by 10
        assertEquals(p1.getWealth(), 0);
        // wealth: 24*10 +0*1
        assertEquals(faction1.getTotalWealth(), 240);
        faction1.endTurn();
        assertEquals(faction2.getTreasury(), 56);
        assertEquals(p2.getWealth(), 20);
        assertEquals(faction2.getTotalWealth(), 290);
        faction2.endTurn();
    }  

    // Set to Normal as well as Very High Tax
    // Very HIgh tax to check the wealth < 0 which will
    // set wealth = 0 such that we dont have negative wealth
    @Test
    public void TestVeryHighTaxTotalWealthAndTreasury() throws IOException {
        GameState newgame = new GameState(1);
        
        String name1 = "Romans";
        Faction faction1 = newgame.getFactionByName(name1);
        Province p1 = newgame.getOwnedProvinces(faction1).get(0);

        String name2 = "Gauls";
        Faction faction2 = newgame.getFactionByName(name2);
        Province p2 = newgame.getOwnedProvinces(faction2).get(0);

        faction1.setTax(p1.getName(), "VeryHighTax");
        assertEquals(faction1.getTreasury(), 25);
        faction1.endTurn();
        faction2.setTax(p2.getName(), "Normal");
        assertEquals(faction2.getTreasury(), 28);
        faction2.endTurn();

        assertEquals(faction1.getTreasury(), 51);
        assertEquals(p1.getWealth(), 0);
        // wealth: 24*10 + 0*1
        // Very High Tax reduces province's wealth by -30
        // if the town tax went negative it wouldve been
        // wealth: 24*10 - 1*20 = 220
        assertEquals(faction1.getTotalWealth(), 240);
        faction1.endTurn();

        assertEquals(faction2.getTreasury(), 56);
        assertEquals(p2.getWealth(), 10);
        assertEquals(faction2.getTotalWealth(), 280);
        faction2.endTurn();
    }

    /**
     * Testing that units that require multiple turns to train 
     * will remain inside the trainingTroops List until the amount
     * of turns to train value is decremented to 0 
     * Make sure the cost of the troops trained will be removed
     * from treasury
     */
    @Test
    public void TestTakingTurnsToTrainAndGoldExpended() throws IOException {
        GameState newgame = new GameState(1);
        
        String name = "Romans";
        Faction faction1 = newgame.getFactionByName(name);
        Province p1 = newgame.getOwnedProvinces(faction1).get(0);
        String person1 = "Gauls";
        Faction faction2 = newgame.getFactionByName(person1);
        // creating the Elephants and MeleeCavalry unit
        String name1 = "Elephants";
        String name2 = "MeleeCavalry";
        String name3 = "HorseArchers";
        // EVERY PROVINCE STARTS OFF WITH A MELEE CALVARY
        assertEquals(p1.getUnits().size(), 1);
        faction1.trainTroops(name1, p1.getName());
        faction1.trainTroops(name2, p1.getName());
        faction1.trainTroops(name3, p1.getName());  
        assertEquals(p1.getTrainingUnits().size(), 2);
        assertEquals(faction1.getTreasury(), 19);
        faction1.endTurn();
        faction2.endTurn();
        // SIZE OF PROVINCE UNITS SHOULD NOW BE 2 (including the original given 
        // melee cavalry to every province as well as the melee cavarly that has 
        // just finished training)
        assertEquals(p1.getUnits().size(), 2);
        // faction 1 and faction 2 have ended the turn,
        // melee cavalary should be trained and sent to Province army 
        // elephants should still exist inside the trainingTroops List
        assertEquals(p1.getTrainingUnits().size(), 1);
        assertEquals(p1.getTrainingUnits().get(0).getName(), "Elephants");
        assertEquals(p1.getUnits().get(0).getName(), "MeleeCavalry");
        faction1.endTurn();
        faction2.endTurn();
        assertEquals(p1.getTrainingUnits().size(), 0);
        // Final Unit List in Province contains 
        // - initial given melee cavalry to each province
        // - elephant that took 2 turns to train
        // - meleecavalry take took 1 turn to train
        assertEquals(p1.getUnits().size(), 3);
    }

    /**
     * Testing that the year increments after both players end their turn
     */
    @Test
    public void TestChangeYearUponEndTurn() throws IOException {
        GameState newgame = new GameState(1);
        
        String name1 = "Romans";
        Faction faction1 = newgame.getFactionByName(name1);

        String name2 = "Gauls";
        Faction faction2 = newgame.getFactionByName(name2);
        assertEquals(newgame.getYear(), 200);
        faction1.endTurn();
        // this checks that we will need faction2 to also end turn in order for 
        // year to increment
        assertEquals(newgame.getYear(), 200);
        faction2.endTurn();
        assertEquals(newgame.getYear(), 201);
        faction1.endTurn();
        faction2.endTurn();
        assertEquals(newgame.getYear(), 202);
    }

    @Test
    /**
     * this tests that we are able to move multiple units at a time
     * checks if it gives correct return statemenets if false
     */
    public void TestMovingWithMultipleUnits() throws IOException {
        GameState newgame = new GameState(1);
        
        String name1 = "Romans";
        Faction faction1 = newgame.getFactionByName(name1);
        Province p1 = newgame.findOwnedProvinceByName("X", faction1);
        String name2 = "Gauls";
        Province p2 = newgame.findOwnedProvinceByName("XI", faction1);

        Faction faction2 = newgame.getFactionByName(name2);

        String name3 = "Artillery";
        String name4 = "MeleeInfantry";
        // EVERY PROVINCE STARTS OFF WITH A MELEE CALVARY
        assertEquals(p1.getUnits().size(), 1);
        faction1.trainTroops(name3, p1.getName());  
        faction1.trainTroops(name4, p1.getName());  
        faction1.endTurn();
        faction2.endTurn();
        ArrayList<Integer> uIDs = new ArrayList<Integer>();
        uIDs.add(p1.getUnits().get(1).getID());
        uIDs.add(p1.getUnits().get(2).getID());
        System.out.println(uIDs);
        assertEquals(p2.getUnits().size(), 1);
        // System.out.println("Before move: " + p1.getUnits().size());
        // move units from X to XI 
        // since X is next to XI, they will be able to move 
        faction1.makeAndMoveUnitGroup(p1, p2, uIDs);
        // System.out.println("After move: " + p1.getUnits().size());
        // province2.size() = starter unit + provinceAttacking.size()
        // = UIds.size() + 1 = 3
        assertEquals(p2.getUnits().size(), 3);
        
    }

    @Test
    public void TestCheckBalForTroops() throws IOException {
        GameState newgame = new GameState(1);
        
        String name = "Romans";
        Faction faction1 = newgame.getFactionByName(name);
        Province p1 = newgame.getOwnedProvinces(faction1).get(0);
        // set the treasury balance to 0, to test for error message when 
        // training troops that you cannot afford
        faction1.setTreasury(0);
        String name1 = "Elephants";
        faction1.trainTroops(name1, p1.getName());
    }

    @Test
    public void TestCheckMovingToProvincesTooFar() throws IOException {
        GameState newgame = new GameState(1);
        
        String name1 = "Romans";
        Faction faction1 = newgame.getFactionByName(name1);
        Province p1 = newgame.findOwnedProvinceByName("Noricum", faction1);
        Province p2 = newgame.findOwnedProvinceByName("Macedonia", faction1);

        String name2 = "Gauls";
        Faction faction2 = newgame.getFactionByName(name2);
        String unit1 = "Artillery";
        faction1.trainTroops(unit1, p1.getName()); 
        faction1.endTurn();
        faction2.endTurn();
        Unit unit = p1.getUnits().get(1);
        ArrayList<Integer> uIDs = new ArrayList<Integer>();
        uIDs.add(unit.getID());
        assertEquals(faction1.makeAndMoveUnitGroup(p1, p2, uIDs), false);
    }

    /**
     * Tests case for when faction A's province B cannot move its troops to own 
     * ally province C since it is not connected
     */
    @Test
    public void TestCheckMovingToProvincesNotValid() throws IOException {
        GameState newgame = new GameState(1);
        
        String name1 = "Gauls";
        Faction faction1 = newgame.getFactionByName(name1);
        Province p1 = newgame.findOwnedProvinceByName("Achaia", faction1);
        Province p2 = newgame.findOwnedProvinceByName("Dalmatia", faction1);

        String name2 = "Romans";
        Faction faction2 = newgame.getFactionByName(name2);
        String unit1 = "MeleeCavalry";
        faction1.trainTroops(unit1, p1.getName()); 
        faction1.endTurn();
        faction2.endTurn();
        Unit unit = p1.getUnits().get(1);
        ArrayList<Integer> uIDs = new ArrayList<Integer>();
        uIDs.add(unit.getID());
        assertEquals(faction1.makeAndMoveUnitGroup(p1, p2, uIDs), false);
    }

    /**
     * Revealing the faction win conditions number that is generated thought
     * Random() class in java
     */
    @Test
    public void TestViewWinConditions() throws IOException {
        GameState newgame = new GameState(1);
        
        String name1 = "Romans";
        Faction faction1 = newgame.getFactionByName(name1);
        System.out.println(faction1.getWinConditions().toString());
    }

    /**
     * Function test invading a enemy province
     * @throws IOException
     */
    @Test
    public void TestInvadeTakeOverProvince() throws IOException {
        GameState newgame = new GameState(1);

        String name = "Romans";
        Faction faction1 = newgame.getFactionByName(name);
        String person1 = "Gauls";
        Faction faction2 = newgame.getFactionByName(person1);

        Province p1 = newgame.findOwnedProvinceByName("Macedonia", faction1);
        Province p2 = newgame.findOwnedProvinceByName("Achaia", faction2);

        // creating the Elephants and MeleeCavalry unit
        String name1 = "MeleeInfantry";
        String name2 = "HorseArchers";
        String name3 = "PikemenHoplites";
        faction1.trainTroops(name1, p1.getName());
        faction1.trainTroops(name2, p1.getName());
        faction2.trainTroops(name3, p2.getName());
        faction1.endTurn();
        faction2.endTurn();
        // Units of p1
        Unit unit1 = p1.getUnits().get(1);
        Unit unit2 = p1.getUnits().get(2);
        // Units of p2
        // Unit unit3 = p2.getUnits().get(1);
        ArrayList<Integer> uIDs = new ArrayList<Integer>();
        uIDs.add(unit1.getID());
        uIDs.add(unit2.getID());
        faction1.invade(p1.getName(), p2.getName(), uIDs);
        
    }

        @Test
        public void TestGameLost() throws IOException {
            GameState newgame = new GameState(1);

            String name = "Romans";
            Faction faction1 = newgame.getFactionByName(name);
            String person1 = "Gauls";
            faction1.endTurn();
            Faction faction2 = newgame.getFactionByName(person1);
            for (Province p : newgame.getProvinceList()) {
                    p.setOwner("Romans");
            }
            faction2.endTurn();
        }

    /**
     * Function test invading a enemy province
     * @throws IOException
     */
    @Test
    public void TestSkirmishAlterStats() throws IOException {
        GameState newgame = new GameState(1);
        String name = "Romans";
        Faction faction1 = newgame.getFactionByName(name);
        String person1 = "Gauls";
        Faction faction2 = newgame.getFactionByName(person1);

        Province p1 = newgame.findOwnedProvinceByName("Macedonia", faction1);
        Province p2 = newgame.findOwnedProvinceByName("Achaia", faction2);

        UnitFactory u = new UnitFactory();
        // creating the horsearcher and artillery unit
        String name1 = "MeleeInfantry";
        String name2 = "PikemenHoplites";
        Unit attackUnit = u.createUnit(name1);
        Unit defendUnit = u.createUnit(name2);  
        List<Unit> attackingArmy = new ArrayList<>();
        List<Unit> defendingArmy = new ArrayList<>();
        attackingArmy.add(attackUnit);
        defendingArmy.add(defendUnit);
        faction1.endTurn();
        faction2.endTurn();
        BattleResolver br = new BattleResolver(1, attackingArmy, defendingArmy, p1, p2);
        br.skirmish(attackUnit, defendUnit, br.engagements(attackUnit, defendUnit));
    }

    /**
     * Test for reduce morale due to High Tax
     */
    @Test
    public void TestReduceMoraleHighTax() throws IOException {
        GameState newgame = new GameState(1);

        String name = "Romans";
        Faction faction1 = newgame.getFactionByName(name);
        String person1 = "Gauls";
        Faction faction2 = newgame.getFactionByName(person1);

        Province p1 = newgame.findOwnedProvinceByName("Macedonia", faction1);
        Province p2 = newgame.findOwnedProvinceByName("Achaia", faction2);

        String name1 = "Artillery";
        String name2 = "HorseArchers";
        String name3 = "Elephants";
        faction1.trainTroops(name1, p1.getName());
        faction2.trainTroops(name2, p2.getName());
        faction2.trainTroops(name3, p2.getName());
        faction1.endTurn();
        faction2.endTurn();
        // Melee Cavalry
        Unit unit1 = p1.getUnits().get(1);
        // Horse Archer
        Unit unit2 = p2.getUnits().get(1);
        assertEquals(unit1.getName(), "Artillery");
        assertEquals(unit1.getMorale(), 10);
        assertEquals(unit2.getName(), "HorseArchers");
        assertEquals(unit2.getMorale(), 10);

        p1.setTaxRate("VeryHighTax");
        p2.setTaxRate("VeryHighTax");
        faction1.endTurn();
        faction2.endTurn();
        Unit unit3 = p2.getUnits().get(2);
        List<Unit> attackingArmy = new ArrayList<>();
        attackingArmy.add(unit1);
        attackingArmy.add(unit3);
        List<Unit> defendingArmy = new ArrayList<>();
        attackingArmy.add(unit2);
        assertEquals(unit3.getName(), "Elephants");
        assertEquals(unit3.getName(), "Elephants");
        // p1 invade p2
        BattleResolver br = new BattleResolver(1, attackingArmy, defendingArmy, p1, p2);
        br.skirmish(unit1, unit2, br.engagements(unit1, unit2));
        // Very High Tax reduces morale of units by 1
        assertEquals(unit1.getMorale(), 9);
        assertEquals(unit2.getMorale(), 9);
    }

    /**
     * TEST BERSERKERS
     */
    @Test
    public void TestBreaking() throws IOException {
        GameState newgame = new GameState(1);

        String name = "Romans";
        Faction faction1 = newgame.getFactionByName(name);
        String person1 = "Gauls";
        Faction faction2 = newgame.getFactionByName(person1);
        Province p1 = newgame.findOwnedProvinceByName("Macedonia", faction1);
        Province p2 = newgame.findOwnedProvinceByName("Achaia", faction2);

        String name1 = "PikemenHoplites";
        String name2 = "PikemenHoplites";
        String name3 = "Berserker";
        faction1.trainTroops(name1, p1.getName());
        faction1.trainTroops(name2, p1.getName());
        faction2.trainTroops(name3, p2.getName());

        faction1.endTurn();
        faction2.endTurn();
        faction1.endTurn();
        faction2.endTurn();

        Unit unit1 = p1.getUnits().get(1);
        Unit unit2 = p1.getUnits().get(2);
        Unit unit3 = p2.getUnits().get(1);

        List<Unit> attackingArmy = new ArrayList<>();
        List<Unit> defendingArmy = new ArrayList<>();

        attackingArmy.add(unit1);
        attackingArmy.add(unit2);
        defendingArmy.add(unit3);

        BattleResolver br = new BattleResolver(1, attackingArmy, defendingArmy, p1, p2);
        br.skirmish(unit1, unit2, br.engagements(unit1, unit2));
        // attacking unit breaks
        assertEquals(unit1.isBreaking(), true);
    }

    @Test
    public void TestDefendingUnitBreakingInRangeEngagement() throws IOException {
        GameState newgame = new GameState(1);

        String name = "Romans";
        Faction faction1 = newgame.getFactionByName(name);
        String person1 = "Gauls";
        Faction faction2 = newgame.getFactionByName(person1);
        Province p1 = newgame.findOwnedProvinceByName("Macedonia", faction1);
        Province p2 = newgame.findOwnedProvinceByName("Achaia", faction2);

        String name1 = "PikemenHoplites";
        String name2 = "MissileInfantry";

        String name3 = "Artillery";

        faction1.trainTroops(name1, p1.getName());
        faction1.trainTroops(name2, p1.getName());

        faction2.trainTroops(name3, p2.getName());

        faction1.endTurn();
        faction2.endTurn();

        Unit attackUnit1 = p1.getUnits().get(1);
        Unit attackUnit2 = p1.getUnits().get(2);

        Unit defendUnit1 = p2.getUnits().get(1);

        List<Unit> attackingArmy = new ArrayList<>();
        List<Unit> defendingArmy = new ArrayList<>();

        attackingArmy.add(attackUnit1);
        attackingArmy.add(attackUnit2);
        defendingArmy.add(defendUnit1);

        BattleResolver br = new BattleResolver(1, attackingArmy, defendingArmy, p1, p2);
        assertEquals(br.engagements(attackUnit1, defendUnit1), "ranged");
        // melee in ranged engagements do no dmg
        assertEquals(br.defendingUnitRangeEngagementCasualties(attackUnit1, defendUnit1), 0);

    }

    @Test
    public void TestAtckChargeStatistic() throws IOException {
        GameState newgame = new GameState(1);

        String name = "Romans";
        Faction faction1 = newgame.getFactionByName(name);
        String person1 = "Gauls";
        Faction faction2 = newgame.getFactionByName(person1);
        Province p1 = newgame.findOwnedProvinceByName("Macedonia", faction1);
        Province p2 = newgame.findOwnedProvinceByName("Achaia", faction2);

        String name1 = "Elephants";
        String name2 = "MeleeCavalry";

        faction1.trainTroops(name1, p1.getName());
        faction2.trainTroops(name2, p2.getName());

        faction1.endTurn();
        faction2.endTurn();
        faction1.endTurn();
        faction2.endTurn();
        Unit attackUnit1 = p1.getUnits().get(1);
        Unit defendUnit1 = p2.getUnits().get(1);

        List<Unit> attackingArmy = new ArrayList<>();
        List<Unit> defendingArmy = new ArrayList<>();

        attackingArmy.add(attackUnit1);
        defendingArmy.add(defendUnit1);
        assertEquals(attackUnit1.getAttack(), 8);
        assertEquals(defendUnit1.getAttack(), 6);
        BattleResolver br = new BattleResolver(1, attackingArmy, defendingArmy, p1, p2);
        assertEquals(br.engagements(attackUnit1, defendUnit1), "melee");
        br.skirmish(attackUnit1, defendUnit1, br.engagements(attackUnit1, defendUnit1));
        // AttackUnit1 will attain the chargeStatistic ()
        assertEquals(attackUnit1.getAttack(), 8+5);

    }

    @Test
    public void TestBothUnitRoute() throws IOException {
        GameState newgame = new GameState(1);

        String name = "Romans";
        Faction faction1 = newgame.getFactionByName(name);
        String person1 = "Gauls";
        Faction faction2 = newgame.getFactionByName(person1);
        Province p1 = newgame.findOwnedProvinceByName("Macedonia", faction1);
        Province p2 = newgame.findOwnedProvinceByName("Achaia", faction2);

        String name1 = "PikemenHoplites";
        String name2 = "MissileInfantry";

        faction1.trainTroops(name1, p1.getName());
        faction2.trainTroops(name2, p2.getName());

        faction1.endTurn();
        faction2.endTurn();

        Unit attackUnit1 = p1.getUnits().get(1);
        Unit defendUnit1 = p2.getUnits().get(1);

        List<Unit> attackingArmy = new ArrayList<>();
        List<Unit> defendingArmy = new ArrayList<>();

        attackingArmy.add(attackUnit1);
        defendingArmy.add(defendUnit1);

        BattleResolver br = new BattleResolver(1, attackingArmy, defendingArmy, p1, p2);
        assertEquals(br.engagements(attackUnit1, defendUnit1), "ranged");
        // force set two units to break
        attackUnit1.setBreaking(true);
        defendUnit1.setBreaking(true);
        // sysout should print "Both units ran from the engagement!"
        br.skirmish(attackUnit1, defendUnit1, br.engagements(attackUnit1, defendUnit1));
    }

    @Test
    public void TestDefChargeStatistic() throws IOException {
        GameState newgame = new GameState(1);

        String name = "Romans";
        Faction faction1 = newgame.getFactionByName(name);
        String person1 = "Gauls";
        Faction faction2 = newgame.getFactionByName(person1);
        Province p1 = newgame.findOwnedProvinceByName("Macedonia", faction1);
        Province p2 = newgame.findOwnedProvinceByName("Achaia", faction2);

        String name1 = "Elephants";
        String name2 = "Elephants";

        faction1.trainTroops(name1, p1.getName());
        faction2.trainTroops(name2, p2.getName());

        faction1.endTurn();
        faction2.endTurn();
        faction1.endTurn();
        faction2.endTurn();
        Unit attackUnit1 = p1.getUnits().get(1);
        Unit defendUnit1 = p2.getUnits().get(1);

        List<Unit> attackingArmy = new ArrayList<>();
        List<Unit> defendingArmy = new ArrayList<>();

        attackingArmy.add(attackUnit1);
        defendingArmy.add(defendUnit1);
        assertEquals(attackUnit1.getAttack(), 8);
        assertEquals(defendUnit1.getAttack(), 8);
        BattleResolver br = new BattleResolver(1, attackingArmy, defendingArmy, p1, p2);
        assertEquals(br.engagements(attackUnit1, defendUnit1), "melee");
        br.skirmish(attackUnit1, defendUnit1, br.engagements(attackUnit1, defendUnit1));
        // AttackUnit1 will attain the chargeStatistic (5)
        // DefendUnit1 will attain the chargeStatistic (5)
        assertEquals(attackUnit1.getAttack(), 8+5);
        assertEquals(defendUnit1.getAttack(), 8+5);
        assertEquals(attackUnit1.getAttack(), defendUnit1.getAttack());
    }

    @Test
    public void TestDefendingProvinceWins() throws IOException {
        GameState newgame = new GameState(1);

        String name = "Romans";
        Faction faction1 = newgame.getFactionByName(name);
        String person1 = "Gauls";
        Faction faction2 = newgame.getFactionByName(person1);
        Province p1 = newgame.findOwnedProvinceByName("Macedonia", faction1);
        Province p2 = newgame.findOwnedProvinceByName("Achaia", faction2);

        String name1 = "PikemenHoplites";
        String name3 = "Berserker";
        faction1.trainTroops(name1, p1.getName());
        faction2.trainTroops(name3, p2.getName());

        faction1.endTurn();
        faction2.endTurn();
        faction1.endTurn();
        faction2.endTurn();

        Unit unit1 = p1.getUnits().get(1);
        Unit unit2 = p2.getUnits().get(1);

        List<Unit> attackingArmy = new ArrayList<>();
        List<Unit> defendingArmy = new ArrayList<>();

        attackingArmy.add(unit1);
        defendingArmy.add(unit2);

        ArrayList<Integer> uIDs = new ArrayList<Integer>();
        uIDs.add(unit1.getID());
        uIDs.add(unit2.getID());

        BattleResolver br = new BattleResolver(1, attackingArmy, defendingArmy, p1, p2);
        assertEquals(br.battle(), 0);
    }

    /**
     * Saving and loading file
     */
    @Test
    public void TestSaveAndLoadFile() throws IOException {
        GameState newgame = new GameState(1);
        String person1 = "Romans";
        String person2 = "Gauls";

        Faction faction1 = newgame.getFactionByName(person1);
        Faction faction2 = newgame.getFactionByName(person2);

        Province p1 = newgame.findOwnedProvinceByName("Macedonia", faction1);
        Province p2 = newgame.findOwnedProvinceByName("Achaia", faction2);

        p1.setTaxRate("HighTax");
        p2.setTaxRate("LowTax");

        JSONObject saveFile = newgame.saveGameAsJSON();
        GameState restartedGame = new GameState(saveFile);
        assertEquals(saveFile.toString(), restartedGame.saveGameAsJSON().toString());

    }

    /**
     *  Since there are 53 provinces in the game, they are all given unqiue ids
     *  therefore when trained a new troops they will be given an unique id depending
     *  on the number of troops produced in the game
     */
    @Test
    public void TestUniqueId() throws IOException {
        GameState newgame = new GameState(null);
        String person1 = "Romans";
        String person2 = "Gauls";

        Faction faction1 = newgame.getFactionByName(person1);
        Faction faction2 = newgame.getFactionByName(person2);

        Province p1 = newgame.findOwnedProvinceByName("Macedonia", faction1);
        Province p2 = newgame.findOwnedProvinceByName("Achaia", faction2);
        assertEquals(p1.getUnits().size(), 2);
        assertEquals(p2.getUnits().size(), 2);
        faction1.setTreasury(1000);
        String name1 = "HorseArchers";
        String name2 = "HorseArchers";
        // EVERY PROVINCE STARTS OFF WITH A MELEE CALVARY AND MISSILEINFANTRY
        faction1.trainTroops(name1, p1.getName());
        faction1.trainTroops(name2, p1.getName());
        assertEquals(p1.getTrainingUnits().size(), 2);
        faction1.endTurn();
        faction2.endTurn();
        assertEquals(p1.getTrainingUnits().size(), 0);
        assertEquals(p1.getUnits().size(), 4);
        System.out.println(p1.getUnits());
        // check the two different trained horse archers have unique id
        assertEquals(p1.getUnits().get(2).getName(), "HorseArchers");
        assertEquals(p1.getUnits().get(3).getName(), "HorseArchers");
        // check for the unique id 
        assertEquals(p1.getUnits().get(2).getID(), 1448);
        assertEquals(p1.getUnits().get(3).getID(), 1449);
    }
    /**
     * Saving file test
     */
    @Test
    public void TestSaveFile() throws IOException {
        GameState newgame = new GameState(null);
        String person1 = "Romans";
        String person2 = "Gauls";

        Faction faction1 = newgame.getFactionByName(person1);
        Faction faction2 = newgame.getFactionByName(person2);

        Province p1 = newgame.findOwnedProvinceByName("Macedonia", faction1);
        Province p2 = newgame.findOwnedProvinceByName("Achaia", faction2);
        JSONObject saveFile = newgame.saveGameAsJSON();
        GameState restartedGame = new GameState(saveFile);
        assertEquals(saveFile.toString(), restartedGame.saveGameAsJSON().toString());
    }
    /**
     * Saving file test
     */
    @Test
    public void TestLoadFile() throws IOException {
        GameState newgame = new GameState(null);
        String person1 = "Romans";
        String person2 = "Gauls";

        Faction faction1 = newgame.getFactionByName(person1);
        Faction faction2 = newgame.getFactionByName(person2);

        Province p1 = newgame.findOwnedProvinceByName("Macedonia", faction1);
        Province p2 = newgame.findOwnedProvinceByName("Achaia", faction2);
        JSONObject saveFile = newgame.saveGameAsJSON();
        System.out.println(saveFile);
        newgame.loadGame(saveFile);   
    }

    @Test
    public void TestMeleeInfantryStat() throws IOException {
        GameState newgame = new GameState(null);
        String person1 = "Romans";
        String person2 = "Gauls";

        Faction faction1 = newgame.getFactionByName(person1);
        Faction faction2 = newgame.getFactionByName(person2);
        faction1.setTreasury(1000);
        faction2.setTreasury(1000);

        Province p1 = newgame.findOwnedProvinceByName("Macedonia", faction1);
        Province p2 = newgame.findOwnedProvinceByName("Achaia", faction2);
        String name1 = "MeleeInfantry";
        String name2 = "MeleeInfantry";
        faction1.trainTroops(name1, p1.getName());
        faction2.trainTroops(name2, p2.getName());
        faction1.endTurn();
        faction2.endTurn();
        assertEquals(p1.getUnits().get(2).getAttack(), 8);
        assertEquals(p2.getUnits().get(2).getAttack(), 8);
        BattleResolver br = new BattleResolver(p1.getUnits(), p2.getUnits(), p1, p2);
        br.skirmish(p1.getUnits().get(2), p2.getUnits().get(2), "melee");
    }

}

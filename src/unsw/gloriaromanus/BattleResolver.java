package unsw.gloriaromanus;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// import com.esri.arcgisruntime.internal.jni.CoreMapSublayerSource;
/**
 * BattleResolver
 */
public class BattleResolver {
    private Province invader;
    private Province defender;
    private List<Unit> attackingArmy;
    private List<Unit> defendingArmy;
    private List<Unit> attackingRouteList;
    private List<Unit> defendingRouteList;
    private Random r;
    
    public BattleResolver(List<Unit> attackingArmy, List<Unit> defendingArmy, 
                            Province invader, Province defender) {
        this.r = new Random(LocalTime.now().getNano());
        this.attackingArmy = attackingArmy;
        this.defendingArmy = defendingArmy;
        this.attackingRouteList = new ArrayList<Unit>();
        this.defendingRouteList = new ArrayList<Unit>();
        this.invader = invader;
        this.defender = defender;
    }

    public BattleResolver(long seed, List<Unit> attackingArmy, List<Unit> defendingArmy, 
    Province invader, Province defender) {
        this.r = new Random(seed);
        this.attackingArmy = attackingArmy;
        this.defendingArmy = defendingArmy;
        this.attackingRouteList = new ArrayList<Unit>();
        this.defendingRouteList = new ArrayList<Unit>();
        this.invader = invader;
        this.defender = defender;
    }

    /**
     * Given two units, determines the type of engagement.
     * @param attackUnit
     * @param defendUnit
     * @return The type of engagement either "ranged" or "melee"
     */
    public String engagements(Unit attackUnit, Unit defendUnit) {
        String attackType = attackUnit.getType();
        String defendType = defendUnit.getType();
        // if both units are melee, there is a 100% chance of a melee engagement
        if (attackType.equals("melee") && defendType.equals("melee")) {
            return "melee";
        // if both units are missile, there is a 100% chance of a missile engagement
        } else if (attackType.equals("ranged") && defendType.equals("ranged")) {
            return "ranged";
        // if attackUnit is ranged and defendUnit is melee 
        } else if (attackType.equals("ranged") && defendType.equals("melee")) {
            // double rand = Math.random() * 100;
            // Random r = new Random(seed);
            // set base meleeChance to 50
            int meleeChance = 50;
            // meleeChance += 10 * (speed of melee - speed of ranged)
            meleeChance += 10 * (defendUnit.getSpeed() - attackUnit.getSpeed());
            // the maximum chance for an engagement to be either a ranged or melee engagement is 95% in either case
            // the lower bound for meleeChance = 5%
            if (meleeChance < 5) {
                meleeChance = 5;
            }
            // the upper bound for meleeChance = 95%
            if (meleeChance >= 95) {
                meleeChance = 95;
            }
            if (r.nextInt(100) < meleeChance) {
                return "melee";
            } else {
                return "ranged";
            }
        // if attackUnit is melee and defendUnit is ranged
        } else if (attackType.equals("melee") && defendType.equals("ranged")) {
            // double rand = Math.random() * 100;
            // Random r = new Random(seed);
            // set base meleeChance to 50
            int meleeChance = 50;
            // meleeChance += 10 * (speed of melee - speed of ranged)
            meleeChance += 10 * (attackUnit.getSpeed() - defendUnit.getSpeed());
            // the maximum chance for an engagement to be either a ranged or melee engagement is 95% in either case
            // the lower bound for meleeChance = 5%
            if (meleeChance < 5) {
                meleeChance = 5;
            }
            // the upper bound for meleeChance = 95%
            if (meleeChance >= 95) {
                meleeChance = 95;
            }
            if (r.nextInt(100) < meleeChance) {
                return "melee";
            } else {
                return "ranged";
            }
        } 
        return null;
    }

    /**
     * Determines the outcome of a skirmish between two units
     * @param attackUnit
     * @param defendUnit
     * @param engagement
     * @throws IOException
     */
    public void skirmish(Unit attackUnit, Unit defendUnit, String engagement) throws IOException {
        // setDefault sets the units values back to their default values before any engagement buff/debuff
        DefaultUnitStat setDefaultVal = new DefaultUnitStat();
        setDefaultVal.setDefault(attackUnit);
        setDefaultVal.setDefault(defendUnit);
       // sets the morale debuff for provinces with VeryHighTax
        if (getInvader().getTaxRate() instanceof VeryHighTax) {
            attackUnit.setMorale(attackUnit.getMorale() - 1);
        }
        if (getDefender().getTaxRate() instanceof VeryHighTax) {
            defendUnit.setMorale(defendUnit.getMorale() - 1);
        }
        addChargeStatistic(attackUnit, defendUnit);
        System.out.println(attackUnit.getAttack());
        System.out.println(defendUnit.getAttack());

        // apply druidic fervour buff/debuff to both attackers and defenders
        int attackerDruids = setMaxCountDruids(attackingArmy);
        int defenderDruids = setMaxCountDruids(defendingArmy);
        // attackersMoraleBuff is the buff attackers get from ally no. of druids
        double attackersMoraleBuff = attackUnit.getMorale() + attackerDruids * 0.1 * attackUnit.getMorale();
        attackUnit.setMorale((int)attackersMoraleBuff);
        // defendersMoraleBuff is the buff defenders get from their ally no. of druids
        double defendersMoraleBuff = defendUnit.getMorale() + defenderDruids * 0.1 * defendUnit.getMorale();
        defendUnit.setMorale((int)defendersMoraleBuff);

        // attackersMoraleDebuff is the penalised buff received from defenders depending on no. of enemy druids
        double attackersMoraleDeBuff = attackUnit.getMorale() - defenderDruids * 0.05 * attackUnit.getMorale();
        attackUnit.setMorale((int)attackersMoraleDeBuff);

        // defenderMoraleDebuff is the penalised buff recieved from attackers dependning on their no. of enemy druids
        double defendersMoraleDebuff = defendUnit.getMorale() - attackerDruids * 0.05 * defendUnit.getMorale();
        defendUnit.setMorale((int)defendersMoraleDebuff);
        
        // apply units' passive Abilities() buff 
        Abilities attackerPassiveAbility = new Abilities(attackUnit);
        System.out.println("Applied passive ability " + 
                attackerPassiveAbility.getAbility_name() +  " to attacking units");
        attackerPassiveAbility.horseArcherDebuff(attackUnit, defendUnit);

        Abilities defenderPassiveAbility = new Abilities(defendUnit);
        System.out.println("Applied passive ability " + 
                defenderPassiveAbility.getAbility_name() +  " to defending units");      
        defenderPassiveAbility.horseArcherDebuff(defendUnit, attackUnit);
        // System.out.println(attackUnit.getAttack());
        // System.out.println(defendUnit.getAttack());
        int numEngagements = 0;
        while (attackUnit.getNumTroops() > 0 && defendUnit.getNumTroops() > 0) {
            // for every 4th engagement by this unit of melee infantry per battle, 
            // the value of shield defense is added to this unit's attack damage value
            if (attackUnit.getName().equals("MeleeInfantry") && 
                numEngagements % 4 == 0 && numEngagements != 0) {
                int result = attackUnit.getAttack();
                result += attackUnit.getArmour();
                attackUnit.setAttack(result);
            }
            if (defendUnit.getName().equals("MeleeInfantry") && 
                numEngagements % 4 == 0 && numEngagements != 0) {
                int result = defendUnit.getAttack();
                result += defendUnit.getArmour();
                defendUnit.setAttack(result);
            }
            // Activates Elephant running amok if engagement with elephant
            if (elephantAbilities(attackUnit, defendUnit)) {
                // if activated, go to next engagement
                numEngagements++;
                continue;
            }   
            int attackLoss = 0;
            int defendLoss = 0;
            if (engagement.equals("ranged")) {
                attackLoss = defendingUnitRangeEngagementCasualties(attackUnit, defendUnit);
                defendLoss = attackingUnitRangeEngagementCasualties(attackUnit, defendUnit);
            } else if (engagement.equals("melee")) {
                attackLoss = defendingUnitMeleeEngagementCasualties(attackUnit, defendUnit);
                defendLoss = attackingUnitMeleeEngagementCasualties(attackUnit, defendUnit);
            }

            if (attackLoss < 1) {
                attackLoss = 1;
            } 
            if (defendLoss < 1) {
                defendLoss = 1;
            }

            int tempAttack = attackUnit.getNumTroops();
            int tempDefend = defendUnit.getNumTroops();

            attackUnit.removeNumTroops(attackLoss);
            defendUnit.removeNumTroops(defendLoss);
            

            // check if any of the units will break OR ARE CURRENTLY BROKEN FROM A PREV ENGAGEMENT IN THIS SKIRMISH
            if (attackUnit.isBreaking() || defendUnit.isBreaking()) {
                if (unitRoutResult(attackUnit, defendUnit, attackingRouteList, defendingRouteList)) {
                    break;
                }
            } else if (attackBreaking(tempAttack, tempDefend, attackUnit, defendUnit, attackLoss, defendLoss) || 
                defendBreaking(tempAttack, tempDefend, attackUnit, defendUnit, defendLoss, attackLoss)) {
                // if the any of the units successfully routes 
                if (unitRoutResult(attackUnit, defendUnit, attackingRouteList, defendingRouteList)) {
                    break;
                } 
            }
            numEngagements++;
        }
        // if attackUnit.numTroops() == 0, move defendUnit back into their army
        if (attackUnit.getNumTroops() == 0) {
            System.out.println("Defending unit killed all troops in attacking Unit");
            defendingArmy.add(defendUnit);

        // if defendUnit.numTroops() == 0, move attackUnit back into their army
        } else if (defendUnit.getNumTroops() == 0) {
            System.out.println("Attacking unit killed all troops in defending Unit");
            attackingArmy.add(attackUnit);
        }
    }

    
    /**
     * series of skirmishes
     * whichever armyList is empty loses the battle
     * takes in Provicne which they are fighting for -> defendingUnit's province
     * change ownership of province is the attacking win, otherwise 
     * if attacking wins, take over defending province and clear entire defendingArmy Group including defendingrouteList
     * Clear trainingTroop array.
     * @return 1 if the attacking army wins, 0 otherwise
     * @throws IOException
     */
    public int battle () throws IOException {        
        // Random rand = new Random(seed);
        DefaultUnitStat resetUnitStat = new DefaultUnitStat(); 
        int skirmishes = 0;
        while (attackingArmy.size() > 0 && defendingArmy.size() > 0) {
            // if the battle lasts longer than 200 engagements, 
            // the outcome should be a draw, the invading army in a draw should 
            // return to the province it invaded from
            if (skirmishes > 200) {
                System.out.println("Draw");
                resetUnitStat.resetArmyStat(attackingArmy);
                resetUnitStat.resetArmyStat(defendingArmy);
                return 0;
            }
            int attackSize = r.nextInt(attackingArmy.size());
            int defendSize = r.nextInt(defendingArmy.size());
            Unit attackUnit = attackingArmy.get(attackSize);
            Unit defendUnit = defendingArmy.get(defendSize);
            attackingArmy.remove(attackUnit);
            defendingArmy.remove(defendUnit);
            skirmish(attackUnit, defendUnit, engagements(attackUnit, defendUnit));
            skirmishes++;
        }
        // Clears all buffs/debuffs
        resetUnitStat.resetArmyStat(attackingArmy);
        resetUnitStat.resetArmyStat(defendingArmy);
        resetUnitStat.resetArmyStat(attackingRouteList);
        resetUnitStat.resetArmyStat(defendingRouteList);

        if (attackingArmy.size() == 0) {
            System.out.println("Defenders won the battle");
            return 0;
        } else {
            System.out.println("Attackers won the battle");
            return 1;
        }  
    }

    /**
     * Limits the maximum amount of druid buff/debuff to allies to 5 ONLY
     * @param armyList
     * @return
     */
    public int setMaxCountDruids (List<Unit> armyList) {
        int sum = 0;
        for (Unit druids : armyList) {
            if (druids.getName().equals("Druids")) {
                sum += 1;
            }
        }
        if (sum >= 5) {
            return 5;
        }
        return sum;
    }

    /**
     * Performs a 10% chance check to teamkill if any of the units in a skirmish are of type Elephant
     * @param attackUnit
     * @param defendUnit
     * @return True if an Elephant troop triggered the "Elephants Running Amok ability"
     */
    public boolean elephantAbilities (Unit attackUnit, Unit defendUnit) {
        if (attackUnit.getName().equals("Elephants")) {
            // Random rand = new Random(seed);

            // we calcualte the casulaties elephants will inflict
            int numCasualties = attackingUnitMeleeEngagementCasualties(attackUnit, defendUnit);
            // double r = Math.random() * 100;
            // Random r = new Random(seed);
            if (r.nextInt(100) < 10) {
                // we have to grab a random unit from the attacking army
                int attackSize = r.nextInt(attackingArmy.size());
                Unit unluckyUnit = attackingArmy.get(attackSize);
                if (unluckyUnit.getNumTroops() - numCasualties <= 0) {
                    attackingArmy.remove(unluckyUnit);
                } else {
                    unluckyUnit.removeNumTroops(numCasualties);
                }
                return true;
            }
            return false;
        }
        if (defendUnit.getName().equals("Elephants")) {
            // int roll = r.nextInt(100);
            // we calcualte the casulaties elephants will inflict
            int numCasualties = defendingUnitMeleeEngagementCasualties(attackUnit, defendUnit);
            // double r = Math.random() * 100;
            if (r.nextInt(100) < 10) {
                // we have to grab a random unit from the attacking army
                int defendSize = r.nextInt(defendingArmy.size());
                Unit unluckyUnit = defendingArmy.get(defendSize);
                if (unluckyUnit.getNumTroops() - numCasualties <= 0) {
                    defendingArmy.remove(unluckyUnit);
                } else {
                    unluckyUnit.removeNumTroops(numCasualties);
                }
                return true;
            }
            return false;
        }
        return false;
    }
    // The amount of casualties dealt by attacking to defending troops
    // if there is a range unit attacking wihtin a range engagement 
    // double A = (size of enemy unit at start of engagement x 10%)
    // double B = (Missile attack damage of unit)
    // double C = (effective armor of enemy unit + effective shield of enemy unit)) 
    // double D = (N+1), where N is normal distribution N(0,1)
    // (int)result = A * B/C * D
    public int attackingUnitRangeEngagementCasualties(Unit attackUnit, Unit defendUnit) {
        if (attackUnit.getType().equals("melee")) {
            return 0;
        } 

        if (attackUnit.isBreaking()) {
            return 0;
        }
        // if the attacking unit is ranged
        int defendSize = defendUnit.getNumTroops();
        double N = r.nextGaussian();
        double A = defendSize * 0.1;
        double B = attackUnit.getAttack();
        double C;
        // Berserker special ability will result in a Zero Division Error
        // cap to value 10 
        if (defendUnit.getArmour() + defendUnit.getShieldDefense() == 0) {
            C = 10;
        } else {
            C = (defendUnit.getArmour() + defendUnit.getShieldDefense());
        }
        double D = (N + 1);
        double result = A * B/C * D;
        int casualties = (int)result;
        // if the casualties is greater than the size of the defending troops
        // set the casualties to the defending troops size
        if (casualties >= defendSize) {
            casualties = defendSize;
        }
        // if the casualties is less than 0, set the casualties to 0 
        if (casualties < 0) {
            casualties = 1;
        }
        return casualties;
    }

    // The amount of casualties dealt by defending to attacking troops
    // if there is a range unit defending wihtin a range engagement 
    // double A = (size of enemy unit at start of engagement x 10%)
    // double B = (Missile attack damage of unit)
    // double C = (effective armor of enemy unit + effective shield of enemy unit)) 
    // double D = (N+1), where N is normal distribution N(0,1)
    // (int)result = A * B/C * D
    public int defendingUnitRangeEngagementCasualties(Unit attackUnit, Unit defendUnit) {
        if (defendUnit.getType().equals("melee")) {
            return 0;
        }  
        
        if (defendUnit.isBreaking()) {
            return 0;
        }
        // otherwise it is a ranged unit defending, deal casualties
        int attackSize = attackUnit.getNumTroops();
        double N = r.nextGaussian();
        double A = attackSize * 0.1;
        double B = defendUnit.getAttack();
        double C;
        // Berserker special ability will result in a Zero Division Error
        // cap to value 10 
        if (attackUnit.getArmour() + attackUnit.getShieldDefense() == 0) {
            C = 10;
        } else {
            C = attackUnit.getArmour() + attackUnit.getShieldDefense();
        }
        double D = (N + 1);
        double result = A * B/C * D;
        int casualties = (int)result;
        // if the casualties is greater than the size of the attacking troops
        // set the casualties to the attacking troops size
        if (casualties >= attackSize) {
            casualties = attackSize;
        }
        // if the casualties is less than 0, set the casualties to 0 
        if (casualties < 0) {
            casualties = 1;
        }
        return casualties;
    }
    
    // For all attacking and defending melee cavalry/elephants 
    // they will have attack damage = melee attack damage + charge value
    // in all engagements
    public void addChargeStatistic(Unit attackUnit, Unit defendUnit) {
        int attackChargeStat = attackUnit.getChargeStatistic();
        int defendChargeStat = defendUnit.getChargeStatistic();
        String attackType = attackUnit.getType();
        String defendType = defendUnit.getType();
        // melee cavalry and elephants are the only units that share melee type and 15MP
        if (attackType.equals("melee") && attackUnit.getMovementPoints() == 15) {
            int attackUnitDmg = attackUnit.getAttack();
            attackUnit.setAttack(attackUnitDmg + attackChargeStat);
        }
        if (defendType.equals("melee") && defendUnit.getMovementPoints() == 15) {
            int defendUnitDmg = defendUnit.getAttack();
            defendUnit.setAttack(defendUnitDmg + defendChargeStat);
        }
    }

    // The amount of casualties dealt by attacking to defending troops
    // Units in a melee engagement inflict casualties against the opposing unit 
    // double A = (size of enemy unit at start of engagement x 10%) 
    // double B = (Effective melee attack damage of unit)
    // double C = (effective armor of enemy unit + effective shield of enemy unit + effective defense skill of enemy unit)) 
    // double D = (N+1), where N is normal distribution N(0,1)
    // (int)result = A * B/C * D
    public int attackingUnitMeleeEngagementCasualties(Unit attackUnit, Unit defendUnit) {
        if (attackUnit.isBreaking()) {
            return 0;
        }
        
        int defendSize = defendUnit.getNumTroops();
        // Random r = new Random(seed);
        double N = r.nextGaussian();
        double A = defendSize * 0.1;
        double B = attackUnit.getAttack();
        double C = (defendUnit.getArmour() + defendUnit.getShieldDefense() + defendUnit.getDefenseSkill());
        double D = (N + 1);
        double result = A * B/C * D;
        int casualties = (int)result;
        // maximum casualties is entire enemy unit
        if (casualties >= defendSize) {
            casualties = defendSize;
        }
        // minimum casualties is none of the enemy unit
        if (casualties < 0) {
            casualties = 1;
        }
        return casualties;
    }

    // The amount of casualties dealt by defending to attacking troops
    // Units in a melee engagement inflict casualties against the opposing unit 
    // double A = (size of enemy unit at start of engagement x 10%) 
    // double B = (Effective melee attack damage of unit)
    // double C = (effective armor of enemy unit + effective shield of enemy unit + effective defense skill of enemy unit)) 
    // double D = (N+1), where N is normal distribution N(0,1)
    // (int)result = A * B/C * D
    public int defendingUnitMeleeEngagementCasualties(Unit attackUnit, Unit defendUnit) {
        if (defendUnit.isBreaking()) {
            return 0;
        }
        
        int attackSize = attackUnit.getNumTroops();
        // Random r = new Random(seed);
        double N = r.nextGaussian();
        double A = attackSize * 0.1;
        double B = defendUnit.getAttack();
        double C = (attackUnit.getArmour() + attackUnit.getShieldDefense() + attackUnit.getDefenseSkill());
        double D = (N + 1);
        double result = A * B/C * D;
        int casualties = (int)result;
        // maximum casualties is entire enemy unit
        if (casualties >= attackSize) {
            casualties = attackSize;
        }
        // minimum casualties is none of the enemy unit
        if (casualties < 0) {
            casualties = 1;
        }
        return casualties;
    }

    // breaking chance -> attempting to flee is determined by the morale value 
    // which is calculated after consideration of high tax in provibnce and 
    // morale loss due to scary units in enemy team
    // scalar addition of increase chance:
    // int allyCasualties = (casualties suffered by the unit during the engagement)
    // int numStartAllyEngagement = (number of troops in the unit at the start of the engagement)
    // int enemyCasualties = (casualties suffered by the opposing unit during the engagement)
    // int numStartEnemyEngagement = (number of troops in the opposing unit at the start of the engagement) x 10%
    // int resultChance = (attackCasualties/numStartAllyEngagement)/(defendCasualties/numStartEnemyEngagement) * 0.1
    public boolean attackBreaking(int attackStartNum, int defendStartNum, 
        Unit attackUnit, Unit defendUnit, int allyCasualties, int enemyCasualties) {
        // base level probability of a unit breaking is 
        // 100% - (morale x 10%)
        int attackBreakChance = 100 - (attackUnit.getMorale() * 10);

        int numStartAllyEngagement = attackStartNum;
        int numStartEnemyEngagement = defendStartNum;
        numStartAllyEngagement = 1;
        numStartEnemyEngagement = 1;
        System.out.println("allyCasualities: " + allyCasualties);
        System.out.println("enemyCasulaties: " + enemyCasualties);  
        System.out.println("numStartAllyEngagement: " + attackUnit.getNumTroops());
        System.out.println("numStartEnemyEngagement: " + defendUnit.getNumTroops());         
        int increasedChance = (((allyCasualties)/(numStartAllyEngagement))/((enemyCasualties)/(numStartEnemyEngagement))) * 10;
        int chance = attackBreakChance + increasedChance;
        // double r = Math.random() * 100;
        // Random r = new Random(seed);
        if (chance < 5) {
            chance = 5;
        }
        if (chance > 100) {
            chance = 100;
        }
        if (r.nextInt(100) < chance) {
            attackUnit.setBreaking(true);
            return true;
        } else {
            return false;
        }
    }


    // scalar addition of increase chance:
    // int allyCasualties = (casualties suffered by the unit during the engagement)
    // int numStartAllyEngagement = (number of troops in the unit at the start of the engagement)
    // int enemyCasualties = (casualties suffered by the opposing unit during the engagement)
    // int numStartEnemyEngagement = (number of troops in the opposing unit at the start of the engagement) x 10%
    // int resultChance = (attackCasualties/numStartAllyEngagement)/(defendCasualties/numStartEnemyEngagement) * 0.1
    public boolean defendBreaking(int attackStartNum, int defendStartNum, 
    Unit attackUnit, Unit defendUnit, int allyCasualties, int enemyCasualties) {
        // base level probability of a unit breaking is 
        // 100% - (morale x 10%)
        int defendBreakChance = 100 - (defendUnit.getMorale() * 10);

        int numStartAllyEngagement = defendStartNum; 
        // defendUnit.getNumTroops();
        int numStartEnemyEngagement = attackStartNum;
        // attackUnit.getNumTroops();
        numStartAllyEngagement = 1; 
        // defendUnit.getNumTroops();
        numStartEnemyEngagement = 1; 
        int increasedChance = (((allyCasualties)/(numStartAllyEngagement))/((enemyCasualties)/(numStartEnemyEngagement))) * 10;
        int chance = defendBreakChance + increasedChance;
        // double r = Math.random() * 100;
        // Random r = new Random(seed);
        // setting up the lower limit for the chance
        if (chance < 5) {
            chance = 5;
        }
        // settung up the upper limit for the chance
        if (chance > 100) {
            chance = 100;
        }
        if (r.nextInt(100) < chance) {
            defendUnit.setBreaking(true);
            return true;
        } else {
            return false;
        }
    
    }

    /**
     * Calculates whether a unit that has broken routes from the Skirmish or not.
     * @param attackUnit
     * @param defendUnit
     * @param attackingRouteList
     * @param defendingRouteList
     * @return True if even one of them successfully routes and end the skirmish. If both units break, they both successfully flee the battle without inflicting further casualties upon each other.
     */
    public boolean unitRoutResult (Unit attackUnit, Unit defendUnit, List<Unit> attackingRouteList, List<Unit> defendingRouteList) {
        // if both attacking and defending units break, return true
        if (attackUnit.isBreaking() && defendUnit.isBreaking()) {
            attackingRouteList.add(attackUnit);
            defendingRouteList.add(defendUnit);
            attackingArmy.remove(attackUnit);
            attackingArmy.remove(defendUnit);
            System.out.println("Both units ran from the engagement!");
            return true;
        // if the attacking army tries to route
        // have chances here, return true if they run return false if not
        } else if (attackUnit.isBreaking() && !defendUnit.isBreaking()) {
            // if the attacking unit successfully routes
            if (attackRouting(attackUnit, defendUnit)) {
                attackingRouteList.add(attackUnit);
                attackingArmy.remove(attackUnit);
                defendingArmy.add(defendUnit);
                System.out.println("Attacking unit routed successfully");
                System.out.println("Defending Unit moved back into their army");
                return true;
            // they fail to rout
            } else {
                System.out.println("Attacking unit failed to route");
                return false;
            }
        } else if (!attackUnit.isBreaking() && defendUnit.isBreaking()) {
            // if the defending unit successfully routes
            if (defendRouting(attackUnit, defendUnit)) {
                defendingRouteList.add(defendUnit);
                defendingArmy.remove(defendUnit);
                attackingArmy.add(attackUnit);
                System.out.println("Defending unit routed successfully");
                System.out.println("Attacking Unit moved back into their army");

                return true;
            } else {
                System.out.println("Defending unit failed to route");
                return false;
            }
        }
        return false;
    }

    /**
     * Calculates whether an attacking unit routes successfully or not
     * @param attackUnit
     * @param defendUnit
     * @return True for a successful route, false otherwise
     */
    public boolean attackRouting(Unit attackUnit, Unit defendUnit) {
        // chance of routing successfully:
        // 50% + 10% x (speed of routing unit - speed of pursuing unit)
        int baseRoutChance = 50 + 10 * (attackUnit.getSpeed() - defendUnit.getSpeed());

        if (baseRoutChance < 10) {
            baseRoutChance = 10;
        }
        if (baseRoutChance > 100) {
            baseRoutChance = 100;
        }
        // double randomVariable = Math.random() * 100;
        // Random r = new Random(seed);
        if (r.nextInt(100) < baseRoutChance) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * See attackRouting above. 
     * @param attackUnit
     * @param defendUnit
     * @return
     */
    public boolean defendRouting(Unit attackUnit, Unit defendUnit) {
        // chance of routing successfully:
        // 50% + 10% x (speed of routing unit - speed of pursuing unit)
        int baseRoutChance = 50 + 10 * (defendUnit.getSpeed() - attackUnit.getSpeed());
        if (baseRoutChance < 10) {
            baseRoutChance = 10;
        }
        if (baseRoutChance > 100) {
            baseRoutChance = 100;
        }
        // double randomVariable = Math.random() * 100;
        // Random r = new Random(seed);
        if (r.nextInt(100) < baseRoutChance) {
            return true;
        } else {
            return false;
        }
    }

    public Province getInvader() {
        return invader;
    }

    public void setInvader(Province invader) {
        this.invader = invader;
    }

    public Province getDefender() {
        return defender;
    }

    public void setDefender(Province defender) {
        this.defender = defender;
    }

    public List<Unit> getAttackingArmy() {
        return attackingArmy;
    }

    public void setAttackingArmy(List<Unit> attackingArmy) {
        this.attackingArmy = attackingArmy;
    }

    public List<Unit> getDefendingArmy() {
        return defendingArmy;
    }

    public void setDefendingArmy(List<Unit> defendingArmy) {
        this.defendingArmy = defendingArmy;
    }

    public List<Unit> getAttackingRouteList() {
        return attackingRouteList;
    }

    public void setAttackingRouteList(List<Unit> attackingRouteList) {
        this.attackingRouteList = attackingRouteList;
    }

    public List<Unit> getDefendingRouteList() {
        return defendingRouteList;
    }

    public void setDefendingRouteList(List<Unit> defendingRouteList) {
        this.defendingRouteList = defendingRouteList;
    }


}


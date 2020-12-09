package unsw.gloriaromanus;

import java.util.ArrayList;

public class Province {
    //private int id;
    private String owner;                   // The name of the faction that owns this province
    private String name;                    
    private int wealth;                     // Wealth of the province
    private ArrayList<Unit> trainingUnits;  // Units currently training in the province  
    private ArrayList<Unit> units;          // Units currently available to move/attack/defend
    private Tax taxRate;                    // Strategy pattern for taxrate
    private boolean conqueredInCurrTurn;    // Ensures you cannot attack out of a newly conquered province

    public Province(String faction, String name) {
        this.owner = faction;
        this.name = name;
        this.units = new ArrayList<Unit>(); 
        this.trainingUnits = new ArrayList<Unit>();  
        Tax t = new NormalTax();
        this.taxRate = t; // by default, provinces have normal tax rate
        this.wealth = 10; // OUR CHOICE: BY DEFAULT WEALTH = 10
        this.conqueredInCurrTurn = false;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    /**
     * Used when creating a new unit of troops for a province, they sit here until they are fully trained.
     * @param unit
     * @return
     */
    public boolean addTraineeUnit(Unit unit) {
        ArrayList<Unit> tmp = getTrainingUnits();
        if (getTrainingUnits().size() == 2) {
            System.out.println("You can only have at most TWO units training at a time.");
            return false;
        }
        tmp.add(unit);
        this.trainingUnits= tmp;
        return true;
    }

    /**
     * Adds a given unit object into its units List
     * @param unit
     */
    public void addUnit(Unit unit) {
        // System.out.println("unit added: " + unit.getName());
        ArrayList<Unit> tmp = getUnits();
        tmp.add(unit);
        // for (Unit u : tmp) {
        //     System.out.println("List" + u.getName());
        // }
        this.units = tmp;
    }

    public void removeUnit(Unit unit) {
        ArrayList<Unit> tmp = new ArrayList<Unit>();
        tmp.addAll(getUnits());
        tmp.remove(unit);
        this.units = tmp;
    }

    public double collectTax() {
        return taxRate.collectTax(this);
    }

    /**
     * either increases, decreases or does nothing to the province's wealth
     * attribute, depending on the Tax strategy employed for that province
     */
    public void updateProvinceWealth() {
        taxRate.wealthGrowth(this);
    }

    public int getWealth() {
        return wealth;
    }

    public void setWealth(int wealth) {
        this.wealth = wealth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tax getTaxRate() {
        return taxRate;
    }

    public String getTaxRateName() {
        return getTaxRate().getName();
    }

    /**
     * A switch statement that sets the tax rate of the current province
     * @param taxRate
     */
    public void setTaxRate(String taxRate) {
        boolean success = false;
        switch(taxRate) { // TODO: verify that this works thru testing
            case "LowTax":
                this.taxRate = new LowTax();
                success = true;
                break;
            case "NormalTax":
                this.taxRate = new NormalTax();
                success = true;
                break;
            case "HighTax":
                this.taxRate = new HighTax();
                success = true;
                break;
            case "VeryHighTax":
                this.taxRate = new VeryHighTax(); 
                success = true;
                break;                
        }
        if (!success) {
            System.out.println("Failed to set a new tax rate.");
        }
        // System.out.println("New Tax Rate is: "+temp);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setUnits(ArrayList<Unit> units) {
        this.units = units;
    }

    public ArrayList<Unit> getTrainingUnits() {
        return trainingUnits;
    }

    public void setTrainingUnits(ArrayList<Unit> trainingUnits) {
        this.trainingUnits = trainingUnits;
    }

    public boolean isConqueredInCurrTurn() {
        return conqueredInCurrTurn;
    }

    public void setConqueredInCurrTurn(boolean conqueredInCurrTurn) {
        this.conqueredInCurrTurn = conqueredInCurrTurn;
    }
}

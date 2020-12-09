package unsw.gloriaromanus;

public class LowTax implements Tax {

    @Override
    public void wealthGrowth (Province p) {
        // Low tax = +10 town-wealth growth per turn for the province, tax rate = 10%
        int provinceWealth = p.getWealth();
        p.setWealth((provinceWealth + 10));
    }
    
    @Override
    public double collectTax (Province p) {
        return p.getWealth() * 0.1;
        
    }

    public String getName() {
        return "LowTax";
    }
}

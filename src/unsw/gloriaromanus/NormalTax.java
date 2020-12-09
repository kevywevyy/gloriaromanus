package unsw.gloriaromanus;

public class NormalTax implements Tax {
    // No effect on per turn town-wealth growth, tax rate = 15%
    @Override
    public void wealthGrowth (Province p) {
        // do nothing 
        p.setWealth(p.getWealth());
    }
    
    @Override
    public double collectTax (Province p) {
        return p.getWealth() * 0.15;
    }

    public String getName() {
        return "NormalTax";
    }
}


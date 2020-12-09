package unsw.gloriaromanus;

public class HighTax implements Tax {
    @Override
    public void wealthGrowth (Province p) {
        // -10 town-wealth growth per turn for the province (i.e. 10 gold loss to wealth per turn), tax rate = 20%
        int provinceWealth = p.getWealth();
        // ensuring province wealth can't fall below 0
        if (provinceWealth - 10 > 0) {
            p.setWealth((provinceWealth - 10));
        } else {
            p.setWealth(0);
        }
        
    }
    @Override
    public double collectTax (Province p) {
        return p.getWealth() * 0.2;
    }

    public String getName() {
        return "HighTax";
    }
}
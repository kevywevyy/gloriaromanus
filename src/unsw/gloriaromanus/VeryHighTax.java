package unsw.gloriaromanus;

public class VeryHighTax implements Tax {
    @Override
    public void wealthGrowth (Province p) {
        // -30 town-wealth growth per turn for the province (i.e. 30 gold loss to wealth per turn), tax rate = 25%
        int provinceWealth = p.getWealth();
        if ((provinceWealth - 30) > 0) {
            p.setWealth((provinceWealth - 30));
        // ensuring province wealth can't fall below 0
        } else {
            p.setWealth(0);
        }

        // TODO: Stat modifier debuff morale for soldiers
    }
    
    @Override
    public double collectTax (Province p) {
        return p.getWealth() * 0.25;
    }

    public String getName() {
        return "VeryHighTax";
    }
}
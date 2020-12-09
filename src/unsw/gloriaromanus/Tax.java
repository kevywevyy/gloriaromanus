package unsw.gloriaromanus;

/**
 * Tax
 */
public interface Tax {
    public void wealthGrowth(Province p);
    
    public double collectTax(Province p);

    public String getName();
}
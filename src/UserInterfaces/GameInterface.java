/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterfaces;

/**
 *
 * @author ASimionescu
 */
public interface GameInterface
{    
    public void newMinistry(int id, String name, int budget);
    public void newProtester(int protesterID, int ministryID);
    public void protesting(int protesterID);
    public void ueFundingMinistry(int ministryID, int amount);
    public void protesterLeave(int protesterID, int ministryID, boolean withMoney);
    public void ministryTheft(int ministryID, int amount);
    public void ministryDonate(int ministryID, int amount);
    public void policeArived();
    public void policeLeft();

    public void showGameSettings(int ministriesSize, int ministryMaxBudget, int protestsLimit, int intvalProtest, int intvalUEFunding);   
}

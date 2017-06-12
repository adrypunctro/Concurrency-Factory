/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterfaces;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author ASimionescu
 */
public class GameConsole
        implements GameInterface
{
    @Override
    public void showGameSettings(int ministriesSize, int ministryMaxBudget, int protestsLimit, int intvalProtest, int intvalUEFunding)
    {
        System.out.println("===============================================");
        System.out.println("                  DEMOCRACY                    ");
        System.out.println("Settings:");
        System.out.println("> ministries: "+ministriesSize);
        System.out.println("> ministryMaxBudget: "+ministryMaxBudget);
        System.out.println("> protestsLimit: "+protestsLimit);
        System.out.println("> intvalProtest: "+intvalProtest);
        System.out.println("> intvalUEFunding: "+intvalUEFunding);
        System.out.println("===============================================");
        System.out.println("");
    }

    @Override
    public void newMinistry(int id, String name, int budget) {
        ministries.put(id, new MinistryObj(id, name, budget));
        System.out.println("["+id+"] "+name+" ("+budget+"$) was created.");
    }

    @Override
    public void newProtester(int protesterID, int ministryID) {
        MinistryObj obj = ministries.get(ministryID);
        if (obj != null) {
            VA_DEBUG.INFO("New protest ["+protesterID+"] at "+obj.name, true);
        }
        else {
            VA_DEBUG.ERROR("ERR ministery ["+ministryID+"] not found.", true);
        }
    }

    @Override
    public void ueFundingMinistry(int ministryID, int amount) {
        MinistryObj obj = ministries.get(ministryID);
        VA_DEBUG.SUCCESS("UEFunding: ["+ministryID+"] "+obj.name+" ("+obj.budget+"$)+"+amount, true);
    }

    @Override
    public void protesterLeave(int protesterID, boolean withMoney) {
        VA_DEBUG.INFO("Protester ["+protesterID+"] left "+(withMoney?"$$$":"no money"), true);
    }

    @Override
    public void ministryTheft(int ministryID, int amount) {
        VA_DEBUG.ERROR("Minister of ["+ministryID+"] stole "+amount+".", true);
    }

    @Override
    public void ministryDonate(int ministryID, int amount) {
        VA_DEBUG.SUCCESS("Minister of ["+ministryID+"] donate "+amount+".", true);
    }

    @Override
    public void policeArived() {
        VA_DEBUG.WARNING("--== Police arived ==--", true);
    }

    @Override
    public void policeLeave() {
        VA_DEBUG.WARNING("--== Police leaved ==--", true);
    }

    @Override
    public void protesting(int protesterID) {
        VA_DEBUG.INFO("Protester ["+protesterID+"] protesting...", true);
    }
    
    
    private class MinistryObj {
        public int id;
        public String name;
        public int budget;
        public MinistryObj(int id, String name, int budget) {
            this.id = id;
            this.name = name;
            this.budget = budget;
        }
    }
    
    private Map<Integer, MinistryObj> ministries = new TreeMap<>();
}

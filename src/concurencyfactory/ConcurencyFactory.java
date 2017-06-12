/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurencyfactory;

import UserInterfaces.GameInterface;
import UserInterfaces.GameConsole;

/**
 *
 * @author ASimionescu
 */
public class ConcurencyFactory
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        GameInterface ui = new GameConsole();
        Game game = new Game(ui);
        
        game.setMinistries(new String[]{
            "Ministry of Agriculture",
            "Ministry of the Environment",
            "Ministry of Justice",
            "Ministry of Education",
            "Ministry of Health"
        });
        game.setThreftInterval(4000);
        game.setIntvalUEFunding(10000);
        game.setMinistryMaxBudget(5);
        
        game.setProtestsLimit(5);
        game.setIntvalSpawnProtester(2000);
        game.setMinTimeProtesting(8000);
        game.setMaxTimeProtesting(10000);

        
        game.start();
    }
    
}

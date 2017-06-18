/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurencyfactory;

import UserInterfaces.GameInterface;
import UserInterfaces.GameConsole;
import UserInterfaces.GameGUI;

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
        //GameInterface ui = new GameConsole();
        //Game game = new Game(ui);
        
        GameGUI gui = new GameGUI();
        Game game = new Game(gui);
        
        game.setMinistries(new String[]{
            "Ministry of Agriculture",
            "Ministry of the Environment",
            "Ministry of Justice",
            "Ministry of Education",
            "Ministry of Health",
            "Ministry of Economy",
            "Ministry of Transport",
            "Ministry of Youth"
        });
        
        game.setMinTheftInterval(4000);
        game.setMaxTheftInterval(8000);
        game.setIntvalUEFunding(8000);
        game.setMinistryMaxBudget(6);
        
        game.setProtesterLimit(15);
        game.setIntvalSpawnProtester(700);
        game.setMinTimeProtesting(8000);
        game.setMaxTimeProtesting(12000);
        
        gui.setVisible(true);
        game.start();
    }
    
}

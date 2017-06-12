/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurencyfactory;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASimionescu
 */
public class GameMap {
    
    private List<UnitOnMap> units = new ArrayList<>();
    
            
    class UnitOnMap {
        public int x;
        public int y;
        public Unit unit;
    }
}

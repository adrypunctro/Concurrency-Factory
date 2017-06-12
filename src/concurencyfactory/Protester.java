/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurencyfactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author ASimionescu
 */
public class Protester
{
    private static final AtomicInteger AUTO_INCREMENT = new AtomicInteger(0);
    private final int id;
    private final int ministryID;
    private final int timeProtesting;
    
    public Protester(int ministryID, int timeProtesting)
    {
        this.id = AUTO_INCREMENT.getAndIncrement();
        this.ministryID = ministryID;
        this.timeProtesting = timeProtesting;
    }
    
    public int getId() {
        return id;
    }
    
    public int getMinistryId() {
        return ministryID;
    }
    
    public void protest()
    {
        
        try {Thread.sleep(timeProtesting);} catch (InterruptedException ex) {}
    }
}

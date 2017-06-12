package concurencyfactory;

import java.util.concurrent.atomic.AtomicInteger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ASimionescu
 */
public class Ministry
{
    private static final AtomicInteger AUTO_INCREMENT = new AtomicInteger(0);
    
    private final int id;
    private final String name;
    private int budget;
    private AtomicInteger protesting; 
    
    Ministry(String name, int initBudget)
    {
        this.id = AUTO_INCREMENT.getAndIncrement();
        this.name = name;
        this.budget = initBudget;
        this.protesting = new AtomicInteger(0);
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the budget
     */
    public int getBudget() {
        return budget;
    }

    void addFund(int i) {
        this.budget += i;
    }
    
    public int getId() {
        return id;
    }
    
    public void donate(int i)
    {
        this.budget -= i;
    }
    
    public void stole(int i)
    {
        this.budget -= i;
    }

    /**
     * @return the protesting
     */
    public boolean isProtesting() {
        return protesting.get() > 0;
    }

    public void incProtesting() {
        protesting.incrementAndGet();
    }

    public void decProtesting() {
        protesting.decrementAndGet();
    }
}

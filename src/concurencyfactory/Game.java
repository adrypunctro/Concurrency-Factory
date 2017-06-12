/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concurencyfactory;

import UserInterfaces.GameInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author ASimionescu
 */
public class Game
{
    private final GameInterface ui;
    
    private final Map<Integer, Ministry> ministries = new TreeMap<>();
    private final Queue<Protester> protestersQueue = new ConcurrentLinkedQueue<>();
    private final AtomicInteger protestersCount = new AtomicInteger(0);
    private int ministryMaxBudget       = 5;
    private int protestsLimit           = 2;
    private int spawnProtesterInterval  = 2000;
    private int intvalUEFunding         = 6000;
    private int minTimeProtesting       = 1000;
    private int maxTimeProtesting       = 5000;
    private int threftInterval          = 1000;
    
    private final Lock protesterLock;
    private final Condition protesterCond;
    
    public Game(GameInterface ui)
    {
        protesterLock = new ReentrantLock();
        protesterCond = protesterLock.newCondition();
        
        this.ui = ui;
    }
    
    /**
     * @return the ministries
     */
    public Map<Integer, Ministry> getMinistries() {
        return ministries;
    }

    /**
     * @param ministries the ministries to set
     */
    public void setMinistries(String[] ministries) {
        Random rn = new Random();
        int minBudget = 1;
        int maxBudget = 5;
        for(String name : ministries) {
            int initBudget = minBudget + rn.nextInt(maxBudget - minBudget + 1);
            Ministry m = new Ministry(name, initBudget);
            this.ministries.put(m.getId(), m);
            ui.newMinistry(m.getId(), name, initBudget);
        }
    }

    /**
     * @return the europeanFundsLimit
     */
    public int getMinistryMaxBudget() {
        return ministryMaxBudget;
    }

    /**
     * @param europeanFundsLimit the europeanFundsLimit to set
     */
    public void setMinistryMaxBudget(int europeanFundsLimit) {
        this.ministryMaxBudget = europeanFundsLimit;
    }

    /**
     * @return the protestsLimit
     */
    public int getProtestsLimit() {
        return protestsLimit;
    }

    /**
     * @param protestsLimit the protestsLimit to set
     */
    public void setProtestsLimit(int protestsLimit) {
        this.protestsLimit = protestsLimit;
    }
    
    /**
     * @return the intvalProtest
     */
    public int getIntvalProtest() {
        return spawnProtesterInterval;
    }

    /**
     * @param intvalProtest the intvalProtest to set
     */
    public void setIntvalSpawnProtester(int intvalProtest) {
        this.spawnProtesterInterval = intvalProtest;
    }
    
    /**
     * @return the minTimeProtesting
     */
    public int getMinTimeProtesting() {
        return minTimeProtesting;
    }

    /**
     * @param minTimeProtesting the minTimeProtesting to set
     */
    public void setMinTimeProtesting(int minTimeProtesting) {
        this.minTimeProtesting = minTimeProtesting;
    }

    /**
     * @return the maxTimeProtesting
     */
    public int getMaxTimeProtesting() {
        return maxTimeProtesting;
    }

    /**
     * @param maxTimeProtesting the maxTimeProtesting to set
     */
    public void setMaxTimeProtesting(int maxTimeProtesting) {
        this.maxTimeProtesting = maxTimeProtesting;
    }
    
    public void start()
    {
        ui.showGameSettings(ministries.size(), ministryMaxBudget, protestsLimit, spawnProtesterInterval, intvalUEFunding);
        
        // European Union Funding ----------------------------------------------
        Thread ueThread = new Thread(){
            @Override
            public void run(){
                while(true)
                {
                    try {Thread.sleep(intvalUEFunding);} catch (InterruptedException ex) {}
                    for (Ministry ministry : ministries.values())
                    {
                        if (ministry.getBudget() < ministryMaxBudget) {
                            ministry.addFund(1);
                            ui.ueFundingMinistry(ministry.getId(), 1);
                        }
                    }
                }
            }
        };
        ueThread.start();
        
        // Protesting Thread ---------------------------------------------------
        Thread protestingThread = new Thread(){
            @Override
            public void run(){
                while(true)
                {
                    final Protester protester = protestersQueue.poll();
                    if (protester != null)
                    {
                        new Thread(){
                            @Override
                            public void run(){
                                Ministry ministry = ministries.get(protester.getMinistryId());
                                ministry.incProtesting();
                                ui.protesting(protester.getId());
                                protester.protest();
                                boolean withMoney = false;
                                if (ministry.getBudget() > 0) {
                                    ministry.donate(1);
                                    ui.ministryDonate(ministry.getId(), 1);
                                    withMoney = true;
                                }
                                ministry.decProtesting();
                                protestersCount.decrementAndGet();
                                ui.protesterLeave(protester.getId(), withMoney);
                            }
                        }.start();
                    }
                    try {Thread.sleep(100);} catch (InterruptedException ex) {}
                }
            }
        };
        protestingThread.start();
        
        // Stole Thread --------------------------------------------------------
        Thread stoleThread = new Thread() {
            @Override
            public void run() {
                while(true)
                {
                    try {Thread.sleep(getThreftInterval());} catch (InterruptedException ex) {}
                    
                    for(Ministry ministry : ministries.values())
                    {
                        if (!ministry.isProtesting() && ministry.getBudget()>0) 
                        {
                            new Thread(){
                                @Override
                                public void run(){
                                    ui.ministryTheft(ministry.getId(), 1);
                                    ministry.stole(1);
                                }
                            }.start();
                        }
                    }
                }
            }
        };
        stoleThread.start();
        
        
        // New protesters ------------------------------------------------------
        Random rn = new Random();
        int minMinistry = 0;
        int maxMinistry = ministries.size()-1;
        while(true)
        {
            checkProtester();
            
            int ministryTargetIndex = minMinistry + rn.nextInt(maxMinistry - minMinistry + 1);
            addProtest(ministryTargetIndex);
            
            try {Thread.sleep(spawnProtesterInterval);} catch (InterruptedException ex) {}
        }
        
    }
    
    public void addProtest(int ministryID)
    {
        Ministry ministryTarget = ministries.get(ministryID);
        Random rn = new Random();
        int minTime = getMinTimeProtesting()/1000;
        int maxTime = getMaxTimeProtesting()/1000;
        int timeProtestingSec = minTime + rn.nextInt(maxTime - minTime + 1);// in seconds
        Protester p = new Protester(ministryID, timeProtestingSec*1000);
        protestersQueue.add(p);
        protestersCount.incrementAndGet();
        ui.newProtester(p.getId(), ministryID);
    }

    private void checkProtester()
    {
        if (protestersCount.get() >= protestsLimit)
        {
            new Thread(){
                @Override
                public void run(){
                    while(protestersCount.get() > 0)
                    {
                        try {Thread.sleep(100);} catch (InterruptedException ex) {}
                    }
                    unlockProtester();
                }
            }.start();
            
            protesterLock.lock();
            try {
                ui.policeArived();
                protesterCond.await();
            }
            catch (InterruptedException ex) {
                System.err.println(ex.getMessage());
            }
            finally { protesterLock.unlock(); }
        }
    }
    
    private void unlockProtester()
    {
        protesterLock.lock();
        try {
            ui.policeLeave();
            protesterCond.signalAll();
        }
        finally { protesterLock.unlock(); }
    }

    /**
     * @return the intvalUEFunding
     */
    public int getIntvalUEFunding() {
        return intvalUEFunding;
    }

    /**
     * @param intvalUEFunding the intvalUEFunding to set
     */
    public void setIntvalUEFunding(int intvalUEFunding) {
        this.intvalUEFunding = intvalUEFunding;
    }

    /**
     * @return the threftInterval
     */
    public int getThreftInterval() {
        return threftInterval;
    }

    /**
     * @param threftInterval the threftInterval to set
     */
    public void setThreftInterval(int threftInterval) {
        this.threftInterval = threftInterval;
    }
    
}

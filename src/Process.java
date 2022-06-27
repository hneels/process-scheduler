/**
 * Process Class: simulates a process waiting to run in CPU
 * Instances are created and added to PriorityQueue in ProcessScheduling.java
 * CS526 Final Project
 * @author Hope Neels
 */

public class Process {

    // the process id
    private int id;
    // the priority
    private int pr;
    // process execution takes this much time
    private int duration;
    // time when process arrives to system
    private int arrivalTime;
    // time when process actually starts running in CPU
    private int startTime;


    // getters: access instance variables
    public int getId() {return this.id;}
    public int getPriority() {return this.pr;}
    public int getDuration() { return this.duration;}
    public int getArrivalTime() {return this.arrivalTime;}
    public int getStartTime() {return this.startTime;}

    // setters: access instance variables
    public void setPriority(int priority) {this.pr = priority;}
    public void setStartTime(int startTime) {this.startTime = startTime;}

    // no setters for ID, duration, or arrival time because these don't change


    /**
     * Constructor that sets all instance variables
     * @param id the process ID
     * @param priority the process priority
     * @param duration the amount of "time units" process should take until completion
     * @param arrivalTime the "system time" that the process arrives in D data structure
     */
    public Process(int id, int priority, int duration, int arrivalTime) {
        this.id = id;
        this.pr = priority;
        this.duration = duration;
        this.arrivalTime = arrivalTime;
        this.startTime = 0; // will be set by ProcessScheduling.java in runProcess() method
    }

    /**
     * decrement the process's priority by 1 (equivalent of calling setPriority(getPriority() -1)
     * no parameters and no return value
     */
    public void decrementPriority() {
        // priority cannot be lower than 1
        if (this.pr > 1) --this.pr;
    }

    /**
     * Override toString() method to return a String representation of this process,
     * formatted as required by assignment instructions
     * @return a String representation of this process, which is used when printing the output
     */
    @Override
    public String toString() {
        return "id = " + this.id + ", priority = " + this.pr + ", duration = " +
                this.duration + ", arrival time = " + this.arrivalTime;
    }
}

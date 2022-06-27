/**
 * Main class for project
 * simulates a CPU scheduler
 * CS526 Final Project
 * @author Hope Neels
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.PriorityQueue;

public class ProcessScheduling {

    /**
     * createProcessFromFile Helper method
     * reads input from input.txt file and creates new process from each line
     * adds each process to a Priority Queue that represents the D data structure
     * and returns that Priority Queue of processes when file reading complete
     * @return processesD, which is a Priority Queue of processes created from the file
     * that are prioritized by their arrival time
     */
    private static PriorityQueue<Process> createProcessFromFile() {

        // the data structure D representing all processes that have been created but not yet arrived
        // passing in an ArrivalComparator to order the processes by arrival time
        PriorityQueue<Process> processesD = new PriorityQueue<>(new ArrivalComparator());

        // read all processes from input.txt and store them in D
        try (Scanner scanner = new Scanner(new File("input.txt"))) {
            while (scanner.hasNextLine()) {
                // split line on whitespace
                String[] tokens = scanner.nextLine().split("\\s+");
                // get the appropriate values to create the process object
                int id = Integer.parseInt(tokens[0]);
                int priority = Integer.parseInt(tokens[1]);
                int duration = Integer.parseInt(tokens[2]);
                int arrival = Integer.parseInt(tokens[3]);
                // create new process
                Process process = new Process(id, priority, duration, arrival);
                // add process to D priority queue
                processesD.add(process);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return processesD;
    }

    /** runProcess helper method
     * used within the while-loop in main method that represents a time-clock cycle
     * calculates the process wait time (current system time minus arrival time)
     * prints the process information to terminal
     * sets the process's startTime instance variable
     * and returns the total amount of time all processes have had to wait in Q data structure
     *
     * @param runningProcess the current process to run in the CPU simulation
     * @param currentTime the "system time" at which the process starts running
     * @param totalWaitTime the cumulative time all processes have had to wait in Q
     * @return new total wait time: the cumulative wait time of all previous processes plus this process
     */
    private static double runProcess(Process runningProcess, int currentTime, double totalWaitTime) {
        // calculate the wait time of the process and add it to totalWaitTime
        int processWaitTime = currentTime - runningProcess.getArrivalTime();
        totalWaitTime += processWaitTime;

        // print the process information to console
        System.out.println("Process removed from queue is: id = " + runningProcess.getId() + ", at time "
                + currentTime + ", wait time = " + processWaitTime + ", total wait time = " + totalWaitTime);
        System.out.println("Running process " + runningProcess);

        // set process's start time instance variable
        runningProcess.setStartTime(currentTime);

        // return the total wait time, which is the previous total wait time plus this process's wait time
        return totalWaitTime;
    }

    /** updatePriority helper method
     * updates the priority of any process in Q that has been waiting more than 30 system time units
     * no return value, just updates the process priority within the Q Priority Queue
     * @param currentTime the current "system time" represented as the number of times the while-loop CPU simulation runs
     * @param processesQ a PriorityQueue of processes to be updated
     */
    private static void updatePriority(int currentTime, int maxWaitTime, PriorityQueue<Process> processesQ) {
        System.out.println("Update priority: ");

        // temp data structure to hold any updated processes
        ArrayList<Process> tempList = new ArrayList<>();

        // inspect each process in queue using Iterator.remove() instead of enhanced for-loop to safely remove while
        // iterating and prevent ConcurrentModificationException
        Iterator<Process> iterator = processesQ.iterator();
        while (iterator.hasNext()) {
            Process process = iterator.next();
            int waitTime = currentTime - process.getArrivalTime();
            if (waitTime > maxWaitTime) {
                // print the process info before decrementing, then decrement
                System.out.println("PID = " + process.getId() + ", wait time = " + waitTime + ", current priority = " +
                        process.getPriority());
                process.decrementPriority();

                // must remove and reinsert the process to update its correct priority in Q
                iterator.remove();
                tempList.add(process);

                // print with new priority
                System.out.println("PID = " + process.getId() + ", new priority = " + process.getPriority());
            }
        }
        // re-add any updated processes from temp data structure back into Q
        processesQ.addAll(tempList);
        System.out.println();
    }


    public static void main(String[] args) throws IOException {

        // create new PrintStream so the standard output goes to a file, per assignment instructions
        PrintStream stream = new PrintStream("process_scheduling_output.txt");
        System.setOut(stream);

        // data structure "D" in assignment instructions representing all processes that have been created but
        // not yet arrived to CPU Ready Queue. Created with helper above
        PriorityQueue<Process> processesD = createProcessFromFile();

        // print all processes first
        for (Process process : processesD) {
            System.out.println(process);
        }

        // PriorityQueue "Q" in assignment instructions (simulation of a CPU's "Ready Queue")
        // passing in a PriorityComparator to order the processes by priority
        PriorityQueue<Process> processesQ = new PriorityQueue<>(new PriorityComparator());

        // initialize current time of the system
        int currentTime = 0;

        // total amount of time all processes have to wait
        double totalWaitTime = 0.0;

        // max wait time
        int maxWaitTime = 30;
        System.out.println("\nMaximum wait time: " + maxWaitTime  + "\n");

        // flag for whether a process is running in CPU
        boolean running = false;

        // the currently running process (starts as null)
        Process runningProcess = null;

        // flag for whether D is empty (so "D becomes empty" only prints once)
        boolean emptyDPrinted = false;

        // total process number (used to calculate average at end)
        int processNumber = processesD.size();

        // run loop once for every "time unit" until D and Q are empty AND all processes are finished running
        while ((!processesD.isEmpty()) || (!processesQ.isEmpty()) || running) {

            // look at the process in D that has the earliest arrival time.
            // if its arrival time <= currentTime, remove process from D and insert into Q
            if (!processesD.isEmpty()) {
                if (processesD.peek().getArrivalTime() <= currentTime) {
                    processesQ.add(processesD.poll());
                }
            } else if (!emptyDPrinted) {
                // print time at which D becomes empty and set flag, so it doesn't print again
                System.out.println("\nD becomes empty at time " + currentTime);
                emptyDPrinted = true;
            }

            // if there is a currently running process in the CPU
            if (running) {
                // if it's time for currently running process to finish, set running flag to false
                if (currentTime >= runningProcess.getStartTime() + runningProcess.getDuration()) {
                    System.out.println("Process " + runningProcess.getId() + " finished at time " + currentTime + "\n");
                    running = false;

                    // since the current process has finished, if there are more processes left in Q, update
                    // priorities of processes that have been waiting longer than max. wait time, using helper above
                    if (!processesQ.isEmpty()) updatePriority(currentTime, maxWaitTime, processesQ);
                }
            }

            // if Q is not empty and no process is currently running
            if ((!processesQ.isEmpty()) && (!running)) {

                // Remove a process with the smallest priority from Q
                runningProcess = processesQ.poll();

                // use helper method to print process information and calculate new totalWaitTime
                totalWaitTime = runProcess(runningProcess, currentTime, totalWaitTime);

                // Set running flag to true
                running = true;
            }

            // increment currentTime
            currentTime++;
        }

        // All CPU cycles have completed and all processes have run
        // print total wait time
        System.out.println("\nTotal wait time = " + totalWaitTime);
        // print average wait time
        System.out.println("Average wait time = " + (totalWaitTime / processNumber));

    }
}

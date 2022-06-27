/**
 * Compare two processes by ARRIVAL TIME:
 * extends Comparator class to compare two processes based on their arrival time
 * Passed as an argument to the readyQueue ("D" data structure) PriorityQueue constructor.
 * CS526 Final Project
 * @author Hope Neels
 */
import java.util.Comparator;

public class ArrivalComparator implements Comparator<Process> {

    /**
     * Compare two processes and return -1 if the first has lower arrival time,
     * return 1 if the second process has lower arrival time,
     * or return 0 if the processes have equal arrival time
     * @param a the first process
     * @param b the second process
     * @return an integer signifying which process arrives first
     */
    public int compare(Process a, Process b) {
        if (a.getArrivalTime() < b.getArrivalTime()) return -1;
        else if (a.getArrivalTime() == b.getArrivalTime()) return 0;
        else return 1;
    }
}

/**
 * Compare two processes by PRIORITY:
 * extends Comparator class to compare two processes based on their priority number
 * Passed as an argument to the readyQueue ("Q" data structure) PriorityQueue constructor.
 * CS526 Final Project
 * @author Hope Neels
 */
import java.util.Comparator;

public class PriorityComparator implements Comparator<Process> {

    /**
     * Compare two processes and return -1 if the first has lower priority number (higher priority),
     * return 1 if the second process has lower priority number (higher priority),
     * or return 0 if the processes have equal priority
     * @param a the first process
     * @param b the second process
     * @return an integer signifying which process takes priority
     */
    public int compare(Process a, Process b) {
        if (a.getPriority() < b.getPriority()) return -1;
        else if (a.getPriority() == b.getPriority()) return 0;
        else return 1;
    }
}

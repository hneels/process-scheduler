# Process Scheduler
Hope Neels

Data Structures & Algorithms Final Project

## Introduction
This documentation summarizes the data structures and methods used in Data Structures & Algorithms Final Project. The project is a small, simplified simulation of a CPU Scheduler, in which processes enter the “ready queue” when they are waiting to be executed. Unlike a real CPU Scheduler, each process runs to completion without interruption once it has started running. However, like some real CPUs, process priorities are updated when they have been waiting to ensure fair distribution of CPU time, and this is one of the primary tasks of implementing this assignment.

From the assignment instructions: 
"Processes arrive at a computer system and the computer system executes the processes one at a time based on a priority criterion. Each process has a process id, priority, arrival time, and duration. The duration of a process is the amount of time it takes to completely execute the process. The system keeps a priority queue to keep arriving processes and prioritize the execution of processes. When a process arrives, it is inserted into the priority queue. Then, each time the system is ready to execute a process, the system removes a process with the smallest priority from the priority queue and executes it for the duration of the process. Once the system starts executing a process, it finishes the execution of the process completely without any interruption.

"The simulation program uses a logical time to keep track of the simulation process and the same logical time is used to represent the arrivalTime and duration. The simulation goes through a series of iterations and each iteration represents the passage of one logical time unit. At the beginning, the current time is set to time 0. Each iteration implements what occurs during one time unit and, at the end of each iteration, the current time is incremented." &copy; 2022 Boston University


## Description of Data Structures

### "processesD" D Priority Queue
This data structure holds processes which have not yet arrived, meaning their arrival time is later than the current system time (represented by the while-loop in main method that implements the “system clock”). I chose to design this data structure as a priority queue. Several different data structures could have served this purpose, but I believe a Priority Queue will be the fastest choice for removing the next process when its arrival time is reached. Because the Q data structure is also a Priority Queue (see below), I implemented two different Comparators to be passed in when the Priority Queues are instantiated. For this D data structure, the Arrival Comparator class is passed into the Priority Queue constructor, ensuring that the “minimal key” process is always the one with the lowest arrival time. Though the processes in the current “input.txt” file are already ordered by arrival time, a robust implementation of this project will ensure a fast runtime even when process arrival times are received out of order, and this is why I believe a Priority Queue is the best choice.

### "processesQ" Q Priority Queue
This Priority Queue represents the container for processes whose arrival time has passed, that have been removed from the processesD Priority Queue, and are now waiting to be executed. The Priority Comparator class is implemented to be passed into this data structure’s constructor, so that keys are ordered by process priority (instead of arrival time as in the D data structure.)

### Process Class
The Process object encapsulates the data about the Process object, including ID, priority, duration, arrival time (all of which are read from the input file) as well as the start system time that the process begins executing. In addition to the standard constructor, setters and getters, this object has a decrementPriority() method that decreases its priority by one, for use within the updatePriority() method of ProcessScheduling.java.

I defined a toString() method in my Process class, so that the Process object is represented in a uniform String each time it is printed. This method always prints processes in the uniform format “id = 1, priority = 4, duration = 25, arrival time = 10” when printing the list of all processes or printing the individual processes when they are chosen to run. I chose to implement this toString() method to reduce redundant String concatenations throughout the program.

### "tokens" String Array
This String array is a temporary object used to parse the input from the “input.txt” file within the createProcessFromFile() helper method. When each line of the file is read with Scanner, the line is split into the “tokens” String array so each token can be used to create an attribute of the Process.

### "tempList" ArrayList of Processes
This data structure, which is created within the updatePriority() helper method, temporarily stores processes whose priorities must be updated when they have been waiting more than 30 time units. Since Java’s Priority Queue has no way to automatically bubble an object when its key is updated, the minimal key process might not be removed after a priority was updated unless the process object is removed and reinserted. The method safely removes any processes that have been waiting longer than 30 time units using the iterator.remove() method, stores them in tempList to update their priorities, then reinserts them back into the "processesQ" Priority Queue to ensure the heap maintains its proper order.

## Explanation of Helper Methods
More detailed descriptions of each method can be found within the code comments throughout the project files, including clear explanations of their parameters and return values. I have compartmentalized the code into segmented methods to isolate behavior and ensure good design. The ProcessScheduling.java file implements the following helper methods:
* createProcessFromFile() – reads the data from “input.txt” file, creates a new Priority Queue of Process objects based on that data (called "processesD" and described above) and returns that Priority Queue object when complete.
* runProcess() – accepts a Process object, a currentTime int, and a totalWaitTime int to simulate “running” the process in the CPU. The method calculates and stores the process object’s wait time and execution start time, prints the required “run process” data to the output file, and returns the new total wait time for all processes.
* updatePriority() – as described above, this method ensures the heap-order property is maintained in the Q Priority Queue by removing, updating, and reinserting any Process objects that have been waiting in the D data structure for longer than the maximum wait time of 30 time units.

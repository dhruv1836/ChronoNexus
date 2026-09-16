
## CHRONONEXUS




## Overview of the Project
ChronoNexus is a highly advanced Project and Task Management scheduling tool. Using CLI interface, it goes beyond plain to-do lists through utilizing Directed Acyclic Graphs (DAGs) to deal with task dependencies, determine critical paths, and simulate effects of an unexpected delay throughout the entire project timeline.

## Features
* **Intelligent Scheduling Engine:** Implements topological sorting algorithm to manage complex task dependencies and prevent impossible dependency loops (cycle detection).
* **Critical Path Calculation:** Determines the longest chain of dependent tasks and shows exactly which ones delay the whole project if behind schedule.
* **"What-If" Delay Simulations:** Calculates and simulates the effect of a delay on a certain task and displays the way it propagates through the rest of the tasks without actually implementing changes until approved.
* **Smart Status Update:** Implements an NLP-lite parser that understands natural language. Just type such phrases as "halfway", "almost done", or "just started" to automatically adjust the progress percentage and status.
* **Persistent Storage:** Saves and restores your projects using a custom ".cnd" (ChronoNexus Document) format.
* **Built-In Demo Environment:** Has a pre-loaded project to show the power of the scheduling engine right away.

## Technologies/Tools Used
* **Language:** Java (JDK 14 or newer preferred for enhanced switch expressions).
* **Core Libraries:** java.util, java.time, java.nio, java.io (No external dependencies).
* **Architecture:** Custom Graph traversal algorithms (Kahn's Algorithm for topological sorting, DFS for cycle detection).
* **Interface:** Command-Line Interface (CLI).

## Steps to Install & Run the Project
1. Clone or download the project files to your local machine.
2. Navigate to the directory where the files are located.
3. Verify the Java installation by running the following command:
   ```bash
   java -version
Compile the code by compiling the main class using the Java compiler:

Bash
javac Main.java
Run the application by running the following command:

Bash
java Main


## Instructions for Testing
In order to properly check the application functionality, follow the below steps:

**Test Demo Project:**

**At startup, press 1 to load the "AI Viva Examiner" demo project.**

* Press 1 in the menu to ensure that the tasks are loaded with proper deadlines and priorities.

**Test Cycle Detection (error handling):**

* Press 3 to link tasks. Try making Task 2 dependent on Task 4 and Task 4 dependent on Task 2. The application should prevent you from doing so by warning about an impossible dependency loop.

**Test Delay Simulation:**

* Press 4 to start a delay simulation. Pick 3 (Build prototype) and set delay for 5 days. Check that the application predicts possible delays for the downstream tasks (Testing, Documentation, etc.) without implementing any changes until you approve.

**Test NLP-lite parser:**

* Press 8 to update task progress. Choose one of the tasks' IDs and enter a natural phrase like "I am about three quarters done". Ensure that the progress bar is set to 75% and the status becomes IN_PROGRESS.

**Test Serialization:**

* Press 6 to save the project. Exit the application, run it again, choose New Project and press 7 to load your .cnd file. Check that all tasks, their dependencies and statuses are loaded successfully.


## Screenshots


<img width="1684" height="194" alt="Screenshot 2026-09-16 181654" src="https://github.com/user-attachments/assets/e4d59a66-e81a-47c2-9f51-b0e5a5c56b29" />




















<img width="1705" height="612" alt="Screenshot 2026-09-16 181825" src="https://github.com/user-attachments/assets/159d47fc-f765-4265-b82f-c215f887689e" />

























<img width="787" height="458" alt="Screenshot 2026-09-16 181939" src="https://github.com/user-attachments/assets/6fd52b55-401b-4d76-93f6-dc7d2f389a5f" />



















<img width="622" height="235" alt="Screenshot 2026-09-16 182053" src="https://github.com/user-attachments/assets/d03a194a-572d-4b87-b89a-a68713cf2c42" />


















<img width="839" height="331" alt="Screenshot 2026-09-16 182225" src="https://github.com/user-attachments/assets/2c6a1c04-d968-4d21-9d95-9f093717da69" />
























<img width="1684" height="194" alt="Screenshot 2026-09-16 181654" src="https://github.com/user-attachments/assets/f00058c7-339e-460e-856d-4a64d9bd1c21" />















<img width="1184" height="390" alt="Screenshot 2026-09-16 182313" src="https://github.com/user-attachments/assets/8cda1307-36bf-4e95-836b-4440d6b501ad" />





<img width="1186" height="184" alt="Screenshot 2026-09-16 182406" src="https://github.com/user-attachments/assets/1d1c6469-4e73-45a5-b635-7988061ddaba" />




                                                                                          **CHRONONEXUS**
 **Overview of the Project**

 
ChronoNexus is a highly advanced Project and Task Management scheduling tool. Using CLI interface, it goes beyond plain to-do lists through utilizing Directed Acyclic Graphs (DAGs) to deal with task dependencies, determine critical paths, and simulate effects of an unexpected delay throughout the entire project timeline.

 **Features**

 
***Intelligent Scheduling Engine:** Implements topological sorting algorithm to manage complex task dependencies and prevent impossible dependency loops (cycle detection).


***Critical Path Calculation:** Determines the longest chain of dependent tasks and shows exactly which ones delay the whole project if behind schedule.


***"What-If" Delay Simulations:** Calculates and simulates the effect of a delay on a certain task and displays the way it propagates through the rest of the tasks without actually implementing changes until approved.


***Smart Status Update:** Implements an NLP-lite parser that understands natural language. Just type such phrases as "halfway", "almost done", or "just started" to automatically adjust the progress percentage and status.


***Persistent Storage:** Saves and restores your projects using a custom ".cnd" (ChronoNexus Document) format.


***Built-In Demo Environment:** Has a pre-loaded project to show the power of the scheduling engine right away.


**Technologies/Tools Used**


***Language:** Java (JDK 14 or newer preferred for enhanced switch expressions).


***Core Libraries:** `java.util`, `java.time`, `java.nio`, `java.io` (No external dependencies).


***Architecture:** Custom Graph traversal algorithms (Kahn's Algorithm for topological sorting, DFS for cycle detection).


***Interface:** Command-Line Interface (CLI).


**Steps to Install & Run the Project**


1. Clone or download the project files to your local machine.


2. Navigate to the directory where the files are located.


3. Verify the Java installation by running the following command:

   Bash

   java -version


5. Compile the code by compiling the main class using the Java compiler:

   Bash

   javac Main.java


7. Run the application by running the following command:

   Bash

   java Main


**Instructions for Testing**


**In order to properly check the application functionality, follow the below steps:**


**1. Test Demo Project:**

o At startup, press 1 to load the "AI Viva Examiner" demo project.

o Press 1 in the menu to ensure that the tasks are loaded with proper deadlines and priorities.


**2. Test Cycle Detection (error handling):**

o Press 3 to link tasks. Try making Task 2 dependent on Task 4 and Task 4 dependent on Task 2. The application should prevent you from doing so by warning about an impossible dependency loop.


**3. Test Delay Simulation:**

o Press 4 to start a delay simulation. Pick 3 (Build prototype) and set delay for 5 days. Check that the application predicts possible delays for the downstream tasks (Testing, Documentation, etc.) without implementing any changes until you approve.


**4. Test NLP-lite parser:**

o Press 8 to update task progress. Choose one of the tasks' IDs and enter a natural phrase like "I am about three quarters done". Ensure that the progress bar is set to 75% and the status becomes IN_PROGRESS.


**5. Test Serialization:**

o Press 6 to save the project. Exit the application, run it again, choose New Project and press 7 to load your .cnd file. Check that all tasks, their dependencies and statuses are loaded successfully.


**Screenshots:**


<img width="1186" height="184" alt="Screenshot 2026-09-16 182406" src="https://github.com/user-attachments/assets/b01f7979-a911-4902-8f1d-4fe64386ab1f" />





<img width="1184" height="390" alt="Screenshot 2026-09-16 182313" src="https://github.com/user-attachments/assets/d6701789-35fd-4def-9f01-31b13bdae751" />





<img width="839" height="331" alt="Screenshot 2026-09-16 182225" src="https://github.com/user-attachments/assets/85e7635f-d2fd-4124-8600-aae233b921c2" />





<img width="622" height="235" alt="Screenshot 2026-09-16 182053" src="https://github.com/user-attachments/assets/da4f38da-d787-40d7-acaf-9734575cc58d" />





<img width="787" height="458" alt="Screenshot 2026-09-16 181939" src="https://github.com/user-attachments/assets/8d4f9f4f-c0ce-41b4-b6ea-c0ca00352dd0" />






<img width="1705" height="612" alt="Screenshot 2026-09-16 181825" src="https://github.com/user-attachments/assets/beb51e4e-e62a-4a35-aa68-f1593ae25d72" />





<img width="1684" height="194" alt="Screenshot 2026-09-16 181654" src="https://github.com/user-attachments/assets/f00058c7-339e-460e-856d-4a64d9bd1c21" />


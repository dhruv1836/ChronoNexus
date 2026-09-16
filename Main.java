import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

// ==========================================
// 1. MAIN CLI APPLICATION
// ==========================================
public class Main {     
    private static Project activeProject;     
    private static final Scanner scanner = new Scanner(System.in);     
    
    public static void main(String[] args) {         
        System.out.println("==================================================");         
        System.out.println("           Welcome to ChronoNexus!                ");         
        System.out.println("      Master the Ripple, Conquer the Deadline.    ");         
        System.out.println("==================================================");                  
        
        loadDemoOrNew();         
        
        boolean running = true;         
        while (running) {             
            printMenu();             
            String choice = scanner.nextLine().trim();             
            
            try {                 
                switch (choice) {                     
                    case "1" -> showOverview();                     
                    case "2" -> manageTasks();                     
                    case "3" -> manageDependencies();                     
                    case "4" -> simulateDelay();                     
                    case "5" -> showCriticalPath();                     
                    case "6" -> saveProject();                     
                    case "7" -> loadProject();   
                    case "8" -> updateTaskStatusSmartly(); // <-- SMART STATUS UPDATER                  
                    case "0" -> {                         
                        System.out.println("Thanks for using ChronoNexus. Have a great day!");                         
                        running = false;                     
                    }                     
                    default -> System.out.println("Hmm, I didn't recognize that option. Please try again.");                 
                }             
            } catch (Exception e) {                 
                System.out.println("\n[Oops!] " + e.getMessage() + "\n");             
            }         
        }     
    }     
    
    private static void loadDemoOrNew() {         
        System.out.print("Would you like to start with the (1) Demo Project or a (2) New Project? [1/2]: ");         
        String choice = scanner.nextLine().trim();         
        
        if ("2".equals(choice)) {             
            createNewProject();         
        } else {             
            activeProject = createDemoProject();             
            System.out.println("Awesome! Loaded the demo project: " + activeProject.getName());         
        }     
    }     
    
    private static void printMenu() {         
        System.out.println("\n--- MAIN MENU ---");         
        System.out.println("Current Project: " + activeProject.getName() + " | Target Deadline: " + activeProject.getFinalDeadline());         
        System.out.println("1. View Project Overview & Progress");         
        System.out.println("2. Manage Tasks (Add, List, or Remove)");         
        System.out.println("3. Link Tasks (Set Dependencies)");         
        System.out.println("4. Simulate a Delay (What-If Scenario)");         
        System.out.println("5. View Critical Path (Longest chain of tasks)");         
        System.out.println("6. Save Project");         
        System.out.println("7. Load Project");  
        System.out.println("8. Update Task Progress (Smart Text Input)");       
        System.out.println("0. Exit");         
        System.out.print("What would you like to do? ");     
    }     
    
    private static void showOverview() {         
        System.out.println("\n--- PROJECT OVERVIEW ---");         
        if (activeProject.getTasks().isEmpty()) {             
            System.out.println("It looks a bit empty here! There are no tasks in the project yet.");             
            return;         
        }         
        
        int totalTasks = activeProject.getTasks().size();
        long completedTasks = activeProject.getTasks().stream()
            .filter(task -> task.getStatus() == Task.Status.COMPLETED)
            .count();
        int completionPercentage = (int) ((completedTasks * 100.0) / totalTasks);
        
        System.out.println("Overall Plan: " + completionPercentage + "% Complete (" + completedTasks + " of " + totalTasks + " tasks finished)");
        System.out.println("------------------------------------------------------------------------------------------------");

        for (Task task : activeProject.getTasks()) {             
            System.out.println(task.shortInfo());         
        }     
        System.out.println("------------------------------------------------------------------------------------------------");
    }     

    // SMART PARSER: Automatically detects completion percentage from natural user text
    private static void updateTaskStatusSmartly() {
        showOverview();
        if (activeProject.getTasks().isEmpty()) return;

        try {
            System.out.print("\nEnter the ID of the task you want to update: ");
            int taskId = Integer.parseInt(scanner.nextLine().trim());
            Task task = activeProject.getTask(taskId);

            if (task == null) {
                System.out.println("We couldn't find a task with that ID.");
                return;
            }

            System.out.println("\nCurrent progress for '" + task.getName() + "' is: " + task.getCompletionPercentage() + "%");
            System.out.println("How is it going? (e.g., 'just started', 'halfway', 'almost done', 'done', or a number like '75%'): ");
            System.out.print("Your update: ");
            
            String input = scanner.nextLine().trim().toLowerCase();
            int detectedPct = parseSmartInput(input, task.getCompletionPercentage());

            task.setCompletionPercentage(detectedPct);
            System.out.println("✨ Detected " + detectedPct + "% completion! Task '" + task.getName() + "' status is now: " + task.getStatus());

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid numeric task ID.");
        } catch (Exception e) {
            System.out.println("Error updating task: " + e.getMessage());
        }
    }

    // Helper logic to interpret natural human phrases into percentages
    private static int parseSmartInput(String input, int currentPct) {
        // If they typed an explicit number (e.g., "80", "80%")
        input = input.replace("%", "").trim();
        try {
            int val = Integer.parseInt(input);
            if (val >= 0 && val <= 100) return val;
        } catch (NumberFormatException ignored) {}

        // Keyword analysis
        if (input.contains("done") || input.contains("finished") || input.contains("complete") || input.contains("100")) {
            return 100;
        }
        if (input.contains("almost") || input.contains("nearly") || input.contains("wrap") || input.contains("90")) {
            return 90;
        }
        if (input.contains("three quarter") || input.contains("75")) {
            return 75;
        }
        if (input.contains("half") || input.contains("middle") || input.contains("50")) {
            return 50;
        }
        if (input.contains("quarter") || input.contains("25")) {
            return 25;
        }
        if (input.contains("started") || input.contains("began") || input.contains("little") || input.contains("10")) {
            return 15;
        }
        if (input.contains("not") || input.contains("zero") || input.contains("haven't") || input.contains("0")) {
            return 0;
        }

        // Default fallback if it doesn't match known keywords
        System.out.println("Hmm, I couldn't map that text clearly. Keeping current progress.");
        return currentPct;
    }
    
    private static void manageTasks() {         
        System.out.println("\n--- MANAGE TASKS ---");         
        System.out.println("1. List all Tasks");         
        System.out.println("2. Add a new Task");         
        System.out.println("3. Remove a Task");         
        System.out.print("Choose an option: ");         
        
        String choice = scanner.nextLine().trim();         
        switch (choice) {             
            case "1" -> showOverview();             
            case "2" -> addTask();             
            case "3" -> removeTask();             
            default -> System.out.println("Invalid option.");         
        }     
    }     
    
    private static void addTask() {         
        try {             
            System.out.print("What is the name of the task? ");             
            String name = scanner.nextLine().trim();                          
            
            System.out.print("When does it start? (Format: YYYY-MM-DD): ");             
            LocalDate start = LocalDate.parse(scanner.nextLine().trim());                          
            
            System.out.print("How many days will it take? ");             
            int duration = Integer.parseInt(scanner.nextLine().trim());                          
            
            System.out.print("What is its priority? (LOW/MEDIUM/HIGH/CRITICAL): ");             
            Task.Priority priority = Task.Priority.valueOf(scanner.nextLine().trim().toUpperCase());             
            
            int nextId = activeProject.getTasks().stream().mapToInt(Task::getId).max().orElse(0) + 1;             
            activeProject.addTask(new Task(nextId, name, start, duration, priority));             
            
            System.out.println("Perfect! '" + name + "' was added successfully with ID: " + nextId);                      
        } catch (DateTimeParseException e) {             
            System.out.println("That date format didn't quite work. Please make sure it looks exactly like YYYY-MM-DD.");         
        } catch (NumberFormatException e) {             
            System.out.println("That duration didn't look like a number. Please enter a valid whole number of days.");         
        } catch (IllegalArgumentException e) {             
            System.out.println("Oops, there was an issue with your input: " + e.getMessage());         
        }     
    }     
    
    private static void removeTask() {         
        showOverview();         
        System.out.print("Enter the ID of the task you want to remove: ");         
        try {             
            int id = Integer.parseInt(scanner.nextLine().trim());             
            activeProject.removeTask(id);             
            System.out.println("Task removed successfully.");         
        } catch (NumberFormatException e) {             
            System.out.println("Please enter a valid numeric ID.");         
        } catch (Exception e) {             
            System.out.println("We couldn't remove that task: " + e.getMessage());         
        }     
    }     
    
    private static void manageDependencies() {         
        showOverview();         
        try {             
            System.out.print("Which task needs to be finished FIRST? (Enter the ID): ");             
            int preId = Integer.parseInt(scanner.nextLine().trim());                          
            
            System.out.print("Which task depends on it? (Enter the ID): ");             
            int depId = Integer.parseInt(scanner.nextLine().trim());             
            
            activeProject.addDependency(preId, depId);             
            System.out.println("Link established! The tasks are now dependent.");         
        } catch (NumberFormatException e) {             
            System.out.println("Please enter valid numeric Task IDs.");         
        } catch (Exception e) {             
            System.out.println("Couldn't link the tasks: " + e.getMessage());         
        }     
    }     
    
    private static void simulateDelay() {         
        showOverview();         
        try {             
            System.out.print("Which task is being delayed? (Enter the ID): ");             
            int taskId = Integer.parseInt(scanner.nextLine().trim());                          
            
            System.out.print("How many extra days will it take? ");             
            int days = Integer.parseInt(scanner.nextLine().trim());             
            
            Map<Integer, Integer> affected = Scheduler.simulateDelay(activeProject, taskId, days);                          
            
            System.out.println("\n--- DELAY PREVIEW ---");             
            System.out.println("Here is how that delay will affect the rest of your plan:");
            for (Map.Entry<Integer, Integer> entry : affected.entrySet()) {                 
                System.out.println("  -> Task '" + activeProject.getTask(entry.getKey()).getName() + "': +" + entry.getValue() + " days");             
            }                          
            
            int totalDelay = Scheduler.predictedProjectDelay(activeProject, affected);             
            System.out.println("\nPredicted total project delay: " + totalDelay + " days.");             
            System.out.println("Overall Delivery Risk: " + Scheduler.risk(totalDelay, activeProject.getFinalDeadline()));             
            
            System.out.print("\nWould you like to apply these delays to your actual project plan? (Y/N): ");             
            if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {                 
                for (Map.Entry<Integer, Integer> entry : affected.entrySet()) {                     
                    Task t = activeProject.getTask(entry.getKey());                     
                    t.setDelayDays(Math.max(t.getDelayDays(), entry.getValue()));                 
                }                 
                System.out.println("Delays have been permanently applied.");             
            } else {                 
                System.out.println("Simulation discarded. Your plan is safe!");             
            }         
        } catch (NumberFormatException e) {             
            System.out.println("Please enter valid numbers for the ID and days.");         
        } catch (Exception e) {             
            System.out.println("Error running the simulation: " + e.getMessage());         
        }     
    }     
    
    private static void showCriticalPath() {         
        System.out.println("\n--- CRITICAL PATH ---");         
        System.out.println("These tasks form the longest chain. If any of these are delayed, the whole project is delayed.\n");
        try {             
            int number = 1;             
            for (int taskId : Scheduler.criticalPath(activeProject)) {                 
                Task task = activeProject.getTask(taskId);                 
                System.out.println(number++ + ". " + task.getName() + " (" + task.getDurationDays() + " days)");             
            }             
            
            int length = Scheduler.earliestFinish(activeProject).values().stream().max(Integer::compareTo).orElse(0);             
            System.out.println("\nTotal critical path length is " + length + " days.");         
        } catch (IllegalStateException e) {             
            System.out.println("Cannot calculate critical path: " + e.getMessage());         
        }     
    }     
    
    private static void saveProject() {         
        System.out.print("What should we name the save file? (Press Enter for 'project.cnd'): ");         
        String filename = scanner.nextLine().trim();         
        if (filename.isEmpty()) filename = "chrononexus-project.cnd";                  
        
        try {             
            Path path = Paths.get(filename);             
            FileManager.save(activeProject, path);             
            System.out.println("Success! Project saved to " + path.toAbsolutePath());         
        } catch (IOException e) {             
            System.out.println("We ran into a problem saving the file: " + e.getMessage());         
        }     
    }     
    
    private static void loadProject() {         
        System.out.print("Enter the name of the file you want to load: ");         
        String filename = scanner.nextLine().trim();                  
        
        try {             
            activeProject = FileManager.load(Paths.get(filename));             
            System.out.println("Project loaded successfully! We are now working on: " + activeProject.getName());         
        } catch (IOException e) {             
            System.out.println("Failed to load the project: " + e.getMessage());         
        }     
    }     
    
    private static void createNewProject() {         
        System.out.print("What is the name of your new project? ");         
        String name = scanner.nextLine().trim();         
        System.out.print("When is the final, absolute deadline? (YYYY-MM-DD): ");         
        
        try {             
            LocalDate deadline = LocalDate.parse(scanner.nextLine().trim());             
            activeProject = new Project(name, deadline);         
        } catch (DateTimeParseException e) {             
            System.out.println("Invalid date format. We'll set the deadline to 30 days from today by default.");             
            activeProject = new Project(name, LocalDate.now().plusDays(30));         
        }     
    }     
    
    private static Project createDemoProject() {         
        Project demo = new Project("AI Viva Examiner", LocalDate.now().plusDays(25));         
        
        demo.addTask(new Task(1, "Choose topic", LocalDate.now(), 1, Task.Priority.HIGH));         
        demo.addTask(new Task(2, "Research", LocalDate.now().plusDays(1), 3, Task.Priority.HIGH));         
        demo.addTask(new Task(3, "Build prototype", LocalDate.now().plusDays(4), 5, Task.Priority.CRITICAL));         
        demo.addTask(new Task(4, "Testing", LocalDate.now().plusDays(9), 3, Task.Priority.HIGH));         
        demo.addTask(new Task(5, "Documentation", LocalDate.now().plusDays(12), 3, Task.Priority.MEDIUM));         
        demo.addTask(new Task(6, "Presentation", LocalDate.now().plusDays(15), 2, Task.Priority.HIGH));         
        demo.addTask(new Task(7, "Viva practice", LocalDate.now().plusDays(17), 2, Task.Priority.CRITICAL));         
        
        try {             
            demo.addDependency(1, 2);             
            demo.addDependency(2, 3);             
            demo.addDependency(3, 4);             
            demo.addDependency(4, 5);             
            demo.addDependency(5, 6);             
            demo.addDependency(6, 7);         
        } catch (IllegalArgumentException ignored) {}         
        return demo;     
    } 
}

// ==========================================
// 2. TASK CLASS
// ==========================================
class Task {     
    public enum Status { NOT_STARTED, IN_PROGRESS, COMPLETED, DELAYED }     
    public enum Priority { LOW, MEDIUM, HIGH, CRITICAL }     
    
    private final int id;     
    private String name;     
    private LocalDate startDate;     
    private LocalDate deadline;     
    private int durationDays;     
    private Priority priority;     
    private Status status;     
    private int delayDays;  
    private int completionPercentage;  
    
    public Task(int id, String name, LocalDate startDate, int durationDays, Priority priority) {         
        if (durationDays < 1) throw new IllegalArgumentException("Duration must be at least 1 day.");         
        
        this.id = id;         
        setName(name);         
        this.startDate = Objects.requireNonNull(startDate);         
        this.durationDays = durationDays;         
        this.deadline = startDate.plusDays(durationDays - 1L);         
        this.priority = Objects.requireNonNull(priority);         
        this.status = Status.NOT_STARTED;         
        this.delayDays = 0;     
        this.completionPercentage = 0;
    }     
    
    public int getId() { return id; }     
    public String getName() { return name; }     
    public LocalDate getStartDate() { return startDate; }     
    public LocalDate getDeadline() { return deadline; }     
    public int getDurationDays() { return durationDays; }     
    public Priority getPriority() { return priority; }     
    public Status getStatus() { return status; }     
    public int getDelayDays() { return delayDays; }     
    public int getCompletionPercentage() { return completionPercentage; }
    
    public void setName(String name) {         
        if (name == null || name.trim().isEmpty()) {             
            throw new IllegalArgumentException("Task name cannot be empty.");         
        }         
        this.name = name.trim();     
    }     
    
    public void setStatus(Status status) { 
        this.status = status; 
        if (status == Status.COMPLETED) this.completionPercentage = 100;
        else if (status == Status.NOT_STARTED) this.completionPercentage = 0;
    }     
    
    public void setCompletionPercentage(int pct) {
        if (pct < 0) pct = 0;
        if (pct > 100) pct = 100;
        this.completionPercentage = pct;
        
        // Auto-update status based on the percentage
        if (this.completionPercentage == 100) {
            this.status = Status.COMPLETED;
        } else if (this.completionPercentage > 0) {
            this.status = Status.IN_PROGRESS;
        } else {
            this.status = Status.NOT_STARTED;
        }
    }

    public void setPriority(Priority priority) { this.priority = priority; }          
    
    public void setStartDate(LocalDate startDate) {         
        this.startDate = Objects.requireNonNull(startDate);         
        this.deadline = startDate.plusDays(durationDays - 1L);     
    }     
    
    public void setDurationDays(int durationDays) {         
        if (durationDays < 1) throw new IllegalArgumentException("Duration must be at least 1 day.");         
        this.durationDays = durationDays;         
        this.deadline = startDate.plusDays(durationDays - 1L);     
    }     
    
    public void setDelayDays(int delayDays) {         
        if (delayDays < 0) throw new IllegalArgumentException("Delay cannot be negative.");         
        this.delayDays = delayDays;         
        
        if (delayDays > 0 && status != Status.COMPLETED) {
            status = Status.DELAYED;     
        }
    }     
    
    public LocalDate getDelayedStart() { return startDate.plusDays(delayDays); }     
    public LocalDate getDelayedDeadline() { return deadline.plusDays(delayDays); }     
    
    public String getProgressBar() {
        int filled = completionPercentage / 10;
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < 10; i++) {
            if (i < filled) sb.append("■");
            else sb.append("-");
        }
        sb.append("] ").append(String.format("%3d%%", completionPercentage));
        return sb.toString();
    }

    public String shortInfo() {         
        return String.format("ID: %-2d | %s | %-15s | %-8s | %-11s | due %s (+%d delay)", 
            id, getProgressBar(), name, priority, status, getDelayedDeadline(), delayDays);
    }     
    
    @Override 
    public String toString() { 
        return id + " - " + name; 
    }
}

// ==========================================
// 3. PROJECT CLASS
// ==========================================
class Project {     
    private String name;     
    private LocalDate finalDeadline;     
    
    private final Map<Integer, Task> tasks = new LinkedHashMap<>();     
    private final Map<Integer, Set<Integer>> dependencies = new LinkedHashMap<>();     
    
    public Project(String name, LocalDate finalDeadline) {         
        this.name = Objects.requireNonNull(name);         
        this.finalDeadline = Objects.requireNonNull(finalDeadline);     
    }     
    
    public String getName() { return name; }     
    public LocalDate getFinalDeadline() { return finalDeadline; }     
    public Collection<Task> getTasks() { return Collections.unmodifiableCollection(tasks.values()); }     
    
    public Map<Integer, Set<Integer>> getDependencies() {         
        Map<Integer, Set<Integer>> copy = new LinkedHashMap<>();         
        for (Map.Entry<Integer, Set<Integer>> entry : dependencies.entrySet()) {             
            copy.put(entry.getKey(), Collections.unmodifiableSet(new LinkedHashSet<>(entry.getValue())));         
        }         
        return Collections.unmodifiableMap(copy);     
    }     
    
    public void addTask(Task task) {         
        Objects.requireNonNull(task, "Task cannot be null.");         
        if (tasks.containsKey(task.getId())) throw new IllegalArgumentException("Duplicate task ID.");         
        
        tasks.put(task.getId(), task);         
        dependencies.putIfAbsent(task.getId(), new LinkedHashSet<>());     
    }     
    
    public void removeTask(int taskId) {         
        if (!tasks.containsKey(taskId)) throw new IllegalArgumentException("Task not found.");         
        
        tasks.remove(taskId);         
        dependencies.remove(taskId);         
        
        for (Set<Integer> dependents : dependencies.values()) {
            dependents.remove(taskId);     
        }
    }     
    
    public Task getTask(int id) { return tasks.get(id); }     
    
    public void addDependency(int prerequisiteId, int dependentId) {         
        if (prerequisiteId == dependentId) throw new IllegalArgumentException("A task cannot depend on itself.");         
        if (!tasks.containsKey(prerequisiteId) || !tasks.containsKey(dependentId))             
            throw new IllegalArgumentException("Both tasks must exist.");                  
        
        dependencies.computeIfAbsent(prerequisiteId, k -> new LinkedHashSet<>()).add(dependentId);                  
        
        if (Scheduler.hasCycle(this)) {             
            dependencies.get(prerequisiteId).remove(dependentId);             
            throw new IllegalArgumentException("Dependency rejected: it creates an impossible circle of dependencies.");         
        }     
    }     
    
    public void removeDependency(int prerequisiteId, int dependentId) {         
        Set<Integer> set = dependencies.get(prerequisiteId);         
        if (set != null) set.remove(dependentId);     
    }     
    
    public List<Task> dependentsOf(int id) {         
        List<Task> result = new ArrayList<>();         
        for (Integer next : dependencies.getOrDefault(id, Set.of()))             
            result.add(tasks.get(next));         
        return result;     
    }     
    
    public List<Task> prerequisitesOf(int id) {         
        List<Task> result = new ArrayList<>();         
        for (Map.Entry<Integer, Set<Integer>> e : dependencies.entrySet())             
            if (e.getValue().contains(id)) result.add(tasks.get(e.getKey()));         
        return result;     
    }     
    
    public void clear() { 
        tasks.clear(); 
        dependencies.clear(); 
    } 
}

// ==========================================
// 4. SCHEDULER ALGORITHMS
// ==========================================
final class Scheduler {     
    private Scheduler() {}     
    
    public static boolean hasCycle(Project p) {         
        Set<Integer> visited = new HashSet<>(), active = new HashSet<>();         
        for (Task t : p.getTasks())             
            if (dfsCycle(p, t.getId(), visited, active)) return true;         
        return false;     
    }     
    
    private static boolean dfsCycle(Project p, int id, Set<Integer> visited, Set<Integer> active) {         
        if (active.contains(id)) return true;         
        if (visited.contains(id)) return false;         
        
        visited.add(id); 
        active.add(id);         
        
        for (Task next : p.dependentsOf(id))             
            if (dfsCycle(p, next.getId(), visited, active)) return true;         
            
        active.remove(id);         
        return false;     
    }     
    
    public static List<Task> topologicalOrder(Project p) {         
        Map<Integer,Integer> indegree = new HashMap<>();         
        for (Task t : p.getTasks()) indegree.put(t.getId(), 0);         
        
        for (Set<Integer> outs : p.getDependencies().values())             
            for (int v : outs) indegree.put(v, indegree.get(v) + 1);         
        
        Queue<Integer> q = new ArrayDeque<>();         
        for (var e : indegree.entrySet()) {
            if (e.getValue() == 0) q.add(e.getKey());                  
        }
        
        List<Task> result = new ArrayList<>();         
        
        while (!q.isEmpty()) {             
            int u = q.remove();             
            result.add(p.getTask(u));             
            
            for (int v : p.getDependencies().getOrDefault(u, Set.of())) {                 
                indegree.put(v, indegree.get(v) - 1);                 
                if (indegree.get(v) == 0) q.add(v);             
            }         
        }         
        
        if (result.size() != p.getTasks().size()) {
            throw new IllegalStateException("The plan is broken because tasks loop back on themselves.");         
        }
        return result;     
    }     
    
    public static Map<Integer,Integer> earliestFinish(Project p) {         
        Map<Integer,Integer> finish = new HashMap<>();         
        for (Task t : topologicalOrder(p)) {             
            int earliestStart = 0;             
            for (Task pre : p.prerequisitesOf(t.getId()))                 
                earliestStart = Math.max(earliestStart, finish.getOrDefault(pre.getId(), 0));             
            finish.put(t.getId(), earliestStart + t.getDurationDays());         
        }         
        return finish;     
    }     
    
    public static List<Integer> criticalPath(Project p) {         
        List<Task> order = topologicalOrder(p);         
        Map<Integer,Integer> dist = new HashMap<>();         
        Map<Integer,Integer> parent = new HashMap<>();         
        int bestId = -1, best = -1;                  
        
        for (Task t : order) {             
            int d = t.getDurationDays(), par = -1;             
            for (Task pre : p.prerequisitesOf(t.getId())) {                 
                int candidate = dist.getOrDefault(pre.getId(), pre.getDurationDays()) + t.getDurationDays();                 
                if (candidate > d) { d = candidate; par = pre.getId(); }             
            }             
            dist.put(t.getId(), d);             
            parent.put(t.getId(), par);             
            if (d > best) { best = d; bestId = t.getId(); }         
        }                  
        
        LinkedList<Integer> path = new LinkedList<>();         
        while (bestId != -1) {             
            path.addFirst(bestId);             
            bestId = parent.getOrDefault(bestId, -1);         
        }         
        return path;     
    }     
    
    public static Map<Integer,Integer> simulateDelay(Project p, int taskId, int extraDays) {         
        if (extraDays < 0) throw new IllegalArgumentException("Delay must be a positive number.");         
        if (p.getTask(taskId) == null) throw new IllegalArgumentException("We couldn't find that task ID.");                  
        
        Map<Integer,Integer> propagated = new LinkedHashMap<>();         
        propagated.put(taskId, extraDays);         
        Queue<Integer> q = new ArrayDeque<>();         
        q.add(taskId);                  
        
        while (!q.isEmpty()) {             
            int u = q.remove();             
            int currentDelay = propagated.get(u);             
            
            for (Task child : p.dependentsOf(u)) {                 
                int existing = propagated.getOrDefault(child.getId(), 0);                 
                if (currentDelay > existing) {                     
                    propagated.put(child.getId(), currentDelay);                     
                    q.add(child.getId());                 
                }             
            }         
        }         
        return propagated;     
    }     
    
    public static int predictedProjectDelay(Project p, Map<Integer,Integer> impact) {         
        int max = 0;         
        for (Task t : p.getTasks()) max = Math.max(max, impact.getOrDefault(t.getId(), 0));         
        return max;     
    }     
    
    public static String risk(int predictedDelay, LocalDate finalDeadline) {         
        if (predictedDelay <= 0) return "LOW";         
        if (LocalDate.now().plusDays(predictedDelay).isAfter(finalDeadline)) return "CRITICAL";         
        if (predictedDelay >= 3) return "HIGH";         
        if (predictedDelay >= 1) return "MEDIUM";         
        return "LOW";     
    } 
}

// ==========================================
// 5. FILE MANAGER
// ==========================================
final class FileManager {     
    private FileManager() {}     
    
    public static void save(Project p, Path path) throws IOException {         
        try (BufferedWriter w = Files.newBufferedWriter(path)) {             
            w.write("PROJECT|" + clean(p.getName()) + "|" + p.getFinalDeadline());             
            w.newLine();             
            
            for (Task t : p.getTasks()) {                 
                w.write(String.join("|", "TASK", ""+t.getId(), clean(t.getName()),                         
                        t.getStartDate().toString(), ""+t.getDurationDays(),                         
                        t.getPriority().name(), t.getStatus().name(), ""+t.getDelayDays(), 
                        ""+t.getCompletionPercentage())); 
                w.newLine();             
            }             
            
            for (var e : p.getDependencies().entrySet()) {                 
                for (int v : e.getValue()) {                     
                    w.write("DEP|" + e.getKey() + "|" + v);                     
                    w.newLine();                 
                }
            }         
        }     
    }     
    
    public static Project load(Path path) throws IOException {         
        List<String> lines = Files.readAllLines(path);         
        if (lines.isEmpty() || !lines.get(0).startsWith("PROJECT|"))             
            throw new IOException("This doesn't look like a valid ChronoNexus file.");                  
            
        String[] h = lines.get(0).split("\\|", -1);         
        Project p = new Project(h[1], LocalDate.parse(h[2]));         
        List<String[]> deps = new ArrayList<>();                  
        
        for (String line : lines.subList(1, lines.size())) {             
            String[] a = line.split("\\|", -1);             
            if ("TASK".equals(a[0])) {                 
                Task t = new Task(Integer.parseInt(a[1]), a[2], LocalDate.parse(a[3]),                         
                        Integer.parseInt(a[4]), Task.Priority.valueOf(a[5]));                 
                t.setStatus(Task.Status.valueOf(a[6]));                 
                t.setDelayDays(Integer.parseInt(a[7]));    

                if (a.length > 8) {
                    t.setCompletionPercentage(Integer.parseInt(a[8]));
                }             
                p.addTask(t);             
            } else if ("DEP".equals(a[0])) {
                deps.add(a);         
            }
        }                  
        
        for (String[] d : deps) {
            p.addDependency(Integer.parseInt(d[1]), Integer.parseInt(d[2]));         
        }
        return p;     
    }     
    
    private static String clean(String s) { 
        return s.replace("|", "/").replace("\n", " "); 
    } 
}
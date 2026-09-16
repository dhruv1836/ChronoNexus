
# Project Statement: ChronoNexus

## Problem Statement
Managing complex projects with traditional to-do lists often leads to missed deadlines and disorganized workflows. Standard task managers fail to account for complex task dependencies, making it difficult to foresee how a delay in one area will ripple through the rest of the project. There is a need for a lightweight, intelligent scheduling tool that can actively map dependencies, calculate critical paths, and simulate delays without requiring the heavy overhead of enterprise software.

## Scope of the Project
The scope of ChronoNexus involves the development of a standalone, Command-Line Interface (CLI) application built entirely in core Java. 

**In-Scope:**
* Creation and management of project tasks with specific durations, priorities, and deadlines.
* Implementation of Directed Acyclic Graphs (DAGs) to map out task prerequisites and dependents.
* Algorithmic calculation of the project's critical path and detection of impossible dependency loops (cycle detection).
* A simulation engine to preview the cascading effects of task delays.
* An NLP-lite parser to translate natural language inputs (e.g., "halfway done") into actionable progress updates.
* Local serialization to save and load projects via a custom `.cnd` text file format.

**Out-of-Scope:**
* Graphical User Interface (GUI) or web-based frontend.
* Cloud synchronization, multi-user real-time collaboration, or database integration.

## Target Users
* **Software Developers & Engineers:** Professionals who prefer terminal-based, keyboard-centric tools for managing their personal development cycles.
* **Project Managers & Team Leads:** Individuals looking for a fast, mathematical approach to calculating critical paths and testing "what-if" delay scenarios.
* **Technical Students & Academics:** Students managing complex, multi-stage assignments (such as the built-in "AI Viva Examiner" demo project) who need to track prerequisites effectively.

## High-Level Features
* **Intelligent Scheduling Engine:** Utilizes topological sorting to structure schedules and prevent circular dependency errors.
* **Critical Path Calculation:** Automatically identifies the specific chain of tasks that dictate the final project deadline.
* **"What-If" Delay Simulations:** Allows users to test the impact of potential delays on downstream tasks before permanently altering the project timeline.
* **Smart Status Updates:** Interprets natural conversational text into quantitative completion percentages and task statuses.
* **Persistent Storage:** Seamlessly saves and restores project states using a proprietary, easily readable `.cnd` document format.

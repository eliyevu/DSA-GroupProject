package com.ug.dsa;

import com.ug.dsa.datastructures.DynamicArray;
import com.ug.dsa.models.AlgorithmRun;
import com.ug.dsa.models.AuditEvent;
import com.ug.dsa.models.Location;
import com.ug.dsa.models.ServiceRequest;
import com.ug.dsa.services.SmartOperationsEngine;

import java.util.Scanner;

/**
 * Main – Console interface for the Smart Service Operations Optimizer.
 *
 * Usage:
 *   java -cp bin com.ug.dsa.Main
 */
public class Main {

    // ── ANSI colour codes (safe fallback on plain terminals) ─────────────────
    private static final String RESET  = "\u001B[0m";
    private static final String BOLD   = "\u001B[1m";
    private static final String CYAN   = "\u001B[36m";
    private static final String GREEN  = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED    = "\u001B[31m";
    private static final String BLUE   = "\u001B[34m";

    private static final SmartOperationsEngine engine = new SmartOperationsEngine();
    private static final Scanner scanner = new Scanner(System.in);

    private static boolean dataLoaded = false;

    // =========================================================================
    //  Entry point
    // =========================================================================

    public static void main(String[] args) {
        clearScreen();
        printBanner();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ", 0, 10);
            System.out.println();
            switch (choice) {
                case 1:  menuLoadData();              break;
                case 2:  menuViewRequests();          break;
                case 3:  menuSearchRequests();        break;
                case 4:  menuScheduleRequests();      break;
                case 5:  menuShortestRoute();         break;
                case 6:  menuReachableLocations();    break;
                case 7:  menuOptimizeAllocation();    break;
                case 8:  menuRunPerformanceTest();    break;
                case 9:  menuViewAlgorithmRuns();     break;
                case 10: menuViewAuditEvents();       break;
                case 0:
                    System.out.println(GREEN + "  Goodbye!" + RESET);
                    running = false;
                    break;
                default:
                    System.out.println(RED + "  Invalid choice." + RESET);
            }
            if (running && choice != 0) {
                System.out.println();
                pause();
            }
        }
        scanner.close();
    }

    // =========================================================================
    //  Menu handlers
    // =========================================================================

    /** Option 1 – Load / Reload Database Data */
    private static void menuLoadData() {
        sectionHeader("1. Load / Reload Database Data");
        System.out.println("  Loading data (PostgreSQL with CSV fallback)...\n");
        String summary = engine.loadOrReloadData();
        dataLoaded = true;
        System.out.println(GREEN + "  ✓ " + summary + RESET);
    }

    /** Option 2 – View Service Requests */
    private static void menuViewRequests() {
        sectionHeader("2. View Service Requests");
        if (!requireData()) return;

        DynamicArray<ServiceRequest> requests = engine.getServiceRequests();
        if (requests.size() == 0) {
            System.out.println(YELLOW + "  No service requests found." + RESET);
            return;
        }

        System.out.printf("  Total: %d service requests%n%n", requests.size());
        int pageSize = readInt("  How many to display (1-300)? ", 1, 300);
        int shown = Math.min(pageSize, requests.size());

        printRequestTableHeader();
        for (int i = 0; i < shown; i++) {
            printRequestRow(requests.get(i));
        }
        printDivider(120);
        if (shown < requests.size()) {
            System.out.printf("  Showing %d of %d. Load more by entering a larger number.%n",
                shown, requests.size());
        }
    }

    /** Option 3 – Search Service Requests */
    private static void menuSearchRequests() {
        sectionHeader("3. Search Service Requests");
        if (!requireData()) return;

        System.out.println("  Search by:");
        System.out.println("    [1] Request ID");
        System.out.println("    [2] Category (MEDICAL, CLEANING, TRANSPORT, ...)");
        System.out.println("    [3] Status   (PENDING, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED)");
        System.out.println("    [4] Source Location ID");
        System.out.println("    [5] Destination Location ID");
        int opt = readInt("  Choose (1-5): ", 1, 5);

        String field, value;
        switch (opt) {
            case 1: field = "id";          value = readString("  Enter Request ID: ");        break;
            case 2: field = "category";    value = readString("  Enter Category: ");           break;
            case 3: field = "status";      value = readString("  Enter Status: ");             break;
            case 4: field = "source";      value = readString("  Enter Source Location ID: "); break;
            default: field = "destination"; value = readString("  Enter Destination ID: ");    break;
        }

        DynamicArray<ServiceRequest> results = engine.searchServiceRequests(field, value);
        System.out.printf("%n  Found %d result(s):%n%n", results.size());

        if (results.size() == 0) {
            System.out.println(YELLOW + "  No matches found." + RESET);
            return;
        }
        printRequestTableHeader();
        for (int i = 0; i < results.size(); i++) {
            printRequestRow(results.get(i));
        }
        printDivider(120);
    }

    /** Option 4 – Schedule Requests */
    private static void menuScheduleRequests() {
        sectionHeader("4. Schedule Requests");
        if (!requireData()) return;

        System.out.println("  Queue modes:");
        System.out.println("    [1] FIFO Queue          – First-Come, First-Served");
        System.out.println("    [2] Priority Min-Heap   – Sorted by urgency (1=highest)");
        System.out.println("    [3] Deque / Urgent      – Urgent requests jump to the front");
        System.out.println("    [4] Circular Queue      – Fixed-capacity buffer (cap=100)");
        int opt = readInt("  Choose mode (1-4): ", 1, 4);

        String[] modes  = {"fifo", "priority", "urgent", "circular"};
        String[] labels = {"FIFO", "Priority Heap", "Deque/Urgent", "Circular Queue"};
        String mode = modes[opt - 1];

        int limit = readInt("  Max requests to schedule (0 = all pending): ", 0, 300);
        int scheduled = engine.scheduleRequests(mode, limit);
        System.out.println();
        System.out.printf(GREEN + "  ✓ Scheduled %d request(s) into %s.%n" + RESET, scheduled, labels[opt - 1]);

        // Show state
        System.out.printf("  Queue sizes after scheduling:%n");
        System.out.printf("    FIFO Queue    : %d%n", engine.getScheduler().getFifoQueueSize());
        System.out.printf("    Priority Heap : %d%n", engine.getScheduler().getPriorityHeapSize());
        System.out.printf("    Deque (urgent): %d%n", engine.getScheduler().getDequeSize());
        System.out.printf("    Circular Queue: %d%n", engine.getScheduler().getCircularQueueSize());
        System.out.println();

        // Dispatch a few to show in action
        int dispatch = readInt("  Dispatch how many requests now? (0 to skip): ", 0, 50);
        if (dispatch > 0) {
            System.out.println();
            printRequestTableHeader();
            for (int i = 0; i < dispatch; i++) {
                ServiceRequest r = engine.dispatchNext(mode);
                if (r == null) { System.out.println("  Queue empty."); break; }
                printRequestRow(r);
            }
            printDivider(120);
        }
    }

    /** Option 5 – Find Shortest Route */
    private static void menuShortestRoute() {
        sectionHeader("5. Find Shortest Route (Dijkstra's Algorithm)");
        if (!requireData()) return;

        printLocationHint();
        int src  = readInt("  Enter Source Location ID      : ", 1, 9999);
        int dest = readInt("  Enter Destination Location ID : ", 1, 9999);

        System.out.println();
        try {
            String result = engine.findShortestRoute(src, dest);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(RED + "  Error: " + e.getMessage() + RESET);
        }
    }

    /** Option 6 – Find Reachable Locations */
    private static void menuReachableLocations() {
        sectionHeader("6. Find Reachable Locations (BFS)");
        if (!requireData()) return;

        printLocationHint();
        int start = readInt("  Enter Start Location ID: ", 1, 9999);

        System.out.println();
        try {
            DynamicArray<Location> reachable = engine.findReachableLocations(start);
            System.out.printf("  %d location(s) reachable from Location %d:%n%n", reachable.size(), start);

            System.out.printf("  %-6s  %-40s  %-16s  %s%n", "ID", "Name", "Area", "Type");
            printDivider(80);
            for (int i = 0; i < reachable.size(); i++) {
                Location loc = reachable.get(i);
                if (loc != null) {
                    System.out.printf("  %-6d  %-40s  %-16s  %s%n",
                        loc.getLocationId(), loc.getName(), loc.getArea(), loc.getType());
                }
            }
            printDivider(80);
        } catch (Exception e) {
            System.out.println(RED + "  Error: " + e.getMessage() + RESET);
        }
    }

    /** Option 7 – Optimize Resource Allocation */
    private static void menuOptimizeAllocation() {
        sectionHeader("7. Optimize Resource Allocation (Greedy vs Dynamic Programming)");
        if (!requireData()) return;

        int capacity = readInt("  Enter capacity constraint (e.g. 100): ", 1, 10000);
        System.out.println();

        try {
            String result = engine.optimizeResourceAllocation(capacity);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(RED + "  Error: " + e.getMessage() + RESET);
        }
    }

    /** Option 8 – Run Algorithm Performance Test */
    private static void menuRunPerformanceTest() {
        sectionHeader("8. Run Algorithm Performance Test");
        if (!requireData()) return;

        System.out.println();
        try {
            String result = engine.runPerformanceTests();
            System.out.println(result);
            System.out.println(GREEN + "  ✓ Results recorded under 'View Algorithm Runs' (option 9)." + RESET);
        } catch (Exception e) {
            System.out.println(RED + "  Error: " + e.getMessage() + RESET);
        }
    }

    /** Option 9 – View Algorithm Runs */
    private static void menuViewAlgorithmRuns() {
        sectionHeader("9. View Algorithm Runs");

        DynamicArray<AlgorithmRun> runs = engine.getAlgorithmRuns();
        if (runs == null || runs.size() == 0) {
            System.out.println(YELLOW + "  No algorithm runs recorded yet." + RESET);
            System.out.println("  Tip: Load data (option 1) and run tests (option 8) first.");
            return;
        }

        System.out.printf("  Total records: %d%n%n", runs.size());
        System.out.printf("  %-6s  %-42s  %-8s  %-14s  %s%n",
            "ID", "Algorithm", "Input N", "Time (ns)", "Memory (KB)");
        printDivider(90);

        // Show last 30 (most recent)
        int from = Math.max(0, runs.size() - 30);
        for (int i = from; i < runs.size(); i++) {
            AlgorithmRun r = runs.get(i);
            System.out.printf("  %-6d  %-42s  %-8d  %-14d  %.2f%n",
                r.getRunId(), r.getAlgorithmName(), r.getInputSize(),
                r.getTimeNs(), r.getMemoryKb());
        }
        printDivider(90);
        if (from > 0) {
            System.out.printf("  Showing last 30 of %d records.%n", runs.size());
        }
    }

    /** Option 10 – View Audit Events */
    private static void menuViewAuditEvents() {
        sectionHeader("10. View Audit Events");

        DynamicArray<AuditEvent> events = engine.getAuditEvents();
        if (events == null || events.size() == 0) {
            System.out.println(YELLOW + "  No audit events recorded yet." + RESET);
            System.out.println("  Tip: Load data (option 1), schedule or optimize to generate events.");
            return;
        }

        System.out.printf("  Total events: %d%n%n", events.size());
        System.out.printf("  %-6s  %-22s  %-8s  %-22s  %s%n",
            "ID", "Event Type", "Req ID", "Timestamp", "Description");
        printDivider(100);

        int from = Math.max(0, events.size() - 30);
        for (int i = from; i < events.size(); i++) {
            AuditEvent e = events.get(i);
            System.out.printf("  %-6d  %-22s  %-8d  %-22s  %s%n",
                e.getEventId(), e.getEventType(),
                e.getRequestId(), e.getTimestamp(), e.getDescription());
        }
        printDivider(100);
        if (from > 0) {
            System.out.printf("  Showing last 30 of %d events.%n", events.size());
        }
    }

    // =========================================================================
    //  UI helpers
    // =========================================================================

    private static void printBanner() {
        System.out.println(CYAN + BOLD);
        System.out.println("  ==========================================");
        System.out.println("   SMART SERVICE OPERATIONS OPTIMIZER");
        System.out.println("   University of Ghana – DSA Group Project");
        System.out.println("  ==========================================");
        System.out.println(RESET);
    }

    private static void printMenu() {
        System.out.println(BOLD + BLUE + "  ─────────────────────────────────────────" + RESET);
        System.out.println(BOLD + "  MAIN MENU" + RESET);
        System.out.println(BLUE + "  ─────────────────────────────────────────" + RESET);
        System.out.println("  1.  Load / Reload Database Data");
        System.out.println("  2.  View Service Requests");
        System.out.println("  3.  Search Service Requests");
        System.out.println("  4.  Schedule Requests");
        System.out.println("  5.  Find Shortest Route");
        System.out.println("  6.  Find Reachable Locations");
        System.out.println("  7.  Optimize Resource Allocation");
        System.out.println("  8.  Run Algorithm Performance Test");
        System.out.println("  9.  View Algorithm Runs");
        System.out.println("  10. View Audit Events");
        System.out.println(RED + "  0.  Exit" + RESET);
        System.out.println(BLUE + "  ─────────────────────────────────────────" + RESET);
    }

    private static void sectionHeader(String title) {
        System.out.println(CYAN + BOLD + "\n  ── " + title + " ──" + RESET);
    }

    private static void printRequestTableHeader() {
        System.out.printf("  %-6s  %-15s  %-8s  %-6s  %-6s  %-22s  %-22s  %s%n",
            "ID", "Category", "Urgency", "Src", "Dest", "Submitted", "Deadline", "Status");
        printDivider(120);
    }

    private static void printRequestRow(ServiceRequest r) {
        System.out.printf("  %-6d  %-15s  %-8d  %-6d  %-6d  %-22s  %-22s  %s%n",
            r.getRequestId(), r.getCategory(), r.getUrgency(),
            r.getSource(), r.getDestination(),
            r.getTimeSubmitted(), r.getDeadline(), r.getStatus());
    }

    private static void printDivider(int len) {
        System.out.println("  " + "─".repeat(len));
    }

    private static void printLocationHint() {
        System.out.println(YELLOW
            + "  Tip: Location IDs 1-52 (e.g. 1=UG Main Gate, 5=Balme Library, 29=Dept. Computer Science)"
            + RESET);
    }

    private static boolean requireData() {
        if (!dataLoaded) {
            System.out.println(RED + "  ✗ Data not loaded. Please choose option 1 first." + RESET);
            return false;
        }
        return true;
    }

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v >= min && v <= max) return v;
                System.out.printf("  Please enter a number between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input – please enter a number.");
            }
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static void pause() {
        System.out.print("  Press Enter to return to menu...");
        scanner.nextLine();
        clearScreen();
        printBanner();
    }

    private static void clearScreen() {
        // Works on most Unix terminals; no-op on plain consoles
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}

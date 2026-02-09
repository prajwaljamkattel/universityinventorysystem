package managers;

import models.Equipment;
import models.InventoryItem;
import models.StaffMember;

/**
 * Reports using required loop types (Task 5).
 */
public class InventoryReports {
    private final InventoryManager manager;

    public InventoryReports(InventoryManager manager) {
        this.manager = manager;
    }

    /**
     * generateInventoryReport() - for loop (Task 5)
     */
    public void generateInventoryReport() {
        InventoryItem[] items = manager.getInventory();
        int count = manager.getInventoryCount();

        System.out.println("\n=== INVENTORY REPORT ===");
        for (int i = 0; i < count; i++) {
            System.out.println(items[i]);
        }
    }

    /**
     * findExpiredWarranties() - while loop (Task 5)
     * Warranty expired = 0 months
     */
    public void findExpiredWarranties() {
        InventoryItem[] items = manager.getInventory();
        int count = manager.getInventoryCount();

        System.out.println("\n=== EXPIRED WARRANTIES (0 months) ===");
        int i = 0;
        boolean found = false;

        while (i < count) {
            if (items[i] instanceof Equipment) {
                Equipment eq = (Equipment) items[i];
                if (eq.getWarrantyMonths() == 0) {
                    System.out.println(eq);
                    found = true;
                }
            }
            i++;
        }

        if (!found) System.out.println("None found.");
    }

    /**
     * displayAssignmentsByDepartment() - enhanced for loop (Task 5)
     * Groups staff by department (simple grouping using arrays).
     */
    public void displayAssignmentsByDepartment() {
        StaffMember[] staffArr = manager.getStaffMembers();
        int staffCount = manager.getStaffCount();

        System.out.println("\n=== ASSIGNMENTS BY DEPARTMENT ===");

        // collect unique departments (arrays only)
        String[] depts = new String[staffCount];
        int deptCount = 0;

        for (StaffMember s : staffArr) { // foreach loop
            if (s == null) continue;

            String dept = s.getDepartment();
            if (dept == null) dept = "Unknown";

            boolean exists = false;
            for (int i = 0; i < deptCount; i++) {
                if (depts[i].equalsIgnoreCase(dept)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                depts[deptCount++] = dept;
            }
        }

        // print per department
        for (int d = 0; d < deptCount; d++) {
            String dept = depts[d];
            System.out.println("\nDepartment: " + dept);

            boolean anyone = false;
            for (StaffMember s : staffArr) { // foreach loop again (still ok)
                if (s == null) continue;
                String sDept = s.getDepartment() == null ? "Unknown" : s.getDepartment();

                if (sDept.equalsIgnoreCase(dept)) {
                    anyone = true;
                    System.out.println("  " + s);

                    Equipment[] assigned = s.getAssignedEquipment();
                    for (Equipment e : assigned) {
                        if (e != null) {
                            System.out.println("    -> " + e.getAssetId() + " | " + e.getName());
                        }
                    }
                }
            }
            if (!anyone) System.out.println("  No staff in this department.");
        }

        if (deptCount == 0) System.out.println("No staff registered yet.");
    }

    /**
     * calculateUtilisationRate() - nested loops (Task 5)
     * Simple stats: how many equipment items are assigned vs total.
     */
    public void calculateUtilisationRate() {
        InventoryItem[] items = manager.getInventory();
        int count = manager.getInventoryCount();

        int totalEquipment = 0;
        int assignedEquipment = 0;

        // first loop counts equipment
        for (int i = 0; i < count; i++) {
            if (items[i] instanceof Equipment) {
                totalEquipment++;
                if (!items[i].isAvailable()) assignedEquipment++;
            }
        }

        double utilisation = (totalEquipment == 0) ? 0 : (assignedEquipment * 100.0 / totalEquipment);

        // nested loop: category-based counts (simple usage statistics)
        String[] categories = new String[totalEquipment];
        int[] catTotal = new int[totalEquipment];
        int[] catAssigned = new int[totalEquipment];
        int catCount = 0;

        for (int i = 0; i < count; i++) {
            if (!(items[i] instanceof Equipment)) continue;
            Equipment eq = (Equipment) items[i];
            String cat = eq.getCategory() == null ? "Unknown" : eq.getCategory();

            int idx = -1;
            for (int j = 0; j < catCount; j++) { // nested loop
                if (categories[j].equalsIgnoreCase(cat)) {
                    idx = j;
                    break;
                }
            }
            if (idx == -1) {
                categories[catCount] = cat;
                catTotal[catCount] = 0;
                catAssigned[catCount] = 0;
                idx = catCount;
                catCount++;
            }

            catTotal[idx]++;
            if (!eq.isAvailable()) catAssigned[idx]++;
        }

        System.out.println("\n=== UTILISATION RATE ===");
        System.out.printf("Overall: %d/%d assigned (%.2f%%)\n", assignedEquipment, totalEquipment, utilisation);

        for (int i = 0; i < catCount; i++) {
            double pct = (catTotal[i] == 0) ? 0 : (catAssigned[i] * 100.0 / catTotal[i]);
            System.out.printf("Category '%s': %d/%d assigned (%.2f%%)\n",
                    categories[i], catAssigned[i], catTotal[i], pct);
        }
    }

    /**
     * generateMaintenanceSchedule() - do-while loop (Task 5)
     * Reasonable schedule rule:
     * - warrantyMonths == 0 OR category contains "lab" -> schedule maintenance
     */
    public void generateMaintenanceSchedule() {
        InventoryItem[] items = manager.getInventory();
        int count = manager.getInventoryCount();

        System.out.println("\n=== MAINTENANCE SCHEDULE ===");

        int i = 0;
        boolean found = false;

        if (count == 0) {
            System.out.println("No items in inventory.");
            return;
        }

        do {
            InventoryItem item = items[i];
            if (item instanceof Equipment) {
                Equipment eq = (Equipment) item;
                String cat = eq.getCategory() == null ? "" : eq.getCategory().toLowerCase();

                if (eq.getWarrantyMonths() == 0 || cat.contains("lab")) {
                    System.out.println("Maintenance required: " + eq.getAssetId()
                            + " | " + eq.getName()
                            + " | Category: " + eq.getCategory()
                            + " | WarrantyMonths: " + eq.getWarrantyMonths());
                    found = true;
                }
            }
            i++;
        } while (i < count);

        if (!found) System.out.println("No maintenance required (based on current rules).");
    }
}

import exceptions.InventoryException;
import managers.InventoryManager;
import managers.InventoryReports;
import models.Equipment;
import models.Furniture;
import models.InventoryItem;
import models.LabEquipment;
import models.StaffMember;

import java.util.Scanner;

/**
 * Main application (Task 6).
 * Menu-driven interface + exception handling.
 */
public class UniversityInventorySystem {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // You can change sizes
        InventoryManager manager = new InventoryManager(200, 100);
        InventoryReports reports = new InventoryReports(manager);

        seedDemoData(manager);

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose option: ");

            try {
                switch (choice) {
                    case 1:
                        addNewItem(manager);
                        break;
                    case 2:
                        registerStaff(manager);
                        break;
                    case 3:
                        assignEquipment(manager);
                        break;
                    case 4:
                        returnEquipment(manager);
                        break;
                    case 5:
                        searchInventory(manager);
                        break;
                    case 6:
                        generateReports(reports);
                        break;
                    case 7:
                        System.out.println("Exiting... Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (InventoryException ex) {
                System.out.println("ERROR: " + ex.getMessage());
            } catch (Exception ex) {
                System.out.println("Unexpected error: " + ex.getMessage());
            }
        }
    }

    // ---------------- Menu ----------------
    private static void printMenu() {
        System.out.println("\n==============================");
        System.out.println(" University Inventory System ");
        System.out.println("==============================");
        System.out.println("1. Add new inventory item");
        System.out.println("2. Register new staff member");
        System.out.println("3. Assign equipment to staff");
        System.out.println("4. Return equipment");
        System.out.println("5. Search inventory");
        System.out.println("6. Generate reports");
        System.out.println("7. Exit");
        System.out.println("==============================");
    }

    // ---------------- Option 1 ----------------
    private static void addNewItem(InventoryManager manager) {
        System.out.println("\nAdd Item Type:");
        System.out.println("1. Equipment");
        System.out.println("2. Furniture");
        System.out.println("3. LabEquipment");
        int type = readInt("Choose type: ");

        String id = readNonEmpty("Enter ID / AssetId: ");
        String name = readNonEmpty("Enter Name: ");

        boolean ok;
        switch (type) {
            case 1: {
                String brand = readNonEmpty("Enter Brand: ");
                String category = readNonEmpty("Enter Category (e.g., Computer/Lab/Furniture): ");
                int warranty = readInt("Enter Warranty Months (0 allowed): ");
                Equipment eq = new Equipment(id, name, brand, true, category, warranty);
                ok = manager.addItem(eq);
                break;
            }
            case 2: {
                String room = readNonEmpty("Enter Room Number: ");
                String material = readNonEmpty("Enter Material: ");
                Furniture f = new Furniture(id, name, true, room, material);
                ok = manager.addItem(f);
                break;
            }
            case 3: {
                String lab = readNonEmpty("Enter Lab Name: ");
                String calDate = readNonEmpty("Enter Calibration Date (e.g., 2026-02-01): ");
                LabEquipment le = new LabEquipment(id, name, true, lab, calDate);
                ok = manager.addItem(le);
                break;
            }
            default:
                System.out.println("Invalid type.");
                return;
        }

        if (ok) System.out.println("Item added successfully.");
        else System.out.println("Failed to add item (inventory full or duplicate ID).");
    }

    // ---------------- Option 2 ----------------
    private static void registerStaff(InventoryManager manager) {
        int staffId = readInt("Enter Staff ID (number): ");
        String name = readNonEmpty("Enter Staff Name: ");
        String email = readNonEmpty("Enter Email: ");
        String dept = readNonEmpty("Enter Department: ");

        StaffMember staff = new StaffMember(staffId, name, email, dept);
        boolean ok = manager.addStaffMember(staff);

        if (ok) System.out.println("Staff registered successfully.");
        else System.out.println("Failed to register staff (array full or duplicate staff ID).");
    }

    // ---------------- Option 3 ----------------
    private static void assignEquipment(InventoryManager manager) throws InventoryException {
        int staffId = readInt("Enter Staff ID: ");
        String assetId = readNonEmpty("Enter Equipment AssetId: ");

        StaffMember staff = manager.findStaffById(staffId);
        Equipment eq = manager.findEquipmentByAssetId(assetId);

        if (eq == null) {
            throw new InventoryException("Item '" + assetId + "' not found or not an Equipment.");
        }

        manager.assignEquipment(staff, eq);
        System.out.println("Equipment assigned successfully.");
    }

    // ---------------- Option 4 ----------------
    private static void returnEquipment(InventoryManager manager) throws InventoryException {
        int staffId = readInt("Enter Staff ID: ");
        String assetId = readNonEmpty("Enter Equipment AssetId: ");

        StaffMember staff = manager.findStaffById(staffId);
        manager.returnEquipment(staff, assetId);

        System.out.println("Equipment returned successfully.");
    }

    // ---------------- Option 5 ----------------
    private static void searchInventory(InventoryManager manager) {
        System.out.println("\nSearch Options:");
        System.out.println("1. Search by name");
        System.out.println("2. Search by category (+ optional availableOnly)");
        System.out.println("3. Search by warranty range");
        int s = readInt("Choose search option: ");

        Equipment[] results;
        switch (s) {
            case 1: {
                String name = readNonEmpty("Enter name keyword: ");
                results = manager.searchEquipment(name);
                printEquipmentResults(results);
                break;
            }
            case 2: {
                String category = readNonEmpty("Enter category: ");
                String yn = readNonEmpty("Available only? (y/n): ");
                boolean availableOnly = yn.equalsIgnoreCase("y");
                results = manager.searchEquipment(category, availableOnly);
                printEquipmentResults(results);
                break;
            }
            case 3: {
                int min = readInt("Enter min warranty: ");
                int max = readInt("Enter max warranty: ");
                results = manager.searchEquipment(min, max);
                printEquipmentResults(results);
                break;
            }
            default:
                System.out.println("Invalid search option.");
        }
    }

    private static void printEquipmentResults(Equipment[] results) {
        System.out.println("\n=== SEARCH RESULTS ===");
        if (results.length == 0) {
            System.out.println("No matching equipment found.");
            return;
        }
        for (Equipment e : results) {
            System.out.println(e);
        }
    }

    // ---------------- Option 6 ----------------
    private static void generateReports(InventoryReports reports) {
        System.out.println("\nReports Menu:");
        System.out.println("1. Inventory Report (for loop)");
        System.out.println("2. Expired Warranties (while loop)");
        System.out.println("3. Assignments by Department (foreach)");
        System.out.println("4. Utilisation Rate (nested loops)");
        System.out.println("5. Maintenance Schedule (do-while)");
        int r = readInt("Choose report: ");

        switch (r) {
            case 1: reports.generateInventoryReport(); break;
            case 2: reports.findExpiredWarranties(); break;
            case 3: reports.displayAssignmentsByDepartment(); break;
            case 4: reports.calculateUtilisationRate(); break;
            case 5: reports.generateMaintenanceSchedule(); break;
            default: System.out.println("Invalid report option.");
        }
    }

    // ---------------- Input helpers ----------------
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("Input cannot be empty.");
        }
    }

    // ---------------- Demo seed data ----------------
    private static void seedDemoData(InventoryManager manager) {
        // Staff
        manager.addStaffMember(new StaffMember(101, "Asha", "asha@naps.edu.au", "IT"));
        manager.addStaffMember(new StaffMember(102, "Bikash", "bikash@naps.edu.au", "Science"));
        manager.addStaffMember(new StaffMember(103, "Sita", "sita@naps.edu.au", "Admin"));

        // Inventory items (polymorphism: InventoryItem array stores different subclasses)
        manager.addItem(new Equipment("E001", "Laptop", "Dell", true, "Computer", 12));
        manager.addItem(new Equipment("E002", "Projector", "Epson", true, "Computer", 0)); // expired warranty
        manager.addItem(new Equipment("E003", "Microscope", "Nikon", true, "Lab", 6));

        manager.addItem(new Furniture("F001", "Office Chair", true, "B-204", "Leather"));
        manager.addItem(new LabEquipment("L001", "Centrifuge", true, "Chem Lab", "2026-01-15"));
    }
}

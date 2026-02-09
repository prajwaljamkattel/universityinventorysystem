package managers;

import exceptions.AssignmentLimitExceededException;
import exceptions.EquipmentNotAvailableException;
import exceptions.InventoryException;
import exceptions.StaffMemberNotFoundException;
import models.Equipment;
import models.InventoryItem;
import models.StaffMember;

/**
 * InventoryManager contains required methods (Task 4) + storage arrays.
 */
public class InventoryManager {

    // Arrays (Task wants arrays)
    private InventoryItem[] inventory;
    private int inventoryCount;

    private StaffMember[] staffMembers;
    private int staffCount;

    public InventoryManager(int maxItems, int maxStaff) {
        inventory = new InventoryItem[maxItems];
        staffMembers = new StaffMember[maxStaff];
        inventoryCount = 0;
        staffCount = 0;
    }

    // ---------- Basic add/find helpers ----------
    public boolean addItem(InventoryItem item) {
        if (item == null || inventoryCount >= inventory.length) return false;

        // prevent duplicate ID
        if (findItemById(item.getId()) != null) return false;

        inventory[inventoryCount++] = item;
        return true;
    }

    public boolean addStaffMember(StaffMember staff) {
        if (staff == null || staffCount >= staffMembers.length) return false;

        if (findStaffById(staff.getStaffId()) != null) return false;

        staffMembers[staffCount++] = staff;
        return true;
    }

    public InventoryItem findItemById(String id) {
        if (id == null) return null;
        for (int i = 0; i < inventoryCount; i++) {
            if (inventory[i] != null && id.equalsIgnoreCase(inventory[i].getId())) {
                return inventory[i];
            }
        }
        return null;
    }

    public Equipment findEquipmentByAssetId(String assetId) {
        InventoryItem item = findItemById(assetId);
        if (item instanceof Equipment) return (Equipment) item;
        return null;
    }

    public StaffMember findStaffById(int staffId) {
        for (int i = 0; i < staffCount; i++) {
            if (staffMembers[i] != null && staffMembers[i].getStaffId() == staffId) {
                return staffMembers[i];
            }
        }
        return null;
    }

    public InventoryItem[] getInventory() { return inventory; }
    public int getInventoryCount() { return inventoryCount; }
    public StaffMember[] getStaffMembers() { return staffMembers; }
    public int getStaffCount() { return staffCount; }

    // ---------- Task 4 required methods ----------

    /**
     * assignEquipment(StaffMember staff, Equipment equipment)
     * uses if-else + limits + availability (Task 4).
     */
    public void assignEquipment(StaffMember staff, Equipment equipment) throws InventoryException {
        validateAssignment(staff, equipment);

        // If validation passed, do assignment
        boolean added = staff.addAssignedEquipment(equipment);
        if (!added) {
            throw new AssignmentLimitExceededException("Staff assignment array is full (max 5).");
        }
        equipment.setAvailable(false);
    }

    /**
     * returnEquipment(StaffMember staff, String assetId)
     * validates return and updates availability.
     */
    public void returnEquipment(StaffMember staff, String assetId) throws InventoryException {
        if (staff == null) {
            throw new StaffMemberNotFoundException("Staff member not found.");
        }

        Equipment eq = findEquipmentByAssetId(assetId);
        if (eq == null) {
            throw new InventoryException("Equipment with assetId '" + assetId + "' not found in inventory.");
        }

        boolean removed = staff.removeAssignedEquipment(assetId);
        if (!removed) {
            throw new InventoryException("This staff member does not have equipment '" + assetId + "' assigned.");
        }
        eq.setAvailable(true);
    }

    /**
     * calculateMaintenanceFee uses switch by category (Task 4).
     * You can adjust rates as you like.
     */
    public double calculateMaintenanceFee(Equipment equipment, int daysOverdue) {
        if (equipment == null || daysOverdue <= 0) return 0.0;

        double dailyRate;
        switch (equipment.getCategory().toLowerCase()) {
            case "computer":
                dailyRate = 5.0;
                break;
            case "lab":
            case "lab equipment":
                dailyRate = 10.0;
                break;
            case "furniture":
                dailyRate = 2.0;
                break;
            default:
                dailyRate = 3.0;
                break;
        }
        return dailyRate * daysOverdue;
    }

    // Overloaded search methods (Task 4)
    public Equipment[] searchEquipment(String name) {
        Equipment[] results = new Equipment[inventoryCount];
        int c = 0;

        for (int i = 0; i < inventoryCount; i++) {
            if (inventory[i] instanceof Equipment) {
                Equipment eq = (Equipment) inventory[i];
                if (eq.getName() != null && eq.getName().toLowerCase().contains(name.toLowerCase())) {
                    results[c++] = eq;
                }
            }
        }
        return trimEquipmentArray(results, c);
    }

    public Equipment[] searchEquipment(String category, boolean availableOnly) {
        Equipment[] results = new Equipment[inventoryCount];
        int c = 0;

        for (int i = 0; i < inventoryCount; i++) {
            if (inventory[i] instanceof Equipment) {
                Equipment eq = (Equipment) inventory[i];

                boolean categoryMatch = eq.getCategory() != null
                        && eq.getCategory().equalsIgnoreCase(category);

                boolean availabilityMatch = !availableOnly || eq.isAvailable();

                if (categoryMatch && availabilityMatch) {
                    results[c++] = eq;
                }
            }
        }
        return trimEquipmentArray(results, c);
    }

    public Equipment[] searchEquipment(int minWarranty, int maxWarranty) {
        Equipment[] results = new Equipment[inventoryCount];
        int c = 0;

        for (int i = 0; i < inventoryCount; i++) {
            if (inventory[i] instanceof Equipment) {
                Equipment eq = (Equipment) inventory[i];
                int w = eq.getWarrantyMonths();
                if (w >= minWarranty && w <= maxWarranty) {
                    results[c++] = eq;
                }
            }
        }
        return trimEquipmentArray(results, c);
    }

    /**
     * validateAssignment with nested if-else (Task 4).
     */
    public void validateAssignment(StaffMember staff, Equipment equipment) throws InventoryException {
        if (staff == null) {
            throw new StaffMemberNotFoundException("Staff member not found.");
        } else {
            if (equipment == null) {
                throw new InventoryException("Equipment is null.");
            } else {
                if (!equipment.isAvailable()) {
                    throw new EquipmentNotAvailableException("Equipment '" + equipment.getAssetId() + "' is not available.");
                } else {
                    if (staff.getAssignedEquipmentCount() >= 5) {
                        throw new AssignmentLimitExceededException("Staff already has 5 items assigned.");
                    }
                }
            }
        }
    }

    // ---------- Utility ----------
    private Equipment[] trimEquipmentArray(Equipment[] arr, int count) {
        Equipment[] trimmed = new Equipment[count];
        for (int i = 0; i < count; i++) {
            trimmed[i] = arr[i];
        }
        return trimmed;
    }
}

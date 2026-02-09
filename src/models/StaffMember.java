package models;

public class StaffMember {
    private int staffId;
    private String name;
    private String email;
    private String department;

    private Equipment[] assignedEquipment; // max 5
    private int assignedCount;

    public StaffMember(int staffId, String name, String email, String department) {
        this.staffId = staffId;
        this.name = name;
        this.email = email;
        this.department = department;
        this.assignedEquipment = new Equipment[5];
        this.assignedCount = 0;
    }

    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Equipment[] getAssignedEquipment() { return assignedEquipment; }

    public boolean addAssignedEquipment(Equipment equipment) {
        if (assignedCount >= 5 || equipment == null) return false;
        assignedEquipment[assignedCount++] = equipment;
        return true;
    }

    public boolean removeAssignedEquipment(String assetId) {
        if (assetId == null) return false;

        for (int i = 0; i < assignedCount; i++) {
            if (assignedEquipment[i] != null && assetId.equalsIgnoreCase(assignedEquipment[i].getAssetId())) {
                // move last to current
                assignedEquipment[i] = assignedEquipment[assignedCount - 1];
                assignedEquipment[assignedCount - 1] = null;
                assignedCount--;
                return true;
            }
        }
        return false;
    }

    public int getAssignedEquipmentCount() {
        return assignedCount;
    }

    @Override
    public String toString() {
        return "Staff | ID: " + staffId
                + " | Name: " + name
                + " | Email: " + email
                + " | Department: " + department
                + " | AssignedCount: " + assignedCount;
    }
}

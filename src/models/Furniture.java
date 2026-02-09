package models;


public class Furniture extends InventoryItem {
    private String roomNumber;
    private String material;

    public Furniture(String id, String name, boolean isAvailable, String roomNumber, String material) {
        super(id, name, isAvailable);
        this.roomNumber = roomNumber;
        this.material = material;
    }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    @Override
    public String getItemType() {
        return "Furniture";
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Room: " + roomNumber
                + " | Material: " + material;
    }
}

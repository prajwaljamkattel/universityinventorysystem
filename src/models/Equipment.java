package models;


public class Equipment extends InventoryItem {
    private String assetId;
    private String brand;
    private int warrantyMonths;
    private String category;

    public Equipment(String assetId, String name, String brand, boolean isAvailable, String category, int warrantyMonths) {
        super(assetId, name, isAvailable); // use assetId as id in base class
        this.assetId = assetId;
        this.brand = brand;
        this.category = category;
        this.warrantyMonths = warrantyMonths;
    }

    public String getAssetId() { return assetId; }
    public void setAssetId(String assetId) {
        this.assetId = assetId;
        setId(assetId);
    }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public int getWarrantyMonths() { return warrantyMonths; }
    public void setWarrantyMonths(int warrantyMonths) { this.warrantyMonths = warrantyMonths; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public String getItemType() {
        return "Equipment";
    }

    @Override
    public String toString() {
        return super.toString()
                + " | AssetId: " + assetId
                + " | Brand: " + brand
                + " | Category: " + category
                + " | WarrantyMonths: " + warrantyMonths;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Equipment)) return false;
        Equipment other = (Equipment) obj;
        return assetId != null && assetId.equals(other.assetId);
    }
}

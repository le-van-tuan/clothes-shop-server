package vn.triumphstudio.clothesshop.domain.enumration;

public enum OrderStatus {
    Pending("Pending"),
    Confirmed("Confirmed"),
    Shipping("Shipping"),
    Shipped("Shipped"),
    Cancelled("Cancelled"),
    Declined("Declined");

    private final String name;

    OrderStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

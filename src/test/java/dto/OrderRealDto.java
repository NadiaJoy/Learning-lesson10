package dto;

public class OrderRealDto {
    private String customerName;
    private String customerPhone;
    private String comment;
    //extra fields for response
    private String status;
    private int courierId;
    private long id;

    public OrderRealDto(String customerName, String customerPhone, String comment) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.comment = comment;
        this.status = "OPEN";
    }

    public OrderRealDto() {
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public int getCourierId() {
        return courierId;
    }

    public long getId() {
        return id;
    }
}

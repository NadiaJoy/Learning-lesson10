package dto;

public class OrderDto {
    private String customerName;
    private String customerPhone;
    private String comment;

    public OrderDto(String customerName, String customerPhone, String comment) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.comment = comment;
    }

    public OrderDto() {
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
}

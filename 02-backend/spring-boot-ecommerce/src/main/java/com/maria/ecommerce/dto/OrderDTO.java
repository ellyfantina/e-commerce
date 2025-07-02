package com.maria.ecommerce.dto;

import java.math.BigDecimal;
import java.util.Date;

public class OrderDTO {

    private Long id;
    private String orderTrackingNumber;
    private Date dateCreated;
    private BigDecimal totalPrice;
    private int totalQuantity;
    private String status;

    // Constructor
    public OrderDTO(Long id, String orderTrackingNumber, Date dateCreated,
                    BigDecimal totalPrice, int totalQuantity, String status) {
        this.id = id;
        this.orderTrackingNumber = orderTrackingNumber;
        this.dateCreated = dateCreated;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.status = status;
    }

    public Long getId() { return id; }

    public void setId (Long id) {this.id = id;}

    public String getOrderTrackingNumber() { return orderTrackingNumber; }
    public void setOrderTrackingNumber(String orderTrackingNumber) { this.orderTrackingNumber = orderTrackingNumber; }

    public Date getDateCreated() { return dateCreated; }
    public void setDateCreated(Date dateCreated) { this.dateCreated = dateCreated; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
